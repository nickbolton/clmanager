package net.deuce.clmanager.gwt.main.client.model;

import net.mygwt.ui.client.data.Model;

public class Folder extends Model {

    public Folder() {
      
    }
    
    public Folder(String name) {
      set("name", name);
    }

    public Folder(String name, Model[] children) {
      this(name);
      for (int i = 0; i < children.length; i++) {
        add(children[i]);
      }
    }

    public String getName() {
      return getAsString("name");
    }

    public String toString() {
      return getName();
    }
}
