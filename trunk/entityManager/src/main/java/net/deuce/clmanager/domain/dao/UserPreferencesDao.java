package net.deuce.clmanager.domain.dao;

import net.deuce.clmanager.domain.UserPreferences;
import net.deuce.gendao.GenericDao;

public interface UserPreferencesDao extends GenericDao<UserPreferences, Long> {
    public UserPreferences findUniqueByUsername(String username);
}
