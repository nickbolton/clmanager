package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.Post;
import net.deuce.gendao.GenericDao;

public interface PostDao extends GenericDao<Post, Long> {
    public Post findUniqueByClId(long clId);
}
