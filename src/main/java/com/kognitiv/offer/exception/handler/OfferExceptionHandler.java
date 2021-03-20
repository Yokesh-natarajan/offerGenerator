package com.kognitiv.offer.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kognitiv.offer.beans.ErrorBean;
import com.kognitiv.offer.constants.ErrorConstants;
import com.kognitiv.offer.exception.OfferGeneratorException;
import com.kognitiv.offer.exception.OfferGeneratorRuntimeException;
import com.kognitiv.offer.exception.OfferInvalidException;

@ControllerAdvice
public class OfferExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OfferExceptionHandler.class);

	/*
	 * the error codes are mapped with the logic - exception_series + nth api + nth exception
	 * eg. 411 => 4xx + 1st api + 1st scenario 
	 */
	@ExceptionHandler(OfferInvalidException.class)
	public ResponseEntity<ErrorBean> offerinvalidException(OfferInvalidException e) {
		ErrorBean er = new ErrorBean();
		ResponseEntity<ErrorBean> response = null;
		er.setMessage(e.getMessage());
		if (e.getMessage().equalsIgnoreCase(ErrorConstants.ADMIN_NOT_ALLOWED)) {
			er.setCode("412");
			response = new ResponseEntity<>(er, HttpStatus.FORBIDDEN);
		} else if (e.getMessage().equalsIgnoreCase(ErrorConstants.OFFER_EMPTY)) {
			er.setCode("422");
			response = new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
		} else if (e.getMessage().equalsIgnoreCase(ErrorConstants.USER_NOT_PRESENT)) {
			er.setCode("423");
			response = new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
		} else if (e.getMessage().equalsIgnoreCase(ErrorConstants.USERS_NOT_ALLOWED)) {
			er.setCode("421");
			response = new ResponseEntity<>(er, HttpStatus.FORBIDDEN);
		}
		return response;
	}

	@ExceptionHandler(OfferGeneratorRuntimeException.class)
	public ResponseEntity<ErrorBean> offerRuntimeGenerationException(OfferGeneratorRuntimeException e) {
		ErrorBean er = new ErrorBean();
		er.setMessage(e.getMessage());
		ResponseEntity<ErrorBean> response = null;
		if(e.getMessage().equalsIgnoreCase(ErrorConstants.OFFER_NOT_SAVED)) {
		er.setCode("524");
		response = new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		
		return response;
	}
	
	@ExceptionHandler(OfferGeneratorException.class)
	public ResponseEntity<ErrorBean> offerGeneratorException(OfferGeneratorException e) {
		ErrorBean er = new ErrorBean();
		er.setMessage(e.getMessage());
		ResponseEntity<ErrorBean> response = null;
		if(e.getMessage().equalsIgnoreCase(ErrorConstants.OFFER_NOT_FOUND)) {
			er.setCode("411");
			response = new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	/*
	 * generic exception.. in case for unchecked exception which weren't caught
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBean> genericException(Exception e) {
		LOG.error("Generic exception occured :: {}", e);
		ErrorBean er = new ErrorBean();
		er.setCode("xxxx");
		er.setMessage(e.getMessage());
		return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
