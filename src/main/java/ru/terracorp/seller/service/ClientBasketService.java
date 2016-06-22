package ru.terracorp.seller.service;

import ru.terracorp.seller.domain.ClientBasket;
import ru.terracorp.seller.repository.ClientBasketRepository;
import ru.terracorp.seller.web.rest.dto.ClientBasketDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ClientBasket.
 */
@Service
public class ClientBasketService {

    private final Logger log = LoggerFactory.getLogger(ClientBasketService.class);

    @Inject
    private ClientBasketRepository clientBasketRepository;

    @Inject
    private ClientBasketMapper clientBasketMapper;

    /**
     * Save a clientBasket.
     *
     * @param clientBasketDTO the entity to save
     * @return the persisted entity
     */
    public ClientBasketDTO save(ClientBasketDTO clientBasketDTO) {
        log.debug("Request to save ClientBasket : {}", clientBasketDTO);
        ClientBasket clientBasket = clientBasketMapper.clientBasketDTOToClientBasket(clientBasketDTO);
        clientBasket = clientBasketRepository.save(clientBasket);
        ClientBasketDTO result = clientBasketMapper.clientBasketToClientBasketDTO(clientBasket);
        return result;
    }

    /**
     *  Get all the clientBaskets.
     *
     *  @return the list of entities
     */
    public List<ClientBasketDTO> findAll() {
        log.debug("Request to get all ClientBaskets");
        List<ClientBasketDTO> result = clientBasketRepository.findAll().stream()
            .map(clientBasketMapper::clientBasketToClientBasketDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get all the ClientBasket by client.
     *
     *  @return the list of entities
     */
    public List<ClientBasketDTO> findByClientAndDeleted(String client, Boolean deleted) {
        log.debug("Request to get all ClientBaskets");
        List<ClientBasketDTO> result = clientBasketRepository.findByClientAndDeleted(client, deleted).stream()
            .map(clientBasketMapper::clientBasketToClientBasketDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get all the ClientBasket by client and employee.
     *
     *  @return the list of entities
     */
    public List<ClientBasketDTO> findByClientAndEmplcodeAndDeleted(String client, String emplcode, Boolean deleted) {
        log.debug("Request to get all ClientBaskets");
        List<ClientBasketDTO> result = clientBasketRepository.findByClientAndEmplcodeAndDeleted(client, emplcode, deleted).stream()
            .map(clientBasketMapper::clientBasketToClientBasketDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one clientBasket by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public ClientBasketDTO findOne(String id) {
        log.debug("Request to get ClientBasket : {}", id);
        ClientBasket clientBasket = clientBasketRepository.findOne(id);
        ClientBasketDTO clientBasketDTO = clientBasketMapper.clientBasketToClientBasketDTO(clientBasket);
        return clientBasketDTO;
    }

    /**
     *  Delete the  clientBasket by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ClientBasket : {}", id);
        clientBasketRepository.delete(id);
    }
}
