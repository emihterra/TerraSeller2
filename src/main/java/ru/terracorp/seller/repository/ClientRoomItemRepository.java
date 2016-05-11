package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.ClientRoomItem;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ClientRoomItem entity.
 */
public interface ClientRoomItemRepository extends MongoRepository<ClientRoomItem,String> {

}
