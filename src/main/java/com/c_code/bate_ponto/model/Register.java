package com.c_code.bate_ponto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "register")
@Getter
@Setter
@NoArgsConstructor
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime dataTime;

    @Enumerated(EnumType.STRING)
    private RegisterType type;

    private boolean edited;

    private String observation;

}
