package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.MessageTemplate;
import net.deuce.gendao.GenericDao;

public interface MessageTemplateDao extends GenericDao<MessageTemplate, Long> {
    public List<MessageTemplate> findAll();
}
