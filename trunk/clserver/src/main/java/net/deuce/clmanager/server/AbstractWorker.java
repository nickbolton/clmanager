package net.deuce.clmanager.server;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.dao.CityAreaDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public abstract class AbstractWorker implements Callable<ScrapResult> {
    
    private boolean running = true;
    private City city;
    private Category category;
    private PostDao postDao;
    private CityAreaDao cityAreaDao;
    private ImageDao imageDao;
    private Log log = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;
    private ScrapResult result;
    private SpamDetector spamDetector;
    
    private Pattern idPattern = Pattern.compile(".*/([0-9]+)\\.html");
    private Pattern titlePattern = Pattern.compile("(.*)(?: \\((.*?)\\))(?: (\\d+)yr)?$");
    private Pattern titlePattern2 = Pattern.compile("(.*?)(?: (\\d+)yr)?$");
    
    public AbstractWorker(SessionFactory sessionFactory, City city, Category category,
        SpamDetector spamDetector, PostDao postDao, ImageDao imageDao, CityAreaDao cityAreaDao) {
        this.sessionFactory = sessionFactory;
        this.city = city;
        this.category = category;
        this.spamDetector = spamDetector;
        this.postDao = postDao;
        this.imageDao = imageDao;
        this.cityAreaDao = cityAreaDao;
        this.result = new ScrapResult(city, category);
    }
    
    abstract protected void doRun() throws Exception;
    
    public Pattern getIdPattern() {
        return idPattern;
    }

    public void setIdPattern(Pattern idPattern) {
        this.idPattern = idPattern;
    }
    
    protected String parseId(String s) {
        if (s == null) return null;
        Matcher m = idPattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public boolean isRunning() {
        return running;
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
    
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            result.setSucceeded(false);
            result.setException(e);
            log.error(e);
        } finally {
            running = false;
        }
    }
    
    public ScrapResult call() throws Exception {
        run();
        return result;
    }
    
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ScrapResult getResult() {
        return result;
    }

    public void setResult(ScrapResult result) {
        this.result = result;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Pattern getTitlePattern() {
        return titlePattern;
    }

    public void setTitlePattern(Pattern titlePattern) {
        this.titlePattern = titlePattern;
    }

    public Pattern getTitlePattern2() {
        return titlePattern2;
    }

    public void setTitlePattern2(Pattern titlePattern2) {
        this.titlePattern2 = titlePattern2;
    }

    public SpamDetector getSpamDetector() {
        return spamDetector;
    }

    public void setSpamDetector(SpamDetector spamDetector) {
        this.spamDetector = spamDetector;
    }

    public CityAreaDao getCityAreaDao() {
        return cityAreaDao;
    }

    public void setCityAreaDao(CityAreaDao cityAreaDao) {
        this.cityAreaDao = cityAreaDao;
    }

}