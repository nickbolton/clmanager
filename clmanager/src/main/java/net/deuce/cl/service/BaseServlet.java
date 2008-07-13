package net.deuce.cl.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.deuce.clmanager.domain.dao.CategoryDao;
import net.deuce.clmanager.domain.dao.CategorySubscriptionDao;
import net.deuce.clmanager.domain.dao.CityDao;
import net.deuce.clmanager.domain.dao.CitySubscriptionDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.MessagePhotoDao;
import net.deuce.clmanager.domain.dao.MessageTemplateDao;
import net.deuce.clmanager.domain.dao.PostActivitiesDao;
import net.deuce.clmanager.domain.dao.PostDao;
import net.deuce.clmanager.domain.dao.PreferenceDao;
import net.deuce.clmanager.domain.dao.UserPreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public abstract class BaseServlet extends FrameworkServlet {
    
    private CityDao cityDao;
    private CitySubscriptionDao citySubscriptionDao;
    private CategoryDao categoryDao;
    private CategorySubscriptionDao categorySubscriptionDao;
    private PostDao postDao;
    private PostActivitiesDao postActivitiesDao;
    private ImageDao imageDao;
    private MessageTemplateDao messageTemplateDao;
    private MessagePhotoDao messagePhotoDao;
    private PreferenceDao preferenceDao;
    private UserPreferencesDao userPreferencesDao;
    
    private Log log = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    public BaseServlet() {
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        WebApplicationContext context = getWebApplicationContext();
        cityDao = (CityDao)context.getBean("cityDao", CityDao.class);
        citySubscriptionDao = (CitySubscriptionDao)context.getBean("citySubscriptionDao", CitySubscriptionDao.class);
        categoryDao = (CategoryDao)context.getBean("categoryDao", CategoryDao.class);
        categorySubscriptionDao = (CategorySubscriptionDao)context.getBean("categorySubscriptionDao", CategorySubscriptionDao.class);
        postDao = (PostDao)context.getBean("postDao", PostDao.class);
        postActivitiesDao = (PostActivitiesDao)context.getBean("postActivitiesDao", PostActivitiesDao.class);
        imageDao = (ImageDao)context.getBean("imageDao", ImageDao.class);
        messageTemplateDao = (MessageTemplateDao)context.getBean("messageTemplateDao", MessageTemplateDao.class);
        messagePhotoDao = (MessagePhotoDao)context.getBean("messagePhotoDao", MessagePhotoDao.class);
        preferenceDao = (PreferenceDao)context.getBean("preferenceDao", PreferenceDao.class);
        userPreferencesDao = (UserPreferencesDao)context.getBean("userPreferencesDao", UserPreferencesDao.class);
        sessionFactory = (SessionFactory)context.getBean("sessionFactory", SessionFactory.class);
    }



    protected String safeString(String s) {
        return s != null ? s : "";
    }
    
    protected Boolean safeBoolean(Boolean b) {
        return b != null ? b : Boolean.FALSE;
    }
    
    protected Session openSession() {
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory,
            new SessionHolder(session));
        return session;
    }

    protected void closeSession() {
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
            .unbindResource(sessionFactory);
        sessionHolder.getSession().flush();
        sessionHolder.getSession().close();
        SessionFactoryUtils.releaseSession(sessionHolder.getSession(),
            sessionFactory);
    }

    public CityDao getCityDao() {
        return cityDao;
    }


    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }


    public CitySubscriptionDao getCitySubscriptionDao() {
        return citySubscriptionDao;
    }


    public void setCitySubscriptionDao(CitySubscriptionDao citySubscriptionDao) {
        this.citySubscriptionDao = citySubscriptionDao;
    }


    public CategoryDao getCategoryDao() {
        return categoryDao;
    }


    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    public CategorySubscriptionDao getCategorySubscriptionDao() {
        return categorySubscriptionDao;
    }


    public void setCategorySubscriptionDao(
        CategorySubscriptionDao categorySubscriptionDao) {
        this.categorySubscriptionDao = categorySubscriptionDao;
    }


    public PostDao getPostDao() {
        return postDao;
    }


    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }


    public PostActivitiesDao getPostActivitiesDao() {
        return postActivitiesDao;
    }


    public void setPostActivitiesDao(PostActivitiesDao postActivitiesDao) {
        this.postActivitiesDao = postActivitiesDao;
    }


    public ImageDao getImageDao() {
        return imageDao;
    }


    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }


    public MessageTemplateDao getMessageTemplateDao() {
        return messageTemplateDao;
    }


    public void setMessageTemplateDao(MessageTemplateDao messageTemplateDao) {
        this.messageTemplateDao = messageTemplateDao;
    }


    public MessagePhotoDao getMessagePhotoDao() {
        return messagePhotoDao;
    }


    public void setMessagePhotoDao(MessagePhotoDao messagePhotoDao) {
        this.messagePhotoDao = messagePhotoDao;
    }


    public PreferenceDao getPreferenceDao() {
        return preferenceDao;
    }


    public void setPreferenceDao(PreferenceDao preferenceDao) {
        this.preferenceDao = preferenceDao;
    }

    public UserPreferencesDao getUserPreferencesDao() {
        return userPreferencesDao;
    }

    public void setUserPreferencesDao(UserPreferencesDao userPreferencesDao) {
        this.userPreferencesDao = userPreferencesDao;
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

}
