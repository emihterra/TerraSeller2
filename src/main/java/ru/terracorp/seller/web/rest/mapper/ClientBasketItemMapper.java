package ru.terracorp.seller.web.rest.mapper;

import ru.terracorp.seller.domain.*;
import ru.terracorp.seller.web.rest.dto.ClientBasketItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientBasketItem and its DTO ClientBasketItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientBasketItemMapper {

    ClientBasketItemDTO clientBasketItemToClientBasketItemDTO(ClientBasketItem clientBasketItem);

    List<ClientBasketItemDTO> clientBasketItemsToClientBasketItemDTOs(List<ClientBasketItem> clientBasketItems);

    ClientBasketItem clientBasketItemDTOToClientBasketItem(ClientBasketItemDTO clientBasketItemDTO);

    List<ClientBasketItem> clientBasketItemDTOsToClientBasketItems(List<ClientBasketItemDTO> clientBasketItemDTOs);
}
