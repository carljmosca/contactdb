/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor .
 */
package com.github.moscaville.contactsdb.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.moscaville.contactsdb.dto.ContactRecords;
import com.github.moscaville.contactsdb.dto.Contact;
import com.github.moscaville.contactsdb.dto.ContactWrapper;
import com.github.moscaville.contactsdb.util.Utility;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

/**
 *
 * @author moscac
 */
public class ContactQuery implements Query {

    private final RestTemplate restTemplate;
    private final List<ClientHttpRequestInterceptor> interceptors;
    private final String AIRTABLE_ENDPOINT_URL;
    private final int LIMIT_MINIMUM = 1;
    private final int LIMIT_MAXIMUM = 100;
    private final int LIMIT_DEFAULT = 20;
    private int batchSize = LIMIT_DEFAULT;
    private final QueryDefinition definition;
    private int size;
    private int itemsLoaded;
    private final boolean OFFLINE_TEST;
    private final int TEST_CONTAINER_SIZE = 10000;

    public ContactQuery(QueryDefinition definition) {

        OFFLINE_TEST = true;

        this.definition = definition;

        restTemplate = new RestTemplate();
        interceptors = new ArrayList<>();
        interceptors.add(new AirtableAuthorizationInterceptor());
        restTemplate.setInterceptors(interceptors);
        String envValue = System.getenv("AIRTABLE_ENDPOINT_URL");
        AIRTABLE_ENDPOINT_URL = envValue + (envValue != null && envValue.endsWith("/") ? "" : "/");
        //size = Integer.MAX_VALUE;
    }

    @Override
    public int size() {
        if (size == 0) {
            List<Item> result = loadItems(0, LIMIT_MAXIMUM);
        }
        return size;
    }

    @Override
    public List<Item> loadItems(int startIndex, int count) {

        batchSize = definition != null && definition.getBatchSize() >= LIMIT_MINIMUM && definition.getBatchSize() <= LIMIT_MAXIMUM ? definition.getBatchSize() : LIMIT_DEFAULT;
        ContactRecords records;

        if (!OFFLINE_TEST) {
            StringBuilder uri = new StringBuilder();
            uri.append(AIRTABLE_ENDPOINT_URL).append("Contact?limit=").append(batchSize);
            if (definition != null && definition.getSortPropertyIds() != null && definition.getSortPropertyIds().length > 0) {
                String sortField = Utility.toHumanName((String) definition.getSortPropertyIds()[0]);
                uri.append("&sortField=").append(sortField);
            }
            records = restTemplate.getForObject(uri.toString(), ContactRecords.class);
        } else {
            records = new ContactRecords();
            if (itemsLoaded < TEST_CONTAINER_SIZE) {
                for (int i = 0; i < count; i++) {
                    ContactWrapper contactWrapper = new ContactWrapper();
                    Contact contact = getDummyContact(i);
                    contactWrapper.setId(contact.getId());
                    contactWrapper.setContact(contact);
                    records.getRecords().add(contactWrapper);
                }
            }
        }
        List<Item> items = new ArrayList<>();
        records.getRecords().stream().forEach((contactWrapper) -> {
            BeanItem<Contact> beanItem = new BeanItem(contactWrapper.getContact());
            beanItem.getBean().setId(contactWrapper.getId());
            items.add(beanItem);
        });

        if (size > 0) {
            itemsLoaded += items.size();
        }
        size += items.size();
        // there may be one more record if we retrieved all
        if (items.size() == count) {
            size += 1;
        }
        size += itemsLoaded;
        return items;
    }

    @Override
    public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {

    }

    @Override
    public boolean deleteAllItems() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Item constructItem() {
        return new BeanItem<>(new Contact());
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
