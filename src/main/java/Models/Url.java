package Models;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="URL")
public class Url implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String longUrl;

    @Column(nullable=false,unique=true)
    private String shortUrl;
    private int rate;


    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UrlDetailInfo> myDetails = new ArrayList<UrlDetailInfo>();

    public Url() {
    }

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.rate = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void addRate(){
        rate++;
    }

    public List<UrlDetailInfo> getMyDetails() {
        return myDetails;
    }

    public void setMyDetails(List<UrlDetailInfo> myDetails) {
        this.myDetails = myDetails;
    }

    public void addDetail(UrlDetailInfo detail){
        this.myDetails.add(detail);
    }


}
