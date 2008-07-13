package net.deuce.cl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.deuce.clmanager.domain.UserPreferences;

public class DefaultFileService implements FileService {
    
    private File baseFileDir;
    private MessageDigest md = null;
    
    private String hash(String s) {
        try {
            if (md == null) {
                md = MessageDigest.getInstance( "MD5" );
            }
            byte[] bytes = md.digest( s.getBytes() );
            
            StringBuffer sb = new StringBuffer();
            for( int i=0; i<bytes.length; i++ ) {
              sb.append( ( int )( 0x00FF & bytes[i] ) );
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
    
    public File getRepository(UserPreferences prefs) {
        StringBuffer sb = new StringBuffer();
        String username = prefs.getUsername();
        String hash = hash(username);
        sb.append(hash.charAt(0));
        for (int i=1; i<hash.length(); i++) {
            sb.append('/').append(hash.charAt(i));
        }
        sb.append('/').append(prefs.getId());
        File f = new File(baseFileDir, sb.toString());
        f.mkdirs();
        return f;
    }

    public void deleteFile(UserPreferences prefs, String name) {
        File f = getFile(prefs, name);
        if (f != null) f.delete();
    }
    
    public File getFile(UserPreferences prefs, String name) {
        File f = new File(getRepository(prefs), name);
        if (f.exists()) return f;
        return null;
    }

    public InputStream getStream(UserPreferences prefs, String name) {
        File f = getFile(prefs, name);;
        if (f != null) {
            try {
                return new FileInputStream(f);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void saveFile(UserPreferences prefs, String name, InputStream is) {
        File f = new File(getRepository(prefs), name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            byte[] buf = new byte[1024];
            int numRead;
            
            while ( (numRead = is.read(buf, 0, 1024)) >= 0) {
                fos.write(buf, 0, numRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {if (fos != null) fos.close();} catch (Exception e) {}
        }
    }

    public void saveFile(UserPreferences prefs, String name, File f) {
        try {
            saveFile(prefs, name, new FileInputStream(f));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public File getBaseFileDir() {
        return baseFileDir;
    }

    public void setBaseFileDir(File baseFileDir) {
        this.baseFileDir = baseFileDir;
    }

}
