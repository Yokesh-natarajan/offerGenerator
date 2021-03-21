package com.kognitiv.offer.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kognitiv.offer.entity.Offers;

@Repository
public interface OfferRepository extends PagingAndSortingRepository<Offers, Long>{
	
	public Optional<Offers> findByName(String name);

}
