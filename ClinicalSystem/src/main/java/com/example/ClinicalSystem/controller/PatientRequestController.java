package com.example.ClinicalSystem.controller;

import com.example.ClinicalSystem.DTO.PatientRequestDTO;
import com.example.ClinicalSystem.model.Patient;
import com.example.ClinicalSystem.model.PatientRequest;
import com.example.ClinicalSystem.service.PatientRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "api/requests")
public class PatientRequestController {

    @Autowired
    private PatientRequestService patientRequestService;


    @RequestMapping(method = RequestMethod.GET, value = "/allrequests")
    @PreAuthorize("hasAuthority('CLINICALCENTREADMIN')")
    public ResponseEntity<List<PatientRequestDTO>> getAllRequests() {

        List<PatientRequestDTO> requests = patientRequestService.findAll();

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/confirmrequest")
    @PreAuthorize("hasAuthority('CLINICALCENTREADMIN')")
    public ResponseEntity<?> confirmRequest(@RequestBody PatientRequestDTO dto) {

        PatientRequestDTO requestDTO = patientRequestService.findByEmail(dto.getEmail());
        boolean isAdded = patientRequestService.confirmUser(requestDTO);

        if(isAdded) {
            return new ResponseEntity<>(requestDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(requestDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/declinerequest/{request_email}")
    @PreAuthorize("hasAuthority('CLINICALCENTREADMIN')")
    public ResponseEntity<?> declineRequest(@PathVariable String request_email, @RequestBody String emailExplanation) {

        PatientRequestDTO requestDTO = patientRequestService.findByEmail(request_email);
        boolean isRemoved = patientRequestService.declineUser(requestDTO, emailExplanation);

        if(isRemoved) {
            return new ResponseEntity<>(requestDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(requestDTO, HttpStatus.BAD_REQUEST);
        }
    }


}
