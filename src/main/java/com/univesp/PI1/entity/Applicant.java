package com.univesp.PI1.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
}
