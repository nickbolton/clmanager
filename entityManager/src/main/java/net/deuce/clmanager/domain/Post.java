package net.deuce.clmanager.domain;
// Generated Apr 11, 2008 1:02:39 PM by Hibernate Tools 3.2.0.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Post generated by hbm2java
 */
public class Post  implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long clId;
    private String title;
    private String age;
    private String content;
    private String location;
    private String replyAddress;
    private Date date;
    private boolean flagged;
    private boolean pic;
    private boolean fetched;
    private boolean spam;
    private String url;
    private City city;
    private CityArea cityArea;
    private Category category;
    private Set<Image> images = new HashSet<Image>(0);

    public Post() {
    }
    
    public Post(Long clId, String title, String age, String content, String location, String replyAddress, Date date, City city, CityArea cityArea, Category category) {
       this.clId = clId;
       this.title = title;
       this.age = age;
       this.content = content;
       this.location = location;
       this.replyAddress = replyAddress;
       this.date = date;
       this.flagged = false;
       this.pic = false;
       this.fetched = false;
       this.city = city;
       this.cityArea = cityArea;
       this.category = category;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAge() {
        return this.age;
    }
    
    public void setAge(String age) {
        this.age = age;
    }
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    public String getReplyAddress() {
        return this.replyAddress;
    }
    
    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    public boolean isFlagged() {
        return this.flagged;
    }
    
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    public boolean isPic() {
        return this.pic;
    }
    
    public void setPic(boolean pic) {
        this.pic = pic;
    }
    public boolean isFetched() {
        return this.fetched;
    }
    
    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }
    public Set<Image> getImages() {
        return this.images;
    }
    
    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Long getClId() {
        return clId;
    }

    public void setClId(Long clId) {
        this.clId = clId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        if (obj != null && obj instanceof Post) {
            b = clId.equals(((Post)obj).getClId());
        }
        return b;
    }

    @Override
    public int hashCode() {
        return clId.hashCode();
    }

    public CityArea getCityArea() {
        return cityArea;
    }

    public void setCityArea(CityArea cityArea) {
        this.cityArea = cityArea;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


