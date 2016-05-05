package ru.terracorp.seller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.terracorp.seller.domain.UserSettings;
import ru.terracorp.seller.repository.UserSettingsRepository;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by emih on 05.05.2016.
 */
@Service
public class UserSettingsService {

    private final Logger log = LoggerFactory.getLogger(UserSettingsService.class);

    @Inject
    private UserSettingsRepository userSettingsRepository;

    public Optional<UserSettings> getUserSettingsByLogin(String login) {
        return userSettingsRepository.findOneByLogin(login).map(u -> {
            return u;
        });
    }

    public Optional<UserSettings> getUserSettingsByEmplCode(String emplcode) {
        return userSettingsRepository.findOneByEmplcode(emplcode).map(u -> {
            return u;
        });
    }

    public UserSettings createUserSettings(String login, String emplcode, String dimension, String lastClientCode) {
        UserSettings newUserSettings = new UserSettings();
        newUserSettings.setLogin(login);
        newUserSettings.setEmplcode(emplcode);
        newUserSettings.setDimension(dimension);
        newUserSettings.setLastClientCode(lastClientCode);
        userSettingsRepository.save(newUserSettings);
        return newUserSettings;
    }

    void checkIfUserSettingsPresent(String login){
        if(!userSettingsRepository.findOneByLogin(login).isPresent()) {
            createUserSettings(login, "", "", "");
        }
    }

    public void updateUserDimension(String login, String dimension){

        checkIfUserSettingsPresent(login);

        userSettingsRepository.findOneByLogin(login).ifPresent(u -> {
            u.setDimension(dimension);
            userSettingsRepository.save(u);
            log.debug("Changed Information for UserSettings: {}", u);
        });
    }

    public void updateUserEmplCode(String login, String emplcode){

        checkIfUserSettingsPresent(login);

        userSettingsRepository.findOneByLogin(login).ifPresent(u -> {
            u.setEmplcode(emplcode);
            userSettingsRepository.save(u);
            log.debug("Changed Information for UserSettings: {}", u);
        });
    }

    public void updateUserLastClient(String login, String lastClientCode){

        checkIfUserSettingsPresent(login);

        userSettingsRepository.findOneByLogin(login).ifPresent(u -> {
            u.setLastClientCode(lastClientCode);
            userSettingsRepository.save(u);
            log.debug("Changed Information for UserSettings: {}", u);
        });
    }

    public void updateUserSettings(String login, String emplcode, String dimension, String lastClientCode){

        checkIfUserSettingsPresent(login);

        userSettingsRepository.findOneByLogin(login).ifPresent(u -> {
            u.setEmplcode(emplcode);
            u.setDimension(dimension);
            u.setLastClientCode(lastClientCode);
            userSettingsRepository.save(u);
            log.debug("Changed Information for UserSettings: {}", u);
        });
    }

}
