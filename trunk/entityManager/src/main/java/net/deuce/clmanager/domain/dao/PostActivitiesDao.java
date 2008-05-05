package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.domain.PostActivities;
import net.deuce.gendao.GenericDao;

public interface PostActivitiesDao extends GenericDao<PostActivities, Long> {
    public List<PostActivities> findByUsername(String username);
    public PostActivities findUniqueByUsernameAndPost(String username, Post post);
}
