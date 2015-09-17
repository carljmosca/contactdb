/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.MainUI;
import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = "")
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "All",
        order = 1)
@FontAwesomeIcon(FontAwesome.TABLE)
@ViewScope
public class ContactsView extends CssLayout implements View {

    @Autowired
    ContactTable contactTable;
    final Panel pnlMain = new Panel();
    VerticalLayout vLayout;
    HorizontalLayout hLayout = new HorizontalLayout();
    HorizontalLayout vControls;
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

        vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        vControls = new HorizontalLayout();
        vControls.setSpacing(true);
        vControls.setMargin(true);
        vControls.addComponent(tfFilter);
        vControls.addComponent(btnEdit);
        vControls.addComponent(btnDuplicate);
        hLayout.setSizeFull();

        //contactTable = new ContactTable();
        vLayout.addComponent(contactTable);
        vLayout.setHeight("75%");
        addComponent(vControls);
        addComponent(vLayout);
        setSizeFull();

        contactTable.addItemClickListener((ItemClickEvent event) -> {
            if (event.isDoubleClick()) {
                editContact(getSelectedContact());
            }
        });

        tfFilter.addValueChangeListener((Property.ValueChangeEvent event) -> {
            contactTable.setFilter(tfFilter.getValue());
        });

        btnEdit.addClickListener((Button.ClickEvent event) -> {
            editContact(getSelectedContact());
        });

        btnDuplicate.addClickListener((Button.ClickEvent event) -> {
            ContactRecord selected = getSelectedContact();
            if (selected != null) {
                ContactRecord contact = new ContactRecord();
                BeanUtils.copyProperties(selected, contact);
                editContact(contact);
            }
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private void editContact(ContactRecord contact) {
        MainUI.get().setContactRecord(contact);
        MainUI.getCurrent().getNavigator().navigateTo(DetailView.VIEW_NAME);
    }

    private ContactRecord getSelectedContact() {
        ContactRecord contact = null;
        Object selected = contactTable.getValue();
        if (selected instanceof ContactRecord) {
            contact = (ContactRecord) selected;
        }
        return contact;
    }

}
