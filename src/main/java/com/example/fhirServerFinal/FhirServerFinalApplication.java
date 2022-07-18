package com.example.fhirServerFinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FhirServerFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FhirServerFinalApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean ServletRegistrationBean(){
		ServletRegistrationBean registration = new ServletRegistrationBean(new RestfulFhirServer(), "/*");
		registration.setName("FhirServlet");
		return registration;
	}

}
