package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.ClientBasket;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ClientBasket entity.
 */
public interface ClientBasketRepository extends MongoRepository<ClientBasket,String> {

}
