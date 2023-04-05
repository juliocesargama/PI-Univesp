package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {
    @Autowired
    private ApplicantRepository applicantRepository;

    public List<Applicant> findAll(){
        return applicantRepository.findAll();
    }

    public void save(Applicant applicant){

        Optional<Applicant> existingApplicant = applicantRepository.findById(applicant.getId());

        if(existingApplicant.isPresent()) {
            existingApplicant.get().setName(applicant.getName());
            existingApplicant.get().setPhone(applicant.getPhone());
            applicantRepository.save(existingApplicant.get());
        }else{
            applicantRepository.save(applicant);
        }
    }
}
