package com.univesp.PI1.entity.DTOS;

import lombok.Data;

import java.io.Serializable;

@Data
public class ItemLoanDTO implements Serializable {

    private Integer itemId;
    private Integer applicantId;
    private Long devolutionDays;
}
