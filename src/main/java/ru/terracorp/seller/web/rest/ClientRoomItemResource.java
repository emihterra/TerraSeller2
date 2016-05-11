package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.terracorp.seller.domain.ClientRoomItem;
import ru.terracorp.seller.service.ClientRoomItemService;
import ru.terracorp.seller.web.rest.util.HeaderUtil;
import ru.terracorp.seller.web.rest.dto.ClientRoomItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomItemMapper;
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
 * REST controller for managing ClientRoomItem.
 */
@RestController
@RequestMapping("/api")
public class ClientRoomItemResource {

    private final Logger log = LoggerFactory.getLogger(ClientRoomItemResource.class);
        
    @Inject
    private ClientRoomItemService clientRoomItemService;
    
    @Inject
    private ClientRoomItemMapper clientRoomItemMapper;
    
    /**
     * POST  /client-room-items : Create a new clientRoomItem.
     *
     * @param clientRoomItemDTO the clientRoomItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientRoomItemDTO, or with status 400 (Bad Request) if the clientRoomItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-room-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomItemDTO> createClientRoomItem(@RequestBody ClientRoomItemDTO clientRoomItemDTO) throws URISyntaxException {
        log.debug("REST request to save ClientRoomItem : {}", clientRoomItemDTO);
        if (clientRoomItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientRoomItem", "idexists", "A new clientRoomItem cannot already have an ID")).body(null);
        }
        ClientRoomItemDTO result = clientRoomItemService.save(clientRoomItemDTO);
        return ResponseEntity.created(new URI("/api/client-room-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientRoomItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-room-items : Updates an existing clientRoomItem.
     *
     * @param clientRoomItemDTO the clientRoomItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientRoomItemDTO,
     * or with status 400 (Bad Request) if the clientRoomItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientRoomItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-room-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomItemDTO> updateClientRoomItem(@RequestBody ClientRoomItemDTO clientRoomItemDTO) throws URISyntaxException {
        log.debug("REST request to update ClientRoomItem : {}", clientRoomItemDTO);
        if (clientRoomItemDTO.getId() == null) {
            return createClientRoomItem(clientRoomItemDTO);
        }
        ClientRoomItemDTO result = clientRoomItemService.save(clientRoomItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientRoomItem", clientRoomItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-room-items : get all the clientRoomItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientRoomItems in body
     */
    @RequestMapping(value = "/client-room-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ClientRoomItemDTO> getAllClientRoomItems() {
        log.debug("REST request to get all ClientRoomItems");
        return clientRoomItemService.findAll();
    }

    /**
     * GET  /client-room-items/:id : get the "id" clientRoomItem.
     *
     * @param id the id of the clientRoomItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientRoomItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-room-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientRoomItemDTO> getClientRoomItem(@PathVariable String id) {
        log.debug("REST request to get ClientRoomItem : {}", id);
        ClientRoomItemDTO clientRoomItemDTO = clientRoomItemService.findOne(id);
        return Optional.ofNullable(clientRoomItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-room-items/:id : delete the "id" clientRoomItem.
     *
     * @param id the id of the clientRoomItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-room-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientRoomItem(@PathVariable String id) {
        log.debug("REST request to delete ClientRoomItem : {}", id);
        clientRoomItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientRoomItem", id.toString())).build();
    }

}
