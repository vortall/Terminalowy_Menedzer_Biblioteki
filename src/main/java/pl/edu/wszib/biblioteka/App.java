package pl.edu.wszib.biblioteka;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.wszib.biblioteka.configuration.AppConfiguration;
import pl.edu.wszib.biblioteka.core.Core;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        AppConfiguration.class);
        Core core = context.getBean(Core.class);
        core.run();
    }
}
