package crud;

import configuration.Datasource;
import configuration.Environment;
import entity.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientCrudService extends JDBCRepository<Client>{

    private final Datasource datasource;

    public ClientCrudService(Datasource datasource){
        this.datasource = datasource;
    }

    @Override
    public List<Client> listAll() {
        return dbCall(session -> session
                .createQuery("select c from Client c", Client.class)
                .getResultList());
    }
    public Client findById(Long id) {
        return dbCall(session -> {
            String queryString = "select c from Client c where c.id=:id";
            Query<Client> query = session.createQuery(queryString, Client.class);
            query.setParameter("id", id);
            Client result;
            try {
                result = query.getSingleResult();
            } catch (Exception e) {
                result = null;
            }
            return result;
        });
    }
    public void setName(Long id, String name) {
        Client client = findById(id);
        client.setName(name);
    }
    @Override
    public Client save (Client client){
        dbVoidCall(session -> persist(client, session));
        return client;
    }

    private Client persist(Client client, Session session) {
        Client saved = session.merge(client);
        client.setId(saved.getId());
        return client;
    }

    @Override
    public int delete(Client client){
        Long id = client.getId();
        return deleteById(id);
    }

    public int deleteById(Long id) {
        return dbCall(session -> {
           String queryString = "delete from Client c where c.id=:id";
            MutationQuery mutationQuery = session.createMutationQuery(queryString);
            mutationQuery.setParameter("id", id);
            return mutationQuery.executeUpdate();
        });
    }
    public Client getById(Long id) {
        Session session = new Datasource(Environment.load()).openSession();
        Client client = session.get(Client.class, id);
        session.close();
        return client;
    }

    public void create(String name){
        Client client = new Client();
        client.setName(name);
        Session session = new Datasource(Environment.load()).openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(client);
        transaction.commit();
        session.close();
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

}
