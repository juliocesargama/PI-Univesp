package com.univesp.PI1.controller;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/applicant")
public class ApplicantController {

    @Autowired
    ApplicantService applicantService;
    @GetMapping(value = "/all")
    public List<Applicant> getApplicants(){
        return applicantService.findAll();
    }

    @PostMapping(value = "/save")
    public void saveApplicant(@RequestBody Applicant applicant){
        applicantService.save(applicant);
    }
}
