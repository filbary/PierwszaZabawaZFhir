package com.example.fhirServerFinal.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "mypatients")
public class MyPatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patient_id;
    private String patient_family;
    private String patient_given;
    private String patient_gender;

    public MyPatient(){

    }

    public Long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Long patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_family() {
        return patient_family;
    }

    public void setPatient_family(String patient_family) {
        this.patient_family = patient_family;
    }

    public String getPatient_given() {
        return patient_given;
    }

    public void setPatient_given(String patient_given) {
        this.patient_given = patient_given;
    }

    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
    }
}
