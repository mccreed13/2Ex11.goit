package crud;

import configuration.Datasource;
import configuration.Environment;
import crud.exception.PlanetIdException;
import entity.Planet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlanetCrudService extends JDBCRepository<Planet>{
    private final Datasource datasource;

    public PlanetCrudService(Datasource datasource){
        this.datasource = datasource;
    }

    @Override
    public List<Planet> listAll() {
        return dbCall(session -> session
                .createQuery("select c from Planet c", Planet.class)
                .getResultList());
    }
    public Planet findById(String id) {
        return dbCall(session -> {
            String queryString = "select c from Planet c where c.id=:id";
            Query<Planet> query = session.createQuery(queryString, Planet.class);
            query.setParameter("id", id);
            Planet result;
            try {
                result = query.getSingleResult();
            } catch (Exception e) {
                result = null;
            }
            return result;
        });
    }

    public void setName(String id, String name) {
        Planet client = findById(id);
        client.setName(name);
    }

    public void setId(String oldId, String newId) {
        try {
            isIdRight(newId);
            Planet client = findById(oldId);
            client.setId(newId);
        } catch (PlanetIdException e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Planet save (Planet planet){
        dbVoidCall(session -> persist(planet, session));
        return planet;
    }

    private Planet persist(Planet planet, Session session) {
        Planet saved = session.merge(planet);
        planet.setId(saved.getId());
        return planet;
    }

    @Override
    public int delete(Planet planet){
        String id = planet.getId();
        return deleteById(id);
    }

    public int deleteById(String id) {
        return dbCall(session -> {
            String queryString = "delete from Planet c where c.id=:id";
            MutationQuery mutationQuery = session.createMutationQuery(queryString);
            mutationQuery.setParameter("id", id);
            return mutationQuery.executeUpdate();
        });
    }

    public Planet getById(String id) {
        Session session = new Datasource(Environment.load()).openSession();
        Planet planet = session.get(Planet.class, id);
        session.close();
        return planet;
    }

    public void create(String id,String name){
        Planet planet = new Planet();
        try {
            isIdRight(id);
            planet.setId(id);
            planet.setName(name);
            Session session = new Datasource(Environment.load()).openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(planet);
            transaction.commit();
            session.close();
        }catch (PlanetIdException e){
            System.out.println(e.getMessage());
        }
    }

    private <R> R dbCall(Function<Session, R> function) {
        try (Session session = datasource.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dbVoidCall(Consumer<Session> function) {
        try (Session session = datasource.openSession()) {
            Transaction transaction = session.beginTransaction();
            function.accept(session);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void isIdRight(String id){
        if(id == null || id.matches("[^A-Z\\d]+")){
            throw new PlanetIdException("Format is not right");
        }
    }
}

