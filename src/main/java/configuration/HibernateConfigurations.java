package configuration;

import jakarta.persistence.Entity;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

public class HibernateConfigurations {

    public static Configuration setup(Environment environment, Class<?>...classesToRegister){
        Configuration configuration = new Configuration();
        String driverProperty = environment.getProperty("hibernate.connection.driver_class");
        String urlProperty = environment.getProperty("hibernate.connection.url");
        String userProperty = environment.getProperty("hibernate.connection.user");
        String passwordProperty = environment.getProperty("hibernate.connection.password");
        String dialectProperty = environment.getProperty("hibernate.dialect");

        configuration.setProperty(AvailableSettings.DRIVER,driverProperty);
        configuration.setProperty(AvailableSettings.URL,urlProperty);
        configuration.setProperty(AvailableSettings.USER,userProperty);
        configuration.setProperty(AvailableSettings.PASS,passwordProperty);
        configuration.setProperty(AvailableSettings.DIALECT,dialectProperty);
        configuration.setProperty(AvailableSettings.SHOW_SQL,"false");

        Reflections reflections = new Reflections("entity");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
        if(classesToRegister.length == 0) {
            classes.forEach(configuration::addAnnotatedClass);
        }else {
            Arrays.stream(classesToRegister).forEach(configuration::addAnnotatedClass);
        }
        return configuration;
    }


}
