package hu.me.iit.malus.thesis.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Task Microservice
 *
 * @author Szőke Attila
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TaskServiceApplication {

    /**
     * The entry point of service.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

    /**
     * Swagger config bean.
     *
     * @return the api docket
     */
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("hu.me.iit.malus.thesis.task"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Task-service API").description("Task service API v1.0").build());
    }
}
