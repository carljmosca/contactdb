/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author moscac
 */

public class LookupBase extends AtBaseRecord {
  
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Contact")
    private String[] contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(String[] contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
