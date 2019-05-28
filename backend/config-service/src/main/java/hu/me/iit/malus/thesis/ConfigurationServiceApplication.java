package hu.me.iit.malus.thesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config service, which serves the config files to the clients.
 *
 * @author Szőke Attila
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigurationServiceApplication
{
    /**
     * The entry point of ConfigurationServiceApplication.
     *
     * @param args the input arguments
     */
    public static void main( String[] args )
    {
        SpringApplication.run(ConfigurationServiceApplication.class, args);
    }
}
