package com.challenge.ecommerce.categories.models;

import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "categories")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity extends BaseEntity {
  @Column(nullable = false, columnDefinition = "VARCHAR(100)")
  String name;

  @Column(columnDefinition = "VARCHAR(2083)")
  String category_img;

  @Column(nullable = false)
  String slug;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<ProductEntity> products = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "parent_category_id")
  @JsonBackReference
  CategoryEntity parentCategory;

  @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<CategoryEntity> childCategories = new HashSet<>();
}
