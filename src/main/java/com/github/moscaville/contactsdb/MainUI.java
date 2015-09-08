/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb;

import com.github.moscaville.contactsdb.dto.Contact;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 *
 * @author moscac
 */
@SpringUI(path = "/main")
@Theme("sidebar") // A custom theme based on Valo
@Widgetset("AppWidgetst.gwt.xml")
public class MainUI extends AbstractSideBarUI {

    private Contact contact;
    
    @Autowired
    ValoSideBar sideBar;

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }
    
    public static MainUI get() {
        return (MainUI) UI.getCurrent();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
}
