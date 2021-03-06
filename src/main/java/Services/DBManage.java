package Services;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vacax on 03/06/16.
 */
public class DBManage<T> {
    public List<JsonElement> padres = new ArrayList<JsonElement>();
    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;


    public DBManage(Class<T> claseEntidad) {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("DefaultPersistanceUnit");
        }
        this.claseEntidad = claseEntidad;

    }

    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    /**
     * Metodo para obtener el valor del campo anotado como @ID.
     * @param entidad
     * @return
     */
    private Object getValorCampo(T entidad){
        if(entidad == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entidad.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) { //preguntando por la anotación ID.
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);

                    System.out.println("Nombre del campo: "+f.getName());
                    System.out.println("Tipo del campo: "+f.getType().getName());
                    System.out.println("Valor del campo: "+valorCampo );

                    return valorCampo;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     *
     * @param entidad
     */
    public boolean crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();
        boolean status = true;
        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();
        }catch (Exception error){
            status = false;
        }finally  {
            em.close();
        }
        return status;
    }

    public boolean crear(List<T> entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();
        boolean status = true;
        try {
            for(T e : entidad){
                em.getTransaction().begin();
                em.persist(e);
                em.getTransaction().commit();
            }

        }catch (Exception error){
            status = false;

        }finally {
            em.close();
        }
        return status;
    }

    /**
     *
     * @param entidad
     */
    public T editar(T entidad) throws PersistenceException{

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();

        }finally {
            em.close();

        }
        return entidad;
    }

    /**
     *
     * @param entidadId
     */
    public boolean eliminar(Object entidadId) throws PersistenceException{
        boolean ok = false;
        if (this.padres.size() > 0 && !this.eliminarPadres(entidadId)) return false;

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }

    public T findBy(Object column, Object value) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            Table table = (Table) claseEntidad.getAnnotations()[1];
            T res = (T) em.createNativeQuery("SELECT * FROM "+ table.name() +" where "+column+ " = "+ value, claseEntidad).getSingleResult();
            return res;
        }catch(Exception e){
            return null;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));

            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean eliminarPadres(Object id) throws PersistenceException{
        boolean ok = true;
        EntityManager em = getEntityManager();
        try{
            System.out.println("eliminarPadres -> ");

            System.out.println(this.padres.size());
            for(JsonElement element : this.padres){
                String tabla = element.getAsJsonObject().get("tabla").getAsString();
                System.out.println(tabla);
                String colum = element.getAsJsonObject().get("colum").getAsString();
                System.out.println(colum);

                em.getTransaction().begin();
//                Object ob = em.createQuery("DELETE FROM "+ tabla +" where "+colum+"= "+id).;
                Object ob = em.createNativeQuery("DELETE FROM "+ tabla +" where "+colum+"= "+id).executeUpdate();
                em.getTransaction().commit();
//                        .createQuery("DELETE FROM "+ tabla +" where "+colum+"= "+id).
//                        .createNativeQuery().executeUpdate();
                System.out.println(ob);
            }
        }catch(Exception e){
            ok = false;
        } finally {
            em.close();
        }
        return ok;
    }


}