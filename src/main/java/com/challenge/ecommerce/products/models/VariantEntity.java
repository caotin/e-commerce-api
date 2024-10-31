package com.challenge.ecommerce.products.models;

import com.challenge.ecommerce.orders.models.OrderItemEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "variants")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class VariantEntity extends BaseEntity {

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    String sku_id;

    @Column(nullable = false)
    Integer stock_quantity;

    @Column(nullable = false)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    ProductEntity product;

    @OneToMany(mappedBy = "variant" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<VariantValueEntity> variantValues = new HashSet<>();

    @OneToMany(mappedBy = "variant" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<OrderItemEntity> orderItems = new HashSet<>();
}
