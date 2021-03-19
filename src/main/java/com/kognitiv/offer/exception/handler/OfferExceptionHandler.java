package com.kognitiv.offer.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kognitiv.offer.beans.ErrorBean;
import com.kognitiv.offer.exception.OfferGeneratorException;

@ControllerAdvice
public class OfferExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(OfferGeneratorException.class)
	public ResponseEntity<ErrorBean> offerGenerationException(String message) {
		ErrorBean er = new ErrorBean();
		er.setCode("41");
		er.setMessage(message);
		return new ResponseEntity<>(er , HttpStatus.NOT_FOUND);
	}

}
