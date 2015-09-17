/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author moscac
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {
    private String id;    
}
