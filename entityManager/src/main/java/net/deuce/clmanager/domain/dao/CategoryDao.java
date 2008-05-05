package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.Category;
import net.deuce.gendao.GenericDao;

public interface CategoryDao extends GenericDao<Category, Long> {
    List<Category> findAll();
    Category findUniqueByName(String name);
    List<Category> findBySubscribed(boolean b);
}
