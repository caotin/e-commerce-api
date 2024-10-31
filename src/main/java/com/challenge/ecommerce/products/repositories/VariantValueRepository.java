package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.VariantValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValueEntity, String> {}
