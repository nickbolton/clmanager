package net.deuce.clmanager.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CityArea;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.CityAreaDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.hibernate.SessionFactory;

public class ScrapCityCategoryWorker extends AbstractWorker {
    
    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public ScrapCityCategoryWorker(SessionFactory sessionFactory, City city,
        Category category, SpamDetector spamDetector, PostDao postDao, ImageDao imageDao, CityAreaDao cityAreaDao) {
        super(sessionFactory, city, category, spamDetector, postDao, imageDao, cityAreaDao);
    }
    
    @SuppressWarnings("unchecked")
    protected void doRun() throws Exception {
        HttpResponse response = Utils.getRssResponse(buildUrl(), 10, true, false);
        Document doc = response.getResponse();
        if (response.getResponseCode() == 200 && doc != null) {
            
            Element root = doc.getRootElement();
            List<Node> nodes = root.elements("item");
            for (Node n : nodes) {
                Element e = (Element)n;
                
                /*StringWriter elementText = new StringWriter();
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter( elementText, format );
                writer.write( e );
                */

                Post p = new Post();
                p.setCategory(getCategory());
                p.setCity(getCity());
                
                String url = e.elementText("source");
                p.setUrl(url);
                parseCityArea(p, url);
                p.setClId(Long.parseLong(parseId(url)));
                p.setReplyAddress("pers-" + p.getClId() + "@craigslist.org");
                
                String title = e.elementText("title");
                Matcher m = getTitlePattern().matcher(title);
                if (m.find()) {
                    p.setTitle(m.group(1));
                    p.setLocation(m.group(2));
                    p.setAge(m.group(3));
                } else {
                    m = getTitlePattern2().matcher(title);
                    if (m.find()) {
                        p.setTitle(m.group(1));
                        p.setAge(m.group(2));
                    }
                }
                p.setContent(e.elementText("description"));
                if (e.elementText("date") == null || e.elementText("date").trim().length() == 0) {
                    p.setDate(new Date());
                } else {
                    p.setDate(dateFormat.parse(e.elementText("date")));
                }
                if (getSpamDetector().isSuspicious(p.getContent())) {
                    p.setSpam(true);
                }
                //System.out.println("ZZZ " + getClass().getSimpleName() + " returning post: " + p.getClId() + " - " + p.getTitle());
                getResult().addPost(p);
            }
        }
    }
    
    private void parseCityArea(Post p, String url) {
        String path = url.substring(getCity().getUrl().length());
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        String area = null;
        String[] split = path.split("/");
        if (split.length == 3) {
            area = split[0];
        }
        
        if (area != null) {
            CityArea ca = getCityAreaDao().findUniqueByExternalName(p.getCity(), area);
            if (ca == null) {
                ca = new CityArea();
                ca.setCity(p.getCity());
                ca.setExternalName(area);
                ca.setName(area);
                getCityAreaDao().create(ca);
            }
            p.setCityArea(ca);
        }
    }

    private String buildUrl() {
        String cityUrl = getCity().getUrl();
        StringBuffer url = new StringBuffer(cityUrl);
        if (!cityUrl.endsWith("/")) {
            url.append('/');
        }
        url.append(getCategory().getExternalName()).append('/');
        url.append("index.rss");
        return url.toString();
    }

}