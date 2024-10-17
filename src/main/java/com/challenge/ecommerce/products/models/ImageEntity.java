package com.challenge.ecommerce.products.models;

import com.challenge.ecommerce.utils.BaseEntity;
import com.challenge.ecommerce.utils.enums.TypeImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "images")
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ImageEntity extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(2083)")
    String images_url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TypeImage type_image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    ProductEntity product;
}
