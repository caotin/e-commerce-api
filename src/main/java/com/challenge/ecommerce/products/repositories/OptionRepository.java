package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, String> {
  @Query("SELECT COUNT(c) > 0 FROM options c WHERE c.option_name = :name AND c.deletedAt IS NULL")
  Boolean existsByOptionNameAndDeletedAtIsNull(@Param("name") String name);

  @Query("SELECT b FROM options b WHERE b.id = :optionId AND b.deletedAt IS NULL")
  Optional<OptionEntity> findByIdAndDeletedAtIsNull(@Param("optionId") String optionId);
}
