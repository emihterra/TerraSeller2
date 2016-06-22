package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.terracorp.seller.domain.ItemType;
import ru.terracorp.seller.repository.ItemTypeRepository;
import ru.terracorp.seller.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ItemType.
 */
@RestController
@RequestMapping("/api")
public class ItemTypeResource {

    private final Logger log = LoggerFactory.getLogger(ItemTypeResource.class);

    @Inject
    private ItemTypeRepository itemTypeRepository;

    /**
     * POST  /item-types : Create a new itemType.
     *
     * @param itemType the itemType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemType, or with status 400 (Bad Request) if the itemType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/item-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemType> createItemType(@RequestBody ItemType itemType) throws URISyntaxException {
        log.debug("REST request to save ItemType : {}", itemType);
        if (itemType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemType", "idexists", "A new itemType cannot already have an ID")).body(null);
        }

        Optional<ItemType> itemTypeOptional = itemTypeRepository.findOneByCode(itemType.getCode());
        if(itemTypeOptional.isPresent()){
            itemType.setId(itemTypeOptional.get().getId());
        }

        ItemType result = itemTypeRepository.save(itemType);
        return ResponseEntity.created(new URI("/api/item-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-types : Updates an existing itemType.
     *
     * @param itemType the itemType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemType,
     * or with status 400 (Bad Request) if the itemType is not valid,
     * or with status 500 (Internal Server Error) if the itemType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/item-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemType> updateItemType(@RequestBody ItemType itemType) throws URISyntaxException {
        log.debug("REST request to update ItemType : {}", itemType);

        if (itemType.getId() == null) {
            Optional<ItemType> itemTypeOptional = itemTypeRepository.findOneByCode(itemType.getCode());
            if(itemTypeOptional.isPresent()){
                itemType.setId(itemTypeOptional.get().getId());
            } else {
                return createItemType(itemType);
            }
        }
        ItemType result = itemTypeRepository.save(itemType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemType", itemType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-types : get all the itemTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of itemTypes in body
     */
    @RequestMapping(value = "/item-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItemType> getAllItemTypes() {
        log.debug("REST request to get all ItemTypes");
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        return itemTypes;
    }

    /**
     * GET  /item-types/:id : get the "id" itemType.
     *
     * @param id the id of the itemType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/item-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemType> getItemType(
        @PathVariable String id,
        @RequestParam(name = "code", required = false, defaultValue = "") String code) {
        log.debug("REST request to get ItemType : {}", id);

        ItemType itemType = null;

        if(!code.isEmpty()){
            Optional<ItemType> itemTypeOptional = itemTypeRepository.findOneByCode(code);
            if(itemTypeOptional.isPresent()) {
                itemType = itemTypeOptional.get();
            }
        } else {
            itemType = itemTypeRepository.findOne(id);
        }

        return Optional.ofNullable(itemType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /item-types/:id : delete the "id" itemType.
     *
     * @param id the id of the itemType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/item-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemType(@PathVariable String id) {
        log.debug("REST request to delete ItemType : {}", id);
        itemTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemType", id.toString())).build();
    }

}
