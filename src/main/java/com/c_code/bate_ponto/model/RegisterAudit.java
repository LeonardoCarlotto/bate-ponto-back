package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "register_audit")
@Getter
@Setter
@NoArgsConstructor
public class RegisterAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long registerId;

    private Long userId;

    @Column(length = 1000)
    private String oldData;

    @Column(length = 1000)
    private String newData;

    @Column(length = 1000)
    private String observation;

    private LocalDateTime editedAt;

    private Long editedByUserId;

}
