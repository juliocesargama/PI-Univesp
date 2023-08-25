package com.univesp.PI1.controller;

import com.univesp.PI1.entity.DTOS.FindLoansDTO;
import com.univesp.PI1.entity.DTOS.ItemLoanDTO;
import com.univesp.PI1.entity.Loan;
import com.univesp.PI1.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/loan/new")
    public Loan executeLoan(@RequestBody ItemLoanDTO dto){
        return loanService.itemLoan(dto);
    }

    @PostMapping("/devolution/{devolutionId}")
    public void executeDevolution(@PathVariable Integer devolutionId){
        loanService.itemDevolution(devolutionId);
    }

    @GetMapping("/loan/find/{status}")
    public List<FindLoansDTO> findLoans(@PathVariable String status){
        return loanService.findLoans(status);
    }

    @PostMapping("/loan/check-delayed")
    public void checkLoansDelayed(){
        loanService.checkLoansDelayed();
    }
}
