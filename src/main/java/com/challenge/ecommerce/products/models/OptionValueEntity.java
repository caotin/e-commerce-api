package com.challenge.ecommerce.products.models;

import com.challenge.ecommerce.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "option_values")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OptionValueEntity extends BaseEntity {
  @Column(nullable = false, columnDefinition = "VARCHAR(100)")
  String value_name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @JsonBackReference
  OptionEntity option;

  @OneToMany(mappedBy = "optionValue", fetch = FetchType.LAZY)
  @JsonManagedReference
  Set<VariantValueEntity> variantValues = new HashSet<>();
}
