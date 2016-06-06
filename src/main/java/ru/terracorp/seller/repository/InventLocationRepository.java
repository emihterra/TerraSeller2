package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.InventLocation;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the InventLocation entity.
 */
public interface InventLocationRepository extends MongoRepository<InventLocation,String> {

}
