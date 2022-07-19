package com.example.fhirServerFinal.repositories;

import com.example.fhirServerFinal.models.MyPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPatientRepository extends JpaRepository<MyPatient, Long> {

}
