package com.kognitiv.offer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.kognitiv.offer.beans.model.request.Offer;
import com.kognitiv.offer.beans.model.request.OfferRequest;
import com.kognitiv.offer.beans.model.response.OfferResponse;
import com.kognitiv.offer.entity.Users;
import com.kognitiv.offer.repository.OfferRepository;
import com.kognitiv.offer.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.kognitiv.offer")
@TestMethodOrder(MethodOrderer.MethodName.class)
class OfferGeneratorApplicationTests {
	
	@Autowired
	TestRestTemplate tRest;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OfferRepository offerRepo;
	
	@BeforeEach
	public void initData() {
		if(!userRepo.findAll().iterator().hasNext()) {
		Users user = new Users();
		user.setUsername("user");
		user.setUserId(1L);
		userRepo.save(user);
		user.setUsername("ADMIN");
		user.setUserId(0L);
		userRepo.save(user);
		}
	}
	
	public void initSecurity(String username , String pwd , String role) {
		List<GrantedAuthority> auth = new ArrayList<>(); 
		auth.add(new SimpleGrantedAuthority(role));
		User applicationUser = new User(username , pwd , auth);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
	}
	
	@Test
	void A_loggedInAsUser() {
		initSecurity("user" , "user" , "USER");
		ResponseEntity<String> response = this.tRest.withBasicAuth("user", "user")
				.getForEntity("http://localhost:8080/collect/offer", String.class);
		Assert.isTrue(response.getStatusCodeValue() != 401, "user not authenticated");
	}
	
	@Test
	void B_loggedInAsUser_no_offer() {
		initSecurity("user" , "user" , "USER");
		ResponseEntity<String> response = this.tRest.withBasicAuth("user", "user")
				.getForEntity("http://localhost:8080/collect/offer", String.class);
		Assert.isTrue(response.getStatusCodeValue() == 404, "user not authenticated");
	}
	
	@Test
	void C_loggedInAsAdmin_no_offer() {
		initSecurity("admin" , "admin" , "ADMIN");
		ResponseEntity<String> response = this.tRest.withBasicAuth("admin", "admin")
				.getForEntity("http://localhost:8080/collect/offer", String.class);
		Assert.isTrue(response.getStatusCodeValue() == 403, "admin not recognizaed??");
	}
	
	@Test
	void D_loggedInAsAdmin_insert_offer() {
		initSecurity("admin" , "admin" , "ADMIN");
		ResponseEntity<String> response = this.tRest.withBasicAuth("admin", "admin")
				.postForEntity("http://localhost:8080/collect/offer", generateRequest(false , "user") , String.class);
		Assert.isTrue(response.getStatusCodeValue() == 201, "Resource not created");
	}
	
	@Test
	void E_loggedInAsUser_with_offer() {
		initSecurity("user" , "user" , "USER");
		ResponseEntity<OfferResponse> response = this.tRest.withBasicAuth("user", "user")
				.getForEntity("http://localhost:8080/collect/offer", OfferResponse.class);
		Assert.isTrue(response.getStatusCodeValue() == 200, "user did not get his offer");
	}
	
	@Test
	void F_loggedInAsAdmin_re_insert_offer() {
		initSecurity("admin" , "admin" , "ADMIN");
		ResponseEntity<String> response = this.tRest.withBasicAuth("admin", "admin")
				.postForEntity("http://localhost:8080/collect/offer", generateRequest(false , "user") , String.class);
		Assert.isTrue(response.getStatusCodeValue() == 406, "Resource re-created");
	}
	
	@Test
	void G_loggedInAsAdmin_null_offer() {
		initSecurity("admin" , "admin" , "ADMIN");
		ResponseEntity<String> response = this.tRest.withBasicAuth("admin", "admin")
				.postForEntity("http://localhost:8080/collect/offer", generateRequest(true , "user") , String.class);
		Assert.isTrue(response.getStatusCodeValue() == 400, "Request parsed!!!!");
	}
	
	@Test
	void H_loggedInAsUser_post_offer() {
		initSecurity("user" , "user" , "USER");
		ResponseEntity<String> response = this.tRest.withBasicAuth("user", "user")
				.postForEntity("http://localhost:8080/collect/offer", generateRequest(false , "user") , String.class);
		Assert.isTrue(response.getStatusCodeValue() == 403, "OMG all users are now admin");
	}
	
	@Test
	void I_loggedInAsAdmin_diff_user_insert_offer() {
		initSecurity("admin" , "admin" , "ADMIN");
		ResponseEntity<String> response = this.tRest.withBasicAuth("admin", "admin")
				.postForEntity("http://localhost:8080/collect/offer", generateRequest(false , "someone else") , String.class);
		Assert.isTrue(response.getStatusCodeValue() == 400, "Resource created for some one else or something else happened");
	}
	
	@Test
	void J_loggedInAsUser_get_offer_but_offer_disappeared() {
		initSecurity("user" , "user" , "USER");
		Optional<Users> user = userRepo.findByUsername("user");
		offerRepo.deleteById(user.get().getOfferId());
		ResponseEntity<String> response = this.tRest.withBasicAuth("user", "user")
				.getForEntity("http://localhost:8080/collect/offer", String.class);
		Assert.isTrue(response.getStatusCodeValue() == 404, "user did not get his offer");
	}
	
	private OfferRequest generateRequest(boolean flag , String username) {
		if(flag) {
			return new OfferRequest();
		}
		
		Calendar cal = Calendar.getInstance();
		
		
		OfferRequest req = new OfferRequest();
		Offer offer = new Offer();
		offer.setLocation("Chennai");
		offer.setName(username);
		offer.setValidFrom(cal.getTime());
		cal.add(Calendar.DATE, 5);
		offer.setValidTill(cal.getTime());
		req.setOffer(offer);
		
		return req;
	}

}
