package com.challenge.ecommerce.reviews.models;

import com.challenge.ecommerce.orders.models.OrderItemEntity;
import com.challenge.ecommerce.users.models.UserEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "reviews")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity extends BaseEntity {
  @Column(columnDefinition = "TEXT")
  String content;

  @Min(1)
  @Max(5)
  Integer rating;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  UserEntity user;

  @OneToOne @JoinColumn OrderItemEntity orderItem;
}
