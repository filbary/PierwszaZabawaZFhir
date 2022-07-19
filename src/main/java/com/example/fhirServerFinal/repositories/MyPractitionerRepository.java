package com.example.fhirServerFinal.repositories;

import com.example.fhirServerFinal.models.MyPractitioner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPractitionerRepository extends JpaRepository<MyPractitioner, Long> {
}
