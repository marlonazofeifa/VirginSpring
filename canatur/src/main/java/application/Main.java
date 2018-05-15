package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.IOException;

@SpringBootApplication
// Descomentar para correr con Tomcat y cambiar a tomcat cuando se le da run al tipo de configuración
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class Main  extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
         SpringApplication.run(Main.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

}


