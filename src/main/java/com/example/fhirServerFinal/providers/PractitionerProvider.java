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
import com.example.fhirServerFinal.models.MyPatient;
import com.example.fhirServerFinal.models.MyPractitioner;
import com.example.fhirServerFinal.repositories.MyPatientRepository;
import com.example.fhirServerFinal.repositories.MyPractitionerRepository;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PractitionerProvider implements IResourceProvider {
    private final FhirContext ctx;
    private final FhirValidator validator;
    private final IValidatorModule module;
    private final MyPractitionerRepository myPractitionerRepository;

    public PractitionerProvider(MyPractitionerRepository myPractitionerRepository){
        this.myPractitionerRepository = myPractitionerRepository;
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
        List<Practitioner> practitioners = new ArrayList<>();
        for(MyPractitioner myPractitioner:myPractitionerRepository.findAll()){
            practitioners.add(fillPractitioner(Optional.ofNullable(myPractitioner)));
        }
        return practitioners;
    }

    @Read()
    public Practitioner getResourceById(@IdParam IdType theId){
        return fillPractitioner(myPractitionerRepository.findById(theId.getIdPartAsLong()));
    }

    @Create
    public MethodOutcome  createPractitioner(@ResourceParam Practitioner practitioner){
        ValidationResult result = validator.validateWithResult(practitioner);
        System.out.println("Sprawdzanko wprowadzonych danych");
        for (SingleValidationMessage next : result.getMessages()) {
            System.out.println(next.getLocationString() + " " + next.getMessage());
        }
        System.out.println("Koniec sprawdzanka wprowadzonych danych");
        MyPractitioner myPractitioner = new MyPractitioner();
        myPractitioner.setPractitioner_id(practitioner.getIdElement().getIdPartAsLong());
        myPractitioner.setPractitioner_family(practitioner.getName().get(0).getFamily());
        myPractitioner.setPractitioner_given(practitioner.getName().get(0).getGivenAsSingleString());
        myPractitioner.setPractitioner_gender(practitioner.getGender().toString().toLowerCase());
        myPractitioner.setPractitioner_phone(practitioner.getTelecom().get(0).getValue());
        myPractitionerRepository.saveAndFlush(myPractitioner);
        return new MethodOutcome();
    }

    @Delete
    public MethodOutcome deletePractitioner(@IdParam IdType theId){
        myPractitionerRepository.deleteById(theId.getIdPartAsLong());
        return new MethodOutcome();
    }

    @Update
    public MethodOutcome updatePractitioner(@IdParam IdType theId, @ResourceParam Practitioner practitioner){
        Optional<MyPractitioner> myPractitioner = myPractitionerRepository.findById(theId.getIdPartAsLong());
        myPractitioner.get().setPractitioner_family(practitioner.getName().get(0).getFamily());
        myPractitioner.get().setPractitioner_given(practitioner.getName().get(0).getGivenAsSingleString());
        myPractitioner.get().setPractitioner_gender(practitioner.getGender().toString().toLowerCase());
        myPractitioner.get().setPractitioner_phone(practitioner.getTelecom().get(0).getValue());
        myPractitionerRepository.save(myPractitioner.get());
        return  new MethodOutcome();
    }

    @Search
    public List<Practitioner> getPractitioners(@RequiredParam(name = Practitioner.SP_FAMILY) StringParam theFamilyName) {
        String familyName = theFamilyName.getValue();
        List<Practitioner> practitioners = new ArrayList<>();
        for(MyPractitioner myPractitioner:myPractitionerRepository.findAll()){
            if (myPractitioner.getPractitioner_family().equals(familyName)) {
                practitioners.add(fillPractitioner(Optional.of(myPractitioner)));
            }
        }
        return practitioners;
    }

    @Operation(name="$newest_practitioner", idempotent=true)
    public Practitioner getNewestPractitioner(){
        return fillPractitioner(Optional.ofNullable(myPractitionerRepository.findAll().get(myPractitionerRepository.findAll().size() - 1)));
    }

//    public static void main(String[] args) {
//        Practitioner practitioner = new Practitioner();
//        practitioner.setId("Practitioner/" + 1);
//        practitioner.addName().setFamily("JAKis").addGiven("name");
//        practitioner.setGender(Enumerations.AdministrativeGender.MALE);
//        practitioner.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("123456789");
//    }

    private Practitioner fillPractitioner(Optional<MyPractitioner> myPractitioner){
        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner/" + myPractitioner.get().getPractitioner_id());
        practitioner.addName().setFamily(myPractitioner.get().getPractitioner_family()).addGiven(myPractitioner.get().getPractitioner_given());
        switch (myPractitioner.get().getPractitioner_gender()) {
            case "male":
                practitioner.setGender(Enumerations.AdministrativeGender.MALE);
                break;
            case "female":
                practitioner.setGender(Enumerations.AdministrativeGender.FEMALE);
                break;
            default:
                practitioner.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }
        practitioner.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(myPractitioner.get().getPractitioner_phone());
        return practitioner;
    }

}
