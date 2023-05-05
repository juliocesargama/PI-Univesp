package com.univesp.PI1.service;

import com.univesp.PI1.entity.*;
import com.univesp.PI1.entity.DTOS.FindLoansDTO;
import com.univesp.PI1.entity.DTOS.ItemLoanDTO;
import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.entity.Enums.LoanStatus;
import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
     public Loan itemLoan(ItemLoanDTO dto){

         Loan loan = new Loan();
        Applicant applicant = applicantRepository.findById(dto.getApplicantId())
                .orElseThrow(() -> new RuntimeException("Applicant does not exists."));
         loan.setApplicant(applicant);

         Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item does not exists."));

        if(!item.getStatus().equals(ItemStatus.AVAILABLE)){
            throw new RuntimeException("Item is not available for loan.");
        }
        loan.setItem(item);
        itemService.executeLoan(item);

         loan.setLoanDate(Instant.now());
         loan.setLoanStatus(LoanStatus.PROGGRESS);

        if(dto.getDevolutionDays() < 1) {
            throw new NumberFormatException("Invalid devolution days.");
        }else{
            loan.setLoanDevolution(Instant.now().plus(dto.getDevolutionDays(), ChronoUnit.DAYS));
        }
        loanRepository.save(loan);
        return loan;
     }

     @Transactional
     public Loan itemDevolution(Integer loanId){

         Loan loan = loanRepository.findById(loanId)
                 .orElseThrow(() -> new RuntimeException("Loan does not exists."));

         if(!loan.getLoanStatus().equals(LoanStatus.RETURNED)){
             Item item = itemRepository.findById(loan.getItem().getId())
                     .orElseThrow(() -> new RuntimeException("Item does not exists."));
             itemService.executeDevolution(item);
             loan.setLoanStatus(LoanStatus.RETURNED);
             loanRepository.save(loan);
             return loan;
         }else throw new RuntimeException("Loan is not in progress.");
     }
     @Transactional
     public List<FindLoansDTO> findLoans(String status){
         List<Loan> loans;
        if(status.equalsIgnoreCase("ALL")){
            loans = loanRepository.findAll();
        } else {
            loans = loanRepository.findByStatus(LoanStatus.valueOf(status));
        }
         return loans.stream().map(FindLoansDTO::new).toList();
     }

     @Transactional
    public void checkLoansDelayed() {
        List<Loan> loans = loanRepository.findAll();
        loans.forEach(loan -> {
             if(loan.getLoanStatus().equals(LoanStatus.PROGGRESS) &&
                     Instant.now().isAfter(loan.getLoanDevolution())) {
                 loan.setLoanStatus(LoanStatus.DELAYED);
             }
        });
        loanRepository.saveAll(loans);
    }
}
