/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.MainUI;
import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.controller.ContactController;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = DetailView.VIEW_NAME)
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "Details",
        order = 1)
@FontAwesomeIcon(FontAwesome.USER)
@ViewScope
public class DetailView extends CssLayout implements View {

    @Autowired
    ContactController controller;
    public static final String VIEW_NAME = "DetailView";
    protected BeanFieldGroup<ContactRecord> fieldGroup;
    private ContactRecord contact;
    private TextField firstName;
    private TextField lastName;
    private TextField companyName;
    private TextField address;
    private TextField city;
    private TextField state;
    private TextField zip;
    private TextField email;
    private TextField workPhone;
    private TextField cellPhone;
    private VerticalLayout mainLayout;
    private HorizontalLayout nameLayout;
    private HorizontalLayout addressLayout;
    private HorizontalLayout address2Layout;
    private HorizontalLayout emailPhoneLayout;
    private HorizontalLayout buttonLayout;
    private Button btnSave;

    public DetailView() {
        init();
    }

    private void init() {
        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        nameLayout = new HorizontalLayout();
        nameLayout.setSpacing(true);
        companyName = createTextField("company name", nameLayout);
        firstName = createTextField("first name", nameLayout);
        lastName = createTextField("last name", nameLayout);

        emailPhoneLayout = new HorizontalLayout();
        emailPhoneLayout.setSpacing(true);
        email = createTextField("email", emailPhoneLayout);
        cellPhone = createTextField("cell phone", emailPhoneLayout);
        workPhone = createTextField("work phone", emailPhoneLayout);

        addressLayout = new HorizontalLayout();
        addressLayout.setSpacing(true);
        addressLayout.setWidth("400px   ");
        address = createTextField("address", addressLayout);
        address.setWidth("90%");

        address2Layout = new HorizontalLayout();
        address2Layout.setSpacing(true);
        city = createTextField("city", address2Layout);
        state = createTextField("state", address2Layout);
        zip = createTextField("zip", address2Layout);
        
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        btnSave = new Button("Save");
        btnSave.addClickListener((Button.ClickEvent event) -> {
            controller.saveItem(contact, contact.getId());
        });
        buttonLayout.addComponent(btnSave);

        mainLayout.addComponent(nameLayout);
        mainLayout.addComponent(emailPhoneLayout);
        mainLayout.addComponent(addressLayout);
        mainLayout.addComponent(address2Layout);
        mainLayout.addComponent(buttonLayout);
        addComponent(mainLayout);
        bind();
    }

    private TextField createTextField(String inputPrompt, HorizontalLayout horizontalLayout) {
        TextField textField = new TextField();
        textField.setInputPrompt(inputPrompt);
        if (horizontalLayout != null) {
            horizontalLayout.addComponent(textField);
        }
        return textField;
    }

    private void bind() {
        fieldGroup = new BeanFieldGroup<>(ContactRecord.class);
        contact = MainUI.get().getContact();
        fieldGroup.setItemDataSource(contact);
        fieldGroup.setBuffered(false);
        fieldGroup.bindMemberFields(this);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
