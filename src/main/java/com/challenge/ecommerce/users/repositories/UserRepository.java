package com.challenge.ecommerce.users.repositories;

import com.challenge.ecommerce.users.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
    extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

  @Query("SELECT u FROM users u WHERE u.email = :email AND u.deletedAt IS NULL")
  Optional<UserEntity> findByEmailAndNotDeleted(@Param("email") String email);

  @Query("SELECT u FROM users u WHERE u.id = :userId AND u.deletedAt IS NULL")
  Optional<UserEntity> findByIdAndNotDeleted(@Param("userId") String userId);

  @Query(
      "select case when count(u) > 0 then true else false end from users u where u.deletedAt is  null and u.email = :email")
  boolean findActiveUserEmails(@Param(value = "email") String email);

  @Query(
      "select case when count(u) > 0 then true else false end from users u where u.deletedAt is  null and u.name = :name")
  boolean findActiveUserName(@Param(value = "name") String name);

  Page<UserEntity> findAllByDeletedAtIsNull(Pageable pageable);
}
