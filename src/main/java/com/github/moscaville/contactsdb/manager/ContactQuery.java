/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.manager;

import com.github.moscaville.contactsdb.dto.ContactRecords;
import com.github.moscaville.contactsdb.dto.Contact;
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

    public ContactQuery(QueryDefinition definition) {

        if (definition != null) {
            batchSize = definition.getBatchSize() >= LIMIT_MINIMUM && definition.getBatchSize() <= LIMIT_MAXIMUM ? definition.getBatchSize() : LIMIT_DEFAULT;
        }

        restTemplate = new RestTemplate();
        interceptors = new ArrayList<>();
        interceptors.add(new AirtableAuthorizationInterceptor());
        restTemplate.setInterceptors(interceptors);
        String envValue = System.getenv("AIRTABLE_ENDPOINT_URL");
        AIRTABLE_ENDPOINT_URL = envValue + (envValue != null && envValue.endsWith("/") ? "" : "/");
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public List<Item> loadItems(int startIndex, int count) {

        ContactRecords records = restTemplate.getForObject(AIRTABLE_ENDPOINT_URL + "Contact?limit=" + batchSize,
                ContactRecords.class);

        List<Item> items = new ArrayList<>();
        records.getRecords().stream().forEach((contactWrapper) -> {
            BeanItem<Contact> beanItem = new BeanItem(contactWrapper.getContact());
            beanItem.getBean().setId(contactWrapper.getId());
            items.add(beanItem);
        });

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

}
