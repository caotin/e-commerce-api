package com.challenge.ecommerce.orders.models;

import com.challenge.ecommerce.users.models.DeliveryAddressEntity;
import com.challenge.ecommerce.users.models.UserEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.challenge.ecommerce.utils.enums.OrderStatus;
import com.challenge.ecommerce.utils.enums.PaymentMethod;
import com.challenge.ecommerce.utils.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @Column(nullable = false)
    BigDecimal total_price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentMethod payment_method;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus payment_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    DeliveryAddressEntity deliveryAddress;

    @OneToMany(mappedBy = "order" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<OrderItemEntity> orderItems = new HashSet<>();
}
