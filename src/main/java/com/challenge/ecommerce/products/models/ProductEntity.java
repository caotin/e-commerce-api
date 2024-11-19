package com.challenge.ecommerce.products.models;

import com.challenge.ecommerce.categories.models.CategoryEntity;
import com.challenge.ecommerce.favorites.models.FavoriteEntity;
import com.challenge.ecommerce.reviews.models.ReviewEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "products")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity extends BaseEntity {
  @Column(nullable = false)
  String title;

  @Lob
  @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
  String description;

  @Column(nullable = false)
  String slug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  CategoryEntity category;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<ProductOptionEntity> productOptions = new HashSet<>();

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<ImageEntity> images = new HashSet<>();

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<FavoriteEntity> favorites = new HashSet<>();

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<VariantEntity> variants = new HashSet<>();
}
