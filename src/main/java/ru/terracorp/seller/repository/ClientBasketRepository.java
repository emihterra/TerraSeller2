package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.ClientBasket;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the ClientBasket entity.
 */
public interface ClientBasketRepository extends MongoRepository<ClientBasket,String> {
    /**
     *  Get all the ClientBasket by client.
     *
     *  @return the list of entities
     */
    List<ClientBasket> findByClientAndDeleted(String client, Boolean deleted);

}
