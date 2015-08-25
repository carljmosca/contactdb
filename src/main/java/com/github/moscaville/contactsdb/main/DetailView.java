/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.Sections;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = "DetailView")
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "Details",
        order = 1)
@FontAwesomeIcon(FontAwesome.USER)
@ViewScope
public class DetailView extends CssLayout implements View {

    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
