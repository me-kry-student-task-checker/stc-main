package hu.me.iit.malus.thesis.filemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * File management Microservice
 *
 * @author Ilku Krisztian
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class FileManagementServiceApplication {

	/**
	 * The entry point of service.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FileManagementServiceApplication.class, args);
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
				.apis(RequestHandlerSelectors.basePackage("hu.me.iit.malus.thesis.filemanagement"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("FileManagement-service API").description("File Management service API v1.0").build());
	}
}
