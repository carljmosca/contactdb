/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.github.moscaville.contactsdb.dto.RecordCollection;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class CategoryController extends BaseController<CategoryRecord, String> {

    @Override
    protected String getAirTableName() {
        return "";
    }

    @Override
    protected RecordCollection getRecordCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
