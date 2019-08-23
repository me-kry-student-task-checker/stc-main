package hu.me.iit.malus.thesis.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main class of User service
 *
 * @author Javorek Dénes
 */
@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication
{
    /**
     * The entry point of the application
     * @param args the input arguments
     */
    public static void main( String[] args ) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
