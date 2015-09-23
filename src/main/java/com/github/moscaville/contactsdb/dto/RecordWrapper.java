/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author moscac
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordWrapper<T> {
    
    @JsonIgnore
    private String id;
    private T fields;
    
    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }    

    public T getFields() {
        return fields;
    }

    public void setFields(T fields) {
        this.fields = fields;
    }
          
}
