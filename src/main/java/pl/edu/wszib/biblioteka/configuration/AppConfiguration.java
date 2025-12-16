package pl.edu.wszib.biblioteka.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@ComponentScan({
        "pl.edu.wszib.biblioteka.authentication",
        "pl.edu.wszib.biblioteka.core",
        "pl.edu.wszib.biblioteka.database",
        "pl.edu.wszib.biblioteka.gui"
})
public class AppConfiguration {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}