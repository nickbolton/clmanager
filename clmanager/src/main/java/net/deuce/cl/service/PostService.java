package net.deuce.cl.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.deuce.clmanager.domain.CategorySubscription;
import net.deuce.clmanager.domain.CitySubscription;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.clmanager.domain.UserPreferences;

import org.hibernate.Query;
import org.hibernate.Session;

public class PostService extends BaseServlet {

    private static List<Post> EMPTY_RESULT = new ArrayList<Post>(0);
    private static DateFormat df = new SimpleDateFormat("MM/dd");
    private static String selectByCityAndCategoryPrefix = "select p from net.deuce.clmanager.domain.Post p "
        + "where p.spam = ? and (";

    private static final long serialVersionUID = 1L;

    @Override
    protected void doService(HttpServletRequest req, HttpServletResponse res)
        throws Exception {

        HttpSession session = req.getSession();
        UserPreferences prefs = (UserPreferences) session.getAttribute("userPreferences");
        prefs = getUserPreferencesDao().findUniqueByUsername("larvell.jones@mac.com");
        if (prefs == null) {
            return;
        }
        
        res.setContentType("text/x-json");

        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        
        /*
        out.print("{\n");
        out.print("page: 1,\n");
        out.print("total: 4,\n");
        out.print("rows: [");
        
        for (int i=0; i<4; i++) {
            if (i>0) {
                out.print(",");
            }
            out.print("\n{");
            out.print("id:'"+i+"',");
            out.print("cell:['F'");
            out.print(",'3/1'");
            out.print(",'R'");
            out.print(",'P'");
            out.print(",'34'");
            out.print(",'F'");
            out.print(",'Sample Title-"+i+"'");
            out.print(",'"+i+"'");
            out.print(",'Sample Message "+i+"']");
            out.print("}");
        }
        out.print("]\n");
        out.print("}");
        */
        
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        int rp = Integer.parseInt(req.getParameter("rp"));
        
        List<Post> posts = getPosts(prefs);
        printJsonHeader(page, out, posts);
        printJsonRecords(out, prefs.getUsername(), posts, page, rp);
        printJsonFooter(out);
        //System.out.println("ZZZ out:\n"+sw.toString());
        res.getWriter().print(sw.toString());
    }
    
    private void printJsonRecords(PrintWriter out, String username, List<Post> posts, int page, int rp) {
        Map<Post, PostActivities> activityMap = new HashMap<Post, PostActivities>();
        for (PostActivities pa : getPostActivitiesDao().findByUsername(username)) {
            activityMap.put(pa.getPost(), pa);
        }
        
        int count=0;
        int startIndex = (page-1)*rp;
        for (int i=(page-1)*rp; i<Math.min(startIndex+rp,posts.size()); i++) {
            Post p = posts.get(i);
            PostActivities pa = activityMap.get(p);
            if (count++>0) {
                out.print(",");
            }
            out.print("\n{");
            out.print("id:'"+p.getClId()+"',");
            out.print("cell:['"+buildFlagsMask(p, pa)+"'");
            out.print(",'"+df.format(p.getDate())+"'");
            out.print(",'"+(p.getAge() != null ? p.getAge() : "")+"'");
            out.print(",'"+addEscapes(p.getTitle())+"'");
            out.print(",'"+p.getClId()+"'");
            out.print(",'"+addEscapes(p.getContent())+"']");
            out.print("}");
        }
    }
    
    private String buildFlagsMask(Post p, PostActivities pa) {
        StringBuffer sb = new StringBuffer();
        /*
        sb.append(pa != null && pa.isViewed() ? 1 : 0);
        sb.append(pa != null && pa.isFavorite() ? 1 : 0);
        sb.append(pa != null && pa.isResponded() ? 1 : 0);
        sb.append(p.isFlagged() ? 1 : 0);
        sb.append(p.isPic() ? 1 : 0);
        */
        sb.append("<div id=\"flags-"+p.getClId()+"\" class=\"post-flags\">");
        sb.append("<div id=\"viewed-"+p.getClId()+"\" class=\"post-viewed\"><img src=\"images/icons/post-viewed.png\"/></div>");
        sb.append("<div id=\"fav-"+p.getClId()+"\" class=\"post-fav\"><img src=\"images/icons/post-fav.png\"/></div>");
        sb.append("<div id=\"res-"+p.getClId()+"\" class=\"post-res\"><img src=\"images/icons/post-res.png\"/></div>");
        sb.append("<div id=\"flag-"+p.getClId()+"\" class=\"post-flag\"><img src=\"images/icons/post-flag.png\"/></div>");
        sb.append("<div id=\"pic-"+p.getClId()+"\" class=\"post-pic\"><img src=\"images/icons/post-pic.png\"/></div>");
        sb.append("</div>");
        return sb.toString();
    }
    
    private String addEscapes(String s) {
        return s
            .replaceAll("'", "\\\\'")
            .replaceAll("\n", "<br/>");
    }
    
    private void printJsonHeader(int page, PrintWriter out, List<Post> posts) {
        out.print("{\n");
        out.print("page: "+page+",\n");
        out.print("total: "+posts.size()+",\n");
        //out.print("total: 4,\n");
        out.print("rows: [");
    }
    
    private void printJsonFooter(PrintWriter out) {
        out.print("]\n");
        out.print("}");
    }

    public List<Post> getPosts(UserPreferences prefs) {
        Session session = openSession();

        try {
            Query query = buildPostQuery(session, prefs);
            return query.list();
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            return EMPTY_RESULT;
        } finally {
            closeSession();
        }
    }

    private Query buildPostQuery(Session session, UserPreferences prefs) {
        StringBuffer sb = new StringBuffer();
        sb.append(selectByCityAndCategoryPrefix);
        int count = 0;
        for (CitySubscription city : prefs.getCitySubscriptions()) {
            for (CategorySubscription category : prefs
                .getCategorySubscriptions()) {
                if (count++ > 0) {
                    sb.append(" or ");
                }
                sb.append("(p.city.name = ? and p.category.name = ?)");
            }
        }
        sb.append(") order by p.clId desc");

        int index = 0;
        Query query = session.createQuery(sb.toString());
        query.setBoolean(index++, false);
        for (CitySubscription city : prefs.getCitySubscriptions()) {
            for (CategorySubscription category : prefs
                .getCategorySubscriptions()) {
                query.setString(index++, city.getCity().getName());
                query.setString(index++, category.getCategory().getName());
            }
        }
        query.setMaxResults(200);
        return query;
    }
}
