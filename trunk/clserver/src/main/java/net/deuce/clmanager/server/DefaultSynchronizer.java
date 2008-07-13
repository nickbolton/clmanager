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
                    ScrapCityCategoryWorker worker = new ScrapCityCategoryWorker(sessionFactory, city, category, spamDetector, postDao, imageDao, cityAreaDao);
                    trackers.add(pool.submit(worker));
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } finally {
            for (Future<ScrapResult> f : trackers) {
                try {
                    ScrapResult result = f.get();
                    if (result.isSucceeded()) {
                        posts.addAll(result.getPosts());
                    }
                    if (!result.isSucceeded() && result.getException() != null) {
                        log.error("ScrapeWorker failed", result.getException());
                    }
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
                refreshSession();
                Post fetchedPost = postDao.findUniqueByClId(p.getClId());
                if ( fetchedPost == null && p.isSpam()) {
                    createdPosts.add(p);
                } else if (!p.isSpam() && !p.isFlagged()){
                    if (fetchedPost != null) {
                        p.setId(fetchedPost.getId());
                    }
                    ScrapPostWorker worker = new ScrapPostWorker(sessionFactory, imagePath, p, spamDetector, postDao, imageDao, cityAreaDao);
                    trackers.add(pool.submit(worker));
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } finally {
            closeSession();
            for (Future<ScrapResult> f : trackers) {
                try {
                    ScrapResult result = f.get();
                    Post p = result.getPost();
                    if (result.isSucceeded()) {
                        if (p.getId() == null) {
                            createdPosts.add(p);
                        } else {
                            updatedPosts.add(p);
                        }
                    }
                    if (!result.isSucceeded() && result.getException() != null) {
                        log.error("ScrapeWorker failed", result.getException());
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
            trackers.clear();
        }
        
        openSession();
        try {
            for (Post p : createdPosts) {
                for (Image i : p.getImages()) {
                    imageDao.create(i);
                }
                System.out.println("ZZZ creating new post: " + p.getId() + ", " + p.getClId() + ", " + p.getCity().getName() + ", " + p.getCategory().getExternalName() + ", " + p.getAge() + ", " + p.isFlagged() + ", " + p.isSpam() + " - " + p.getTitle());
                postDao.create(p);
            }
            for (Post p : updatedPosts) {
                postDao.update(p);
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
