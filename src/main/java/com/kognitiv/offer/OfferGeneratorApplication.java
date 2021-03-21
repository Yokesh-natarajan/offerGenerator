package com.kognitiv.offer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.kognitiv.offer.entity.Users;
import com.kognitiv.offer.repository.UserRepository;


@EnableScheduling
@SpringBootApplication
@EnableCaching
public class OfferGeneratorApplication {
	
	private static final Logger lOG = LoggerFactory.getLogger(OfferGeneratorApplication.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(OfferGeneratorApplication.class, args);
		UserRepository repo = ctx.getBean(UserRepository.class);
		
		//initializing users and admin
		lOG.info("Initializing users table");
		Users user = new Users();
		user.setUsername("user");
		user.setUserId(1L);
		repo.save(user);
		user.setUsername("ADMIN");
		user.setUserId(0L);
		repo.save(user);
		
	}

}
