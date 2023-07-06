package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String CONNECTION_URL = "jdbc:h2:./mydb";
    public static final String CONNECTION_USERNAME = "admin";
    public static final String CONNECTION_PASSWORD = "admin";
    public static final String CONNECTION_AUTOCOMMIT = "com.h2.autocommit";
    public static final String TRANSACTION_ISOLATION = "com.h2.isolation.level";
    public static final String FLYWAY_CONNECTION_URL = "org.flywaydb.url";
    public static final String FLYWAY_USER = "org.flywaydb.user";
    public static final String FLYWAY_PASSWORD = "org.flywaydb.password";

}