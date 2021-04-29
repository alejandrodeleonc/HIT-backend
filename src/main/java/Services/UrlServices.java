package Services;

import Models.Url;
import Models.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class UrlServices extends DBManage<Url>{
    private static UrlServices instancia;

    private UrlServices() { super(Url.class); }

    public static UrlServices getInstancia(){
        if(instancia==null){
            instancia = new UrlServices();
        }
        return instancia;
    }




    public List<Url> getUrlsOfAnUser(User user){
        List<Url> myUrls = new ArrayList<Url>();
        EntityManager em = getEntityManager();
        List<Url> urls = null;
        String sqlStatement = "SELECT * FROM URL INNER JOIN USER_URL UU ON (URL.ID = UU.MYURLS_ID) AND (UU.USER_ID ="+ user.getId()+")";

        System.out.println(sqlStatement);
        try {

            urls = em.createNativeQuery(sqlStatement, Url.class).getResultList();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        return urls.size() > 0 ? urls : null ;



    }
}
