/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.Contact;
import com.github.moscaville.contactsdb.dto.ContactRecords;
import com.github.moscaville.contactsdb.dto.ContactWrapper;
import com.github.moscaville.contactsdb.manager.AirtableAuthorizationInterceptor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author moscac
 */
public class ContactController {

    private final boolean OFFLINE_TEST = true;
    private final RestTemplate restTemplate;
    private final List<ClientHttpRequestInterceptor> interceptors;
    private final String AIRTABLE_ENDPOINT_URL; 
    private final int TEST_CONTAINER_SIZE = 10000;

    public ContactController() {

        restTemplate = new RestTemplate();
        interceptors = new ArrayList<>();
        interceptors.add(new AirtableAuthorizationInterceptor());
        restTemplate.setInterceptors(interceptors);
        String envValue = System.getenv("AIRTABLE_ENDPOINT_URL");
        AIRTABLE_ENDPOINT_URL = envValue + (envValue != null && envValue.endsWith("/") ? "" : "/");
    }

    public List<Contact> loadItems(int count, int itemsLoaded) {

        List<Contact> result = new ArrayList<>();
        if (!OFFLINE_TEST) {
            StringBuilder uri = new StringBuilder();
            uri.append(AIRTABLE_ENDPOINT_URL).append("Contact?limit=").append(count);
//            if (definition != null && definition.getSortPropertyIds() != null && definition.getSortPropertyIds().length > 0) {
//                String sortField = Utility.toHumanName((String) definition.getSortPropertyIds()[0]);
//                uri.append("&sortField=").append(sortField);
//            }
            ContactRecords contactRecords = restTemplate.getForObject(uri.toString(), ContactRecords.class);
            contactRecords.getRecords().stream().forEach((contactWrapper) -> {
                result.add(contactWrapper.getContact());
            });
        } else {
            //result = new ContactRecords();
            if (itemsLoaded < TEST_CONTAINER_SIZE) {
                for (int i = 0; i < count; i++) {
                    ContactWrapper contactWrapper = new ContactWrapper();
                    Contact contact = getDummyContact(i + itemsLoaded);
                    contactWrapper.setId(contact.getId());
                    contactWrapper.setContact(contact);
                    result.add(contact);
                }
            }
        }
        return result;
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
