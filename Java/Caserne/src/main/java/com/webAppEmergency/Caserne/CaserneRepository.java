package com.webAppEmergency.Caserne;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaserneRepository extends CrudRepository<Caserne, Integer> {
	
	public Optional<Caserne> findById(int id);
	public List<Caserne> findAll();
	//public void delete(Caserne c);

}