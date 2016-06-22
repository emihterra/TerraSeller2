package ru.terracorp.seller.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.terracorp.seller.domain.ItemType;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the ItemType entity.
 */
public interface ItemTypeRepository extends MongoRepository<ItemType,String> {
    Optional<ItemType> findOneByCode(String code);
}
