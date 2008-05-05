package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.City;
import net.deuce.gendao.GenericDao;

public interface CityDao extends GenericDao<City, Long>
{
    List<City> findAll();
    City findUniqueByName(String name);
    List<City> findBySubscribed(boolean b);
}
