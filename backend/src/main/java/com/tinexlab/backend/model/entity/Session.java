package com.tinexlab.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sesiones")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column(nullable = true, length = 10240)
    private String jwt;

    @Column(name = "fecha_inicio_sesion")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaInicioSesion;

    @Column(name = "fecha_fin_sesion")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaFinSesion;

    @ManyToOne
    @JoinColumn(name="usuario_id", referencedColumnName = "usuario_id")
    @JsonBackReference
    private User user;
}
