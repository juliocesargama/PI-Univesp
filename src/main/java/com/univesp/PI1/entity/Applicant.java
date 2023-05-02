package com.univesp.PI1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phone", nullable = false)
    private String phone;
}
