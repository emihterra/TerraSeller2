package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.terracorp.seller.service.ClientBasketItemService;
import ru.terracorp.seller.service.ClientBasketService;
import ru.terracorp.seller.web.rest.dto.ClientBasketDTO;
import ru.terracorp.seller.web.rest.dto.ClientBasketItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketItemMapper;
import ru.terracorp.seller.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ClientBasketItem.
 */
@RestController
@RequestMapping("/api")
public class ClientBasketItemResource {

    private final Logger log = LoggerFactory.getLogger(ClientBasketItemResource.class);

    @Inject
    private ClientBasketItemService clientBasketItemService;

    @Inject
    private ClientBasketService clientBasketService;

    @Inject
    private ClientBasketItemMapper clientBasketItemMapper;

    /**
     * POST  /client-basket-items : Create a new clientBasketItem.
     *
     * @param clientBasketItemDTO the clientBasketItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientBasketItemDTO, or with status 400 (Bad Request) if the clientBasketItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-basket-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketItemDTO> createClientBasketItem(@RequestBody ClientBasketItemDTO clientBasketItemDTO) throws URISyntaxException {
        log.debug("REST request to save ClientBasketItem : {}", clientBasketItemDTO);
        if (clientBasketItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientBasketItem", "idexists", "A new clientBasketItem cannot already have an ID")).body(null);
        }
        ClientBasketItemDTO result = clientBasketItemService.save(clientBasketItemDTO);
        return ResponseEntity.created(new URI("/api/client-basket-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientBasketItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-basket-items : Updates an existing clientBasketItem.
     *
     * @param clientBasketItemDTO the clientBasketItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientBasketItemDTO,
     * or with status 400 (Bad Request) if the clientBasketItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientBasketItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-basket-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketItemDTO> updateClientBasketItem(@RequestBody ClientBasketItemDTO clientBasketItemDTO) throws URISyntaxException {
        log.debug("REST request to update ClientBasketItem : {}", clientBasketItemDTO);
        if (clientBasketItemDTO.getId() == null) {
            return createClientBasketItem(clientBasketItemDTO);
        }
        ClientBasketItemDTO result = clientBasketItemService.save(clientBasketItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientBasketItem", clientBasketItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-basket-items : get all the clientBasketItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientBasketItems in body
     */
    @RequestMapping(value = "/client-basket-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ClientBasketItemDTO> getAllClientBasketItems(
        @RequestParam(name = "idbasket", required = false, defaultValue = "") String idbasket,
        @RequestParam(name = "orderedOnly", required = false, defaultValue = "false") Boolean orderedOnly,
        @RequestParam(name = "client", required = false, defaultValue = "") String client,
        @RequestParam(name = "emplcode", required = false, defaultValue = "") String emplcode
        ) {

        if (idbasket.isEmpty()) {

            if(orderedOnly) {
                log.debug("REST request to get all ClientBasketItems");

                if(!client.isEmpty()||!emplcode.isEmpty()){

                    List<ClientBasketItemDTO> basketItems = null;
                    List<ClientBasketItemDTO> tmpBasketItems = null;
                    List<ClientBasketDTO> baskets = null;
                    Boolean deleted = false;

                    if(!client.isEmpty()&emplcode.isEmpty()){
                        baskets = clientBasketService.findByClientAndDeleted(client, deleted);
                    } else if(!client.isEmpty()&!emplcode.isEmpty()){
                        baskets = clientBasketService.findByClientAndEmplcodeAndDeleted(client, emplcode, deleted);
                    }

                    if(baskets != null){
                        for (ClientBasketDTO basket: baskets) {

                            if(basketItems == null){
                                basketItems = clientBasketItemService.findByIdClientBasket(basket.getId(), orderedOnly);
                            } else {
                                tmpBasketItems = clientBasketItemService.findByIdClientBasket(basket.getId(), orderedOnly);
                                for (ClientBasketItemDTO basketItem: tmpBasketItems) {
                                    basketItems.add(basketItem);
                                }
                            }
                        }
                    } else {
                        String emptyID = "";
                        basketItems = clientBasketItemService.findByIdClientBasket(emptyID, orderedOnly);
                    }

                    return basketItems;

                } else {
                    return clientBasketItemService.findByOrdered(true);
                }
            } else {
                log.debug("REST request to get all ClientBasketItems");
                return clientBasketItemService.findAll();
            }

        } else {
            log.debug("REST request to get all ClientBasketItems");
            return clientBasketItemService.findByIdClientBasket(idbasket, orderedOnly);
        }
    }

    /**
     * GET  /client-basket-items/:id : get the "id" clientBasketItem.
     *
     * @param id the id of the clientBasketItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientBasketItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-basket-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientBasketItemDTO> getClientBasketItem(@PathVariable String id) {
        log.debug("REST request to get ClientBasketItem : {}", id);
        ClientBasketItemDTO clientBasketItemDTO = clientBasketItemService.findOne(id);
        return Optional.ofNullable(clientBasketItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-basket-items/:id : delete the "id" clientBasketItem.
     *
     * @param id the id of the clientBasketItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-basket-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientBasketItem(@PathVariable String id) {
        log.debug("REST request to delete ClientBasketItem : {}", id);
        clientBasketItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientBasketItem", id.toString())).build();
    }

}
