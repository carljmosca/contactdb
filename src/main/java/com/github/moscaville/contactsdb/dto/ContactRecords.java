/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author moscac
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactRecords {

    private List<ContactWrapper> records;
    private String offset;
    
    public ContactRecords() {
        records = new ArrayList<>();
    }
    
}
