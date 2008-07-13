package net.deuce.clmanager.domain;

/**
 * MessagePhoto generated by hbm2java
 */
public class SavedFile implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String path;
    private String type;
    private UserPreferences userPreferences;

    public SavedFile() {
    }

    public SavedFile(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof SavedFile) {
            b = path.equals(((SavedFile) obj).getPath());
        }
        return b;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
