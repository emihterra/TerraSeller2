package ru.terracorp.seller.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ClientRoomItem entity.
 */
public class ClientRoomItemDTO implements Serializable {

    private String id;

    private String id_client_room;


    private String name;


    private Integer item_type;


    private Integer i_width;


    private Integer i_height;


    private Integer i_top;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId_client_room() {
        return id_client_room;
    }

    public void setId_client_room(String id_client_room) {
        this.id_client_room = id_client_room;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getItem_type() {
        return item_type;
    }

    public void setItem_type(Integer item_type) {
        this.item_type = item_type;
    }
    public Integer geti_width() {
        return i_width;
    }

    public void seti_width(Integer i_width) {
        this.i_width = i_width;
    }
    public Integer geti_height() {
        return i_height;
    }

    public void seti_height(Integer i_height) {
        this.i_height = i_height;
    }
    public Integer geti_top() {
        return i_top;
    }

    public void seti_top(Integer i_top) {
        this.i_top = i_top;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientRoomItemDTO clientRoomItemDTO = (ClientRoomItemDTO) o;

        if ( ! Objects.equals(id, clientRoomItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientRoomItemDTO{" +
            "id=" + id +
            ", id_client_room='" + id_client_room + "'" +
            ", name='" + name + "'" +
            ", item_type='" + item_type + "'" +
            ", i_width='" + i_width + "'" +
            ", i_height='" + i_height + "'" +
            ", i_top='" + i_top + "'" +
            '}';
    }
}
