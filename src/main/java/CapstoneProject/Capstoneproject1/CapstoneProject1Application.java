package CapstoneProject.Capstoneproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing /* 자동생성("date")을 위한 annotation */
@SpringBootApplication
public class CapstoneProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneProject1Application.class, args);
	}

}
