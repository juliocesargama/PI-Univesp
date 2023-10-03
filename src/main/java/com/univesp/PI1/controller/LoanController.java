package com.univesp.PI1.controller;

import com.univesp.PI1.entity.DTOS.FindLoansDTO;
import com.univesp.PI1.entity.DTOS.ItemLoanDTO;
import com.univesp.PI1.service.LoanService;
import com.univesp.PI1.utils.handler.EndpointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/loan/new")
    public ResponseEntity<EndpointResponse> executeLoan(@RequestBody ItemLoanDTO dto){
        loanService.itemLoan(dto);
        return ResponseEntity.ok().body(new EndpointResponse("Empréstimo realizado com sucesso!"));
    }

    @PostMapping("/devolution/{devolutionId}")
    public ResponseEntity<EndpointResponse> executeDevolution(@PathVariable Integer devolutionId){
        loanService.itemDevolution(devolutionId);
        return ResponseEntity.ok().body(new EndpointResponse("Devolução realizada com sucesso!"));
    }

    @GetMapping("/loan/find/{status}")
    public ResponseEntity<List<FindLoansDTO>> findLoans(@PathVariable String status){
        return ResponseEntity.ok(loanService.findLoans(status));
    }

    @PostMapping("/loan/check-delayed")
    public ResponseEntity<EndpointResponse> checkLoansDelayed(){

        loanService.checkLoansDelayed();
        return ResponseEntity.ok().body(new EndpointResponse("Verificação efetuada com sucesso!"));
    }
}
