package ru.terracorp.seller.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.terracorp.seller.domain.Authority;
import ru.terracorp.seller.domain.UserSettings;
import ru.terracorp.seller.repository.UserSettingsRepository;
import ru.terracorp.seller.security.AuthoritiesConstants;
import ru.terracorp.seller.service.UserSettingsService;
import ru.terracorp.seller.web.rest.dto.UserSettingsDTO;
import ru.terracorp.seller.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

/**
 * Created by emih on 05.05.2016.
 */
@RestController
@RequestMapping("/api")
public class UserSettingsResource {

    private final Logger log = LoggerFactory.getLogger(UserSettingsResource.class);

    @Inject
    private UserSettingsRepository userSettingsRepository;

    @Inject
    private UserSettingsService userSettingsService;

    /**
     * POST  /userSettings  : Creates a new user settings.
     * <p>
     * Creates a new user settingsd if the employee code are not already used.
     * </p>
     *
     * @param UserSettingsDTO the user settings to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user settings, or with status 400 (Bad Request) if the employee code is already in use
     * @throws URISyntaxException if the Location URI syntaxt is incorrect
     */
    @RequestMapping(value = "/usersettings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<?> createUserSettings(@RequestBody UserSettingsDTO userSettingsDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User settings : {}", userSettingsDTO.toString());

        if (userSettingsRepository.findOneByLogin(userSettingsDTO.getLogin()).isPresent()) {

            return userSettingsRepository.findOneByLogin(userSettingsDTO.getLogin())
                            .map(user -> {
                    user.setEmplcode(userSettingsDTO.getEmplcode());
                    user.setDimension(userSettingsDTO.getDimension());
                    user.setLastClientCode(userSettingsDTO.getLastClientCode());
                    user.setUseDefaultClient(userSettingsDTO.getUseDefaultClient());
                    user.setClients(userSettingsDTO.getClients());
                    userSettingsRepository.save(user);
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createAlert("userManagement.updated", userSettingsDTO.getEmplcode()))
                        .body(new UserSettingsDTO(userSettingsRepository
                            .findOne(user.getId())));
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        } else {
            UserSettings newUserSettings = userSettingsService.createUserSettings(
                userSettingsDTO.getLogin(), userSettingsDTO.getEmplcode(),
                userSettingsDTO.getDimension(), userSettingsDTO.getLastClientCode(),
                userSettingsDTO.getUseDefaultClient(), userSettingsDTO.getClients());
            return ResponseEntity.created(new URI("/api/usersettings/" + newUserSettings.getEmplcode()))
                .headers(HeaderUtil.createAlert( "userSettings.created", newUserSettings.getEmplcode()))
                .body(newUserSettings);
        }
    }

    /**
     * PUT  /usersettings : Updates an existing User settings.
     *
     * @param UserSettingsDTO the user settings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldnt be updated
     */
    @RequestMapping(value = "/usersettings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<?> updateUserSettings(@RequestBody UserSettingsDTO userSettingsDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update User settings : {}", userSettingsDTO.toString());

        Optional<UserSettings> existingUserSettings = userSettingsRepository.findOneByLogin(userSettingsDTO.getLogin());
        if (!existingUserSettings.isPresent()) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login has not been found")).body(null);
            UserSettings newUserSettings = userSettingsService.createUserSettings(
                userSettingsDTO.getLogin(), userSettingsDTO.getEmplcode(),
                userSettingsDTO.getDimension(), userSettingsDTO.getLastClientCode(),
                userSettingsDTO.getUseDefaultClient(), userSettingsDTO.getClients());
            return ResponseEntity.created(new URI("/api/usersettings/" + newUserSettings.getEmplcode()))
                .headers(HeaderUtil.createAlert( "userSettings.created", newUserSettings.getEmplcode()))
                .body(newUserSettings);
        } else {
            return userSettingsRepository
                .findOneByLogin(userSettingsDTO.getLogin())
                .map(user -> {
                    user.setEmplcode(userSettingsDTO.getEmplcode());
                    user.setDimension(userSettingsDTO.getDimension());
                    user.setLastClientCode(userSettingsDTO.getLastClientCode());
                    user.setUseDefaultClient(userSettingsDTO.getUseDefaultClient());
                    user.setClients(userSettingsDTO.getClients());

                    userSettingsRepository.save(user);
                    return ResponseEntity.ok()
                        .headers(HeaderUtil.createAlert("userManagement.updated", userSettingsDTO.getEmplcode()))
                        .body(new UserSettingsDTO(userSettingsRepository
                            .findOne(user.getId())));
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    /**
     * GET  /usersettings/:login : get user settings.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the user settings, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/usersettings/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserSettingsDTO> getUserSettings(@PathVariable String login) {
        log.debug("REST request to get User settings : {}", login);
        return userSettingsService.getUserSettingsByLogin(login)
            .map(UserSettingsDTO::new)
            .map(userSettingsDTO -> new ResponseEntity<>(userSettingsDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
