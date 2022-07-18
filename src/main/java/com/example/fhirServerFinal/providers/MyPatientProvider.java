package com.example.fhirServerFinal.providers;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.fhirServerFinal.controllers.PatientDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableJpaRepositories
@RequestMapping("/Patient")
public class MyPatientProvider implements IResourceProvider {
    private PatientDao patientDao = new PatientDao();
//    private FhirContext ctx;
//    private FhirValidator validator;
//    private IValidatorModule module;
//    private PractitionerDao practitionerDao;

//    public MyPatientProvider(){
////        this.ctx = ctx;
////        this.validator = ctx.newValidator();
////        this.module = new FhirInstanceValidator(ctx);
////        this.practitionerDao = new PractitionerDao();
////        this.validator.registerValidatorModule(module);
//    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }

    @Search
    public List<Patient> list(){
        return patientDao.getPatients();
    }

    @Read()
    public Patient getResourceById(@IdParam IdType theId){
        return patientDao.get(theId.getIdPartAsLong());
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient patient){
//        ValidationResult result = validator.validateWithResult(patient);
//        System.out.println("Sprawdzanko wprowadzonych danych");
//        for (SingleValidationMessage next : result.getMessages()) {
//            System.out.println(next.getLocationString() + " " + next.getMessage());
//        }
//        System.out.println("Koniec sprawdzanka wprowadzonych danych");
        patientDao.create(patient);
        return new MethodOutcome();
    }

    @Delete
    public MethodOutcome deletePatient(@IdParam IdType theId){
        patientDao.delete(theId.getIdPartAsLong());
        return new MethodOutcome();
    }

    @Update
    public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient){
        patientDao.update(thePatient);
        return  new MethodOutcome();
    }

    @Search
    public List<Patient> getPatient(@RequiredParam(name = Patient.SP_FAMILY) StringParam theFamilyName) {
        String familyName = theFamilyName.getValue();
        return patientDao.searchByFamily(familyName);
    }

    @Operation(name="$newest_patient", idempotent=true)
    public Patient getNewestPatient(){
        return patientDao.getLatest();
    }

}
