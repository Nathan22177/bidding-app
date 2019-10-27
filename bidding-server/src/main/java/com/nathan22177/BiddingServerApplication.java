package com.nathan22177;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.nathan22177"})
@EnableJpaRepositories("com.nathan22177.repository")
public class BiddingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingServerApplication.class, args);
	}

}
