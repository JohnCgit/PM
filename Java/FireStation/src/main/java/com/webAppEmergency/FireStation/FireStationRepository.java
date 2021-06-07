package com.webAppEmergency.FireStation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation, Integer> {
	
	public Optional<FireStation> findById(int id);
	public List<FireStation> findAll();
	//public void delete(Caserne c);

}