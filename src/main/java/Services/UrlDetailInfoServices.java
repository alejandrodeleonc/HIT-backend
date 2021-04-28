package Services;

import Models.Url;
import Models.UrlDetailInfo;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UrlDetailInfoServices  extends DBManage<UrlDetailInfo> {
    private static UrlDetailInfoServices instancia;

    private UrlDetailInfoServices() {
        super(UrlDetailInfo.class);
    }

    public static UrlDetailInfoServices getInstancia() {
        if (instancia == null) {
            instancia = new UrlDetailInfoServices();
        }
        return instancia;
    }


    public List<String> getTheTopAgents(int cantidad){
        List<String> topAgents = new ArrayList<String>();
        String sql ="SELECT USERAGENT  FROM URL_DETAIL_INFO UDI GROUP BY USERAGENT ORDER BY COUNT(*) DESC LIMIT "+cantidad;
        EntityManager em = getEntityManager();
        int i  = 0;
        try{
            List<Object[]> results = em.createNativeQuery(sql).getResultList();
            for (Object obj : results) {
                System.out.println(obj);
                topAgents.add(String.valueOf(obj));
                i++;
            }


        }catch(Exception e){

        } finally {
            em.close();
        }

        return topAgents;
    }
}