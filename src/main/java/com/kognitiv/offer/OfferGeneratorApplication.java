package com.kognitiv.offer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.kognitiv.offer.entity.Users;
import com.kognitiv.offer.repository.UserRepository;

@SpringBootApplication
public class OfferGeneratorApplication {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(OfferGeneratorApplication.class, args);
		UserRepository repo = ctx.getBean(UserRepository.class);
		//initializing users and admin
		Users user = new Users();
		user.setUsername("user");
		user.setUserId(1L);
		repo.save(user);
		user.setUsername("ADMIN");
		user.setUserId(0L);
		repo.save(user);
		
	}

}
