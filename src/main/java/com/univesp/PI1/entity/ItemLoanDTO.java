package com.univesp.PI1.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;


@Data
public class ItemLoanDTO implements Serializable {

    private Integer itemId;
    private Integer applicantId;
    private Instant loanDevolution;
}
