package hu.me.iit.malus.thesis.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Course Microservice
 *
 * @author Javorek Dénes
 */
@SpringBootApplication
public class CourseServiceApplication {

	/**
	 * The entry point of service.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}

}
