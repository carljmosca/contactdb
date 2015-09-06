/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 *
 * @author moscac
 */
public class ContactDetailView extends Panel {

    private final TextField firstName;
    private int contactId;
    
    public ContactDetailView() {
        firstName = new TextField();
        setContent(firstName);
        
        
    }
    
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    
    @Override
    public void focus() {
        super.focus();
    }
        
}
