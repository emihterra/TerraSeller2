package ru.terracorp.seller.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ClientRoom entity.
 */
public class ClientRoomDTO implements Serializable {

    private String id;

    private String client;


    private String name;


    private Integer r_length;


    private Integer r_width;


    private Integer r_height;


    private Integer bottom_border_height;


    private Integer top_border_height;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getr_length() {
        return r_length;
    }

    public void setr_length(Integer r_length) {
        this.r_length = r_length;
    }
    public Integer getr_width() {
        return r_width;
    }

    public void setr_width(Integer r_width) {
        this.r_width = r_width;
    }
    public Integer getr_height() {
        return r_height;
    }

    public void setr_height(Integer r_height) {
        this.r_height = r_height;
    }
    public Integer getBottom_border_height() {
        return bottom_border_height;
    }

    public void setBottom_border_height(Integer bottom_border_height) {
        this.bottom_border_height = bottom_border_height;
    }
    public Integer getTop_border_height() {
        return top_border_height;
    }

    public void setTop_border_height(Integer top_border_height) {
        this.top_border_height = top_border_height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientRoomDTO clientRoomDTO = (ClientRoomDTO) o;

        if ( ! Objects.equals(id, clientRoomDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientRoomDTO{" +
            "id=" + id +
            ", client='" + client + "'" +
            ", name='" + name + "'" +
            ", r_length='" + r_length + "'" +
            ", r_width='" + r_width + "'" +
            ", r_height='" + r_height + "'" +
            ", bottom_border_height='" + bottom_border_height + "'" +
            ", top_border_height='" + top_border_height + "'" +
            '}';
    }
}
