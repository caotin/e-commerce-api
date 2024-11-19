package com.challenge.ecommerce.orders.models;

import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.reviews.models.ReviewEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity(name = "order_items")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OrderItemEntity extends BaseEntity {
  @Column(nullable = false)
  Integer quantity;

  @Column(nullable = false)
  BigDecimal item_total_price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  OrderEntity order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  VariantEntity variant;

  @OneToOne(mappedBy = "orderItem")
  ReviewEntity review;
}
