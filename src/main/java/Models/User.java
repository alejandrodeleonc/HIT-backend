package Models;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="USER")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name ;

    @NotNull
    private Date birthDate =  new Date();

    @NotNull
    private String address ;


    @Column(nullable=false,unique=true)
    private String username;

    @NotNull
    private String password;
    @NotNull
    private String email;


    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Url> myUrls = new ArrayList<Url>();

    public User() {
    }

    public User(String name, Date birthDate, String address, String username, String password, String email) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public List<Url> getMyUrls() {
        return myUrls;
    }

    public void setMyUrls(List<Url> myUrls) {
        this.myUrls = myUrls;
    }

    public void addAnUrl(Url url){
        this.myUrls.add(url);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
