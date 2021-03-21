package com.kognitiv.offer.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
public class BoilerPlateConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
				.setReadTimeout(Duration.ofMillis(5000))
				.setConnectTimeout(Duration.ofMillis(5000))
				.build();
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	PasswordEncoder encoder = 
          PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	auth
          .inMemoryAuthentication()
          .withUser("admin")
          .password(encoder.encode("admin"))
          .roles("ADMIN")
          .and()
          .withUser("user")
          .password(encoder.encode("user"))
          .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
          .authorizeRequests()
          .antMatchers("/collect/offer")
          .authenticated()
          .and()
          .httpBasic()
          .and()
          .logout()
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID")
          .and()
          .authorizeRequests()
          .antMatchers("/v2/api-docs" , "/swagger-ui.html")
          .permitAll();
    }

}
