package ru.terracorp.seller.service;

import ru.terracorp.seller.domain.ClientRoomItem;
import ru.terracorp.seller.web.rest.dto.ClientRoomItemDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ClientRoomItem.
 */
public interface ClientRoomItemService {

    /**
     * Save a clientRoomItem.
     * 
     * @param clientRoomItemDTO the entity to save
     * @return the persisted entity
     */
    ClientRoomItemDTO save(ClientRoomItemDTO clientRoomItemDTO);

    /**
     *  Get all the clientRoomItems.
     *  
     *  @return the list of entities
     */
    List<ClientRoomItemDTO> findAll();

    /**
     *  Get the "id" clientRoomItem.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    ClientRoomItemDTO findOne(String id);

    /**
     *  Delete the "id" clientRoomItem.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
