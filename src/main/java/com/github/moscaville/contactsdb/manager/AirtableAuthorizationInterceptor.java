/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.manager;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class AirtableAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    private final String AIRTABLE_API_KEY;
    
    public AirtableAuthorizationInterceptor() {
        AIRTABLE_API_KEY = System.getenv("AIRTABLE_API_KEY");
    }
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("Authorization", "Bearer " + AIRTABLE_API_KEY);

        return execution.execute(request, body);
    }

}
