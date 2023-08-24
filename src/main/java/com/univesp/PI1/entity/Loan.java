package com.univesp.PI1.entity;

import com.univesp.PI1.entity.Enums.LoanStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Item item;
    @ManyToOne
    private Applicant applicant;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")

    private Instant loanDate;
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant loanDevolution;

    @Column(nullable = false)
    private LoanStatus loanStatus;
}
