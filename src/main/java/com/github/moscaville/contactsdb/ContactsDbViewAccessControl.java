/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;

/**
 *
 * @author moscac
 */
@SpringComponent
public class ContactsDbViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String viewName) {
        return ValoSideBarUI.get().isLoggedIn();
    }
    
}
