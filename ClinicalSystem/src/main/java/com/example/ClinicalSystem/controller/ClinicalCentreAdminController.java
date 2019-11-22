package com.example.ClinicalSystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ClinicalSystem.DTO.ClinicAdminDTO;
import com.example.ClinicalSystem.DTO.ClinicDTO;
import com.example.ClinicalSystem.DTO.ClinicalCentreAdminDTO;
import com.example.ClinicalSystem.model.Clinic;
import com.example.ClinicalSystem.model.ClinicAdmin;
import com.example.ClinicalSystem.model.ClinicalCentreAdmin;
import com.example.ClinicalSystem.model.User;
import com.example.ClinicalSystem.service.ClinicAdminService;
import com.example.ClinicalSystem.service.ClinicalCentreAdminService;
import com.example.ClinicalSystem.service.UserService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "api/clinicalCentreAdmins")
public class ClinicalCentreAdminController {



	@Autowired
	private ClinicalCentreAdminService ccaService;

	@GetMapping(value = "/all")
	public ResponseEntity<List<ClinicalCentreAdminDTO>> getAllccAdmins(){
		List<ClinicalCentreAdminDTO> ccas = ccaService.findAll();

		return new ResponseEntity<>(ccas, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/addccAdmin")
	public ResponseEntity<ClinicalCentreAdminDTO> addccAdmin(@RequestBody ClinicalCentreAdminDTO ccAdminDTO) {
		ccaService.save(ccAdminDTO);
		return new ResponseEntity<>(ccAdminDTO,HttpStatus.CREATED);

	}

}
