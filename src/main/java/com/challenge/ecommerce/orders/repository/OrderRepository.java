package com.challenge.ecommerce.orders.repository;

import com.challenge.ecommerce.orders.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
  @Query("select od from orders od where od.id=:orderId and od.deletedAt is null ")
  Optional<OrderEntity> findOrderActiveByOrderId(@Param("orderId") String orderId);
}
