/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author moscac
 */
public class ContactLayout extends VerticalLayout implements Listener {
    
    public ContactLayout() {
        addListener(this);
    }

    @Override
    public void componentEvent(Event event) {
    }
    
}
