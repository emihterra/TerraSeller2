package ru.terracorp.seller.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.terracorp.seller.domain.UserSettings;

import java.util.Optional;

/**
 * Created by emih on 05.05.2016.
 */
public interface UserSettingsRepository extends MongoRepository<UserSettings, String> {

    Optional<UserSettings> findOneByLogin(String login);

    Optional<UserSettings> findOneByEmplcode(String emplcode);

    Optional<UserSettings> findOneById(String userId);

    @Override
    void delete(UserSettings t);
}
