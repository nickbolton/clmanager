package net.deuce.clmanager.server;

import java.util.List;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.Post;

public interface Scraper {
    public List<Post> scrape(City city, Category category, String url) throws Exception;
    public Post scrapePost(City city, Category category, long clId) throws Exception;
}
