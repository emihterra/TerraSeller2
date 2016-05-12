package ru.terracorp.seller.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.terracorp.seller.domain.ClientRoom;
import ru.terracorp.seller.repository.ClientRoomRepository;
import ru.terracorp.seller.service.ClientRoomService;
import ru.terracorp.seller.web.rest.dto.ClientRoomDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomMapper;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ClientRoom.
 */
@Service
public class ClientRoomServiceImpl implements ClientRoomService{

    private final Logger log = LoggerFactory.getLogger(ClientRoomServiceImpl.class);

    @Inject
    private ClientRoomRepository clientRoomRepository;

    @Inject
    private ClientRoomMapper clientRoomMapper;

    /**
     * Save a clientRoom.
     *
     * @param clientRoomDTO the entity to save
     * @return the persisted entity
     */
    public ClientRoomDTO save(ClientRoomDTO clientRoomDTO) {
        log.debug("Request to save ClientRoom : {}", clientRoomDTO);
        ClientRoom clientRoom = clientRoomMapper.clientRoomDTOToClientRoom(clientRoomDTO);
        clientRoom = clientRoomRepository.save(clientRoom);
        ClientRoomDTO result = clientRoomMapper.clientRoomToClientRoomDTO(clientRoom);
        return result;
    }

    /**
     *  Get all the clientRooms.
     *
     *  @return the list of entities
     */
    public List<ClientRoomDTO> findAll() {
        log.debug("Request to get all ClientRooms");
        List<ClientRoomDTO> result = clientRoomRepository.findAll().stream()
            .map(clientRoomMapper::clientRoomToClientRoomDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get all the clientRooms by client.
     *
     *  @return the list of entities
     */
    public List<ClientRoomDTO> findByClient(String client) {
        log.debug("Request to get all ClientRooms by client code");
        List<ClientRoomDTO> result = clientRoomRepository.findByClient(client).stream()
            .map(clientRoomMapper::clientRoomToClientRoomDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    };

    /**
     *  Get one clientRoom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public ClientRoomDTO findOne(String id) {
        log.debug("Request to get ClientRoom : {}", id);
        ClientRoom clientRoom = clientRoomRepository.findOne(id);
        ClientRoomDTO clientRoomDTO = clientRoomMapper.clientRoomToClientRoomDTO(clientRoom);
        return clientRoomDTO;
    }

    /**
     *  Delete the  clientRoom by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ClientRoom : {}", id);
        clientRoomRepository.delete(id);
    }
}
