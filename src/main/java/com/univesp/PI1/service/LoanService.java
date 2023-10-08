package com.univesp.PI1.service;

import com.univesp.PI1.entity.*;
import com.univesp.PI1.entity.DTOS.FindLoansDTO;
import com.univesp.PI1.entity.DTOS.ItemLoanDTO;
import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.entity.Enums.LoanStatus;
import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.repository.LoanRepository;
import com.univesp.PI1.utils.handler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Transactional
     public void itemLoan(ItemLoanDTO dto){

         Loan loan = new Loan();
        Applicant applicant = applicantRepository.findById(dto.getApplicantId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Requerente não encontrado."));
         loan.setApplicant(applicant);

         Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Item não encontrado."));

        if(!item.getStatus().equals(ItemStatus.Disponivel)){
            throw new CustomException(HttpStatus.BAD_REQUEST, "Item não está disponível para empréstimo.");
        }
        loan.setItem(item);
        itemService.executeLoan(item);

         loan.setLoanDate(Instant.now());
         loan.setLoanStatus(LoanStatus.Andamento);

        if(dto.getDevolutionDays() < 1) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Quantidade de dias inválido.");
        }else{
            loan.setLoanDevolution(Instant.now().plus(dto.getDevolutionDays(), ChronoUnit.DAYS));
        }
        loanRepository.save(loan);
     }

     @Transactional
     public Loan itemDevolution(Integer loanId){

         Loan loan = loanRepository.findById(loanId)
                 .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Empréstimo inexistente."));

         if(!loan.getLoanStatus().equals(LoanStatus.Devolvido)){
             Item item = itemRepository.findById(loan.getItem().getId())
                     .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Item não encontrado."));
             itemService.executeDevolution(item);
             loan.setLoanStatus(LoanStatus.Devolvido);
             loanRepository.save(loan);
             return loan;
         }else throw new CustomException(HttpStatus.BAD_REQUEST, "Empréstimo não está em andamento.");
     }
     @Transactional
     public List<FindLoansDTO> findLoans(String status){
         List<Loan> loans;
        if(status.equalsIgnoreCase("ALL")){
            loans = loanRepository.findAll();
        } else {
            loans = loanRepository.findByStatus(LoanStatus.valueOf(status));
        }
         return loans.stream().map(FindLoansDTO::new).collect(Collectors.toList());
     }

     @Transactional
    public void checkLoansDelayed() {
        List<Loan> loans = loanRepository.findAll();
        loans.forEach(loan -> {
             if(loan.getLoanStatus().equals(LoanStatus.Andamento) &&
                     Instant.now().isAfter(loan.getLoanDevolution())) {
                 loan.setLoanStatus(LoanStatus.Atrasado);
             }
        });
        loanRepository.saveAll(loans);
    }
}
