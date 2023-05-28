package com.mrbear.yppo.services;

import com.mrbear.yppo.entities.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
public class LocationService {

  @PersistenceContext(unitName = "yppo")
  private EntityManager entityManager;

  public Optional<Location> getLocation(Long id) {
    return Optional.ofNullable(entityManager.find(Location.class, id));
  }
}
