FROM openjdk:11
LABEL maintainer="fhirServerFinal"
ADD target/fhirServerFinal-0.0.1-SNAPSHOT.jar fhirServerFinal.jar
ENTRYPOINT ["java", "-jar", "fhirServerFinal.jar"]