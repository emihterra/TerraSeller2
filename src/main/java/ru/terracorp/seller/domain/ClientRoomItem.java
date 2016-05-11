package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientRoomItem.
 */

@Document(collection = "client_room_item")
public class ClientRoomItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("id_client_room")
    private Long id_client_room;

    @Field("name")
    private String name;

    @Field("item_type")
    private Integer item_type;

    @Field("i_width")
    private Integer i_width;

    @Field("i_height")
    private Integer i_height;

    @Field("i_top")
    private Integer i_top;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getId_client_room() {
        return id_client_room;
    }

    public void setId_client_room(Long id_client_room) {
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
        ClientRoomItem clientRoomItem = (ClientRoomItem) o;
        if(clientRoomItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientRoomItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientRoomItem{" +
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
