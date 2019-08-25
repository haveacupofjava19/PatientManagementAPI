package com.saket.patientmanagement.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saket.patientmanagement.dto.ClinicalDataRequest;
import com.saket.patientmanagement.entities.ClinicalData;
import com.saket.patientmanagement.entities.Patient;
import com.saket.patientmanagement.repositories.ClinicalDataRepository;
import com.saket.patientmanagement.repositories.PatientRepository;
import com.saket.patientmanagement.utilities.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClinicalDataController {
	
	private ClinicalDataRepository clinicalDataRepo;
	
	private PatientRepository patientRepo;
	
	public ClinicalDataController(ClinicalDataRepository clinicalDataRepo, PatientRepository patientRepo) {
		this.clinicalDataRepo = clinicalDataRepo;
		this.patientRepo = patientRepo;
	}
	
	@RequestMapping(value = "/clinicals", method = RequestMethod.POST)
	public ClinicalData saveClinicalDate(@RequestBody ClinicalDataRequest clinicalDataRequest) {
		Patient patient = patientRepo.findById(clinicalDataRequest.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(clinicalDataRequest.getComponentName());
		clinicalData.setComponentValue(clinicalDataRequest.getComponentValue());
		clinicalData.setPatient(patient);
		return clinicalDataRepo.save(clinicalData);
	}
	
	@RequestMapping(value = "/clinicals/{patientId}/{componentName}", method = RequestMethod.GET)
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId,
			@PathVariable("componentName") String componentName){
		
		if(componentName.equals("bmi")) {
			componentName="hw";
		}
		
		List<ClinicalData> clinicalDataList = clinicalDataRepo.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId, componentName);
		ArrayList<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalDataList);
		for (ClinicalData data : duplicateClinicalData) {
			BMICalculator.calculateBMI(clinicalDataList, data);
		}
		return clinicalDataList;
	}

}
