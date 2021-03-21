package com.kognitiv.offer.beans.model.request;

public class OfferRequest {
	
	private Offer offer;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	@Override
	public String toString() {
		return "OfferRequest [offer=" + offer + "]";
	}
	
}
