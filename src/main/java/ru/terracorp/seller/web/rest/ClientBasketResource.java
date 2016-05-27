package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.terracorp.seller.service.ClientBasketService;
import ru.terracorp.seller.web.rest.dto.ClientBasketDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketMapper;
import ru.terracorp.seller.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ClientBasket.
 */
@RestController
@RequestMapping("/api")
public class ClientBasketResource {

    private final Logger log = LoggerFactory.getLogger(ClientBasketResource.class);

    @Inject
    private ClientBasketService clientBasketService;

    @Inject
    private ClientBasketMapper clientBasketMapper;

    /**
     * POST  /client-baskets : Create a new clientBasket.
     *
     * @param clientBasketDTO the clientBasketDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientBasketDTO, or with status 400 (Bad Request) if the clientBasket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-baskets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketDTO> createClientBasket(@RequestBody ClientBasketDTO clientBasketDTO) throws URISyntaxException {
        log.debug("REST request to save ClientBasket : {}", clientBasketDTO);
        if (clientBasketDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientBasket", "idexists", "A new clientBasket cannot already have an ID")).body(null);
        }
        ClientBasketDTO result = clientBasketService.save(clientBasketDTO);
        return ResponseEntity.created(new URI("/api/client-baskets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientBasket", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-baskets : Updates an existing clientBasket.
     *
     * @param clientBasketDTO the clientBasketDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientBasketDTO,
     * or with status 400 (Bad Request) if the clientBasketDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientBasketDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-baskets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketDTO> updateClientBasket(@RequestBody ClientBasketDTO clientBasketDTO) throws URISyntaxException {
        log.debug("REST request to update ClientBasket : {}", clientBasketDTO);
        if (clientBasketDTO.getId() == null) {
            return createClientBasket(clientBasketDTO);
        }
        ClientBasketDTO result = clientBasketService.save(clientBasketDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientBasket", clientBasketDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-baskets : get all the clientBaskets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientBaskets in body
     */
    @RequestMapping(value = "/client-baskets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ClientBasketDTO> getAllClientBaskets(
        @RequestParam(name = "client", required = false, defaultValue = "") String client,
        @RequestParam(name = "deleted", required = false, defaultValue = "false") Boolean deleted
    ) {
        if(client.isEmpty()) {
            log.debug("REST request to get all ClientBaskets");
            return clientBasketService.findAll();
        } else {
            log.debug("REST request to get all ClientBaskets by client");
            return clientBasketService.findByClientAndDeleted(client, deleted);
        }
    }

    /**
     * GET  /client-baskets/:id : get the "id" clientBasket.
     *
     * @param id the id of the clientBasketDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientBasketDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-baskets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketDTO> getClientBasket(@PathVariable String id) {
        log.debug("REST request to get ClientBasket : {}", id);
        ClientBasketDTO clientBasketDTO = clientBasketService.findOne(id);
        return Optional.ofNullable(clientBasketDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-baskets/:id : delete the "id" clientBasket.
     *
     * @param id the id of the clientBasketDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-baskets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientBasket(@PathVariable String id) {
        log.debug("REST request to delete ClientBasket : {}", id);
        clientBasketService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientBasket", id.toString())).build();
    }

}
