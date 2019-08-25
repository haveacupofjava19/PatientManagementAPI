package com.saket.patientmanagement.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saket.patientmanagement.entities.ClinicalData;
import com.saket.patientmanagement.entities.Patient;
import com.saket.patientmanagement.repositories.PatientRepository;
import com.saket.patientmanagement.utilities.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

	@Autowired
	PatientRepository patientRepo;
	
	Map<String, String> filters = new HashMap<String, String>();

	@Autowired
	public PatientController(PatientRepository patientRepo) {
		this.patientRepo = patientRepo;
	}

	@RequestMapping(value = "/patients", method = RequestMethod.GET)
	public List<Patient> getPatients() {
		return patientRepo.findAll();
	}

	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public Patient getPatient(@PathVariable int id) {
		return patientRepo.findById(id).get();
	}

	@RequestMapping(value = "/patients", method = RequestMethod.POST)
	public Patient savePatient(@RequestBody Patient patient) {
		return patientRepo.save(patient);
	}

	@RequestMapping(value = "/patients/analyze/{id}", method = RequestMethod.GET)
	public Patient analyze(@PathVariable int id) {
		Patient patient = patientRepo.findById(id).get();
		List<ClinicalData> clinicalDataList = patient.getClinicalData();
		ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalDataList);
		for (ClinicalData data : duplicateClinicalData) {
			if(filters.containsKey(data.getComponentName())) {
				clinicalDataList.remove(data);
				continue;
			}else {
				filters.put(data.getComponentName(), null);
			}
			BMICalculator.calculateBMI(clinicalDataList, data);
		}
		filters.clear();
		return patient;
	}

}
