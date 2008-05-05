package net.deuce.clmanager.entityManager;

import java.util.Date;

import junit.framework.TestCase;
import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.CategoryDao;
import net.deuce.clmanager.domain.dao.CityDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DaoTest extends TestCase {

    private ApplicationContext factory;

    public DaoTest(String s) {
        super(s);
        factory = new ClassPathXmlApplicationContext(
            "test-applicationContext.xml");
    }

    public City createCity(String name, String url) {
        City city = new City();
        city.setName(name);
        city.setUrl(url);
        getCityDao().create(city);
        return city;
    }
    
    public Category createCategory(String name) {
        Category cat = new Category();
        cat.setName(name);
        getCategoryDao().create(cat);
        return cat;
    }
    
    public Post createPost(City city, Category cat, Long clId, String title, String age, String content, String location, String replyAddress) {
        Post post = new Post(clId, title, age, content, location, replyAddress, new Date(), city, cat);
        getPostDao().create(post);
        return post;
    }

    public void testCityCrud() throws Exception {

        // Create
        CityDao cityDao = getCityDao();
        City createCity = createCity("Create Name", "Create URL");
        assertNotNull(createCity.getId());
        Long id = createCity.getId();

        restartSession();

        // Read
        City foundCity = cityDao.read(id);
        assertEquals(createCity.getName(), foundCity.getName());
        assertEquals(createCity.getUrl(), foundCity.getUrl());

        restartSession();

        // Update
        String updateName = "Update Name";
        String updateUrl = "Update Url";
        foundCity.setName(updateName);
        foundCity.setUrl(updateUrl);
        cityDao.update(foundCity);
        City updatedCity = cityDao.read(id);
        assertEquals(updateName, updatedCity.getName());
        assertEquals(updateUrl, updatedCity.getUrl());

        restartSession();

        // Delete
        cityDao.delete(updatedCity);
        restartSession();
        assertNull(cityDao.read(id));
    }

    public void testFindCityByName() throws Exception {
        CityDao cityDao = getCityDao();
        City city1 = createCity("findName1", "findUrl1");
        City city2 = createCity("findName2", "findUrl2");

        restartSession();

        City byName = cityDao.findUniqueByName("findName1");
        assertNotNull(byName);
        assertEquals(city1.getName(), byName.getName());
        assertEquals(city1.getUrl(), byName.getUrl());

        restartSession();

        cityDao.delete(city1);
        cityDao.delete(city2);
    }

    public void testCategoryCrud() throws Exception {
        CategoryDao categoryDao = getCategoryDao();
        // Create
        City city = createCity("Test City", "Test Url");
        Category createCategory = createCategory("Category Name");
        
        assertNotNull(createCategory.getId());
        Long id = createCategory.getId();

        restartSession();

        // Read
        Category foundCategory = categoryDao.read(id);
        assertEquals(createCategory.getName(), foundCategory.getName());

        restartSession();

        // Update
        String updateName = "Update Name";
        foundCategory.setName(updateName);
        categoryDao.update(foundCategory);
        Category updatedCategory = categoryDao.read(id);
        assertEquals(updateName, updatedCategory.getName());

        restartSession();

        // Delete
        categoryDao.delete(updatedCategory);
        restartSession();
        assertNull(categoryDao.read(id));
        getCityDao().delete(city);
    }
    
    public void testCategoryFindByName() throws Exception {
        CategoryDao categoryDao = getCategoryDao();
        City city = createCity("cityName", "cityUrl");
        
        Category category1 = createCategory("cat name1");
        Category category2 = createCategory("cat name2");

        restartSession();
        
        
        city = getCityDao().read(city.getId());
        Category byName = categoryDao.findUniqueByName("cat name1");
        assertNotNull(byName);
        assertEquals(category1.getName(), byName.getName());

        restartSession();

        getCityDao().delete(city);
        categoryDao.delete(category1);
        categoryDao.delete(category2);
    }
    
    public void testPostCrud() throws Exception {
        PostDao postDao = getPostDao();
        // Create
        City city = createCity("Test City", "Test Url");
        Category category = createCategory("Category Name");
        Post createPost = createPost(city, category, new Long(639475860), "Post Title", "Post Age", "Post Content", "Post loc", "Post replyAddress");
        
        assertNotNull(createPost.getId());
        Long id = createPost.getId();

        restartSession();

        // Read
        Post foundPost = postDao.read(id);
        assertEquals(createPost.getClId(), foundPost.getClId());
        assertEquals(createPost.getTitle(), foundPost.getTitle());
        assertEquals(createPost.getAge(), foundPost.getAge());
        assertEquals(createPost.getContent(), foundPost.getContent());
        assertEquals(createPost.getLocation(), foundPost.getLocation());
        assertEquals(createPost.getReplyAddress(), foundPost.getReplyAddress());

        restartSession();

        // Update
        String updateTitle = "Update Title";
        String updateAge = "Update Age";
        String updateContent = "Update Content";
        String updateLocation = "Update Location";
        String updateReplyAddress = "Update ReplyAddresss";
        foundPost.setTitle(updateTitle);
        foundPost.setAge(updateAge);
        foundPost.setContent(updateContent);
        foundPost.setLocation(updateLocation);
        foundPost.setReplyAddress(updateReplyAddress);
        postDao.update(foundPost);
        Post updatedPost = postDao.read(id);
        assertEquals(updateTitle, updatedPost.getTitle());
        assertEquals(updateAge, updatedPost.getAge());
        assertEquals(updateContent, updatedPost.getContent());
        assertEquals(updateLocation, updatedPost.getLocation());
        assertEquals(updateReplyAddress, updatedPost.getReplyAddress());

        restartSession();

        // Delete
        postDao.delete(updatedPost);
        restartSession();
        assertNull(postDao.read(id));
        getCityDao().delete(city);
        getCategoryDao().delete(category);
    }

    public void testFindPostByClId() throws Exception {
        PostDao postDao = getPostDao();
        // Create
        City city = createCity("Test City", "Test Url");
        Category category = createCategory("Category Name");
        Post createPost = createPost(city, category, new Long(639475860), "Post Title", "Post Age", "Post Content", "Post loc", "Post replyAddress");
        
        restartSession();
        
        Post posts = postDao.findUniqueByClId(createPost.getClId());
        assertNotNull(posts);
        
        restartSession();
        
        postDao.delete(createPost);
        getCityDao().delete(city);
        getCategoryDao().delete(category);
    }

    protected void setUp() throws Exception {
        openSession();
    }

    protected void tearDown() throws Exception {
        closeSession();
    }

    private void openSession() {
        SessionFactory sessionFactory = getSessionFactory();
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory,
            new SessionHolder(session));
    }

    private void closeSession() {
        SessionFactory sessionFactory = getSessionFactory();
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

    private SessionFactory getSessionFactory() {
        return (SessionFactory) factory.getBean("sessionFactory");
    }

    private CityDao getCityDao() {
        CityDao cityDao = (CityDao) factory.getBean("CityDao");
        return cityDao;
    }

    private CategoryDao getCategoryDao() {
        CategoryDao categoryDao = (CategoryDao) factory.getBean("CategoryDao");
        return categoryDao;
    }

    private PostDao getPostDao() {
        PostDao postDao = (PostDao) factory.getBean("PostDao");
        return postDao;
    }

}
