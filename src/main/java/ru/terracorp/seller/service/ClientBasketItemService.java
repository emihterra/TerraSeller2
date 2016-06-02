package ru.terracorp.seller.service;

import ru.terracorp.seller.domain.ClientBasketItem;
import ru.terracorp.seller.repository.ClientBasketItemRepository;
import ru.terracorp.seller.web.rest.dto.ClientBasketItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ClientBasketItem.
 */
@Service
public class ClientBasketItemService {

    private final Logger log = LoggerFactory.getLogger(ClientBasketItemService.class);

    @Inject
    private ClientBasketItemRepository clientBasketItemRepository;

    @Inject
    private ClientBasketItemMapper clientBasketItemMapper;

    /**
     * Save a clientBasketItem.
     *
     * @param clientBasketItemDTO the entity to save
     * @return the persisted entity
     */
    public ClientBasketItemDTO save(ClientBasketItemDTO clientBasketItemDTO) {
        log.debug("Request to save ClientBasketItem : {}", clientBasketItemDTO);
        ClientBasketItem clientBasketItem = clientBasketItemMapper.clientBasketItemDTOToClientBasketItem(clientBasketItemDTO);
        clientBasketItem = clientBasketItemRepository.save(clientBasketItem);
        ClientBasketItemDTO result = clientBasketItemMapper.clientBasketItemToClientBasketItemDTO(clientBasketItem);
        return result;
    }

    /**
     *  Get all the clientBasketItems.
     *
     *  @return the list of entities
     */
    public List<ClientBasketItemDTO> findAll() {
        log.debug("Request to get all ClientBasketItems");
        List<ClientBasketItemDTO> result = clientBasketItemRepository.findAll().stream()
            .map(clientBasketItemMapper::clientBasketItemToClientBasketItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get the clientBasketItems by Basket ID.
     *
     *  @param idClientBasket the id of the Client Basket
     *  @param orderedOnly ordered items only (which are in the final basket)
     *  @return the list of entities
     */
    public List<ClientBasketItemDTO> findByIdClientBasket(String idClientBasket, Boolean orderedOnly) {
        log.debug("Request to get all ClientBasketItems by Basket ID");

        List<ClientBasketItemDTO> result;

        if(orderedOnly) {
            result = clientBasketItemRepository.findByIdClientBasketAndOrdered(idClientBasket, orderedOnly).stream()
                .map(clientBasketItemMapper::clientBasketItemToClientBasketItemDTO)
                .collect(Collectors.toCollection(LinkedList::new));
        } else {
            result = clientBasketItemRepository.findByIdClientBasket(idClientBasket).stream()
                .map(clientBasketItemMapper::clientBasketItemToClientBasketItemDTO)
                .collect(Collectors.toCollection(LinkedList::new));
        }

        return result;
    }

    /**
     *  Get ordered clientBasketItems (which are in the final basket).
     *
     *  @param ordered the id of the Client Basket
     *  @return the list of entities
     */
    public List<ClientBasketItemDTO> findByOrdered(Boolean ordered) {
        log.debug("Request to get ordered ClientBasketItems");
        List<ClientBasketItemDTO> result = clientBasketItemRepository.findByOrdered(ordered).stream()
            .map(clientBasketItemMapper::clientBasketItemToClientBasketItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one clientBasketItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public ClientBasketItemDTO findOne(String id) {
        log.debug("Request to get ClientBasketItem : {}", id);
        ClientBasketItem clientBasketItem = clientBasketItemRepository.findOne(id);
        ClientBasketItemDTO clientBasketItemDTO = clientBasketItemMapper.clientBasketItemToClientBasketItemDTO(clientBasketItem);
        return clientBasketItemDTO;
    }

    /**
     *  Delete the  clientBasketItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ClientBasketItem : {}", id);
        clientBasketItemRepository.delete(id);
    }
}
