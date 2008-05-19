package net.deuce.clmanager.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.sax.SAXTransformerFactory;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.PostDao;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DefaultScraper implements Scraper {
    
    private DateFormat dateFormat;
    private Log log = LogFactory.getLog(getClass());
    private SAXTransformerFactory saxTFactory = null;
    private HttpClient client;
    private Pattern idPattern;
    private Pattern agePattern;
    private Pattern titlePattern;
    private Pattern locationPattern;
    private Pattern datePattern;
    private String imagePath;
    private SpamDetector spamDetector;
    private PostDao postDao;
    private SessionFactory sessionFactory;
    
    public DefaultScraper() {
        client = new HttpClient();
        client.getHttpConnectionManager().
            getParams().setConnectionTimeout(5000);
        
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        idPattern = Pattern.compile(".*/([0-9]+)\\.html");
        agePattern = Pattern.compile(".*- ([0-9]+)");
        titlePattern = Pattern.compile("(.*)- [0-9]* ?-");
        locationPattern = Pattern.compile("\\(?(.*)\\)?");
        datePattern = Pattern.compile(".*Date: ([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]).*");
    }
    
    private String parseId(String s) {
        if (s == null) return null;
        Matcher m = idPattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String parseAge(String s) {
        if (s == null) return null;
        Matcher m = agePattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String parseTitle(String s) {
        if (s == null) return null;
        Matcher m = titlePattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String parseLocation(String s) {
        if (s == null) return null;
        Matcher m = locationPattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
    
    private String parseDate(String s) {
        if (s == null) return null;
        Matcher m = datePattern.matcher(s);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }


    public List<Post> scrape(City city, Category category, String url) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("scraping url: " + url);
        }
        List<Post> posts = new LinkedList<Post>();
        Document doc = Utils.getResponse(url);
        
        try {
            Element root = doc.getRootElement();
            List<Element> nodes = root.selectNodes("//*[name()='p']");
            for (Element p : nodes) {
                Element a = p.element("a");
                if (a == null) continue;
                String href = a.attributeValue("href");
                String clIdStr = parseId(href);
                if (clIdStr == null) {
                    log.warn("No ID for href: " + href);
                    continue;
                }
                String age = parseAge(a.getText());
                String title = parseTitle(a.getText());
                if (title == null) {
                    title = a.getText();
                }
                String location = parseLocation(p.elementText("font"));
                String replyAddress = "pers-" + clIdStr + "@craigslist.org";
                Long clId = Long.valueOf(clIdStr);
                Post post = postDao.findUniqueByClId(clId);
                if (post != null && (post.isFlagged() || post.isSpam())) continue;
                
                post = new Post(clId, title, age, null, location, replyAddress, null, city, category);
                try {
                    scrapePost(post, category);
                    if (post.getClId() > 0) {
                        posts.add(post);
                    }
                } catch (Exception e) {
                    log.error("Failed to scrap post: " + post.buildUrl() + " - " + e.getMessage());
                }
                refreshSession();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter( sw, format );
            writer.write( doc );
            log.error("Failed to scrap url: " + url + "\n" + sw.toString(), e);
        }
        
        return posts;
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
    
    public void scrapePost(Post post, Category category) throws Exception {
        String postUrl = post.buildUrl();
        if (log.isDebugEnabled()) {
            log.debug("scraping post: " + postUrl);
        }
        Document doc = Utils.getResponse(postUrl);
        
        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(sw, format );
        writer.write( doc );
        String html = sw.toString();
        try {
            //System.out.println(html);
            
            if (html.contains("404 Error")) {
                post.setClId(-1L);
                return;
            }
            
            if (html.contains("This posting has been")) {
                post.setFlagged(true);
                return;
            }
            
            String date = parseDate(html);
            if (date != null) {
                post.setDate(dateFormat.parse(date));
            }
            
            int pos1 = html.indexOf("Date:");
            if (pos1 < 0) {
                log.error("No Date found in post: " + postUrl);
                post.setClId(-1L);
                return;
            }
            pos1 = pos1+html.substring(pos1).indexOf("<br");
            int pos2 = pos1+html.substring(pos1).indexOf("<table");
            String content = html.substring(pos1, pos2);
            content = content.replaceAll("<br clear=\"none\"/>", "\n").replaceAll("<br/?>", "\n");
            if (spamDetector.isSuspicious(content)) {
                post.setSpam(true);
            } else {
                //System.out.println("ZZZ it thinks this isn't spam:\n"+content);
            }
            post.setContent(content);
            post.setFetched(true);
            
            post.setCategory(category);
            if (!post.isSpam()) {
                Element root = doc.getRootElement();
                Element imageTable = (Element)root.selectSingleNode("//*[name()='table' and @summary='craigslist hosted images']");
                if (imageTable != null) {
                    List<Element> images = imageTable.selectNodes("*/*/*[name()='img']");
                    Set<Image> imageSet = new HashSet<Image>();
                    for (Element e : images) {
                        try {
                            imageSet.add(fetchAndStoreImage(post, e.attributeValue("src")));
                            post.setPic(true);
                        } catch (Throwable t) {
                            log.error("Failed retrieving image at url: " + e.attributeValue("src"));
                        }
                    }
                    post.setImages(imageSet);
                }
            }
        
        } catch (Exception e) {
            //log.error("Failed to scrap post: " + post.getClId() + " - " +post.getTitle() + "\n" + html, e);
            throw e;
        }
    }
    
    private Image fetchAndStoreImage(Post post, String urlStr) throws Exception {
        Image image = new Image();
        URL url = new URL(urlStr);
        String path = "/"+imagePath+"/"+post.getCity().getName()+"/"+post.getCategory().getExternalName()+"/"+post.getClId()+"/"+url.getPath();
        File f = new File(getClass().getResource("/").getPath(), "../.."+path);
        image.setPath(path);
        image.setUrl(urlStr);
        image.setPost(post);
        
        if (!f.exists()) {
            HttpMethod method = new GetMethod(urlStr);
            method.setFollowRedirects(true);
            String responseBody = null;
            client.executeMethod(method);
            StringWriter sw = new StringWriter();
            InputStream is = null;
            OutputStream fos = null;
            try {
                is = method.getResponseBodyAsStream();
                f.getParentFile().mkdirs();
                fos = new FileOutputStream(f);
                
                byte[] buf = new byte[1024];
                int numRead;
                
                while ((numRead = is.read(buf, 0, 1024)) >= 0) {
                    fos.write(buf, 0, numRead);
                }
            } finally {
                if (is != null) { is.close(); is = null; }
                if (fos != null) { fos.close(); fos = null; }
            }
        }

        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public SpamDetector getSpamDetector() {
        return spamDetector;
    }

    public void setSpamDetector(SpamDetector spamDetector) {
        this.spamDetector = spamDetector;
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
