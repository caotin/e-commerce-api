package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.OptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, String> {
  @Query("SELECT COUNT(c) > 0 FROM options c WHERE c.option_name = :name AND c.deletedAt IS NULL")
  Boolean existsByOptionNameAndDeletedAtIsNull(@Param("name") String name);

  @Query("SELECT b FROM options b WHERE b.id = :optionId AND b.deletedAt IS NULL")
  Optional<OptionEntity> findByIdAndDeletedAtIsNull(@Param("optionId") String optionId);

  @Query("SELECT b FROM options b WHERE b.deletedAt IS NULL")
  Page<OptionEntity> findAllByDeletedAtIsNull(Pageable pageable);

  @Query(
      "SELECT b.id FROM options b INNER JOIN option_values c ON c.option.id = b.id WHERE c.id =:optionValueId AND b.deletedAt IS NULL AND c.deletedAt IS NULL")
  Optional<String> findByOptionValueIdAndDeletedAtIsNull(
      @Param("optionValueId") String optionValueId);

  @Query("SELECT COUNT(c) > 0 FROM option_values c WHERE c.option.id = :id AND c.deletedAt IS NULL")
  Boolean existsOptionValueByOption(@Param("id") String id);
}
