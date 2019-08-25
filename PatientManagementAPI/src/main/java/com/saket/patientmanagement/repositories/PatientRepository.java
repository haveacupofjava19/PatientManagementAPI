package com.saket.patientmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saket.patientmanagement.entities.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
