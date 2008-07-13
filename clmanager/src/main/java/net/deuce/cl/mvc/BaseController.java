package net.deuce.cl.mvc;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;
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
import net.deuce.clmanager.domain.dao.SavedFileDao;
import net.deuce.clmanager.domain.dao.UserPreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public abstract class BaseController implements Controller {
    
    protected static ModelAndView OK = new ModelAndView("ok");
    protected static ModelAndView ERROR = new ModelAndView("error");
    protected static ModelAndView UNAUTHENTICATED_ERROR = new ModelAndView("msg", "msg", "error:unauthenticated");
    
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
    private SavedFileDao savedFileDao;
    private PostCache cache;
    
    private Log log = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    public BaseController() {
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
    
    public PostActivities getPostActivities(UserPreferences prefs, Post p) {
        PostActivities pa = getPostActivitiesDao().findUniqueByUsernameAndPost(prefs.getUsername(), p);
        if (pa == null) {
            pa = new PostActivities();
            pa.setPost(p);
            pa.setUsername(prefs.getUsername());
            getPostActivitiesDao().create(pa);
        }
        return pa;
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
    
    protected void clearFavorites(UserPreferences prefs) {
        String name = "favorites";
        Preference p = prefs.getPreference(name);
        if (p != null) {
            removePreference(prefs, name);
            getCache().remove(prefs);
        }
    }
    
    protected void setPreference(UserPreferences prefs, String name, String value) {
        Preference p = prefs.getPreference(name);
        if (p == null) {
            p = new Preference();
            p.setName(name);
            p.setValue(value);
            p.setUserPreferences(prefs);
            getPreferenceDao().create(p);
            prefs.addPreference(p);
            getUserPreferencesDao().update(prefs);
        } else if (p.getValue() == null || !p.getValue().equals(value)){
            p.setValue(value);
            getPreferenceDao().update(p);
            getUserPreferencesDao().update(prefs);
        }
    }
    
    protected void removePreference(UserPreferences prefs, String name) {
        Preference p = prefs.getPreference(name);
        if (p != null) {
            prefs.removePreference(name);
            getUserPreferencesDao().update(prefs);
            getPreferenceDao().delete(p);
        }
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

    public SavedFileDao getSavedFileDao() {
        return savedFileDao;
    }

    public void setSavedFileDao(SavedFileDao savedFileDao) {
        this.savedFileDao = savedFileDao;
    }
    
    public PostCache getCache() {
        return cache;
    }

    public void setCache(PostCache cache) {
        this.cache = cache;
    }

    public void afterPropertiesSet() throws Exception {
        if (cache == null) {
            throw new RuntimeException("cache is null");
        }
    }

}
