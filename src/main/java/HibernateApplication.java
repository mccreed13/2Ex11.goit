import configuration.Datasource;
import configuration.Environment;
import configuration.FlywayConfigurations;
import crud.ClientCrudService;
import crud.PlanetCrudService;
import crud.TicketCrudService;
import entity.Client;
import entity.Planet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HibernateApplication {
    public static void main(String[] args) throws IOException, SQLException {
        Environment environment = Environment.load();
        new FlywayConfigurations()
                .setup()
                .migrate();
        TicketCrudService ticketCrudService = new TicketCrudService(new Datasource(environment));
        System.out.println(ticketCrudService.listAll());
    }
}
