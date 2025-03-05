package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.entity.DTOS.FindLoansDTO;
import com.univesp.PI1.entity.DTOS.ItemLoanDTO;
import com.univesp.PI1.entity.Enums.ItemStatus;
import com.univesp.PI1.entity.Enums.LoanStatus;
import com.univesp.PI1.entity.Item;
import com.univesp.PI1.entity.Loan;
import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    LoanRepository loanRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ApplicantRepository applicantRepository;
    @InjectMocks
    LoanService loanService;

    @Mock
    ItemService itemService;

    Applicant applicant = new Applicant();
    Item item = new Item();
    ItemLoanDTO dto = new ItemLoanDTO();
    Loan loan = new Loan();
    Loan otherLoan = new Loan();

    List<Loan> loans = new ArrayList<>();
    @BeforeEach
    void setup(){
        applicant.setId(1);
        applicant.setName("Ana");
        applicant.setPhone("99999-9999");

        item.setId(1);
        item.setName("item");
        item.setDescription("description");
        item.setStatus(ItemStatus.Disponivel);

        loan.setItem(item);
        loan.setApplicant(applicant);
        loan.setLoanDate(Instant.now());
        loan.setLoanDevolution(Instant.now().plus(15, ChronoUnit.DAYS));
        loan.setLoanStatus(LoanStatus.Andamento);

        otherLoan.setId(2);
        otherLoan.setItem(item);
        otherLoan.setApplicant(applicant);
        otherLoan.setLoanDate(Instant.now());
        otherLoan.setLoanDevolution(Instant.now().plus(15, ChronoUnit.DAYS));
        otherLoan.setLoanStatus(LoanStatus.Devolvido);

        loans.add(loan);
        loans.add(otherLoan);

        dto.setItemId(item.getId());
        dto.setApplicantId(applicant.getId());
        dto.setDevolutionDays(15L);
    }

    @Test
    void ItemLoanTest(){
        Mockito.when(applicantRepository.findById(applicant.getId()))
                .thenReturn(Optional.ofNullable(applicant));

        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.doNothing().when(itemService).executeLoan(Mockito.any(Item.class));

        loanService.itemLoan(dto);

        Mockito.verify(loanRepository, Mockito.times(1)).save(Mockito.any(Loan.class));
        verify(itemService).executeLoan(item);
    }

    @Test
    void LoanItemNotAvailableException(){
        item.setStatus(ItemStatus.Emprestado);

        Mockito.when(applicantRepository.findById(applicant.getId()))
                .thenReturn(Optional.ofNullable(applicant));

        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.ofNullable(item));

        assertThrows(RuntimeException.class,() -> loanService.itemLoan(dto),
                "Item não está disponível para empréstimo.");
    }

    @Test
    void LoanDevolutionDaysInvalid(){
        dto.setDevolutionDays(-1L);

        Mockito.when(applicantRepository.findById(applicant.getId()))
                .thenReturn(Optional.ofNullable(applicant));

        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.doNothing().when(itemService).executeLoan(Mockito.any(Item.class));

        assertThrows(RuntimeException.class,() -> loanService.itemLoan(dto),
                "Quantidade de dias inválido.");
    }

    @Test
    void ItemDevolutionTest(){

        Mockito.when(loanRepository.findById(loan.getId()))
                .thenReturn(Optional.ofNullable(loan));

        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.doNothing().when(itemService).executeDevolution(Mockito.any(Item.class));

        Loan expectedLoan = loanService.itemDevolution(loan.getId());

        verify(itemService).executeDevolution(item);
        verify(loanRepository).save(loan);
        assertEquals(expectedLoan.getLoanStatus(),LoanStatus.Devolvido);
    }

    @Test
    void DevolutionFailedLoanNotInProgress(){
        loan.setLoanStatus(LoanStatus.Devolvido);

        Mockito.when(loanRepository.findById(loan.getId()))
                .thenReturn(Optional.ofNullable(loan));

        assertThrows(RuntimeException.class,() -> loanService.itemDevolution(loan.getId()),
                "Empréstimo não está em andamento.");
    }

    @Test
    void FindAllLoansTest(){
        String status = "Todos";

        Mockito.when(loanRepository.findAll()).thenReturn(loans);
        List<FindLoansDTO> expectedList = loanService.findLoans(status);

        assertEquals(expectedList.size(),2);
    }

    @Test
    void FindLoansByStatusTest(){
        String status = "Devolvido";

        Mockito.when(loanRepository.findByStatus(LoanStatus.valueOf(status)))
                .thenReturn(Collections.singletonList(otherLoan));

        List<FindLoansDTO> expectedList = loanService.findLoans(status);

        assertEquals(expectedList.size(),1);
        assertEquals(otherLoan.getLoanStatus(),
                expectedList.stream()
                .findFirst().get()
                .getLoanStatus());
    }

    @Test
    void CheckLoansDelayedTest(){
        loan.setLoanDevolution(Instant.now().minus(2, ChronoUnit.DAYS));

        Mockito.when(loanRepository.findAll()).thenReturn(loans);

        loanService.checkLoansDelayed();
        List<Loan> expectedResult = loanRepository.findAll();

        assertTrue(expectedResult.stream()
                .anyMatch(result -> result.getLoanStatus().equals(LoanStatus.Atrasado)));
        verify(loanRepository).saveAll(loans);
    }
}
