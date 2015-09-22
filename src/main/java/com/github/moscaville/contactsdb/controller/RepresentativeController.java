/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.RecordCollection;
import com.github.moscaville.contactsdb.dto.RepresentativeCollection;
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class RepresentativeController extends BaseController<RepresentativeRecord, String> {

    @Override
    protected String getAirTableName() {
        return "Representative";
    }

    @Override
    protected RecordCollection getRecordCollection() {
        return new RepresentativeCollection();
    }
    
}
