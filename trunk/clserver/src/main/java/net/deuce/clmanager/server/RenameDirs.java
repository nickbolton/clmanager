package net.deuce.clmanager.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class RenameDirs {

    public static void main(String[] args) {
        try {
        File f = new File(args[0]);
        File[] dirs = f.listFiles();
        for (int i=0; i<dirs.length; i++) {
            String filename = dirs[i].getName();
            File subdir = f;
            for (int j=0; j<filename.length(); j++) {
                subdir = new File(subdir, ""+filename.charAt(j));
                subdir.mkdirs();
            }
            
            File[] images = dirs[i].listFiles();
            for (int j=0; j<images.length; j++) {
                FileReader in = new FileReader(images[j]);
                FileWriter out = new FileWriter(new File(subdir, images[j].getName()));
                int c;

                while ((c = in.read()) != -1)
                  out.write(c);

                in.close();
                out.close();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
