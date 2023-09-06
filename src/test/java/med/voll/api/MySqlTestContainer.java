package med.voll.api;

import org.testcontainers.containers.MySQLContainer;

public class MySqlTestContainer extends MySQLContainer<MySqlTestContainer> {

    private static final String IMAGE_VERSION = "mysql:8.0.33";
    private static MySqlTestContainer container;

    private MySqlTestContainer() {
        super(IMAGE_VERSION);
    }

    public static MySqlTestContainer getInstance() {
        if (container == null) {
            container = new MySqlTestContainer();
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }   
    
}
