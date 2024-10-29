package com.challenge.ecommerce.products.models;

import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "options")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OptionEntity extends BaseEntity {
  @Column(nullable = false, columnDefinition = "VARCHAR(100)")
  String option_name;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<OptionValueEntity> optionValues = new HashSet<>();

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<ProductOptionEntity> productOptions = new HashSet<>();

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<VariantValueEntity> variantValues = new HashSet<>();
}
