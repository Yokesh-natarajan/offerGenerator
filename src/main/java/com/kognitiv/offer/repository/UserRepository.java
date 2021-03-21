package com.kognitiv.offer.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kognitiv.offer.entity.Users;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Users, Long>{
	
	//Assuming username to be unique....
	public Optional<Users> findByUsername(String userName);

}
