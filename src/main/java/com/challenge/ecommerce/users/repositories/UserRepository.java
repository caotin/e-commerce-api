package com.challenge.ecommerce.users.repositories;

import com.challenge.ecommerce.users.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository
    extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query(
      "select case when count(u) > 0 then true else false end from users u where u.deletedAt is  null and u.email = :email")
  boolean findActiveUserEmails(@Param(value = "email") String email);

  @Query(
      "select case when count(u) > 0 then true else false end from users u where u.deletedAt is  null and u.name = :name")
  boolean findActiveUserName(@Param(value = "name") String name);

  Page<UserEntity> findAllByDeletedAtIsNull(Pageable pageable);
}
