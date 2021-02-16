package com.quoteme.qmservice.repository;

import com.quoteme.qmservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmailAndDeletedFalse(String email);

    @Modifying
    @Query("update user u set u.lastLoginDate = ?2 where u.id = ?1")
    void updateLastLoginDate(UUID id, Long date);

    @Modifying
    @Query(value = "update user u set u.deleted = true where u.email = ?1")
    void delete(String email);
}
