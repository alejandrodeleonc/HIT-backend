package Models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="URL_DETAIL_INFO")
public class UrlDetailInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String host;

    private String userAgent;
    private Date dateOfAccess;

    public UrlDetailInfo() {
    }

    public UrlDetailInfo(String host, String userAgent, Date dateOfAccess) {
        this.host = host;
        this.userAgent = userAgent;
        this.dateOfAccess = dateOfAccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getDateOfAccess() {
        return dateOfAccess;
    }

    public void setDateOfAccess(Date dateOfAccess) {
        this.dateOfAccess = dateOfAccess;
    }
}
