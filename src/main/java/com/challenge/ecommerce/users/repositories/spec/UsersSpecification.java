package com.challenge.ecommerce.users.repositories.spec;

import com.challenge.ecommerce.users.models.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UsersSpecification {
  public static Specification<UserEntity> hasName(String name) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("name"), "%" + name + "%");
  }

  public static Specification<UserEntity> isNotDeleted() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
  }
}
