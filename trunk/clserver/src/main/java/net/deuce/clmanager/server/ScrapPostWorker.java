package net.deuce.clmanager.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.dao.CityAreaDao;
import net.deuce.clmanager.domain.dao.ImageDao;
import net.deuce.clmanager.domain.dao.PostDao;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;

public class ScrapPostWorker extends AbstractWorker {
    
    private HttpClient client = new HttpClient();
    private String imagePath;
    
    public ScrapPostWorker(SessionFactory sessionFactory, String imagePath, Post post,
        SpamDetector spamDetector, PostDao postDao, ImageDao imageDao, CityAreaDao cityAreaDao) {
        super(sessionFactory, post.getCity(), post.getCategory(), spamDetector, postDao, imageDao, cityAreaDao);
        getResult().setPost(post);
        this.imagePath = imagePath;
    }
    
    @SuppressWarnings("unchecked")
    protected void doRun() throws Exception {
        Post p = getResult().getPost();
        HttpResponse response = Utils.getResponse(p.getUrl(), 10, true, false);
        Document doc = response.getResponse();
        if (response.getResponseCode() == 200 && doc != null) {
            
            Element root = doc.getRootElement();
            
            List<Element> anchors = root.selectNodes("//*[name()='a']");
            for (Element a : anchors) {
                if ("flagged".equals(a.getText())) {
                    p.setFlagged(true);
                    p.setFetched(true);
                    return;
                }
            }
            
            List<Element> images = root.selectNodes("//*[name()='img']");
            Set<Image> imageSet = new HashSet<Image>();
            for (Element e : images) {
                try {
                    String srcAttr = e.attributeValue("src");
                    if (srcAttr != null && srcAttr.startsWith("http://images")) {
                        imageSet.add(fetchAndStoreImage(p, srcAttr));
                        p.setPic(true);
                    }
                } catch (Throwable t) {
                    getLog().error("Failed retrieving image at url: " + e.attributeValue("src"));
                }
            }
            p.setImages(imageSet);
            p.setFetched(true);
        }
    }
    
    private String fixCity(String s) {
        return s.replaceAll(" ", "").replaceAll("/", "");
    }
    
    private String getImageDir(Post p) {
        StringBuffer path = new StringBuffer();
        path.append(imagePath).append('/');
        path.append(fixCity(p.getCity().getName())).append('/');
        path.append(p.getCategory().getExternalName()).append('/');
        String clId = Long.toString(p.getClId());
        for (int i=0; i<clId.length(); i++) {
            path.append(clId.charAt(i));
            if (i < (clId.length()-1)) {
                path.append('/');
            }
        }
        return path.toString();
    }

    private Image fetchAndStoreImage(Post post, String urlStr) throws Exception {
        Exception exception = null;
        int numTries = 0;
        while (numTries++ <= 10) {
            try {
                return _fetchAndStoreImage(post, urlStr);
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            throw exception;
        }
        throw new RuntimeException("Unknown exception");
    }
    
    private Image _fetchAndStoreImage(Post post, String urlStr) throws Exception {
        Image image = new Image();
        URL url = new URL(urlStr);
        String path = getImageDir(post)+url.getPath();
        File f = null;
        
        f = new File(path);
        image.setPath(path.substring(imagePath.length()+1));
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
