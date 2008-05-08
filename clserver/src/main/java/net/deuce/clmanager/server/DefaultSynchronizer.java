package net.deuce.clmanager.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.CategoryDao;
import net.deuce.clmanager.domain.dao.CityDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
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
    private SessionFactory sessionFactory;
    private Scraper scraper;
    private String citiesUrl;
    private Pattern cityUrlPattern;
    private int numConcurrentThreads = 10;
    private int threadPoolSize = 50;
    private ExecutorService pool;
    
    public void afterPropertiesSet() throws Exception {
        pool = new ThreadPoolExecutor(numConcurrentThreads, threadPoolSize, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue(threadPoolSize));
    }
    
    public void shutdown() {
        pool.shutdown();
    }
    
    public void synchronize() {
        openSession();
        try {
            fetchCities();
            List<City> cities = cityDao.findBySubscribed(Boolean.TRUE);
            List<Future<ScrapResult>> trackers = new ArrayList<Future<ScrapResult>>();
            for (City city : cities) {
                List<Category> categories = categoryDao.findBySubscribed(Boolean.TRUE);
                
                for (Category category : categories) {
                    System.out.println("ZZZ creating scraper worker for city/category: " + city.getName() + "/" + category.getName());
                    ScrapWorker sw = new ScrapWorker(sessionFactory, city, category, scraper, postDao, imageDao);
                    trackers.add(pool.submit(new ScrapWorker(sessionFactory, city, category, scraper, postDao, imageDao)));
                }
            }
            for (Future<ScrapResult> f : trackers) {
                try {
                    ScrapResult result = f.get();
                    if (result.isSucceeded()) {
                        System.out.println("ZZZ ScrapeWorker("+result.getCity().getName()+", "+result.getCategory().getExternalName()+") succeeded.");
                    }
                    if (!result.isSucceeded() && result.getException() != null) {
                        log.error("ScrapeWorker failed", result.getException());
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } finally {
            closeSession();
        }
    }
    
    private void fetchCities() throws Exception {
        Document doc = Utils.getResponse(citiesUrl);
        List<Element> links = doc.selectNodes("//*[name() = 'a']");
        for (Element e : links) {
            String url = e.attributeValue("href");
            if (url.equals("http://www.craigslist.org/")) continue;
            Matcher m = cityUrlPattern.matcher(url);
            if (m.find()) {
                String name = e.getText();
                Element b = (Element)e.selectSingleNode("*[name() = 'b']");
                if (b != null && (name == null || name.length() == 0)) {
                    name = b.getText();
                }
                if (name == null) {
                    log.warn("No city name for url: " + url);
                    continue;
                }
                City city = cityDao.findUniqueByName(name);
                if (city != null) {
                    city.setUrl(url);
                    cityDao.update(city);
                } else {
                    cityDao.create(new City(name, url));
                }
            }
        }
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

    private void refreshSession() {
        closeSession();
        openSession();
    }
    
    private void openSession() {
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory,
            new SessionHolder(session));
    }

    private void closeSession() {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
            .unbindResource(sessionFactory);
        sessionHolder.getSession().flush();
        sessionHolder.getSession().close();
        SessionFactoryUtils.releaseSession(sessionHolder.getSession(),
            sessionFactory);
    }

    private void restartSession() {
        closeSession();
        openSession();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Scraper getScraper() {
        return scraper;
    }

    public void setScraper(Scraper scraper) {
        this.scraper = scraper;
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
    
    private static class ScrapResult {
        private boolean succeeded = true;
        private Exception exception;
        private City city;
        private Category category;
        
        public ScrapResult(City city, Category category) {
            this.city = city;
            this.category = category;
        }

        public void setSucceeded(boolean b) {
            this.succeeded = b;
        }
        
        public boolean isSucceeded() {
            return succeeded;
        }
        
        public void setException(Exception e) {
            this.exception = e;
        }
        
        public Exception getException() {
            return exception;
        }
        
        public City getCity() {
            return city;
        }
        
        public Category getCategory() {
            return category;
        }
    }

    private static class ScrapWorker implements Callable<ScrapResult> {
        
        private boolean running = true;
        private City city;
        private Category category;
        private Scraper scraper;
        private PostDao postDao;
        private ImageDao imageDao;
        private Log log = LogFactory.getLog(getClass());
        private SessionFactory sessionFactory;
        private ScrapResult result;
        
        public ScrapWorker(SessionFactory sessionFactory, City city, Category category, Scraper scraper,
            PostDao postDao, ImageDao imageDao) {
            this.sessionFactory = sessionFactory;
            this.city = city;
            this.category = category;
            this.scraper = scraper;
            this.postDao = postDao;
            this.imageDao = imageDao;
            this.result = new ScrapResult(city, category);
        }
        
        public boolean isRunning() {
            return running;
        }
        
        private void refreshSession() {
            closeSession();
            openSession();
        }
        
        private void openSession() {
            Session session = SessionFactoryUtils.getSession(sessionFactory, true);
            TransactionSynchronizationManager.bindResource(sessionFactory,
                new SessionHolder(session));
        }

        private void closeSession() {
            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
                .unbindResource(sessionFactory);
            sessionHolder.getSession().flush();
            sessionHolder.getSession().close();
            SessionFactoryUtils.releaseSession(sessionHolder.getSession(),
                sessionFactory);
        }
        
        private List<Post> getPosts() {
            openSession();
            try {
                String url = buildUrl(city, category);
                return scraper.scrape(city, category, url);
            } catch (Exception e) {
                result.setSucceeded(false);
                result.setException(e);
                log.error(e);
            } finally {
                running = false;
                closeSession();
            }
            return null;
        }
        
        public void run() {
            List<Post> posts = getPosts();
            if (posts == null) return;
            for (Post p : posts) {
                openSession();
                try {
                    Post fetchedPost = postDao.findUniqueByClId(p.getClId());
                    Set<Image> images = p.getImages();
                    if (fetchedPost == null) {
                        log.info("Adding post: " + p.getClId() + " - " + p.getTitle());
                        postDao.create(p);
                        for (Image image : images) {
                            imageDao.create(image);
                        }
                    } else {
                        if (p.isFlagged() && !fetchedPost.isFlagged()) {
                            fetchedPost.setFlagged(true);
                            postDao.update(fetchedPost);
                        } else if (p.isSpam() && !fetchedPost.isSpam()) {
                            System.out.println("Marking as spam: " + p.getClId());
                            fetchedPost.setSpam(true);
                            postDao.update(fetchedPost);
                        }
                    }
                } catch (Exception e) {
                    result.setSucceeded(false);
                    result.setException(e);
                    log.error(e);
                } finally {
                    running = false;
                    closeSession();
                }
            }
        }
        
        private String buildUrl(City city, Category category) {
            String cityUrl = city.getUrl();
            StringBuffer url = new StringBuffer(cityUrl);
            if (!cityUrl.endsWith("/")) {
                url.append('/');
            }
            url.append(category.getExternalName()).append('/');
            return url.toString();
        }

        public ScrapResult call() throws Exception {
            System.out.println("ZZZ running ScrapWorker("+city.getName()+", "+category.getExternalName()+")");
            run();
            return result;
        }

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

}
