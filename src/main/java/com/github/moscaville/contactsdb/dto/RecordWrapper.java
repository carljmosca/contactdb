/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author moscac
 * @param <T>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordWrapper<T> {
    
    private String id;
    @JsonProperty("fields")
    private T fields;

    public RecordWrapper() {
    }    
        
}
