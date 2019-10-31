package com.nathan22177;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EntityScan(basePackages = {"com.nathan22177"})
@EnableJpaRepositories("com.nathan22177.repositories")
@EnableWebSocket
public class BiddingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingServerApplication.class, args);
	}

}
