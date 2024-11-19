package com.challenge.ecommerce.orders.repository;

import com.challenge.ecommerce.orders.models.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, String> {
  @Query("SELECT oi FROM order_items oi WHERE oi.id = :orderId AND oi.deletedAt IS NULL")
  Optional<OrderItemEntity> findActiveOrderItemByOrderId(@Param("orderId") String orderId);
}
