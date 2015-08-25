/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb;

import com.vaadin.annotations.Theme;
import com.vaadin.spring.annotation.SpringUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 *
 * @author moscac
 */
@SpringUI(path = "/main")
@Theme("sidebar") // A custom theme based on Valo
public class MainUI extends AbstractSideBarUI {

    @Autowired
    ValoSideBar sideBar;

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }
    
}
