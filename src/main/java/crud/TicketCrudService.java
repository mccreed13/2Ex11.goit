package crud;

import configuration.Datasource;
import configuration.Environment;
import entity.Client;
import entity.Planet;
import entity.Ticket;
import org.h2.table.Plan;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TicketCrudService extends JDBCRepository<Ticket>{
    private final Datasource datasource;

    public TicketCrudService(Datasource datasource){this.datasource = datasource;}

    @Override
    public List<Ticket> listAll() {
        return dbCall(session -> session
                .createQuery("select c from Ticket c", Ticket.class)
                .getResultList());
    }

    public Ticket findById(Long id) {
        return dbCall(session -> {
            String queryString = "select c from Ticket c where c.id=:id";
            Query<Ticket> query = session.createQuery(queryString, Ticket.class);
            query.setParameter("id", id);
            Ticket result;
            try {
                result = query.getSingleResult();
            } catch (Exception e) {
                result = null;
            }
            return result;
        });
    }

    public int deleteById(Long id) {
        return dbCall(session -> {
            String queryString = "delete from Ticket c where c.id=:id";
            MutationQuery mutationQuery = session.createMutationQuery(queryString);
            mutationQuery.setParameter("id", id);
            return mutationQuery.executeUpdate();
        });
    }

    private Ticket persist(Ticket ticket, Session session) {
        Ticket saved = session.merge(ticket);
        ticket.setId(saved.getId());
        return ticket;
    }

    public Ticket getById(Long id) {
        Session session = new Datasource(Environment.load()).openSession();
        Ticket ticket = session.get(Ticket.class, id);
        session.close();
        return ticket;
    }

    @Override
    public Ticket save(Ticket ticket) {
        dbVoidCall(session -> persist(ticket, session));
        return ticket;
    }

    @Override
    public int delete(Ticket ticket) {
        Long id = ticket.getId();
        return deleteById(id);
    }

    public void create(Timestamp created_at, Client client,  Planet fromPlanet, Planet toPlanet){
        try {
            checkParams(client, fromPlanet, toPlanet);
            Ticket ticket = new Ticket();
            ticket.setCreated_at(created_at);
            ticket.setClient(client);
            ticket.setFromPlanet(fromPlanet);
            ticket.setToPlanet(toPlanet);
            Session session = new Datasource(Environment.load()).openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void checkParams(Client client, Planet fromPlanet, Planet toPlanet) throws Exception {
        if (client != null) {
            if(fromPlanet != null){
                if(toPlanet!= null){
                    System.out.println("all not null");
                }else throw new Exception("toPlanet is null");
            } else throw new Exception("fromPlanet is null");
        }else throw new Exception("client is null");
    }

}
