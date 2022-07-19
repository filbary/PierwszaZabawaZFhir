package com.example.fhirServerFinal;

import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.fhirServerFinal.interceptors.SimpleServerLoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class FhirServerFinalApplication {
	public static void main(String[] args) {
		SpringApplication.run(FhirServerFinalApplication.class, args);
	}
	@Bean
	public ServletRegistrationBean ServletRegistrationBean(SimpleServerLoggingInterceptor simpleServerLoggingInterceptor, List<IResourceProvider> resourceProviders){
		ServletRegistrationBean registration = new ServletRegistrationBean(new RestfulFhirServer(simpleServerLoggingInterceptor, resourceProviders), "/*");
		registration.setName("FhirServlet");
		return registration;
	}

}
