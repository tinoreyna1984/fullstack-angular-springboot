package com.tinexlab.backend.repository;

import com.tinexlab.backend.model.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);

    // actualiza usuario con token generado
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.resetPasswordToken = :token WHERE u.email = :email")
    void updateResetPasswordToken(@Param(value = "email")String email, @Param(value = "token")String token);

    // implementación con clases Custom
    @Query("SELECT u.failedAttempts FROM User u WHERE u.username = ?1")
    public int getFailedAttempts(String username);
    @Query("SELECT u.accountNonLocked FROM User u WHERE u.username = ?1")
    public boolean isAccountNotLocked(String username);

    // dashboard
    @Query("SELECT count(*) FROM User u")
    public long totalUsers();
    //@Query("SELECT count(*) FROM User u WHERE u.accountNonExpired = true")
    @Query(value = "SELECT totalActiveUsers()", nativeQuery = true)
    public long totalActiveUsers();
    //@Query("SELECT count(*) FROM User u WHERE u.accountNonLocked = false")
    @Query(value = "select totalLockedUsers()", nativeQuery = true)
    public long totalLockedUsers();
}
