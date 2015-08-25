package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.dto.Contact;
import com.github.moscaville.contactsdb.manager.ContactQueryFactory;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

/**
 * Table of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ContactTable extends Table {
        
    protected ContactQueryFactory contactQueryFactory = new ContactQueryFactory();
    protected QueryDefinition queryDefinition = new LazyQueryDefinition(false, 20, "id" );
    protected LazyQueryContainer container = new LazyQueryContainer(queryDefinition, contactQueryFactory);
    
    public ContactTable() {
        setSizeFull();
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);

        //container = new BeanItemContainer<>(Contact.class);
        // Set up sorting if the natural order is not appropriate
        //container.sort(new String[]{"companyName", "lastName", "firstName"},
        //     new boolean[]{true, true, true});
        List<String> visibleColumns = new ArrayList<>();
        visibleColumns.add("companyName");
        setContainerDataSource(container);
        //setVisibleColumns("firstName", "lastName", "company",
        //        "email", "phone", "contactTypes");
        //setColumnHeaders("First", "Last", "Company",
        //        "Email", "Phone", "Types");
        //setColumnCollapsingAllowed(true);
        //setColumnCollapsed("integerProperty", true);
        //setColumnCollapsed("bigDecimalProperty", true);

        //setColumnWidth("id", 50);
        //setColumnAlignment("price", Align.RIGHT);
        setSelectable(true);
        setImmediate(true);
//        addGeneratedColumn("email", emailGenerator);
        // Add an traffic light icon in front of availability
        //addGeneratedColumn("availability", availabilityGenerator);
        // Add " €" automatically after price
        //setConverter("price", new EuroConverter());
        // Show categories as a comma separated list
//        setConverter("contactTypes", new CollectionToStringConverter());
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId,
            Property property) {
        if (colId.equals("stockCount")) {
            Integer stock = (Integer) property.getValue();
            if (stock.equals(0)) {
                return "-";
            } else {
                return stock.toString();
            }
        } else if ("email".equals(colId)) {
            return "<a href=\"mailto:" + property.getValue() + "\">" + property.getValue() + "</a>";
        }
        return super.formatPropertyValue(rowId, colId, property);
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
                    "company", filterString, true, false);
            container.addContainerFilter(new Or(lastNameFilter, firstNameFilter,
                    companyFilter));
        }

    }

    @Override
    public Contact getValue() {
        if (super.getValue() instanceof Contact) {
            return (Contact) super.getValue();
        } else if (super.getValue() instanceof Long) {
            BeanItem item = (BeanItem) container.getItem(super.getValue());
            return (Contact) item.getBean();
        }
        return null;
    }

    @Override
    public LazyQueryContainer getContainerDataSource() {
        return container;
    }


}
