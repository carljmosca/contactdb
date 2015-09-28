/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.moscaville.contactsdb.dto.AtBaseRecord;
import com.github.moscaville.contactsdb.util.AirtableAuthorizationInterceptor;
import com.github.moscaville.contactsdb.util.Utility;
import com.github.moscaville.contactsdb.dto.RecordCollection;
import com.github.moscaville.contactsdb.dto.RecordWrapper;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
 * @param <T>
 * @param <ID>
 */
@Component
public abstract class BaseController<T extends AtBaseRecord, ID extends Serializable> {

    private final boolean OFFLINE_TEST = false;
    private final RestTemplate restTemplate;
    private final List<ClientHttpRequestInterceptor> interceptors;
    private final String AIRTABLE_ENDPOINT_URL;
    //private final int TEST_CONTAINER_SIZE = 10000;
    private String sortColumn;

    public BaseController() {
        restTemplate = new RestTemplate();
        interceptors = new ArrayList<>();
        String envValue = System.getenv("AIRTABLE_ENDPOINT_URL");
        AIRTABLE_ENDPOINT_URL = envValue + (envValue != null && envValue.endsWith("/") ? "" : "/");
    }

    @PostConstruct
    private void init() {
        interceptors.add(new AirtableAuthorizationInterceptor());
        restTemplate.setInterceptors(interceptors);
    }

    protected abstract String getAirTableName();

    protected abstract RecordCollection getRecordCollection();

    public String saveItem(RecordWrapper<T> recordWrapper, ID id) {

        if (OFFLINE_TEST) {
            return "";
        }
        String result = null;

        //RecordWrapper<T> recordWrapper = new RecordWrapper<>();
        //BeanUtils.copyProperties(t, recordWrapper.getFields());
        StringBuilder sUri = new StringBuilder();
        sUri.append(AIRTABLE_ENDPOINT_URL).append(getAirTableName());
        URI uri;
        if (id == null) {
            try {
                uri = new URI(sUri.toString());
                ResponseEntity<BaseResponse> response = restTemplate.postForEntity(uri, recordWrapper, BaseResponse.class);
                if (response != null && response.getBody() != null) {
                    result = response.getBody().getId();
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sUri.append("/").append(id.toString());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("HeaderName", "value");
            headers.add("Content-Type", "application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            restTemplate.getMessageConverters().add(messageConverter);
            HttpEntity<RecordWrapper> request = new HttpEntity<>(recordWrapper, headers);
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
                Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public List<T> loadItems(int count, int itemsLoaded, T t) {

        List<T> result = new ArrayList<>();
        if (!OFFLINE_TEST) {
            StringBuilder uri = new StringBuilder();
            uri.append(AIRTABLE_ENDPOINT_URL).append(getAirTableName());
            if (count > 0) {
                uri.append("?limit=").append(count);
            }
            if (sortColumn != null) {
                uri.append("&sortField=").append(Utility.toHumanName(sortColumn));
            }
            RecordCollection recordCollection = restTemplate.getForObject(uri.toString(), getRecordCollection().getClass());
            for (Object o : recordCollection.getRecords()) {
                RecordWrapper recordWrapper = (RecordWrapper) o;
                Object r = BeanUtils.instantiate(t.getClass());
                //BeanUtils.copyProperties(recordWrapper, r);
                BeanUtils.copyProperties(recordWrapper.getFields(), r);
                ((AtBaseRecord) r).setId(recordWrapper.getId());
                result.add((T) r);
            }
        }
        return result;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

}
