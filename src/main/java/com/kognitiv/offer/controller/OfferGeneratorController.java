package com.kognitiv.offer.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kognitiv.offer.beans.model.request.OfferRequest;
import com.kognitiv.offer.beans.model.response.OfferResponse;
import com.kognitiv.offer.constants.ErrorConstants;
import com.kognitiv.offer.exception.OfferGeneratorException;
import com.kognitiv.offer.exception.OfferInvalidException;
import com.kognitiv.offer.service.OfferManager;

@RestController
public class OfferGeneratorController {

	@Autowired
	OfferManager offerManager;

	@GetMapping(value = "collect/offer", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OfferResponse viewOffer() throws OfferInvalidException, OfferGeneratorException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getUsername().equalsIgnoreCase("ADMIN")) {
			throw new OfferInvalidException(ErrorConstants.ADMIN_NOT_ALLOWED);
		}
		return offerManager.getOffer(user.getUsername());
	}

	@PostMapping("collect/offer")
	public String insertOffer(@RequestBody OfferRequest request, HttpServletResponse response)
			throws OfferInvalidException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!user.getUsername().equalsIgnoreCase("ADMIN")) {
			throw new OfferInvalidException(ErrorConstants.USERS_NOT_ALLOWED);
		}
		if (ObjectUtils.isEmpty(request) || ObjectUtils.isEmpty(request.getOffer())) {
			throw new OfferInvalidException(ErrorConstants.OFFER_EMPTY);
		}
		String result = offerManager.postOffer(request);
		response.setStatus(201);
		return result;
	}

}
