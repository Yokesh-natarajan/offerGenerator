package com.kognitiv.offer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kognitiv.offer.beans.OfferResponse;
import com.kognitiv.offer.constants.ErrorConstants;
import com.kognitiv.offer.entity.Offers;
import com.kognitiv.offer.exception.OfferGeneratorException;
import com.kognitiv.offer.repository.OfferRepository;

@Service
public class OfferManager {
	
	@Autowired
	OfferRepository repo;
	
	public OfferResponse getOffer(Long id) throws OfferGeneratorException {
		Optional<Offers> offer = repo.findById(id);
		
		OfferResponse offerResponse = null;
		
		if(offer.isPresent()) {
			offerResponse = new OfferResponse();
			offerResponse.setLocation(offer.get().getLocation());
			offerResponse.setName(offer.get().getName());
			offerResponse.setValidFrom(offer.get().getValidFrom());
			offerResponse.setValidTo(offer.get().getValidTo());
		} else {
			throw new OfferGeneratorException(ErrorConstants.OFFER_NOT_FOUND);
		}
		
		return offerResponse;
	}

}
