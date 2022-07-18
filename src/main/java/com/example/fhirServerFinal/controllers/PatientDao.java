package com.example.fhirServerFinal.controllers;


import com.example.fhirServerFinal.databaseHandler.DatabaseHandler;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientDao {
    private Patient fillPatient(ResultSet rs){
        Patient patient = new Patient();
        try{
            patient.setId("Patient/" + rs.getString("patient_id"));
            patient.addName().setFamily(rs.getString("patient_family")).addGiven(rs.getString("patient_given"));
            switch (rs.getString("patient_gender")) {
                case "male":
                    patient.setGender(Enumerations.AdministrativeGender.MALE);
                    break;
                case "female":
                    patient.setGender(Enumerations.AdministrativeGender.FEMALE);
                    break;
                default:
                    patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return patient;
    }
    public Patient get(long id){
        String query = "SELECT * FROM mypatients WHERE patient_id = " + id;
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return fillPatient(rs);
    }

    public List<Patient> getPatients(){
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM mypatients";
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            while(rs.next()){
                patients.add(fillPatient(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return patients;
    }

    public List<Patient> searchByFamily(String familyName){
        List<Patient> patients = new ArrayList<>();
            String query = String.format("SELECT * FROM mypatients WHERE patient_family = '%s'", familyName);
            ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            while(rs.next()){
                patients.add(fillPatient(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return patients;
    }

    public void create(Patient patient){
        String family = patient.getName().get(0).getFamily();
        String given = patient.getName().get(0).getGivenAsSingleString();
        String gender = patient.getGender().toString().toLowerCase(Locale.ROOT);
        String update = String.format("INSERT INTO mypatients(patient_family, patient_given, patient_gender) " +
                "VALUES ('%s', '%s', '%s')", family, given, gender);
        DatabaseHandler.update(update);
    }

    public void delete(long id){
        String update = "DELETE FROM mypatients WHERE patient_id = " + id;
        DatabaseHandler.update(update);
    }

    public void update(Patient patient){
        String family = patient.getName().get(0).getFamily();
        String given = patient.getName().get(0).getGivenAsSingleString();
        String gender = patient.getGender().toString().toLowerCase(Locale.ROOT);
        String update = String.format("UPDATE mypatients SET patient_family = '%s', patient_given = '%s', patient_gender = '%s' " +
                "WHERE patient_id = '%d'", family, given, gender, patient.getIdElement().getIdPartAsLong());
        DatabaseHandler.update(update);
    }

    public Patient getLatest(){
        String query = "SELECT * FROM mypatients ORDER BY patient_id DESC LIMIT 1";
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return fillPatient(rs);
    }
}
