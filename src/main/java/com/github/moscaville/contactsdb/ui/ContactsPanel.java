/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.ui;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;

/**
 *
 * @author moscac
 */
public class ContactsPanel extends Panel {

    VerticalLayout vl;
    TextField firstName;

    public ContactsPanel() {

        init();
    }

    private void init() {
        
        setImmediate(true);
        firstName = new TextField("First Name");
        vl = new VerticalLayout();
        vl.addComponent(firstName);
        setContent(vl);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        System.out.println("changeVariables");
    }

    @Override
    public void focus() {
        super.focus();
        System.out.println("focus");
    }
}
