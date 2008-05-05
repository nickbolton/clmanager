package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.gendao.GenericDao;

public interface ImageDao extends GenericDao<Image, Long> {
    public List<Image> findByPost(Post post);
}
