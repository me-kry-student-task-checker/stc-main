package hu.me.iit.malus.thesis.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * This is the main class for Cloud Gateway service
 *
 * @author Szőke Attila
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayServiceApplication
{
    /**
     * The entry point of the application
     *
     * @param args the input arguments
     */
    public static void main( String[] args ) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

}
