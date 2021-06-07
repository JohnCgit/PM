package com.webAppEmergency.Vehicle;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
	
	public Optional<Vehicle> findById(int id);
	public List<Vehicle> findAll();
	public void delete(Vehicle v);

}