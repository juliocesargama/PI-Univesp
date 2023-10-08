package com.univesp.PI1.controller;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.entity.DTOS.ApplicantDTO;
import com.univesp.PI1.service.ApplicantService;
import com.univesp.PI1.utils.handler.EndpointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/applicant")
public class ApplicantController {

    @Autowired
    ApplicantService applicantService;
    @GetMapping(value = "/all")
    public ResponseEntity<List<Applicant>> getApplicants(){
        return ResponseEntity.ok(applicantService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Applicant> getApplicantById(@PathVariable Integer id){
        return ResponseEntity.ok(applicantService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<EndpointResponse> saveApplicant(@RequestBody Applicant applicant){
        applicantService.save(applicant);

        return ResponseEntity.ok()
                .body(new EndpointResponse("Requerente salvo com sucesso!"));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<EndpointResponse> updateApplicant(@PathVariable Integer id,@RequestBody ApplicantDTO applicant){
        applicantService.update(id, applicant);

        return ResponseEntity.ok()
                .body(new EndpointResponse("Requerente atualizado com sucesso!"));
    }
}
