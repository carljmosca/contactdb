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
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
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
    HorizontalLayout vButtons = new HorizontalLayout();
    Button btnDuplicate = new Button("Duplicate");
    private final List<ContactDetailView> contactDetailViews;

    public ContactsView() {
        contactDetailViews = new ArrayList<>();
    }

    @PostConstruct
    void init() {

        vLayout.setMargin(true);
        vButtons.setMargin(true);
        vButtons.addComponent(btnDuplicate);
        hLayout.setSizeFull();

        contactTable = new ContactTable();
        vLayout.addComponent(contactTable);
        vLayout.setHeight("75%");
        addComponent(vButtons);
        addComponent(vLayout);
        setSizeFull();

        btnDuplicate.addClickListener((Button.ClickEvent event) -> {
//            ContactDetailView v = new ContactDetailView();
//            contactDetailViews.add(v);
//            v.setContactId(contactDetailViews.size());
//            vLayout.addComponent(v);
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
