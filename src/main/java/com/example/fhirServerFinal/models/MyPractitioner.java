package com.example.fhirServerFinal.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MyPractitioner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long practitioner_id;
    private String practitioner_family;
    private String practitioner_given;
    private String practitioner_gender;
    private String practitioner_phone;

    public MyPractitioner(){

    }

    public Long getPractitioner_id() {
        return practitioner_id;
    }

    public void setPractitioner_id(Long practitioner_id) {
        this.practitioner_id = practitioner_id;
    }

    public String getPractitioner_family() {
        return practitioner_family;
    }

    public void setPractitioner_family(String practitioner_family) {
        this.practitioner_family = practitioner_family;
    }

    public String getPractitioner_given() {
        return practitioner_given;
    }

    public void setPractitioner_given(String practitioner_given) {
        this.practitioner_given = practitioner_given;
    }

    public String getPractitioner_gender() {
        return practitioner_gender;
    }

    public void setPractitioner_gender(String practitioner_gender) {
        this.practitioner_gender = practitioner_gender;
    }

    public String getPractitioner_phone() {
        return practitioner_phone;
    }

    public void setPractitioner_phone(String practitioner_phone) {
        this.practitioner_phone = practitioner_phone;
    }
}
