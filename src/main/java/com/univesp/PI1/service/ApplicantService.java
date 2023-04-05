package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantService {
    @Autowired
    private ApplicantRepository applicantRepository;

    public List<Applicant> findAll(){
        return applicantRepository.findAll();
    }

    public void save(Applicant applicant){

        if(applicant.getId() != null) {
            Applicant existingApplicant = applicantRepository
                    .findById(applicant.getId())
                    .orElseThrow(() -> new RuntimeException("Applicant does not exists"));

            existingApplicant.setName(applicant.getName());
            existingApplicant.setPhone(applicant.getPhone());
            applicantRepository.save(existingApplicant);
        }else{
            applicantRepository.save(applicant);
        }
    }
}
