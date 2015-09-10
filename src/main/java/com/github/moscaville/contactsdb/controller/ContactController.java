/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.Contact;
import com.github.moscaville.contactsdb.dto.ContactPojo;
import com.github.moscaville.contactsdb.dto.ContactRecords;
import com.github.moscaville.contactsdb.dto.ContactResponse;
import com.github.moscaville.contactsdb.dto.ContactPojoWrapper;
import com.github.moscaville.contactsdb.dto.ContactWrapper;
import com.github.moscaville.contactsdb.util.AirtableAuthorizationInterceptor;
import com.github.moscaville.contactsdb.util.Utility;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author moscac
 */
@Component
public class ContactController {

    private final boolean OFFLINE_TEST = false;
    private final RestTemplate restTemplate;
    private final List<ClientHttpRequestInterceptor> interceptors;
    private final String AIRTABLE_ENDPOINT_URL;
    private final int TEST_CONTAINER_SIZE = 10000;
    private String sortColumn;

    public ContactController() {

        restTemplate = new RestTemplate();
        interceptors = new ArrayList<>();
        interceptors.add(new AirtableAuthorizationInterceptor());
        restTemplate.setInterceptors(interceptors);
        String envValue = System.getenv("AIRTABLE_ENDPOINT_URL");
        AIRTABLE_ENDPOINT_URL = envValue + (envValue != null && envValue.endsWith("/") ? "" : "/");
    }

    public String saveItem(Contact contact) {

        if (OFFLINE_TEST) {
            return "";
        }
        String result = null;
        ContactPojoWrapper contactPojoWrapper = new ContactPojoWrapper();
        ContactPojo contactPojo = new ContactPojo();
        BeanUtils.copyProperties(contactPojo, contact);
        contactPojoWrapper.setContactPojo(contactPojo);
        StringBuilder sUri = new StringBuilder();
        sUri.append(AIRTABLE_ENDPOINT_URL).append("Contact");
        URI uri;
        if (contact.getId() == null) {
            try {
                uri = new URI(sUri.toString());
                ResponseEntity<ContactResponse> response = restTemplate.postForEntity(uri, contactPojoWrapper, ContactResponse.class);
                if (response != null && response.getBody() != null) {
                    result = response.getBody().getId();
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(ContactController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sUri.append("/").append(contact.getId());                        
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("HeaderName", "value");
            headers.add("Content-Type", "application/json");
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpEntity<ContactPojoWrapper> request = new HttpEntity<>(contactPojoWrapper, headers);
            try {
                uri = new URI(sUri.toString());
                restTemplate.put(uri, request);
                result = "";
            } catch (RestClientException e) {
                if (e instanceof HttpStatusCodeException) {
                    String errorResponse = ((HttpStatusCodeException) e).getResponseBodyAsString();
                    System.out.println(errorResponse);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(ContactController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<Contact> loadItems(int count, int itemsLoaded) {

        List<Contact> result = new ArrayList<>();
        if (!OFFLINE_TEST) {
            StringBuilder uri = new StringBuilder();
            uri.append(AIRTABLE_ENDPOINT_URL).append("Contact?limit=").append(count);
            if (sortColumn != null) {
                uri.append("&sortField=").append(Utility.toHumanName(sortColumn));
            }
            ContactRecords contactRecords = restTemplate.getForObject(uri.toString(), ContactRecords.class);
            contactRecords.getRecords().stream().forEach((contactWrapper) -> {
                contactWrapper.getContact().setId(contactWrapper.getId());
                result.add(contactWrapper.getContact());
            });
        } else if (itemsLoaded < TEST_CONTAINER_SIZE) {
            for (int i = 0; i < count; i++) {
                ContactWrapper contactWrapper = new ContactWrapper();
                Contact contact = getDummyContact(i + itemsLoaded);
                contactWrapper.setId(contact.getId());
                contactWrapper.setContact(contact);
                result.add(contact);
            }
        }
        return result;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    private Contact getDummyContact(int i) {

        Contact contact = new Contact();
        contact.setId("Id" + i);
        contact.setCompanyName("ABC Company");
        contact.setFirstName("John");
        contact.setLastName("Smith");
        contact.setAddress("123 Main Street");
        contact.setCity("Anytown");
        contact.setState("VA");
        contact.setZip("12345");
        contact.setEmail("john.smith@test.com");
        contact.setCellPhone("804.123.4567");
        contact.setWorkPhone("804.111.2222");
        return contact;
    }

}
