package com.univesp.PI1.repository;

import com.univesp.PI1.entity.Loan;
import com.univesp.PI1.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer> {
    List<Loan> findByStatus(LoanStatus status);
}
