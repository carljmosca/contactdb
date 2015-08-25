package com.github.moscaville.contactsdb.main;


import com.github.moscaville.contactsdb.dto.Contact;
import java.util.Collection;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A form for editing a single product.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
public class ContactForm extends CssLayout {

    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField company = new TextField();
    TextField email = new TextField();
    TextField phone = new TextField();
    TextField addressLine1 = new TextField();
    TextField addressLine2 = new TextField();
    TextField addressLine3 = new TextField();
    TextField addressCity = new TextField();
    TextField addressState = new TextField();
    TextField postalCode = new TextField();
    @PropertyId("contactTypes")
    //ClassificationField contactTypes = new ClassificationField("Categories");
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    protected ContactLogic viewLogic;
    private BeanFieldGroup<Contact> fieldGroup;
    protected BeanItemContainer<Contact> container;

    public ContactForm(ContactLogic sampleCrudLogic, BeanItemContainer<Contact> container) {
        viewLogic = sampleCrudLogic;
        this.container = container;
        populateForm();
    }

    private void populateForm() {
        addStyleName("product-form-wrapper");
        setId("product-form");
        setTextFieldProperties(firstName, "First Name");
        setTextFieldProperties(lastName, "Last Name");
        setTextFieldProperties(company, "Company");
        setTextFieldProperties(email, "Email");
        setTextFieldProperties(phone, "Phone");
        setTextFieldProperties(addressLine1, "Address");
        setTextFieldProperties(addressLine2, "Address 2");
        setTextFieldProperties(addressLine3, "Address 3");
        setTextFieldProperties(addressCity, "City");
        setTextFieldProperties(addressState, "State");
        setTextFieldProperties(postalCode, "Zip");
        
        //availability.setNullSelectionAllowed(false);
        //availability.setTextInputAllowed(false);
        //for (Availability s : Availability.values()) {
        //    availability.addItem(s);
        //}
        //contactTypes.setWidth("100%");

        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancelButton.addStyleName("cancel");

        VerticalLayout layout = new VerticalLayout();
        layout.setHeight("100%");
        layout.setSpacing(true);
        layout.addStyleName("form-layout");

        layout.addComponent(firstName);
        layout.addComponent(lastName);
        layout.addComponent(company);
        layout.addComponent(email);
        layout.addComponent(phone);
        layout.addComponent(addressLine1);
        layout.addComponent(addressLine2);
        layout.addComponent(addressLine3);
        layout.addComponent(addressCity);
        layout.addComponent(addressState);
        layout.addComponent(postalCode);
        //layout.addComponent(contactTypes);

        CssLayout expander = new CssLayout();
        expander.addStyleName("expander");
        layout.addComponent(expander);
        layout.setExpandRatio(expander, 1);

        layout.addComponent(saveButton);
        layout.addComponent(cancelButton);

        addComponent(layout);

        fieldGroup = new BeanFieldGroup<Contact>(Contact.class);
        //fieldGroup.bind(contactTypes, "contactTypes");
        fieldGroup.bindMemberFields(this);
        fieldGroup.setBuffered(false);

        // perform validation and enable/disable buttons while editing
        ValueChangeListener valueListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };
        for (Field f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
        }

        fieldGroup.addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent)
                    throws CommitException {
            }

            @Override
            public void postCommit(CommitEvent commitEvent)
                    throws CommitException {
//                DataService.get().updateContact(
//                        fieldGroup.getItemDataSource().getBean());
            }
        });

        saveButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    Contact contact = fieldGroup.getItemDataSource().getBean();
                    //contact.setContactTypes(contactTypes.getValue());
                    if (contact.getId() == null) {
                        container.addBean(contact);
                    } else {
                        Contact item = container.getIdByIndex(0);
                        
//                        item.getItemProperty("firstName").setValue(contact.getFirstName());
//                        item.getItemProperty("lastName").setValue(contact.getLastName());
//                        item.getItemProperty("company").setValue(contact.getCompany());
//                        item.getItemProperty("email").setValue(contact.getEmail());
//                        item.getItemProperty("phone").setValue(contact.getPhone());
//                        item.getItemProperty("addressLine1").setValue(contact.getAddressLine1());
//                        item.getItemProperty("addressLine2").setValue(contact.getAddressLine2());
//                        item.getItemProperty("addressLine3").setValue(contact.getAddressLine3());
//                        item.getItemProperty("addressCity").setValue(contact.getAddressCity());
//                        item.getItemProperty("addressState").setValue(contact.getAddressState());
//                        item.getItemProperty("postalCode").setValue(contact.getPostalCode());
//                        item.getItemProperty("contactTypes").setValue(contact.getContactTypes());
//                        container.commit();
                    }
//                    viewLogic.saveContact(contact);
                } catch (UnsupportedOperationException e){  
//                        IllegalStateException | 
//                        Property.ReadOnlyException | 
//                        Buffered.SourceException | 
//                        Validator.InvalidValueException e) {
                    Notification n = new Notification("Please re-check the fields",
                            Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                }
            }
        });

        cancelButton.setClickShortcut(KeyCode.ESCAPE);
        cancelButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelContact();
            }
        });
    }

//    public void setCategories(Collection<Classification> categories) {
//        contactTypes.setOptions(categories);
//    }

    public void editContact(Contact contact) {
        if (contact == null) {
            contact = new Contact();
        }
        fieldGroup.setItemDataSource(new BeanItem<>(contact));

        // before the user makes any changes, disable validation error indicator
        firstName.setValidationVisible(false);
        lastName.setValidationVisible(false);
        company.setValidationVisible(false);
        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    private void formHasChanged() {
        // show validation errors after the user has changed something
        firstName.setValidationVisible(true);
        lastName.setValidationVisible(true);
        company.setValidationVisible(true);
    }
    
    private void setTextFieldProperties(TextField textField, String prompt) {
        textField.setWidth("100%");
        textField.setInputPrompt(prompt);
        textField.setNullRepresentation("");
    }
}
