package com.example.fhirServerFinal.controllers;

import com.example.fhirServerFinal.databaseHandler.DatabaseHandler;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Practitioner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PractitionerDao {

    private Practitioner fillPractitioner(ResultSet rs){
        Practitioner practitioner = new Practitioner();
        try{
            practitioner.setId("Practitioner/" + rs.getString("practitioner_id"));
            practitioner.addName().setFamily(rs.getString("practitioner_family")).addGiven(rs.getString("practitioner_given"));
            switch (rs.getString("practitioner_gender")) {
                case "male":
                    practitioner.setGender(Enumerations.AdministrativeGender.MALE);
                    break;
                case "female":
                    practitioner.setGender(Enumerations.AdministrativeGender.FEMALE);
                    break;
                default:
                    practitioner.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }
            practitioner.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(rs.getString("practitioner_phone"));
        }catch(SQLException e){
            e.printStackTrace();
        }
        return practitioner;
    }

    public Practitioner get(long id){
        String query = "SELECT * FROM practitioners WHERE practitioner_id = " + id;
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return fillPractitioner(rs);
    }

    public List<Practitioner> getPractitioners(){
        List<Practitioner> practitioners = new ArrayList<>();
        String query = "SELECT * FROM practitioners";
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            while(rs.next()){
                practitioners.add(fillPractitioner(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return practitioners;
    }

    public List<Practitioner> searchByFamily(String familyName){
        List<Practitioner> practitioners = new ArrayList<>();
        String query = String.format("SELECT * FROM practitioners WHERE practitioner_family = '%s'", familyName);
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            while(rs.next()){
                practitioners.add(fillPractitioner(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return practitioners;
    }

    public void create(Practitioner practitioner){
        String family = practitioner.getName().get(0).getFamily();
        String given = practitioner.getName().get(0).getGivenAsSingleString();
        String gender = practitioner.getGender().toString().toLowerCase(Locale.ROOT);
        String phone = practitioner.getTelecom().get(0).getValue();
        String update = String.format("INSERT INTO practitioners(practitioner_family, practitioner_given, "
        + "practitioner_gender, practitioner_phone) VALUES ('%s', '%s', '%s', '%s')", family, given, gender, phone);
        DatabaseHandler.update(update);
    }

    public void delete(long id){
        String update = "DELETE FROM practitioners WHERE practitioner_id = " + id;
        DatabaseHandler.update(update);
    }

    public void update(Practitioner practitioner){
        String family = practitioner.getName().get(0).getFamily();
        String given = practitioner.getName().get(0).getGivenAsSingleString();
        String gender = practitioner.getGender().toString().toLowerCase(Locale.ROOT);
        String phone = practitioner.getTelecom().get(0).getValue();
        String update = String.format("UPDATE practitioners SET practitioner_family = '%s', practitioner_given = '%s', " +
                "practitioner_gender = '%s', practitioner_phone = '%s' " +
                "WHERE practitioner_id = '%d'", family, given, gender, phone, practitioner.getIdElement().getIdPartAsLong());
        DatabaseHandler.update(update);
    }

    public Practitioner getLatest(){
        String query = "SELECT * FROM practitioner ORDER BY practitioner_id DESC LIMIT 1";
        ResultSet rs = DatabaseHandler.createQuery(query);
        try{
            rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return fillPractitioner(rs);
    }

}
