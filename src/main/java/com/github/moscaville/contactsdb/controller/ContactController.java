/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.ContactCollection;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.dto.RecordCollection;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class ContactController extends BaseController<ContactRecord, String> {

    @Override
    protected String getAirTableName() {
        return "Contact";
    }

    @Override
    protected RecordCollection getRecordCollection() {
        return new ContactCollection();
    }    
    
}
