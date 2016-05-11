package ru.terracorp.seller.web.rest.mapper;

import ru.terracorp.seller.domain.*;
import ru.terracorp.seller.web.rest.dto.ClientRoomItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientRoomItem and its DTO ClientRoomItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientRoomItemMapper {

    ClientRoomItemDTO clientRoomItemToClientRoomItemDTO(ClientRoomItem clientRoomItem);

    List<ClientRoomItemDTO> clientRoomItemsToClientRoomItemDTOs(List<ClientRoomItem> clientRoomItems);

    ClientRoomItem clientRoomItemDTOToClientRoomItem(ClientRoomItemDTO clientRoomItemDTO);

    List<ClientRoomItem> clientRoomItemDTOsToClientRoomItems(List<ClientRoomItemDTO> clientRoomItemDTOs);
}
