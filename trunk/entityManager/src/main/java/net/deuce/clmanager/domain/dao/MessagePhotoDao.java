package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.MessagePhoto;
import net.deuce.gendao.GenericDao;

public interface MessagePhotoDao extends GenericDao<MessagePhoto, Long> {
    public List<MessagePhoto> findAll();
}
