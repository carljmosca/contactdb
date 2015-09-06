/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.Sections;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
//@SpringView(name = "ContactsView")

@SpringView(name = "")
@SideBarItem(sectionId = Sections.CONTACTS,
                caption = "All",
                order = 1)
@FontAwesomeIcon(FontAwesome.TABLE)
@ViewScope
public class ContactsView extends CssLayout implements View {

    private ContactTable contactTable;
    final Panel pnlMain = new Panel();
    VerticalLayout vLayout = new VerticalLayout();
    HorizontalLayout hLayout = new HorizontalLayout();
    HorizontalLayout vControls = new HorizontalLayout();
    private Button btnEdit;
    private Button btnDuplicate;    
    private TextField tfFilter;

    public ContactsView() {
    }

    @PostConstruct
    void init() {

        btnEdit = new Button("Edit");
        btnDuplicate = new Button("Duplicate");
        tfFilter = new TextField();
        tfFilter.setImmediate(true);
        tfFilter.setInputPrompt("Filter");
        
        vLayout.setMargin(true);
        vControls.setMargin(true);
        vControls.addComponent(tfFilter);
        vControls.addComponent(btnEdit);
        vControls.addComponent(btnDuplicate);
        hLayout.setSizeFull();

        contactTable = new ContactTable();
        vLayout.addComponent(contactTable);
        vLayout.setHeight("75%");
        addComponent(vControls);
        addComponent(vLayout);
        setSizeFull();
        
        tfFilter.addValueChangeListener((Property.ValueChangeEvent event) -> {
            
        });

        btnEdit.addClickListener((Button.ClickEvent event) -> {
            
        });
        
        btnDuplicate.addClickListener((Button.ClickEvent event) -> {
//            ContactDetailView v = new ContactDetailView();
//            contactDetailViews.add(v);
//            v.setContactId(contactDetailViews.size());
//            vLayout.addComponent(v);
        });
        
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
