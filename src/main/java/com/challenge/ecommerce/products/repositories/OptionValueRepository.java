package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.OptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValueEntity, String> {
  @Query(
      "SELECT COUNT(c) > 0 FROM option_values c WHERE c.value_name = :name AND c.deletedAt IS NULL")
  Boolean existsByOptionValueNameAndDeletedAtIsNull(@Param("name") String name);

  @Query(
      "SELECT b.value_name FROM option_values b WHERE b.option.id =:optionId AND b.deletedAt IS NULL")
  List<String> findAllValueNamesByOptionAndDeletedAtIsNull(@Param("optionId") String optionId);

  @Query("SELECT b FROM option_values b WHERE b.id =:id AND b.deletedAt IS NULL")
  Optional<OptionValueEntity> findByIdAndDeletedAtIsNull(@Param("id") String id);

  @Query("SELECT b FROM option_values b WHERE b.value_name = :valueName AND b.deletedAt IS NULL")
  Optional<OptionValueEntity> findByValueAndDeletedAtIsNull(@Param("valueName") String valueName);
}
