package hu.me.iit.malus.thesis.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Netflix's Eureka discovery service for microservices to register themselves into and share metadata.
 *
 * @author Szőke Attila
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    /**
     * The entry point of EurekaDiscoveryServiceApplication.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
