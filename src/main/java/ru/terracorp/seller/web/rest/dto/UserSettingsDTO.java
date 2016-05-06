package ru.terracorp.seller.web.rest.dto;

import ru.terracorp.seller.domain.UserSettings;

import javax.validation.constraints.Size;

/**
 * Created by emih on 29.04.2016.
 */
public class UserSettingsDTO {

    @Size(min = 1, max = 100)
    private String login;

    @Size(max = 10)
    private String emplcode;

    @Size(max = 20)
    private String dimension;

    @Size(max = 10)
    private String lastClientCode;

    private Boolean useDefaultClient;

    public UserSettingsDTO() {
    }

    public UserSettingsDTO(UserSettings user) {
        this(user.getLogin(), user.getEmplcode(), user.getDimension(), user.getLastClientCode(), user.getUseDefaultClient());
    }

    public UserSettingsDTO(String login, String emplcode, String dimension, String lastClientCode, Boolean useDefaultClient) {

        this.login = login;
        this.emplcode = emplcode;
        this.dimension = dimension;
        this.lastClientCode = lastClientCode;
        this.useDefaultClient = useDefaultClient;
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
    public String toString() {
        return "UserSettingsDTO{" +
            "login='" + login + '\'' +
            ", emplcode='" + emplcode + '\'' +
            ", dimension='" + dimension + '\'' +
            ", lastClientCode='" + lastClientCode + '\'' +
            "}";
    }

}
