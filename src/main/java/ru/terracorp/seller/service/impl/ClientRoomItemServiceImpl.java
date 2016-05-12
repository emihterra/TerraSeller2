package ru.terracorp.seller.service.impl;

import ru.terracorp.seller.service.ClientRoomItemService;
import ru.terracorp.seller.domain.ClientRoomItem;
import ru.terracorp.seller.repository.ClientRoomItemRepository;
import ru.terracorp.seller.web.rest.dto.ClientRoomItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ClientRoomItem.
 */
@Service
public class ClientRoomItemServiceImpl implements ClientRoomItemService{

    private final Logger log = LoggerFactory.getLogger(ClientRoomItemServiceImpl.class);
    
    @Inject
    private ClientRoomItemRepository clientRoomItemRepository;
    
    @Inject
    private ClientRoomItemMapper clientRoomItemMapper;
    
    /**
     * Save a clientRoomItem.
     * 
     * @param clientRoomItemDTO the entity to save
     * @return the persisted entity
     */
    public ClientRoomItemDTO save(ClientRoomItemDTO clientRoomItemDTO) {
        log.debug("Request to save ClientRoomItem : {}", clientRoomItemDTO);
        ClientRoomItem clientRoomItem = clientRoomItemMapper.clientRoomItemDTOToClientRoomItem(clientRoomItemDTO);
        clientRoomItem = clientRoomItemRepository.save(clientRoomItem);
        ClientRoomItemDTO result = clientRoomItemMapper.clientRoomItemToClientRoomItemDTO(clientRoomItem);
        return result;
    }

    /**
     *  Get all the clientRoomItems.
     *  
     *  @return the list of entities
     */
    public List<ClientRoomItemDTO> findAll() {
        log.debug("Request to get all ClientRoomItems");
        List<ClientRoomItemDTO> result = clientRoomItemRepository.findAll().stream()
            .map(clientRoomItemMapper::clientRoomItemToClientRoomItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one clientRoomItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public ClientRoomItemDTO findOne(String id) {
        log.debug("Request to get ClientRoomItem : {}", id);
        ClientRoomItem clientRoomItem = clientRoomItemRepository.findOne(id);
        ClientRoomItemDTO clientRoomItemDTO = clientRoomItemMapper.clientRoomItemToClientRoomItemDTO(clientRoomItem);
        return clientRoomItemDTO;
    }

    /**
     *  Delete the  clientRoomItem by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ClientRoomItem : {}", id);
        clientRoomItemRepository.delete(id);
    }
}
