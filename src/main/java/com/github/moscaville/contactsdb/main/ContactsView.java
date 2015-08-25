/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.Sections;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = "ContactsView")
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "All",
        order = 1)
@FontAwesomeIcon(FontAwesome.TABLE)
@ViewScope
public class ContactsView extends CssLayout implements View {

    private ContactTable contactTable;
    
    public ContactsView() {

    }

    @PostConstruct
    void init() {

        vLayout.setMargin(true);
        vButtons.setMargin(true);
        hLayout.setSizeFull();
        
        contactTable = new ContactTable();
        vLayout.addComponent(contactTable);
        
        addComponent(vLayout);
        addComponent(vButtons);

        setSizeFull();
    }

    final Panel pnlMain = new Panel();
    VerticalLayout vLayout = new VerticalLayout();
    HorizontalLayout hLayout = new HorizontalLayout();
    HorizontalLayout vButtons = new HorizontalLayout();
    Button btnContinue = new Button("Continue");

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

