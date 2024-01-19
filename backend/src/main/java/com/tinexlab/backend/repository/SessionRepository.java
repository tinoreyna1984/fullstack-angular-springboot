package com.tinexlab.backend.repository;

import com.tinexlab.backend.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Transactional
    @Modifying
    @Query("update Session s set s.fechaFinSesion = current_timestamp where s.jwt = :jwt")
    void agregarFechaFinSesion(@Param(value = "jwt") String jwt);
}
