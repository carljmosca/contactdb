/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.controller;

import com.github.moscaville.contactsdb.dto.RecordCollection;
import com.github.moscaville.contactsdb.dto.UserCollection;
import com.github.moscaville.contactsdb.dto.UserRecord;
import org.springframework.stereotype.Component;

/**
 *
 * @author moscac
 */
@Component
public class UserController extends BaseController<UserRecord, String> {

    @Override
    protected String getAirTableName() {
        return "User";
    }

    @Override
    protected RecordCollection getRecordCollection() {
        return new UserCollection();
    }
}
