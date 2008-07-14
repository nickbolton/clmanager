package net.deuce.clmanager.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.CategoryDao;
import net.deuce.clmanager.domain.dao.CityAreaDao;
import net.deuce.clmanager.domain.dao.CityDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DefaultSynchronizer implements Synchronizer, InitializingBean {
    
    private Log log = LogFactory.getLog(getClass());
    private CityDao cityDao;
    private CategoryDao categoryDao;
    private PostDao postDao;
    private ImageDao imageDao;
    private CityAreaDao cityAreaDao;
    private SessionFactory sessionFactory;
    private SpamDetector spamDetector;
    private String imagePath;
    private String citiesUrl;
    private Pattern cityUrlPattern;
    private int numConcurrentThreads = 10;
    private int threadPoolSize = 50;
    private ThreadPoolExecutor pool;
    private List<Future<ScrapResult>> trackers = new ArrayList<Future<ScrapResult>>();
    
    public void afterPropertiesSet() throws Exception {
        pool = new ThreadPoolExecutor(numConcurrentThreads, numConcurrentThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(threadPoolSize));
    }
    
    public void shutdown() {
        pool.shutdown();
    }
    
    public void synchronize() {
        List<Post> posts = new LinkedList<Post>();
        try {
            trackers.clear();
            for (City city : cityDao.findBySubscribed(true)) {
                for (Category category : categoryDao.findBySubscribed(true)) {
                    if (log.isDebugEnabled()) {
                        log.debug("creating "+city.getName()+"/"+category.getExternalName()+" worker");
                    }
                    ScrapCityCategoryWorker worker = new ScrapCityCategoryWorker(sessionFactory, city, category, spamDetector, postDao, imageDao, cityAreaDao);
                    if (log.isDebugEnabled()) {
                        log.debug("submitting "+city.getName()+"/"+category.getExternalName()+" worker");
                    }
                    trackers.add(pool.submit(worker));
                    if (log.isDebugEnabled()) {
                        log.debug("done submitting "+city.getName()+"/"+category.getExternalName()+" worker");
                    }
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } finally {
            int count=0;
            for (Future<ScrapResult> f : trackers) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("getting city/cat result " + count);
                    }
                    ScrapResult result = f.get();
                    if (log.isDebugEnabled()) {
                        log.debug("received "+result.getCity().getName()+"/"+result.getCategory().getExternalName()+" result " + count);
                    }
                    if (result.isSucceeded()) {
                        if (log.isDebugEnabled()) {
                            StringBuffer sb = new StringBuffer();
                            for (Post p : result.getPosts()) {
                                sb.append(p.getClId()).append(" - ").append(p.getTitle()).append('\n');
                            }
                            log.debug("succeeded "+result.getCity().getName()+"/"+result.getCategory().getExternalName()+" result " + count + " posts:\n" + sb.toString());
                        }
                        posts.addAll(result.getPosts());
                    }
                    if (!result.isSucceeded() && result.getException() != null) {
                        log.error("ScrapeWorker failed", result.getException());
                    }
                    count++;
                } catch (Exception e) {
                    log.error(e);
                }
            }
            trackers.clear();
        }
        
        List<Post> createdPosts = new LinkedList<Post>();
        List<Post> updatedPosts = new LinkedList<Post>();
        openSession();
        try {
            for (Post p : posts) {
                if (log.isDebugEnabled()) {
                    log.debug("Processing post " + p.getClId() + " - " + p.getTitle());
                }
                refreshSession();
                Post fetchedPost = postDao.findUniqueByClId(p.getClId());
                if ( fetchedPost == null && p.isSpam()) {
                    if (log.isDebugEnabled()) {
                        log.debug("new post " + p.getClId() + " - " + p.getTitle());
                    }
                    createdPosts.add(p);
                } else if (!p.isSpam() && !p.isFlagged()){
                    if (fetchedPost != null) {
                        p.setId(fetchedPost.getId());
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("scaping post " + p.getClId() + " - " + p.getTitle());
                    }
                    ScrapPostWorker worker = new ScrapPostWorker(sessionFactory, imagePath, p, spamDetector, postDao, imageDao, cityAreaDao);
                    if (log.isDebugEnabled()) {
                        log.debug("submitting post " + p.getClId() + " - " + p.getTitle());
                    }
                    trackers.add(pool.submit(worker));
                    if (log.isDebugEnabled()) {
                        log.debug("done submitting post " + p.getClId() + " - " + p.getTitle());
                    }
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } finally {
            closeSession();
            int count=0;
            for (Future<ScrapResult> f : trackers) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("getting scrap result " + count);
                    }
                    ScrapResult result = f.get();
                    Post p = result.getPost();
                    if (log.isDebugEnabled()) {
                        log.debug("received scrap result, post " + p.getClId() + " - " + p.getTitle());
                    }
                    if (result.isSucceeded()) {
                        if (p.getId() == null) {
                            if (log.isDebugEnabled()) {
                                log.debug("new scrap post " + p.getClId() + " - " + p.getTitle());
                            }
                            createdPosts.add(p);
                        } else {
                            if (log.isDebugEnabled()) {
                                log.debug("updated scrap post " + p.getClId() + " - " + p.getTitle());
                            }
                            updatedPosts.add(p);
                        }
                    }
                    if (!result.isSucceeded() && result.getException() != null) {
                        log.error("ScrapeWorker failed", result.getException());
                    }
                } catch (Exception e) {
                    log.error(e);
                }
                count++;
            }
            trackers.clear();
        }
        
        openSession();
        try {
            for (Post p : createdPosts) {
                if (log.isDebugEnabled()) {
                    log.debug("processing new post " + p.getClId() + " - " + p.getTitle());
                }
                for (Image i : p.getImages()) {
                    imageDao.create(i);
                }
                postDao.create(p);
                if (log.isDebugEnabled()) {
                    log.debug("done processing new post " + p.getClId() + " - " + p.getTitle());
                }
            }
            for (Post p : updatedPosts) {
                if (log.isDebugEnabled()) {
                    log.debug("processing updated post " + p.getClId() + " - " + p.getTitle());
                }
                postDao.update(p);
                if (log.isDebugEnabled()) {
                    log.debug("done processing updated post " + p.getClId() + " - " + p.getTitle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }
    
    protected void refreshSession() {
        closeSession();
        openSession();
    }
    
    protected void openSession() {
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory,
            new SessionHolder(session));
    }

    protected void closeSession() {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
            .unbindResource(sessionFactory);
        sessionHolder.getSession().flush();
        sessionHolder.getSession().close();
        SessionFactoryUtils.releaseSession(sessionHolder.getSession(),
            sessionFactory);
    }
    
    public PostDao getPostDao() {
        return postDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public CityDao getCityDao() {
        return cityDao;
    }

    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String getCitiesUrl() {
        return citiesUrl;
    }

    public void setCitiesUrl(String cityUrl) {
        this.citiesUrl = cityUrl;
    }

    public String getCityUrlPattern() {
        return cityUrlPattern.pattern();
    }

    public void setCityUrlPattern(String cityUrlPattern) {
        this.cityUrlPattern = Pattern.compile(cityUrlPattern);
    }
    
    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public int getNumConcurrentThreads() {
        return numConcurrentThreads;
    }

    public void setNumConcurrentThreads(int numConcurrentThreads) {
        this.numConcurrentThreads = numConcurrentThreads;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    
    public static class CallerRunsPolicy implements RejectedExecutionHandler {
        /**
         * Creates a <tt>CallerRunsPolicy</tt>.
         */
        public CallerRunsPolicy() { }

        /**
         * Executes task r in the caller's thread, unless the executor
         * has been shut down, in which case the task is discarded.
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                r.run();
            }
        }
    }

    public SpamDetector getSpamDetector() {
        return spamDetector;
    }

    public void setSpamDetector(SpamDetector spamDetector) {
        this.spamDetector = spamDetector;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CityAreaDao getCityAreaDao() {
        return cityAreaDao;
    }

    public void setCityAreaDao(CityAreaDao cityAreaDao) {
        this.cityAreaDao = cityAreaDao;
    }

}
