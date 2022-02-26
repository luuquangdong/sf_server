package com.it5240.sportfriend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class SportFriend {

	public static void main(String[] args) {
		SpringApplication.run(SportFriend.class, args);
	}

}
