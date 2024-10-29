package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.OptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValueEntity, String> {
    @Query("SELECT COUNT(c) > 0 FROM option_values c WHERE c.value_name = :name AND c.deletedAt IS NULL")
    Boolean existsByOptionValueNameAndDeletedAtIsNull(@Param("name") String name);
}
