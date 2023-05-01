package com.univesp.PI1.repository;

import com.univesp.PI1.entity.Loan;
import com.univesp.PI1.entity.Enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer> {

   @Query("SELECT l FROM Loan l WHERE l.loanStatus = ?1")
    List<Loan> findByStatus(LoanStatus status);
}
