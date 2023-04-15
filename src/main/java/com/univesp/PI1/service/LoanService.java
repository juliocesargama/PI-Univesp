package com.univesp.PI1.service;

import com.univesp.PI1.entity.*;
import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

        item.setStatus(ItemStatus.BORROWED);
        itemRepository.save(item);

         loan.setLoanDate(Instant.now());
         loan.setLoanStatus(LoanStatus.PROGGRESS);
         loan.setLoanDevolution(validadeDevolutionDate(dto.getLoanDevolution()));

        loanRepository.save(loan);
     }

     public void itemDevolution(Integer devolutionId){

         Loan loan = retrieveLoan(devolutionId);
         if(loan.getLoanStatus().equals(LoanStatus.PROGGRESS)){
             Item item = retrieveItem(loan.getItem().getId());
             itemService.executeDevolution(item);
             loan.setLoanStatus(LoanStatus.RETURNED);
             loanRepository.save(loan);
         }else throw new RuntimeException("Loan is not in progress.");

     }
     public List<Loan> findLoans(LoanStatus status){
        return loanRepository.findByStatus(status);
     }

     private Loan retrieveLoan(Integer id){
         return loanRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Loan does not exists."));
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

     private Instant validadeDevolutionDate(Instant devolutionDate){
         if(devolutionDate.isAfter(Instant.now())) return devolutionDate;
         else throw new RuntimeException("Invalid Date.");
     }
}
