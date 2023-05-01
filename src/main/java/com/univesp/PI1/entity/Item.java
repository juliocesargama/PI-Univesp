package com.univesp.PI1.entity;

import com.univesp.PI1.entity.Enums.ItemStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

}
