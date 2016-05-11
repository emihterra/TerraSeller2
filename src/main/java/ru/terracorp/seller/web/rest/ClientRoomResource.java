package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.terracorp.seller.domain.ClientRoom;
import ru.terracorp.seller.service.ClientRoomService;
import ru.terracorp.seller.web.rest.util.HeaderUtil;
import ru.terracorp.seller.web.rest.dto.ClientRoomDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ClientRoom.
 */
@RestController
@RequestMapping("/api")
public class ClientRoomResource {

    private final Logger log = LoggerFactory.getLogger(ClientRoomResource.class);
        
    @Inject
    private ClientRoomService clientRoomService;
    
    @Inject
    private ClientRoomMapper clientRoomMapper;
    
    /**
     * POST  /client-rooms : Create a new clientRoom.
     *
     * @param clientRoomDTO the clientRoomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientRoomDTO, or with status 400 (Bad Request) if the clientRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-rooms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomDTO> createClientRoom(@RequestBody ClientRoomDTO clientRoomDTO) throws URISyntaxException {
        log.debug("REST request to save ClientRoom : {}", clientRoomDTO);
        if (clientRoomDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientRoom", "idexists", "A new clientRoom cannot already have an ID")).body(null);
        }
        ClientRoomDTO result = clientRoomService.save(clientRoomDTO);
        return ResponseEntity.created(new URI("/api/client-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientRoom", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-rooms : Updates an existing clientRoom.
     *
     * @param clientRoomDTO the clientRoomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientRoomDTO,
     * or with status 400 (Bad Request) if the clientRoomDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientRoomDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-rooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomDTO> updateClientRoom(@RequestBody ClientRoomDTO clientRoomDTO) throws URISyntaxException {
        log.debug("REST request to update ClientRoom : {}", clientRoomDTO);
        if (clientRoomDTO.getId() == null) {
            return createClientRoom(clientRoomDTO);
        }
        ClientRoomDTO result = clientRoomService.save(clientRoomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientRoom", clientRoomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-rooms : get all the clientRooms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientRooms in body
     */
    @RequestMapping(value = "/client-rooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ClientRoomDTO> getAllClientRooms() {
        log.debug("REST request to get all ClientRooms");
        return clientRoomService.findAll();
    }

    /**
     * GET  /client-rooms/:id : get the "id" clientRoom.
     *
     * @param id the id of the clientRoomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientRoomDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-rooms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomDTO> getClientRoom(@PathVariable String id) {
        log.debug("REST request to get ClientRoom : {}", id);
        ClientRoomDTO clientRoomDTO = clientRoomService.findOne(id);
        return Optional.ofNullable(clientRoomDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-rooms/:id : delete the "id" clientRoom.
     *
     * @param id the id of the clientRoomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-rooms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientRoom(@PathVariable String id) {
        log.debug("REST request to delete ClientRoom : {}", id);
        clientRoomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientRoom", id.toString())).build();
    }

}
