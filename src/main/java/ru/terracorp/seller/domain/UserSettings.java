package ru.terracorp.seller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by emih on 05.05.2016.
 */
@Document(collection = "vh_usersettings")
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 100)
    private String login;

    @Size(max = 10)
    @Field("emplcode")
    private String emplcode;

    @Size(max = 20)
    @Field("dimension")
    private String dimension;

    @Size(max = 10)
    @Field("lastClientCode")
    private String lastClientCode;

    @Field("useDefaultClient")
    private Boolean useDefaultClient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmplcode() {
        return emplcode;
    }

    public void setEmplcode(String emplcode) {
        this.emplcode = emplcode;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getLastClientCode() {
        return lastClientCode;
    }

    public void setLastClientCode(String lastClientCode) {
        this.lastClientCode = lastClientCode;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getUseDefaultClient() {
        return useDefaultClient;
    }

    public void setUseDefaultClient(Boolean useDefaultClient) {
        this.useDefaultClient = useDefaultClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserSettings user = (UserSettings) o;

        if (!emplcode.equals(user.emplcode)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return emplcode.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", emplcode='" + emplcode + '\'' +
            ", dimension='" + dimension + '\'' +
            ", lastClientCode='" + lastClientCode + '\'' +
            "}";
    }

}
