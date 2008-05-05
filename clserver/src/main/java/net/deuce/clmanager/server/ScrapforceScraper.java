package net.deuce.clmanager.server;

import java.util.List;
import java.util.Properties;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Post;
import Scrapeforge.ScrapeforgeNewHandler;

public class ScrapforceScraper implements Scraper {
    
    private Properties properties = new Properties();
    
    public ScrapforceScraper() {
        properties.setProperty("ScraperProjectName", "");
        properties.setProperty("ScraperOutput", "Map");
        properties.setProperty("ScraperOutputMapHandler", StaticMapHandler.class.getName());
    }

    public List<Post> scrape(City city, Category category, String url) throws Exception {
        properties.setProperty("ScraperProjectURL", url);
        org.xml.sax.XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader("hotsax.html.sax.SaxParser");
        
        ScrapeforgeNewHandler sfh = new ScrapeforgeNewHandler("ScraperRules",properties);
        parser.setContentHandler(sfh);
        parser.parse(url);

        return null;
    }

}
