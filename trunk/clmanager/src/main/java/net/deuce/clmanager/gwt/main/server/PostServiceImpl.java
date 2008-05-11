package net.deuce.clmanager.gwt.main.server;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.gwt.main.client.PostService;
import net.deuce.clmanager.gwt.main.client.model.PostFilter;
import net.deuce.clmanager.gwt.main.client.model.PostModel;
import net.deuce.clmanager.gwt.main.client.model.PostingGroup;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import com.google.gwt.user.client.rpc.SerializationException;

public class PostServiceImpl extends BaseServiceImpl implements PostService {
    
    private static DateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private static List EMPTY_RESULT = new ArrayList(0);
    
    private static String selectByCityAndCategoryPrefix =
        "select p from net.deuce.clmanager.domain.Post p " +
        "where p.spam = 'f' and (";
    
    public PostServiceImpl() {
    }
    
    public String processCall(String payload) throws SerializationException {
        try {
        return super.processCall(payload);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new SerializationException(t);
        }
    }

    public void flagPost(Long id) throws Exception {
        Post p = getPostDao().read(id);
        if (p == null) {
            getLog().warn("No post exists with id: " + id);
            return;
        }
        StringBuffer url = new StringBuffer(p.getCity().getUrl());
        if (!url.toString().endsWith("/")) {
            url.append('/');
        }
        url.append("flag/?flagCode=15&postingID=").append(id);
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url.toString());
        method.setFollowRedirects(true);
        client.executeMethod(method);
        StringWriter sw = new StringWriter();
        if (method.getStatusCode()!=200) {
            getLog().error("Response code (" + method.getStatusCode() + ") for url: " + url.toString());
        }
    }
    
    private Query buildPostQuery(Session session, List postingGroups) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByCityAndCategoryPrefix);
        int count=0;
        for (int i=0; i<postingGroups.size(); i++) {
            if (((PostingGroup)postingGroups.get(i)).isActive()) {
                if (count++>0) {
                    sb.append(" or ");
                }
                sb.append("(p.city.name = ? and p.category.name = ?)");
            }
        }
        sb.append(") order by p.clId desc");
        
        int index=0;
        Query query = session.createQuery(sb.toString());
        StringBuffer msg = new StringBuffer("Post query: " + query.getQueryString());
        msg.append('\n');
        for (int i=0; i<postingGroups.size(); i++) {
            PostingGroup pg = (PostingGroup)postingGroups.get(i);
            if (pg.isActive()) {
                msg.append("\tString: ").append(pg.getCity()).append('\n');
                query.setString(index++, pg.getCity());
                msg.append("\tString: ").append(pg.getCategory()).append('\n');
                query.setString(index++, pg.getCategory());
            }
        }
        System.out.println(msg.toString());
        query.setMaxResults(200);
        return query;
    }
    
    private Query buildNewPostQuery(Session session, List postingGroups, PostFilter postFilter) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByCityAndCategoryPrefix);
        int count=0;
        for (int i=0; i<postingGroups.size(); i++) {
            if (((PostingGroup)postingGroups.get(i)).isActive()) {
                if (count++>0) {
                    sb.append(" or ");
                }
                sb.append("(p.clId > ? and p.city.name = ? and p.category.name = ?)");
            }
        }
        sb.append(")");
        if (postFilter != null) {
            if (postFilter.getMinAge() > 0) {
                sb.append(" and p.age >= ?");
            }
            if (postFilter.getMaxAge() < 99) {
                sb.append(" and p.age <= ?");
            }
            if (postFilter.getNoFlagged()) {
                sb.append(" and p.flagged = ?");
            }
            if (postFilter.getPhotosOnly()) {
                sb.append(" and p.pic = ?");
            }
        }
        sb.append(" order by p.clId desc");
        
        int index=0;
        Query query = session.createQuery(sb.toString());
        StringBuffer msg = new StringBuffer("new post query: " + query.getQueryString());
        msg.append('\n');
        for (int i=0; i<postingGroups.size(); i++) {
            PostingGroup pg = (PostingGroup)postingGroups.get(i);
            if (pg.isActive()) {
                msg.append("\tLong: ").append(pg.getLastFetched().longValue()).append('\n');
                query.setLong(index++, pg.getLastFetched().longValue());
                msg.append("\tString: ").append(pg.getCity()).append('\n');
                query.setString(index++, pg.getCity());
                msg.append("\tString: ").append(pg.getCategory()).append('\n');
                query.setString(index++, pg.getCategory());
            }
        }
        if (postFilter != null) {
            if (postFilter.getMinAge() > 0) {
                msg.append("\tInteger: ").append(postFilter.getMinAge()).append('\n');
                query.setInteger(index++, postFilter.getMinAge());
            }
            if (postFilter.getMaxAge() < 99) {
                msg.append("\tInteger: ").append(postFilter.getMaxAge()).append('\n');
                query.setInteger(index++, postFilter.getMaxAge());
            }
            if (postFilter.getNoFlagged()) {
                msg.append("\tBoolean: false").append('\n');
                query.setBoolean(index++, false);
            }
            if (postFilter.getPhotosOnly()) {
                msg.append("\tBoolean: true").append('\n');
                query.setBoolean(index++, true);
            }
        }
        query.setMaxResults(200);
        System.out.println(msg.toString());
        return query;
    }
    
    private Query buildMarkAllViewedPostQuery(Session session, List postingGroups) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByCityAndCategoryPrefix);
        int count=0;
        for (int i=0; i<postingGroups.size(); i++) {
            if (((PostingGroup)postingGroups.get(i)).isActive()) {
                if (count++>0) {
                    sb.append(" or ");
                }
                sb.append("(p.clId < ? and p.city.name = ? and p.category.name = ?)");
            }
        }
        sb.append(") order by p.clId desc");
        
        int index=0;
        Query query = session.createQuery(sb.toString());
        for (int i=0; i<postingGroups.size(); i++) {
            PostingGroup pg = (PostingGroup)postingGroups.get(i);
            if (pg.isActive()) {
                query.setLong(index++, pg.getLastFetched().longValue());
                query.setString(index++, pg.getCity());
                query.setString(index++, pg.getCategory());
            }
        }
        query.setMaxResults(200);
        return query;
    }
    
    private boolean postContainsSearchTerm(PostModel p, String s) {
        boolean result = false;
        Iterator itr = p.getPropertyNames();
        while (!result && itr.hasNext()) {
            String value = p.getAsString((String) itr.next())
                .toLowerCase();
            result = value.indexOf(s) >= 0;
        }
        return result;
    }
    
    private List<PostModel> filterPosts(List<Post> posts, List<PostActivities> activities, PostFilter postFilter) {
        Map<Post, PostActivities> activityMap = new HashMap<Post, PostActivities>();
        for (PostActivities pa : activities) {
            activityMap.put(pa.getPost(), pa);
        }
        List<PostModel> result = new ArrayList<PostModel>();
        for (Post p : posts) {
            PostActivities pa = activityMap.get(p);
            if (!p.isFlagged() || (pa != null && pa.isViewed())) {
                PostModel model = buildPostModel(p, pa);
                if (postFilter == null || 
                    ((postFilter.getSearchTerm().trim().length() == 0 || postContainsSearchTerm(model, postFilter.getSearchTerm().trim())) &&
                        (!postFilter.getUnreadOnly() || pa.isViewed()))) {
                    result.add(model);
                }
            }
        }
        return result;
    }
    
    public List getPosts(String username, List postingGroups) throws Exception {
        Session session = openSession();
           
        try {
            Query query = buildPostQuery(session, postingGroups);
            List<Post> l = query.list();
            if (l.size() > 0) {
                List<PostActivities> activities = getPostActivitiesDao().findByUsername(username);
                List result = filterPosts(l, activities, null);
                System.out.println("ZZZ returning " + result.size() + " posts for postingGroups: " + postingGroups);
                return result;
            }
            System.out.println("ZZZ returning EMPTY_RESULT for postingGroups: " + postingGroups);
            return EMPTY_RESULT;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    public Integer getNewPostCount(String username, List postingGroups, PostFilter postFilter) throws Exception {
        Integer result = getNewPosts(username, postingGroups, postFilter).size();
        if (postFilter != null) {
            System.out.println("ZZZ new post count: " + result.intValue() + " for postingGroups: " + postingGroups +
                " and " + postFilter);
        } else {
            System.out.println("ZZZ new post count: " + result.intValue() + " for postingGroups: " + postingGroups);
        }
        return result;
    }
    
    public List getNewPosts(String username, List postingGroups, PostFilter postFilter) throws Exception {
        Session session = openSession();
           
        try {
            Query query = buildNewPostQuery(session, postingGroups, postFilter);
            List<Post> l = query.list();
            if (l.size() > 0) {
                List<PostActivities> activities = getPostActivitiesDao().findByUsername(username);
                List result = filterPosts(l, activities, postFilter);
                System.out.println("ZZZ get new posts returning " + result.size() + " posts for postingGroups: " + postingGroups);
                return result;
            }
            return EMPTY_RESULT;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    public PostModel getPost(String username, Long id) throws Exception {
        openSession();
        try {
            Post p = getPostDao().read(id);
            if (p != null) {
                PostActivities pa = getPostActivitiesDao().findUniqueByUsernameAndPost(username, p);
                return buildPostModel(p, pa);
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public PostModel getPostByClId(String username, long id) throws Exception {
        openSession();
        try {
            Post p = getPostDao().findUniqueByClId(id);
            if (p != null) {
                PostActivities pa = getPostActivitiesDao().findUniqueByUsernameAndPost(username, p);
                return buildPostModel(p, pa);
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    private PostActivities getPostActivities(String username, Post post) throws Exception {
        PostActivities pa = getPostActivitiesDao().findUniqueByUsernameAndPost(username, post);
        if (pa == null) {
            pa = new PostActivities();
            pa.setPost(post);
            pa.setUsername(username);
            getPostActivitiesDao().create(pa);
        }
        return pa;
    }
    
    public void setViewed(String username, Long id) throws Exception {
        openSession();
        try {
            Post post = getPostDao().read(id);
            if (post == null) {
                throw new RuntimeException("Post not found with id: "+ id);
            }
            PostActivities postActivities = getPostActivities(username, post);
            postActivities.setViewed(true);
            getPostActivitiesDao().update(postActivities);
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void setReplied(String username, Long id) throws Exception {
        openSession();
        try {
            Post post = getPostDao().read(id);
            if (post == null) {
                throw new RuntimeException("Post not found with id: "+ id);
            }
            PostActivities postActivities = getPostActivities(username, post);
            postActivities.setResponded(true);
            getPostActivitiesDao().update(postActivities);
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    public void setAllViewed(String username, List postingGroups) throws Exception {
        Session session = openSession();
           
        try {
            ScrollableResults posts = buildMarkAllViewedPostQuery(session, postingGroups)
                .setCacheMode(CacheMode.IGNORE)
                .scroll(ScrollMode.FORWARD_ONLY);
            int count=0;
            while ( posts.next() ) {
                Post post = (Post) posts.get(0);
                
                PostActivities postActivities = getPostActivities(username, post);
                postActivities.setViewed(true);
                getPostActivitiesDao().update(postActivities);

                if ( ++count % 20 == 0 ) {
                    //flush a batch of updates and release memory:
                    session.flush();
                    session.clear();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    private PostModel buildPostModel(Post p, PostActivities pa) {
        PostModel model = new PostModel();
        model.setId(p.getId());
        model.setClId(p.getClId());
        model.setTitle(safeString(p.getTitle()));
        model.setAge(safeString(p.getAge()));
        model.setUrl(safeString(p.buildUrl()));
        model.setContent(safeString(p.getContent()));
        model.setLocation(safeString(p.getLocation()));
        model.setReplyAddress(safeString(p.getReplyAddress()));
        //model.setDate((p.getDate() != null ? dateFormat.format(p.getDate()) : ""));
        model.setDate(p.getDate());
        model.setFlagged(safeBoolean(p.isFlagged()));
        model.setPic(safeBoolean(p.isPic()));
        model.setFetched(safeBoolean(p.isFetched()));
        if (pa != null) {
            model.setViewed(safeBoolean(pa.isViewed()));
            model.setResponded(safeBoolean(pa.isResponded()));
        } else {
            model.setViewed(Boolean.FALSE);
            model.setResponded(Boolean.FALSE);
        }
        model.setSpam(safeBoolean(p.isSpam()));
        model.setCity(p.getCity().getName());
        model.setCategory(p.getCategory().getName());
        return model;
 
    }

}
