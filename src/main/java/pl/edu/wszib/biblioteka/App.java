package pl.edu.wszib.biblioteka;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.wszib.biblioteka.configuration.AppConfiguration;
import pl.edu.wszib.biblioteka.core.Core;
import pl.edu.wszib.biblioteka.database.DatabaseConnection;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        AppConfiguration.class);

        DatabaseConnection databaseConnection = context.getBean(DatabaseConnection.class);
        databaseConnection.initDatabase();

        Core core = context.getBean(Core.class);
        core.run();
    }
}
