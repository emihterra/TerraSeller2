package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.ClientBasketItem;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ClientBasketItem entity.
 */
public interface ClientBasketItemRepository extends MongoRepository<ClientBasketItem,String> {

}
