package ru.terracorp.seller.web.rest.mapper;

import ru.terracorp.seller.domain.*;
import ru.terracorp.seller.web.rest.dto.ClientRoomDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientRoom and its DTO ClientRoomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientRoomMapper {

    ClientRoomDTO clientRoomToClientRoomDTO(ClientRoom clientRoom);

    List<ClientRoomDTO> clientRoomsToClientRoomDTOs(List<ClientRoom> clientRooms);

    ClientRoom clientRoomDTOToClientRoom(ClientRoomDTO clientRoomDTO);

    List<ClientRoom> clientRoomDTOsToClientRooms(List<ClientRoomDTO> clientRoomDTOs);
}
