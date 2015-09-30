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
import com.github.moscaville.contactsdb.controller.LevelController;
import com.github.moscaville.contactsdb.controller.RepresentativeController;
import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.dto.LevelRecord;
import com.github.moscaville.contactsdb.dto.LookupBase;
import com.github.moscaville.contactsdb.dto.RecordWrapper;
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import com.github.moscaville.contactsdb.util.RepresentativeUIConverter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    @Autowired
    LevelController levelController;
    public static final String VIEW_NAME = "DetailView";
    protected BeanFieldGroup<ContactRecord> fieldGroup;
    protected BeanItem<ContactRecord> contactRecordBeanItem;
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
    private TextArea notes;
    private ComboBox category;
    private ComboBox account;
    private ComboBox level;
    private VerticalLayout mainLayout;
    private HorizontalLayout nameLayout;
    private HorizontalLayout addressLayout;
    private HorizontalLayout address2Layout;
    private HorizontalLayout emailPhoneLayout;
    private HorizontalLayout classificationLayout;
    private HorizontalLayout notesLayout;
    private HorizontalLayout buttonLayout;
    private Button btnNew;
    private Button btnSave;
    private Button btnDuplicate;
    private Button btnCancel;
    private List<LookupBase> categories;
    private List<LookupBase> representatives;
    private List<LookupBase> levels;

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
                    categories.add(categoryRecord);
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
        List<LevelRecord> levelRecords = levelController.loadItems(100, 0, new LevelRecord());
        levels = new ArrayList<>();
        if (levelRecords != null) {
            levelRecords.stream().forEach((levelRecord) -> {
                if (levelRecord.getName() != null) {
                    levels.add(levelRecord);
                }
            });
        }

        classificationLayout = new HorizontalLayout();
        classificationLayout.setSpacing(true);
        category = createComboBox("Category", categories, classificationLayout);
        account = createComboBox("Account", representatives, classificationLayout);
        level = createComboBox("Level", levels, classificationLayout);

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

        notesLayout = new HorizontalLayout();
        notesLayout.setSpacing(true);
        notes = createNotesField("notes", notesLayout);

        buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        btnNew = new Button("New");
        btnNew.addClickListener((Button.ClickEvent event) -> {
            populateFields(new ContactRecord());
        });
        buttonLayout.addComponent(btnNew);
        btnSave = new Button("Save");
        btnSave.addClickListener((Button.ClickEvent event) -> {
            RecordWrapper<ContactRecord> recordWrapper = new RecordWrapper();
            recordWrapper.setFields(contact);
            controller.saveItem(recordWrapper, contact.getId());
        });
        buttonLayout.addComponent(btnSave);
        btnCancel = new Button("Cancel");
        btnCancel.addClickListener((Button.ClickEvent event) -> {
            fieldGroup.discard();
        });
        buttonLayout.addComponent(btnCancel);
        btnDuplicate = new Button("Duplicate");
        btnDuplicate.addClickListener((Button.ClickEvent event) -> {
            ContactRecord duplicate;
            try {
                duplicate = (ContactRecord) BeanUtils.cloneBean(contact);
                populateFields(duplicate);
                contact.setId(null);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(DetailView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        buttonLayout.addComponent(btnDuplicate);

        mainLayout.addComponent(nameLayout);
        mainLayout.addComponent(classificationLayout);
        mainLayout.addComponent(emailPhoneLayout);
        mainLayout.addComponent(addressLayout);
        mainLayout.addComponent(address2Layout);
        mainLayout.addComponent(notesLayout);
        mainLayout.addComponent(buttonLayout);
        addComponent(mainLayout);
        bind();
    }

    private TextField createTextField(String inputPrompt, HorizontalLayout horizontalLayout) {
        TextField textField = new TextField();
        textField.setInputPrompt(inputPrompt);
        textField.setImmediate(true);
        textField.setNullRepresentation("");
        if (horizontalLayout != null) {
            horizontalLayout.addComponent(textField);
        }
        return textField;
    }

    private TextArea createNotesField(String inputPrompt, HorizontalLayout horizontalLayout) {
        TextArea textArea = new TextArea();
        textArea.setInputPrompt(inputPrompt);
        textArea.setImmediate(true);
        textArea.setNullRepresentation("");
        if (horizontalLayout != null) {
            horizontalLayout.addComponent(textArea);
        }
        return textArea;
    }

    private ComboBox createComboBox(String label, List<LookupBase> items, HorizontalLayout horizontalLayout) {
        ComboBox comboBox = new ComboBox();
        RepresentativeUIConverter converter = new RepresentativeUIConverter(items);
        comboBox.setConverter(converter);
        comboBox.setInputPrompt(label);
        comboBox.setImmediate(true);
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
        fieldGroup.setBuffered(true);
        fieldGroup.bindMemberFields(this);
        contactRecordBeanItem = fieldGroup.getItemDataSource();
    }

    private void populateFields(ContactRecord c) {
        firstName.setValue(c.getFirstName());
        lastName.setValue(c.getLastName());
        companyName.setValue(c.getCompanyName());
        address.setValue(c.getAddress());        
        city.setValue(c.getCity());
        state.setValue(c.getState());
        zip.setValue(c.getZip());
        cellPhone.setValue(c.getCellPhone());
        workPhone.setValue(c.getWorkPhone());
        email.setValue(c.getEmail());
        notes.setValue(c.getNotes());
        account.setValue(c.getAccount());
        level.setValue(c.getLevel());
        category.setValue(c.getCategory());
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
