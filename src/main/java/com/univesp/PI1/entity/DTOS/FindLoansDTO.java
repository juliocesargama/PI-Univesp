package com.univesp.PI1.entity.DTOS;

import com.univesp.PI1.entity.Enums.LoanStatus;
import com.univesp.PI1.entity.Loan;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Data
public class FindLoansDTO implements Serializable {

    Integer loanId;
    String itemName;
    String applicantName;
    String loanDate;
    String loanDevolution;
    LoanStatus loanStatus;
    public FindLoansDTO(Loan loan){
        loanId = loan.getId();
        itemName = loan.getItem().getName();
        applicantName = loan.getApplicant().getName();
        loanDate = formatDate(loan.getLoanDate());
        loanDevolution = formatDate(loan.getLoanDevolution());
        loanStatus = loan.getLoanStatus();
    }

    private String formatDate(Instant instant){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.UK)
                        .withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }
}

