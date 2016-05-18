package ru.terracorp.seller.web.rest.mapper;

import ru.terracorp.seller.domain.*;
import ru.terracorp.seller.web.rest.dto.ClientBasketDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientBasket and its DTO ClientBasketDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientBasketMapper {

    ClientBasketDTO clientBasketToClientBasketDTO(ClientBasket clientBasket);

    List<ClientBasketDTO> clientBasketsToClientBasketDTOs(List<ClientBasket> clientBaskets);

    ClientBasket clientBasketDTOToClientBasket(ClientBasketDTO clientBasketDTO);

    List<ClientBasket> clientBasketDTOsToClientBaskets(List<ClientBasketDTO> clientBasketDTOs);
}
