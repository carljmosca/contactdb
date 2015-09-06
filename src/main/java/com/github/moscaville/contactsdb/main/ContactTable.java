package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.dto.Contact;
import com.github.moscaville.contactsdb.controller.ContactController;
import com.github.moscaville.contactsdb.ui.ScrollingTable;
import com.github.moscaville.contactsdb.ui.ScrollingTableEventListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;

/**
 * Table of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ContactTable extends ScrollingTable implements ScrollingTableEventListener {

    private final BeanItemContainer container;
    private final List<Contact> contacts;
    private final ContactController controller;
    private static final int BATCH_SIZE = 20;

    public ContactTable() {

        controller = new ContactController();
        contacts = new ArrayList<>();
        container = new BeanItemContainer(Contact.class, contacts);
        init();
    }

    private void init() {

        setSizeFull();
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);

        setContainerDataSource(container);
        setVisibleColumns("companyName", "firstName", "lastName",
                "email", "workPhone", "cellPhone", "address", "city", "state", "zip");

        setColumnHeaders("Company", "First", "Last", "Email", "Work Phone",
                "Cell Phone", "Address", "City", "State", "Zip");
        setColumnCollapsingAllowed(true);
        setColumnCollapsed("integerProperty", true);
        setColumnCollapsed("bigDecimalProperty", true);

        setSelectable(true);
        setImmediate(true);

        // Show categories as a comma separated list
//        setConverter("contactTypes", new CollectionToStringConverter());
        addScrollListener(this);
        addHeaderClickListener((HeaderClickEvent event) -> {
            controller.setSortColumn(event.getPropertyId().toString());
            reset();
        });
        loadRecords();

    }

    private void reset() {
        container.removeAllItems();
        loadRecords();
    }
    
    private void loadRecords() {
        container.addAll(controller.loadItems(BATCH_SIZE, container.size()));
    }

    /**
     * Filter the table based on a search string that is searched for in the
     * product name, availability and category columns.
     *
     * @param filterString string to look for
     */
    public void setFilter(String filterString) {
        container.removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter lastNameFilter = new SimpleStringFilter(
                    "lastName", filterString, true, false);
            SimpleStringFilter firstNameFilter = new SimpleStringFilter(
                    "firstName", filterString, true, false);
            SimpleStringFilter companyFilter = new SimpleStringFilter(
                    "companyName", filterString, true, false);
            SimpleStringFilter cityFilter = new SimpleStringFilter(
                    "city", filterString, true, false);
            container.addContainerFilter(new Or(lastNameFilter, firstNameFilter,
                    companyFilter, cityFilter));
        }

    }

    @Override
    public void processEvent(String event, Integer value) {
        if ("lastToBeRendered".equals(event) && (value > (0.9 * container.size()))) {
            loadRecords();
        }
    }

}
