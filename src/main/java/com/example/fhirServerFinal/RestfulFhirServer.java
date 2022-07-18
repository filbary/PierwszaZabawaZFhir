package com.example.fhirServerFinal;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.fhirServerFinal.interceptors.SimpleServerLoggingInterceptor;
import com.example.fhirServerFinal.providers.MyPatientProvider;
import com.example.fhirServerFinal.providers.PractitionerProvider;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/*"}, displayName = "FHIR Server")
@EnableJpaRepositories
public class RestfulFhirServer extends RestfulServer {
    private FhirContext ctx;

    @Override
    protected void initialize() throws ServletException {
        /*
         * The servlet defines any number of resource providers, and
         * configures itself to use them by calling
         * setResourceProviders()
         */
        ctx = FhirContext.forR4();
        registerInterceptor(new SimpleServerLoggingInterceptor());
        List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
        resourceProviders.add(new MyPatientProvider());
        resourceProviders.add(new PractitionerProvider());
        setResourceProviders(resourceProviders);

    }
}
