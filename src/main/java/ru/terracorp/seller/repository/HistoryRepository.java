package ru.terracorp.seller.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.terracorp.seller.domain.History;

import java.util.List;

/**
 * Spring Data MongoDB repository for the History entity.
 */
public interface HistoryRepository extends MongoRepository<History,String> {
    List<History> findByClientAndEmplcodeAndType(String client, String emplcode, Integer type);
    List<History> findByEmplcodeAndType(String emplcode, Integer type);
}
