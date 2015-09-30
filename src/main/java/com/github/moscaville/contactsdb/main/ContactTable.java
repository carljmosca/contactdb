package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.controller.ContactController;
import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.github.moscaville.contactsdb.dto.LevelRecord;
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import com.github.moscaville.contactsdb.ui.ScrollingTable;
import com.github.moscaville.contactsdb.ui.ScrollingTableEventListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;

public class ContactTable extends ScrollingTable implements ScrollingTableEventListener {

    private BeanItemContainer container;
    private List<ContactRecord> contacts;
    private final ContactController controller;
    private final List<LevelRecord> levels;
    private final List<CategoryRecord> categories;
    private final List<RepresentativeRecord> representatives;

    private static final int BATCH_SIZE = 20;

    public ContactTable(ContactController controller, List<CategoryRecord> categories,
            List<LevelRecord> levels, List<RepresentativeRecord> representatives) {
        this.controller = controller;
        this.levels = levels;
        this.categories = categories;
        this.representatives = representatives;
        init();
    }

    private void init() {

        contacts = new ArrayList<>();
        container = new BeanItemContainer(ContactRecord.class, contacts);

        setSizeFull();
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);

        addContainerProperty("categoryName", String.class, null, "Category", null, null);
        addContainerProperty("levelName", String.class, null, "Level", null, null);
        addContainerProperty("accountName", String.class, null, "Account", null, null);
        addGeneratedColumn("categoryName", new LookupColumnGenerator());
        addGeneratedColumn("levelName", new LookupColumnGenerator());
        addGeneratedColumn("accountName", new LookupColumnGenerator());
        setContainerDataSource(container);

        setVisibleColumns("selected", "companyName", "firstName", "lastName",
                "email", "workPhone", "cellPhone", "address", "city",
                "state", "zip", "categoryName", "levelName", "accountName");

        setColumnHeaders("", "Company", "First", "Last", 
                "Email", "Work Phone", "Cell Phone", "Address", "City", 
                "State", "Postal Code", "Category", "Level", "Account");
        setColumnCollapsingAllowed(true);
        //setColumnCollapsed("integerProperty", true);
        //setColumnCollapsed("bigDecimalProperty", true);

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
        container.addAll(controller.loadItems(BATCH_SIZE, container.size(), new ContactRecord()));
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

    class LookupColumnGenerator implements Table.ColumnGenerator {

        public LookupColumnGenerator() {
        }

        /**
         * Generates the cell containing the Double value. The column is
         * irrelevant in this use case.
         */
        @Override
        public Component generateCell(Table source, Object record,
                Object column) {
            Label label = new Label();
            label.addStyleName("column-type-value");
            label.addStyleName("column-" + (String) column);
            StringBuilder result = new StringBuilder();
            ContactRecord contactRecord = (ContactRecord) record;
            if ("categoryName".equals(column)) {
                if (contactRecord.getCategory() != null) {
                    for (String s : contactRecord.getCategory()) {
                        categories.stream().filter((r) -> (s != null && s.equals(r.getId()))).forEach((r) -> {
                            if (result.length() > 0) {
                                result.append(",");
                            }
                            result.append(r.getName());
                        });
                    }
                }
            } else if ("levelName".equals(column)) {
                if (contactRecord.getLevel() != null) {
                    for (String s : contactRecord.getLevel()) {
                        levels.stream().filter((r) -> (s != null && s.equals(r.getId()))).forEach((r) -> {
                            if (result.length() > 0) {
                                result.append(",");
                            }
                            result.append(r.getName());
                        });
                    }
                }
            } else if ("accountName".equals(column)) {
                if (contactRecord.getAccount() != null) {
                    for (String s : contactRecord.getAccount()) {
                        representatives.stream().filter((r) -> (s != null && s.equals(r.getId()))).forEach((r) -> {
                            if (result.length() > 0) {
                                result.append(",");
                            }
                            result.append(r.getName());
                        });
                    }
                }
            }
            if (result.length() == 0) {
                return null;
            }
            label.setValue(result.toString());
            return label;
        }
    }

}
