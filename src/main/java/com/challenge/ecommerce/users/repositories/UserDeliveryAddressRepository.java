package com.challenge.ecommerce.users.repositories;

import com.challenge.ecommerce.users.models.DeliveryAddressEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeliveryAddressRepository
    extends JpaRepository<DeliveryAddressEntity, String> {
  @Query("SELECT d FROM delivery_address d WHERE d.user.id = :userId AND d.deletedAt IS NULL")
  List<DeliveryAddressEntity> findAllDeliveryAddressActive(@Param("userId") String userId);

  @Query("SELECT d FROM delivery_address d WHERE d.id = :id AND d.deletedAt IS NULL")
  Optional<DeliveryAddressEntity> findDeliveryAddressActive(@Param("id") String id);

  @Query("SELECT COUNT(da) > 0 FROM delivery_address  da WHERE da.user.id = :userId AND da.id = :deliveryId")
  boolean existsByUserIdAndDeliveryId(@Param("userId") String userId, @Param("deliveryId") String deliveryId);

}
