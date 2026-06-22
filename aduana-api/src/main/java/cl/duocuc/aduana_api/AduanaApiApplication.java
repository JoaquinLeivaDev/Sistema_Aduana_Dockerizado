package cl.duocuc.aduana_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AduanaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AduanaApiApplication.class, args);
	}

}
