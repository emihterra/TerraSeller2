package ru.terracorp.seller.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.terracorp.seller.domain.ClientRoom;

import java.util.List;

/**
 * Spring Data MongoDB repository for the ClientRoom entity.
 */
public interface ClientRoomRepository extends MongoRepository<ClientRoom,String> {
    /**
     *  Get all the clientRooms by client.
     *
     *  @return the list of entities
     */
    List<ClientRoom> findByClient(String client);

}
