/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author moscac
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactRecord extends AtBaseRecord {

    @JsonProperty("Company Name")
    private String companyName;
    @JsonProperty("First Name")
    private String firstName;
    @JsonProperty("Last Name")
    private String lastName;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("City")
    private String city;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Zip")
    private String zip;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Work Phone")
    private String workPhone;
    @JsonProperty("Cell Phone")
    private String cellPhone;
    @JsonProperty("Category")
    private String[] category;
    @JsonProperty("Account")
    private String[] account;
}
