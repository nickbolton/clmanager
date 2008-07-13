package net.deuce.cl.service;

import java.io.File;
import java.io.InputStream;

import net.deuce.clmanager.domain.UserPreferences;

public interface FileService {
    public InputStream getStream(UserPreferences prefs, String name);
    public File getFile(UserPreferences prefs, String name);
    public void saveFile(UserPreferences prefs, String name, InputStream is);
    public void saveFile(UserPreferences prefs, String name, File f);
    public void deleteFile(UserPreferences prefs, String name);
    public File getRepository(UserPreferences prefs);
}
