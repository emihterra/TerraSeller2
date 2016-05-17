package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientBasket.
 */

@Document(collection = "client_basket")
public class ClientBasket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("client")
    private String client;

    @Field("name")
    private String name;

    @Field("emplcode")
    private String emplcode;

    @Field("id_client_room")
    private String idClientRoom;

    @Field("info")
    private String info;

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

    public String getEmplcode() {
        return emplcode;
    }

    public void setEmplcode(String emplcode) {
        this.emplcode = emplcode;
    }

    public String getIdClientRoom() {
        return idClientRoom;
    }

    public void setIdClientRoom(String idClientRoom) {
        this.idClientRoom = idClientRoom;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientBasket clientBasket = (ClientBasket) o;
        if(clientBasket.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clientBasket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientBasket{" +
            "id=" + id +
            ", client='" + client + "'" +
            ", name='" + name + "'" +
            ", emplcode='" + emplcode + "'" +
            ", idClientRoom='" + idClientRoom + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
