package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.entity.DTOS.ApplicantDTO;
import com.univesp.PI1.repository.ApplicantRepository;
import com.univesp.PI1.utils.handler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantService {
    @Autowired
    private ApplicantRepository applicantRepository;

    public List<Applicant> findAll(){
        return applicantRepository.findAll();
    }

    public Applicant findById(Integer id){
        return applicantRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Requerente não encontrado."));
    }

    public void save(Applicant applicant){

        if(applicant.getId() == null) {
            applicantRepository.save(applicant);
        }
    }

    public void update(Integer id, ApplicantDTO applicant){
        Applicant existingApplicant = applicantRepository
                .findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Requerente não encontrado."));

        existingApplicant.setName(applicant.getName());
        existingApplicant.setPhone(applicant.getPhone());
        applicantRepository.save(existingApplicant);
    }
}
