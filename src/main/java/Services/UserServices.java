package Services;

import Models.Url;
import Models.User;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kong.unirest.json.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

import java.util.List;

import static Controllers.AuthController.LLAVE_SECRETA;

public class UserServices extends DBManage<User> {
    private static UserServices instancia;

    private UserServices() {
        super(User.class);
    }

    public static UserServices getInstancia() {
        if (instancia == null) {
            instancia = new UserServices();
        }
        return instancia;
    }


    public Boolean verifyLogin(JsonObject userToLogin, User user) {
        boolean status = false;

        if (userToLogin.get("username").getAsString().equals(user.getUsername()) && userToLogin.get("password").getAsString().equals(user.getPassword())) {
            status = true;
        }

        return status;
    }


    public User getUserFromHeader(String header) {
        User user = null;
        try {
            String token = header.split("Bearer", 2)[1];
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(LLAVE_SECRETA))
                    .parseClaimsJws(token).getBody();
            String requestUser = claims.get("usuario").toString();
            user = UserServices.getInstancia().findBy("USERNAME", "'" + requestUser + "'");
            ;
            if (user != null) {
                return user;
            } else {
                return null;
            }

        }catch (Exception ex){
                return null;
        }
    }


    public List<Url> myUrlsWithMoreHits(User user, int cantidad) {
        EntityManager em = getEntityManager();
        List<Url> urls = null;
        String sqlStatement = "SELECT * FROM URL INNER JOIN USER_URL ON (USER_URL.MYURLS_ID = URL.ID) AND (USER_URL.USER_ID = " + user.getId() + ") ORDER BY (URL.RATE) DESC LIMIT " + cantidad;


        System.out.println(sqlStatement);
        try {

            urls = em.createNativeQuery(sqlStatement, Url.class).getResultList();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        return urls;
    }


}
