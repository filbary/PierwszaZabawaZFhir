package com.example.fhirServerFinal;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.fhirServerFinal.interceptors.SimpleServerLoggingInterceptor;
import com.example.fhirServerFinal.providers.MyPatientProvider;
import com.example.fhirServerFinal.providers.PractitionerProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/*"}, displayName = "FHIR Server")
public class RestfulFhirServer extends RestfulServer {
    private final SimpleServerLoggingInterceptor simpleServerLoggingInterceptor;
    private final List<IResourceProvider> resourceProviders;

    public RestfulFhirServer(SimpleServerLoggingInterceptor simpleServerLoggingInterceptor, List<IResourceProvider> resourceProviders) {
        this.simpleServerLoggingInterceptor = simpleServerLoggingInterceptor;
        this.resourceProviders = resourceProviders;
    }
    @Override
    protected void initialize() throws ServletException {
        registerInterceptor(simpleServerLoggingInterceptor);
        setResourceProviders(resourceProviders);

    }
}
