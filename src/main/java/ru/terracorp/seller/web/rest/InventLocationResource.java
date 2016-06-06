package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.terracorp.seller.domain.InventLocation;
import ru.terracorp.seller.repository.InventLocationRepository;
import ru.terracorp.seller.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InventLocation.
 */
@RestController
@RequestMapping("/api")
public class InventLocationResource {

    private final Logger log = LoggerFactory.getLogger(InventLocationResource.class);
        
    @Inject
    private InventLocationRepository inventLocationRepository;
    
    /**
     * POST  /invent-locations : Create a new inventLocation.
     *
     * @param inventLocation the inventLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inventLocation, or with status 400 (Bad Request) if the inventLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invent-locations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InventLocation> createInventLocation(@RequestBody InventLocation inventLocation) throws URISyntaxException {
        log.debug("REST request to save InventLocation : {}", inventLocation);
        if (inventLocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inventLocation", "idexists", "A new inventLocation cannot already have an ID")).body(null);
        }
        InventLocation result = inventLocationRepository.save(inventLocation);
        return ResponseEntity.created(new URI("/api/invent-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inventLocation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invent-locations : Updates an existing inventLocation.
     *
     * @param inventLocation the inventLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inventLocation,
     * or with status 400 (Bad Request) if the inventLocation is not valid,
     * or with status 500 (Internal Server Error) if the inventLocation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invent-locations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InventLocation> updateInventLocation(@RequestBody InventLocation inventLocation) throws URISyntaxException {
        log.debug("REST request to update InventLocation : {}", inventLocation);
        if (inventLocation.getId() == null) {
            return createInventLocation(inventLocation);
        }
        InventLocation result = inventLocationRepository.save(inventLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inventLocation", inventLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invent-locations : get all the inventLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inventLocations in body
     */
    @RequestMapping(value = "/invent-locations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InventLocation> getAllInventLocations() {
        log.debug("REST request to get all InventLocations");
        List<InventLocation> inventLocations = inventLocationRepository.findAll();
        return inventLocations;
    }

    /**
     * GET  /invent-locations/:id : get the "id" inventLocation.
     *
     * @param id the id of the inventLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inventLocation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invent-locations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InventLocation> getInventLocation(@PathVariable String id) {
        log.debug("REST request to get InventLocation : {}", id);
        InventLocation inventLocation = inventLocationRepository.findOne(id);
        return Optional.ofNullable(inventLocation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invent-locations/:id : delete the "id" inventLocation.
     *
     * @param id the id of the inventLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invent-locations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInventLocation(@PathVariable String id) {
        log.debug("REST request to delete InventLocation : {}", id);
        inventLocationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inventLocation", id.toString())).build();
    }

}
