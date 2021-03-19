package com.kognitiv.offer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kognitiv.offer.beans.OfferResponse;

@RestController
public class OfferGeneratorController {
	
	@GetMapping("/collect/offer")
	public OfferResponse viewOffer() {
		//TODO: implement logic for getting value from db wrt user
		return new OfferResponse();
	}
	
	@PostMapping("/collect/offer")
	public Object insertOffer() {
		//TODO: implement logic for inserting values into db
		return new Object();
	}

}
