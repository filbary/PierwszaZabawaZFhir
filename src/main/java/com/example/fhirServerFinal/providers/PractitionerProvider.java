package com.example.fhirServerFinal.providers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.example.fhirServerFinal.controllers.PractitionerDao;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.List;

public class PractitionerProvider implements IResourceProvider {

    private FhirContext ctx;
    private FhirValidator validator;
    private IValidatorModule module;
    private PractitionerDao practitionerDao;

    public PractitionerProvider(){
        this.practitionerDao = new PractitionerDao();
        this.ctx = FhirContext.forR4();
        this.validator = ctx.newValidator();
        this.module = new FhirInstanceValidator(ctx);
        this.validator.registerValidatorModule(module);
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Practitioner.class;
    }

    @Search
    public List<Practitioner> list(){
        return practitionerDao.getPractitioners();
    }

    @Read()
    public Practitioner getResourceById(@IdParam IdType theId){
        return practitionerDao.get(theId.getIdPartAsLong());
    }

    @Create
    public MethodOutcome  createPractitioner(@ResourceParam Practitioner practitioner){
        ValidationResult result = validator.validateWithResult(practitioner);
        System.out.println("Sprawdzanko wprowadzonych danych");
        for (SingleValidationMessage next : result.getMessages()) {
            System.out.println(next.getLocationString() + " " + next.getMessage());
        }
        System.out.println("Koniec sprawdzanka wprowadzonych danych");
        practitionerDao.create(practitioner);
        return new MethodOutcome();
    }

    @Delete
    public MethodOutcome deletePractitioner(@IdParam IdType theId){
        practitionerDao.delete(theId.getIdPartAsLong());
        return new MethodOutcome();
    }

    @Update
    public MethodOutcome updatePractitioner(@IdParam IdType theId, @ResourceParam Practitioner practitioner){
        practitionerDao.update(practitioner);
        return  new MethodOutcome();
    }

    @Search
    public List<Practitioner> getPractitioners(@RequiredParam(name = Practitioner.SP_FAMILY) StringParam theFamilyName) {
        String familyName = theFamilyName.getValue();
        return practitionerDao.searchByFamily(familyName);
    }

    @Operation(name="$newest_practitioner", idempotent=true)
    public Practitioner getNewestPractitioner(){
        return practitionerDao.getLatest();
    }

//    public static void main(String[] args) {
//        Practitioner practitioner = new Practitioner();
//        practitioner.setId("Practitioner/" + 1);
//        practitioner.addName().setFamily("JAKis").addGiven("name");
//        practitioner.setGender(Enumerations.AdministrativeGender.MALE);
//        practitioner.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("123456789");
//    }

}
