package com.challenge.ecommerce.users.models;

import com.challenge.ecommerce.favorites.models.FavoriteEntity;
import com.challenge.ecommerce.orders.models.OrderEntity;
import com.challenge.ecommerce.reviews.models.ReviewEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.challenge.ecommerce.utils.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {
    @Column(nullable = false,columnDefinition = "VARCHAR(100)")
    String name;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(columnDefinition = "VARCHAR(2083)")
    String avatar_link;

    String refresh_token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<DeliveryAddressEntity> deliveryAddresses = new HashSet<>();

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<FavoriteEntity> favorites = new HashSet<>();

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<ReviewEntity> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<OrderEntity> orders = new HashSet<>();
}
