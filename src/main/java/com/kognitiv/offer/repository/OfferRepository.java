package com.kognitiv.offer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kognitiv.offer.entity.Offers;

@Repository
public interface OfferRepository extends CrudRepository<Offers, Long>{

}
