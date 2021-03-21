package com.kognitiv.offer.service;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kognitiv.offer.beans.model.request.OfferRequest;
import com.kognitiv.offer.beans.model.response.OfferResponse;
import com.kognitiv.offer.config.CacheableConfig;
import com.kognitiv.offer.constants.ErrorConstants;
import com.kognitiv.offer.constants.OfferGeneratorConstants;
import com.kognitiv.offer.entity.Offers;
import com.kognitiv.offer.entity.Users;
import com.kognitiv.offer.exception.OfferGeneratorException;
import com.kognitiv.offer.exception.OfferGeneratorRuntimeException;
import com.kognitiv.offer.exception.OfferInvalidException;
import com.kognitiv.offer.repository.OfferRepository;
import com.kognitiv.offer.repository.UserRepository;

@Service
public class OfferManager {

	@Autowired
	OfferRepository repo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	CacheableConfig cache;

	private static final Logger LOG = LoggerFactory.getLogger(OfferManager.class);

	public OfferResponse getOffer(String username) throws OfferInvalidException, OfferGeneratorException {

		Optional<Users> loggedIn = userRepo.findByUsername(username);
		LOG.info("user is :: {}" , loggedIn.get().getUsername());
		
		// Assuming the user must exist since this is a prototype and in-mem db.
		if (loggedIn.get().getOfferId() == null) {
			throw new OfferGeneratorException(ErrorConstants.OFFER_NOT_FOUND);
		}

		Optional<Offers> offer = repo.findById(loggedIn.get().getOfferId());

		OfferResponse offerResponse = null;

		if (offer.isPresent()) {
			offerResponse = new OfferResponse();
			offerResponse.setLocation(offer.get().getLocation());
			offerResponse.setName(offer.get().getName());
			offerResponse.setValidFrom(offer.get().getValidFrom());
			offerResponse.setValidTo(offer.get().getValidTo());
		} else {
			LOG.error("Exception no record found for :: {}", loggedIn.get().getOfferId());
			throw new OfferInvalidException(ErrorConstants.OFFER_NOT_FOUND);
		}

		return offerResponse;
	}

	@Transactional
	public String postOffer(OfferRequest request) throws OfferInvalidException {
		try {
			Optional<Users> user = userRepo.findByUsername(request.getOffer().getName());
			if (user.isPresent()) {
				LOG.info("For username :: {}" , user.get().getUsername());
				Offers offer = new Offers();
				offer.setLocation(request.getOffer().getLocation());
				offer.setName(request.getOffer().getName());
				offer.setValidFrom(request.getOffer().getValidFrom());
				offer.setValidTo(request.getOffer().getValidTill());
				setImage(offer);
				try {
					offer = repo.save(offer);
					user.get().setOfferId(offer.getId());
					updateInUserTable(user.get());
				} catch (Exception e) {
					throw new OfferGeneratorRuntimeException(ErrorConstants.OFFER_NOT_SAVED);
				}
				LOG.info("id :: {}", offer.getId());
				return OfferGeneratorConstants.POSTED_SUCCESSFULLY;
			} else {
				throw new OfferInvalidException(ErrorConstants.USER_NOT_PRESENT);
			}
		} catch (OfferInvalidException ie) {
			LOG.error("the user does not exist :: {}", ie.getMessage());
			throw ie;
		} catch (OfferGeneratorRuntimeException e) {
			LOG.error("Exception occured while trying to input offer into db :: {}", request.getOffer().getName(), e);
			throw e;
		}
	}

	private void updateInUserTable(Users user) {
		user = userRepo.save(user);
		LOG.info("user id after update in post :: {}" , user.getUserId());
	}

	private void setImage(Offers offer) {
		offer.setImages(cache.getPhotos()[new Random().nextInt(cache.getPhotos().length)].getUrl());
	}

}
