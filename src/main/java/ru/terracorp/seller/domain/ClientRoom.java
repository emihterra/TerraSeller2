package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientRoom.
 */

@Document(collection = "client_room")
public class ClientRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("client")
    private String client;

    @Field("name")
    private String name;

    @Field("r_length")
    private Integer r_length;

    @Field("r_width")
    private Integer r_width;

    @Field("r_height")
    private Integer r_height;

    @Field("bottom_border_height")
    private Integer bottom_border_height;

    @Field("top_border_height")
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
        ClientRoom clientRoom = (ClientRoom) o;
        if(clientRoom.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientRoom{" +
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
