package net.deuce.cl.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.Preference;
import net.deuce.clmanager.domain.UserPreferences;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;

public class PostsController extends BaseController {
    
    private static List<Post> EMPTY_RESULT = new ArrayList<Post>(0);
    private static String selectByCityAndCategoryPrefix =
        "select p from net.deuce.clmanager.domain.Post p " +
        "where p.spam = ? and (";
    private static String selectByFavoritesPrefix =
        "select p from net.deuce.clmanager.domain.Post p " +
        "where p.id in (";

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Session session = openSession();
        
        try {
            HttpSession httpSession = request.getSession();
            UserPreferences prefs = (UserPreferences)httpSession.getAttribute("userPreferences");
            if (prefs == null) {
                return UNAUTHENTICATED_ERROR;
            }
            
            int page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            //System.out.println("ZZZ params: " + request.getParameterMap());
            int rp = Integer.parseInt(request.getParameter("rp"));
            String name = "query";
            String query = request.getParameter(name);
            Preference p = prefs.getPreference(name);
            if (query == null || query.trim().length() == 0) {
                removePreference(prefs, name);
            } else {
                setPreference(prefs, name, query);
            }
            
            List<Post> posts = getCache().get(prefs);
            if (posts == null) {
                posts = getPosts(session, prefs);
                if (posts != null && posts.size() > 0) {
                    getCache().put(prefs, posts);
                }
            }
            int startIndex = (page-1)*rp;
            int endIndex = Math.min(startIndex+rp,posts.size());
            
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("posts", buildPostModels(prefs, posts.subList(startIndex, endIndex)));
            model.put("page", page);
            model.put("total", posts.size());
    
            return new ModelAndView("posts", "model", model);
        } catch (Exception e) {
            e.printStackTrace();
            getLog().error(e);
        } finally {
            closeSession();
        }
        return ERROR;
    }
    
    private String addEscapes(String s) {
        return s
            .replaceAll("'", "\\\\'")
            .replaceAll("\n", "<br/>")
            .replaceAll("(?:<br/?><br/?>)(?:<br/?>)+", "<br/>");        
    }
    
    public List<Post> getPosts(Session session, UserPreferences prefs) {
        try {
            Query query = null;
            if (Boolean.parseBoolean(prefs.getPreferenceValue("favorites"))) {
                if (prefs.getFavoriteCount() == 0) return EMPTY_RESULT;
                query = buildFavoritesQuery(session, prefs);
            } else {
                query = buildPostQuery(session, prefs);
            }
            List<Post> allPosts = query.list();
            List<Post> posts = new LinkedList<Post>();
            
            String selectedArea = prefs.getPreferenceValue("selectedArea");
            if (selectedArea != null) {
                long selectedAreaId = Long.parseLong(selectedArea);
                for (Post p : allPosts) {
                    if (p.getCityArea().getId() == selectedAreaId) {
                        posts.add(p);
                    }
                }
            } else {
                posts = allPosts;
            }
            return posts;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            return EMPTY_RESULT;
        }
    }
    
    public List<PostModel> buildPostModels(UserPreferences prefs, List<Post> posts) {
        Map<Post, PostActivities> activityMap = new HashMap<Post, PostActivities>();
        for (PostActivities pa : getPostActivitiesDao().findByUsername(prefs.getUsername())) {
            activityMap.put(pa.getPost(), pa);
        }
        
        List<PostModel> result = new LinkedList<PostModel>();
        for (Post p : posts) {
            PostActivities pa = activityMap.get(p);
            PostModel pm = new PostModel();
            pm.setId(p.getId());
            pm.setAge(p.getAge());
            pm.setClId(""+p.getClId());
            pm.setContent(addEscapes(p.getContent()));
            pm.setDate(p.getDate());
            pm.setFavorite(pa != null && pa.isFavorite());
            pm.setFlagged(p.isFlagged());
            pm.setPic(p.isPic());
            pm.setResponded(pa != null && pa.isResponded());
            pm.setTitle(addEscapes(p.getTitle()));
            pm.setViewed(pa != null && pa.isViewed());
            pm.setPhotos(new LinkedList<Image>(p.getImages()));
            pm.setCity(p.getCity().getName());
            if (p.getCityArea() != null) {
                pm.setCityArea(p.getCityArea().getName());
            }
            pm.setCategory(p.getCategory().getName());
            result.add(pm);
        }
        return result;
    }
    
    private Query buildFavoritesQuery(Session session, UserPreferences prefs) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByFavoritesPrefix);
        
        List<Long> favorites = prefs.getFavorites();
        int favoritesCount=0;
        for (Long id : favorites) {
            if (favoritesCount++>0) {
                sb.append(',');
            }
            sb.append('?');
        }
        sb.append(')');
        String queryStr = prefs.getPreferenceValue("query");
        boolean hasQuery = queryStr != null && queryStr.trim().length() > 0;
        if (hasQuery) {
            sb.append("and (lower(title) like lower(?) or lower(content) like lower(?))");
        }
        sb.append(" order by p.clId desc");

        int index=0;
        Query query = session.createQuery(sb.toString());
        
        for (Long id : favorites) {
            query.setLong(index++, id);
        }
        
        if (hasQuery) {
            queryStr = "%"+queryStr+"%";
            query.setString(index++, queryStr);
            query.setString(index++, queryStr);
        }
        return query;
    }

    private Query buildPostQuery(Session session, UserPreferences prefs) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByCityAndCategoryPrefix);
        int subscriptionGroupsCount=0;
        String selectedCity = prefs.getPreferenceValue("selectedCity");
        String selectedCategory = prefs.getPreferenceValue("selectedCategory");
        for (CitySubscription city : prefs.getCitySubscriptions()) {
            if (!city.isSubscribed() || (selectedCity != null && !city.getCity().getId().toString().equals(selectedCity))) continue;
            for (CategorySubscription category : prefs.getCategorySubscriptions()) {
                if (!category.isSubscribed() || (selectedCategory != null && !category.getCategory().getId().toString().equals(selectedCategory))) continue;
                if (subscriptionGroupsCount++>0) {
                    sb.append(" or ");
                }
                sb.append("(p.city.name = ? and p.category.name = ?)");
            }
        }
        sb.append(")");
        String queryStr = prefs.getPreferenceValue("query");
        boolean hasQuery = queryStr != null && queryStr.trim().length() > 0;
        if (hasQuery) {
            sb.append("and (lower(title) like lower(?) or lower(content) like lower(?))");
        }
        sb.append(" order by p.clId desc");

        int index=0;
        //System.out.println("ZZZ query: " + sb.toString());
        Query query = session.createQuery(sb.toString());
        query.setBoolean(index++, false);
        for (CitySubscription city : prefs.getCitySubscriptions()) {
            if (!city.isSubscribed() || (selectedCity != null && !city.getCity().getId().toString().equals(selectedCity))) continue;
            for (CategorySubscription category : prefs.getCategorySubscriptions()) {
                if (!category.isSubscribed() || (selectedCategory != null && !category.getCategory().getId().toString().equals(selectedCategory))) continue;
                //System.out.println("\tcity: " + city.getCity().getName());
                //System.out.println("\tcat : " + category.getCategory().getName());
                query.setString(index++, city.getCity().getName());
                query.setString(index++, category.getCategory().getName());
            }
        }
        if (hasQuery) {
            queryStr = "%"+queryStr+"%";
            query.setString(index++, queryStr);
            query.setString(index++, queryStr);
        }
        query.setMaxResults(200*subscriptionGroupsCount);
        return query;
    }
    
    public class PostModel {
        private long id;
        private String clId;
        private boolean viewed;
        private boolean favorite;
        private boolean responded;
        private boolean flagged;
        private boolean pic;
        private Date date;
        private String age;
        private String title;
        private String content;
        private String city;
        private String cityArea;
        private String category;
        private List<Image> photos;
        
        public long getId() {
            return id;
        }
        public void setId(long id) {
            this.id = id;
        }
        public String getClId() {
            return clId;
        }
        public void setClId(String clId) {
            this.clId = clId;
        }
        public boolean isViewed() {
            return viewed;
        }
        public void setViewed(boolean viewed) {
            this.viewed = viewed;
        }
        public boolean isFavorite() {
            return favorite;
        }
        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }
        public boolean isResponded() {
            return responded;
        }
        public void setResponded(boolean responded) {
            this.responded = responded;
        }
        public boolean isFlagged() {
            return flagged;
        }
        public void setFlagged(boolean flagged) {
            this.flagged = flagged;
        }
        public boolean isPic() {
            return pic;
        }
        public void setPic(boolean pic) {
            this.pic = pic;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public String getAge() {
            return age;
        }
        public void setAge(String age) {
            this.age = age;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public List<Image> getPhotos() {
            return photos;
        }
        public void setPhotos(List<Image> photos) {
            this.photos = photos;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        public String getCityArea() {
            return cityArea;
        }
        public void setCityArea(String cityArea) {
            this.cityArea = cityArea;
        }
    
    }

}

