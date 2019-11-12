package com.example.ClinicalSystem.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Clinic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "adress", nullable = false)
	private String adress;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "freeAppointment")
	private String freeAppointment;
	
	@Column(name = "price", nullable = false)
	private String price;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Doctor> doctors = new HashSet<Doctor>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Nurse> nurses = new HashSet<Nurse>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Patient> patients = new HashSet<Patient>();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private BusinessReport report;
	
	public Clinic() {
		
	}
	
	public Clinic(String id, String name, String adress, String description, String freeAppointment, String price) {
		super();
		this.id = id;
		this.name = name;
		this.adress = adress;
		this.description = description;
		this.freeAppointment = freeAppointment;
		this.price = price;
	}

	public Clinic(String id, String name, String adress, String description, String freeAppointment, String price,
			Set<Doctor> doctors, Set<Nurse> nurses, Set<Patient> patients) {
		super();
		this.id = id;
		this.name = name;
		this.adress = adress;
		this.description = description;
		this.freeAppointment = freeAppointment;
		this.price = price;
		this.doctors = doctors;
		this.nurses = nurses;
		this.patients = patients;
	}

	public BusinessReport getReport() {
		return report;
	}

	public void setReport(BusinessReport report) {
		this.report = report;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFreeAppointment() {
		return freeAppointment;
	}

	public void setFreeAppointment(String freeAppointment) {
		this.freeAppointment = freeAppointment;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Set<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}

	public Set<Nurse> getNurses() {
		return nurses;
	}

	public void setNurses(Set<Nurse> nurses) {
		this.nurses = nurses;
	}

	public Set<Patient> getPatients() {
		return patients;
	}

	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
	
	

}
