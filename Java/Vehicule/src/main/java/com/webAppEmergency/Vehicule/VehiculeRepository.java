package com.webAppEmergency.Vehicule;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webAppEmergency.Vehicule.Vehicule;

@Repository
public interface VehiculeRepository extends CrudRepository<Vehicule, Integer> {
	
	public Optional<Vehicule> findById(int id);
	public List<Vehicule> findAll();
	public void delete(Vehicule v);

}