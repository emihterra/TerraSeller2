package ru.terracorp.seller.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ClientBasket entity.
 */
public class ClientBasketDTO implements Serializable {

    private String id;

    private String client;


    private String name;


    private String emplcode;


    private String idClientRoom;


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

        ClientBasketDTO clientBasketDTO = (ClientBasketDTO) o;

        if ( ! Objects.equals(id, clientBasketDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClientBasketDTO{" +
            "id=" + id +
            ", client='" + client + "'" +
            ", name='" + name + "'" +
            ", emplcode='" + emplcode + "'" +
            ", idClientRoom='" + idClientRoom + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
