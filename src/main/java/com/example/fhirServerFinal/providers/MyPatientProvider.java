package com.example.fhirServerFinal.providers;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.fhirServerFinal.models.MyPatient;
import com.example.fhirServerFinal.repositories.MyPatientRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MyPatientProvider implements IResourceProvider {

    private final MyPatientRepository myPatientRepository;
    public MyPatientProvider(MyPatientRepository myPatientRepository){
        this.myPatientRepository = myPatientRepository;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Search
    public List<Patient> list(){
        List<Patient> patients = new ArrayList<>();
        for(MyPatient myPatient:myPatientRepository.findAll()){
            patients.add(fillPatient(Optional.ofNullable(myPatient)));
        }
        return patients;
    }

    @Read()
    public Patient getResourceById(@IdParam IdType theId){
        return fillPatient(myPatientRepository.findById(theId.getIdPartAsLong()));
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient patient){
//        ValidationResult result = validator.validateWithResult(patient);
//        System.out.println("Sprawdzanko wprowadzonych danych");
//        for (SingleValidationMessage next : result.getMessages()) {
//            System.out.println(next.getLocationString() + " " + next.getMessage());
//        }
//        System.out.println("Koniec sprawdzanka wprowadzonych danych");
        MyPatient myPatient = new MyPatient();
        myPatient.setPatient_id(patient.getIdElement().getIdPartAsLong());
        myPatient.setPatient_family(patient.getName().get(0).getFamily());
        myPatient.setPatient_given(patient.getName().get(0).getGivenAsSingleString());
        myPatient.setPatient_gender(patient.getGender().toString().toLowerCase());
        myPatientRepository.saveAndFlush(myPatient);
        return new MethodOutcome();
    }

    @Delete
    public MethodOutcome deletePatient(@IdParam IdType theId){
        myPatientRepository.deleteById(theId.getIdPartAsLong());
        return new MethodOutcome();
    }

    @Update
    public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient patient){
        Optional<MyPatient> myPatient = myPatientRepository.findById(theId.getIdPartAsLong());
        myPatient.get().setPatient_family(patient.getName().get(0).getFamily());
        myPatient.get().setPatient_given(patient.getName().get(0).getGivenAsSingleString());
        myPatient.get().setPatient_gender(patient.getGender().toString().toLowerCase());
        myPatientRepository.save(myPatient.get());
        return new MethodOutcome();
    }

    @Search
    public List<Patient> getPatient(@RequiredParam(name = Patient.SP_FAMILY) StringParam theFamilyName) {
        String familyName = theFamilyName.getValue();
        List<Patient> patients = new ArrayList<>();
        for(MyPatient myPatient:myPatientRepository.findAll()){
            if (myPatient.getPatient_family().equals(familyName)) {
                patients.add(fillPatient(Optional.of(myPatient)));
            }
        }
        return patients;
    }

    @Operation(name="$newest_patient", idempotent=true)
    public Patient getNewestPatient(){
        return fillPatient(Optional.ofNullable(myPatientRepository.findAll().get(myPatientRepository.findAll().size() - 1)));
    }

    private Patient fillPatient(Optional<MyPatient> myPatient){
        Patient patient = new Patient();
        patient.setId("Patient/" + myPatient.get().getPatient_id());
        patient.addName().setFamily(myPatient.get().getPatient_family()).addGiven(myPatient.get().getPatient_given());
        switch (myPatient.get().getPatient_gender()) {
            case "male":
                patient.setGender(Enumerations.AdministrativeGender.MALE);
                break;
            case "female":
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
                break;
            default:
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }
        return patient;
    }

}
