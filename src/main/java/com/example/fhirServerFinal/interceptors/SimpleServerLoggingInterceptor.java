package com.example.fhirServerFinal.interceptors;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
public class SimpleServerLoggingInterceptor {
    private final Logger ourLog = LoggerFactory.getLogger(SimpleServerLoggingInterceptor.class);

    @Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
    public void logRequests(RequestDetails theRequest){
        ourLog.info("Request of type {} with request ID: {}", theRequest.getRequestType(), theRequest.getRequestId());
    }

    @Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
    public void logResponses(ResponseDetails responseDetails){
        ourLog.info("Response of resource {} with response code: {}", responseDetails.getResponseResource(),
                responseDetails.getResponseCode());
    }
}
