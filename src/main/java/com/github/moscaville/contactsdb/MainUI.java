/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 *
 * @author moscac
 */
@SpringUI
@Theme("valo") // A custom theme based on Valo
@Widgetset("AppWidgetset")
@EnableI18N
public class MainUI extends AbstractSideBarUI {

    @Autowired
    ValoSideBar sideBar;

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }
    
    public static ValoSideBarUI get() {
        return (ValoSideBarUI) UI.getCurrent();
    }

}
