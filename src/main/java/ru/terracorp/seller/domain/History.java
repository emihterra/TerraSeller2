package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A History.
 */

@Document(collection = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("client")
    private String client;

    @Field("emplcode")
    private String emplcode;

    @Field("type")
    private Integer type;

    @Field("info")
    private String info;

    @Field("items")
    private String items;

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

    public String getEmplcode() {
        return emplcode;
    }

    public void setEmplcode(String emplcode) {
        this.emplcode = emplcode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        History history = (History) o;
        if(history.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, history.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "History{" +
            "id=" + id +
            ", client='" + client + "'" +
            ", emplcode='" + emplcode + "'" +
            ", type='" + type + "'" +
            ", info='" + info + "'" +
            ", items='" + items + "'" +
            '}';
    }
}
