package com.challenge.ecommerce.users.models;

import com.challenge.ecommerce.orders.models.OrderEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "delivery_address")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DeliveryAddressEntity extends BaseEntity {
    @Lob
    @Column(columnDefinition = "TEXT")
    String guide_position;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    String province;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    String district;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    String ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    UserEntity user;

    @OneToMany(mappedBy = "deliveryAddress" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<OrderEntity> orders = new HashSet<>();
}
