package ru.terracorp.seller.repository;

import ru.terracorp.seller.domain.ClientRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ClientRoom entity.
 */
public interface ClientRoomRepository extends MongoRepository<ClientRoom,String> {

}
