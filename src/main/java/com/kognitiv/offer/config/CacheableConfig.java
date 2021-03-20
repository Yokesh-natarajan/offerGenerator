package com.kognitiv.offer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.kognitiv.offer.beans.response.Photos;

@Configuration
public class CacheableConfig {
	
	private static Logger LOG = LoggerFactory.getLogger(CacheableConfig.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("photos");
    }
	
	@Cacheable(cacheNames = "photos")
	public Photos[] getPhotos() {
		LOG.info("Inside cache");
		return restTemplate.getForEntity("https://jsonplaceholder.typicode.com/photos",
				Photos[].class).getBody();
	}
	
	@Scheduled(fixedRate = 4000000)
	@CacheEvict(value="photos", allEntries=true)
	public void deleteAllPhotos() {
		LOG.info("All entries in the cache photos have been deleted");
	}

}
