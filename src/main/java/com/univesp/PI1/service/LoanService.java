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
     public void itemLoan(ItemLoanDTO dto){

         Loan loan = new Loan();
         loan.setApplicant(retrieveApplicant(dto.getApplicantId()));

         Item item = retrieveItem(dto.getItemId());
         loan.setItem(checkItemAvailability(item));
         itemService.executeLoan(item);

         loan.setLoanDate(Instant.now());
         loan.setLoanStatus(LoanStatus.PROGGRESS);


         loan.setLoanDevolution(setDevolutionDate(dto.getDevolutionDays()));

        loanRepository.save(loan);
     }

     public void itemDevolution(Integer loanId){

         Loan loan = loanRepository.findById(loanId)
                 .orElseThrow(() -> new RuntimeException("Loan does not exists."));

         if(loan.getLoanStatus().equals(LoanStatus.PROGGRESS)){
             Item item = retrieveItem(loan.getItem().getId());
             itemService.executeDevolution(item);
             loan.setLoanStatus(LoanStatus.RETURNED);
             loanRepository.save(loan);
         }else throw new RuntimeException("Loan is not in progress.");

     }
     public List<FindLoansDTO> findLoans(LoanStatus status){

        List<Loan> loans = loanRepository.findByStatus(status);
        return loans.stream().map(FindLoansDTO::new).toList();
     }

     private Loan retrieveLoan(Integer id){
         return loanRepository.findById(id)
                 .orElseThrow();
     }

     private Applicant retrieveApplicant(Integer id){
         return applicantRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Applicant does not exists."));
     }

     private Item retrieveItem(Integer id){
         return itemRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Item does not exists."));
     }

     private Item checkItemAvailability(Item item){
         if(item.getStatus().equals(ItemStatus.AVAILABLE)) return item;
         else throw new RuntimeException("Item is not available for loan.");
     }

     private Instant setDevolutionDate(Long devolutionDays){
        if(devolutionDays < 1) {throw new NumberFormatException("Invalid devolution days.");}

         return Instant.now().plus(devolutionDays, ChronoUnit.DAYS);
     }
}
