package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.dto.Contact;
import java.util.Collection;


import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link ContactLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
public class ContactView extends CssLayout implements View {

    public static final String VIEW_NAME = "Contacts";
    private ContactTable table;
    protected ContactForm form;

    private ContactLogic viewLogic = new ContactLogic(this);
    private Button newContact;
    protected String lastSelectedColumn = "";
//    protected ClassificationManager classificationManager;

    public ContactView() {
        setSizeFull();
        addStyleName("crud-view");
        HorizontalLayout topLayout = createTopBar();
//        classificationManager = new ClassificationManager();

        table = new ContactTable();
        table.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                viewLogic.rowSelected(table. getValue());
            }
        });

        //form = new ContactForm(viewLogic, table.getContainerDataSource());
//        form.setCategories(classificationManager.get("ContactType"));

        VerticalLayout barAndTableLayout = new VerticalLayout();
        barAndTableLayout.addComponent(topLayout);
        barAndTableLayout.addComponent(table);
        barAndTableLayout.setMargin(true);
        barAndTableLayout.setSpacing(true);
        barAndTableLayout.setSizeFull();
        barAndTableLayout.setExpandRatio(table, 1);
        barAndTableLayout.setStyleName("crud-main-layout");

        addComponent(barAndTableLayout);
        addComponent(form);

        viewLogic.init();
    }

    private HorizontalLayout createTopBar() {
        TextField filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter");
//        ResetButtonForTextField.extend(filter);
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                table.setFilter(event.getText());
            }
        });

        newContact = new Button("New contact");
        newContact.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newContact.setIcon(FontAwesome.PLUS_CIRCLE);
        newContact.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.newContact();
            }
        });

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setWidth("100%");
        topLayout.addComponent(filter);
        topLayout.addComponent(newContact);
        topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(filter, 1);
        topLayout.setStyleName("top-bar");
        return topLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
//        viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewContactEnabled(boolean enabled) {
        newContact.setEnabled(enabled);
    }

    public void clearSelection() {
        table.setValue(null);
    }

    public void selectRow(Contact row) {
        table.setValue(row);
    }

    public void editContact(Contact contact) {
        if (contact != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editContact(contact);
    }

    public Contact getSelectedRow() {
        return table.getValue();
    }

    public void showContacts(Collection<Contact> contacts) {
        //BeanItemContainer<Contact> container = table.getContainerDataSource();
//        JPAContainer<Contact> container = table.getContainerDataSource();
        //container.removeAllItems();
        //container.addAll(products);
    }

}
