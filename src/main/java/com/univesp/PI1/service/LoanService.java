package com.univesp.PI1.service;

import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.repository.ItemRepository;
import com.univesp.PI1.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    ItemRepository itemRepository;

}
