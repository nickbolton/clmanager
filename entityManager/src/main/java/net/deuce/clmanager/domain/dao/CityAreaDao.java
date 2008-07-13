package net.deuce.clmanager.domain.dao;

import java.util.List;

import net.deuce.clmanager.domain.City;
import net.deuce.clmanager.domain.CityArea;
import net.deuce.gendao.GenericDao;

public interface CityAreaDao extends GenericDao<CityArea, Long> {
    List<CityArea> findAll();
    CityArea findUniqueByName(City c, String name);
    CityArea findUniqueByExternalName(City c, String name);
    List<CityArea> findByCity(City c);
    List<CityArea> findByCityName(String name);
}
