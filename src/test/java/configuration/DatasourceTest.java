package configuration;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatasourceTest {

    @Test
    void openSession() {
        Datasource datasource  = new Datasource(Environment.load());
        Session session = datasource.openSession();
        boolean isOpen = session.isOpen();
        Assertions.assertTrue(isOpen);

    }
}