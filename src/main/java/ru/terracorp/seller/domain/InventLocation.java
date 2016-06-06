package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InventLocation.
 */

@Document(collection = "invent_location")
public class InventLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("code")
    private String code;

    @Field("name")
    private String name;

    @Field("priority")
    private Integer priority;

    @Field("info")
    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
        InventLocation inventLocation = (InventLocation) o;
        if(inventLocation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inventLocation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InventLocation{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", priority='" + priority + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
