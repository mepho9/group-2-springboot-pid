package be.iccbxl.pid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservationsSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationsSpringBootApplication.class, args);
	}

}
