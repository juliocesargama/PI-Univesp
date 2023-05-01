package com.univesp.PI1.entity;

import com.univesp.PI1.entity.Enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "loan_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")

    private Instant loanDate;
    @Column(name = "loan_devolution", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant loanDevolution;

    @Column(name = "loan_status", nullable = false)
    private LoanStatus loanStatus;
}
