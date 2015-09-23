/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.MainUI;
import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.controller.CategoryController;
import com.github.moscaville.contactsdb.controller.ContactController;
import com.github.moscaville.contactsdb.controller.RepresentativeController;
import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.dto.RecordWrapper;
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import com.github.moscaville.contactsdb.util.LookupConverter;
import com.github.moscaville.contactsdb.util.RepresentativeUIConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.beanutils.BeanUtils;
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
    @Autowired
    CategoryController categoryController;
    @Autowired
    RepresentativeController representativeController;
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
    private ListSelect category;
    private ComboBox account;
    private VerticalLayout mainLayout;
    private HorizontalLayout nameLayout;
    private HorizontalLayout addressLayout;
    private HorizontalLayout address2Layout;
    private HorizontalLayout emailPhoneLayout;
    private HorizontalLayout classificationLayout;
    private HorizontalLayout buttonLayout;
    private Button btnSave;
    private Button btnDuplicate;
    private List<String> categories;
    private List<RepresentativeRecord> representatives;

    public DetailView() {

    }

    @PostConstruct
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

        List<CategoryRecord> categoryRecords = categoryController.loadItems(100, 0, new CategoryRecord());
        categories = new ArrayList<>();
        if (categoryRecords != null) {
            categoryRecords.stream().forEach((categoryRecord) -> {
                if (categoryRecord.getName() != null) {
                    categories.add(categoryRecord.getName());
                }
            });
        }
        List<RepresentativeRecord> representativeRecords = representativeController.loadItems(100, 0, new RepresentativeRecord());
        representatives = new ArrayList<>();
        if (representativeRecords != null) {
            representativeRecords.stream().forEach((representativeRecord) -> {
                if (representativeRecord.getName() != null) {
                    representatives.add(representativeRecord);
                }
            });
        }

        classificationLayout = new HorizontalLayout();
        classificationLayout.setSpacing(true);
        //category = createListSelect("Category", categories, classificationLayout);
        account = createComboBox("Account", representatives, classificationLayout);

        addressLayout = new HorizontalLayout();
        addressLayout.setSpacing(true);
        addressLayout.setWidth("400px");
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
            RecordWrapper<ContactRecord> recordWrapper = new RecordWrapper();
            recordWrapper.setFields(contact);
            controller.saveItem(recordWrapper, contact.getId());
        });
        buttonLayout.addComponent(btnSave);
        btnDuplicate = new Button("Duplicate");
        btnDuplicate.addClickListener((Button.ClickEvent event) -> {
            ContactRecord duplicate;
            try {
                duplicate = (ContactRecord) BeanUtils.cloneBean(contact);
                contact = duplicate;
                contact.setId(null);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(DetailView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        mainLayout.addComponent(nameLayout);
        mainLayout.addComponent(classificationLayout);
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

    private ListSelect createListSelect(String label, List<String> items, HorizontalLayout horizontalLayout) {
        ListSelect listSelect = new ListSelect(label);
        listSelect.setMultiSelect(true);
        listSelect.setRows(3);
        listSelect.addItems(items);
        
        LookupConverter lookupConverter = new LookupConverter(new HashSet<>(items));
        listSelect.setConverter((Converter)lookupConverter);
        horizontalLayout.addComponent(listSelect);
        return listSelect;
    }
    
    private ComboBox createComboBox(String label, List<RepresentativeRecord> items, HorizontalLayout horizontalLayout) {
        ComboBox comboBox = new ComboBox();
        RepresentativeUIConverter converter = new RepresentativeUIConverter(representatives);
        comboBox.setConverter(converter);
        comboBox.setInputPrompt(label);
        List<String> ids = new ArrayList<>();
        items.stream().forEach((item) -> {
                ids.add(item.getName());
        });
        comboBox.addItems(ids);
        horizontalLayout.addComponent(comboBox);
        return comboBox;
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
