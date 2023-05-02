package com.univesp.PI1.service;

import com.univesp.PI1.entity.Applicant;
import com.univesp.PI1.repository.ApplicantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApplicantServiceTest {

    @Mock
    ApplicantRepository repository;

    @InjectMocks
    ApplicantService service;

    List<Applicant> applicants = new ArrayList<>();
    Applicant applicant = new Applicant();
    @BeforeEach
    void setup(){
        applicant.setId(1);
        applicant.setName("Ana");
        applicant.setPhone("9999-9999");
        applicants.add(applicant);
    }

    @Test
    void FindAllApplicantsTest(){
        Mockito.when(repository.findAll()).thenReturn(applicants);
        assertEquals(applicants, service.findAll());
    }

    @Test
    void SaveApplicantTest(){
        applicant.setId(null);

        Mockito.when(repository.save(applicant)).thenReturn(applicant);
        assertEquals(applicant, service.save(applicant));
    }

    @Test
    void UpdateApplicantTest(){

        applicant.setPhone("11111-1111");

        Mockito.when(repository.save(applicant)).thenReturn(applicant);
        Mockito.when(repository.findById(applicant.getId())).thenReturn(Optional.ofNullable(applicant));
        assertEquals(applicant, service.save(applicant));
    }

    @Test
    void UpdateApplicantFailedTest(){

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.save(applicant));
        assertTrue(runtimeException.getMessage().contains("Applicant does not exists"));

    }
}
