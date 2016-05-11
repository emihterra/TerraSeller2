package ru.terracorp.seller.service;

import ru.terracorp.seller.domain.ClientRoom;
import ru.terracorp.seller.web.rest.dto.ClientRoomDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ClientRoom.
 */
public interface ClientRoomService {

    /**
     * Save a clientRoom.
     * 
     * @param clientRoomDTO the entity to save
     * @return the persisted entity
     */
    ClientRoomDTO save(ClientRoomDTO clientRoomDTO);

    /**
     *  Get all the clientRooms.
     *  
     *  @return the list of entities
     */
    List<ClientRoomDTO> findAll();

    /**
     *  Get the "id" clientRoom.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientRoomDTO findOne(String id);

    /**
     *  Delete the "id" clientRoom.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
