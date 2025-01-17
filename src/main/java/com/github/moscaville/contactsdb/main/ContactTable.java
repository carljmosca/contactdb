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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import org.tepi.filtertable.FilterTable;

public class ContactTable extends ScrollingTable implements ScrollingTableEventListener {

    private BeanItemContainer container;
    private List<ContactRecord> contacts;
    private final ContactController controller;
    private final List<LevelRecord> levels;
    private final List<CategoryRecord> categories;
    private final List<RepresentativeRecord> representatives;
    private boolean selected = false;
    private boolean allColumns = true;
    private final String[] VISIBLE_COLUMNS = {"selectedCb", "companyName", "firstName", "lastName",
        "email", "workPhone", "cellPhone", "address", "city",
        "state", "zip", "categoryName", "levelName", "accountName"};

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

        addGeneratedColumn("selectedCb", (final CustomTable source, final Object itemId, final Object columnId) -> {
            final ContactRecord bean = (ContactRecord) itemId;

            final CheckBox checkBox = new CheckBox();
            checkBox.setImmediate(true);
            checkBox.addListener((Event event) -> {
                if (event.getComponent() instanceof CheckBox) {
                    CheckBox cb = (CheckBox) event.getComponent();
                    bean.setSelected(cb.getValue());
                }
            });
            if (bean.isSelected()) {
                checkBox.setValue(true);
            } else {
                checkBox.setValue(false);
            }
            return checkBox;
        });

        setContainerDataSource(container);
        setVisibleColumns("selectedCb", "companyName", "firstName", "lastName",
                "email", "workPhone", "cellPhone", "address", "city",
                "state", "zip", "categoryName", "levelName", "accountName");
        setColumnHeaders("", "Company", "First", "Last",
                "Email", "Work Phone", "Cell Phone", "Address", "City",
                "State", "Postal Code", "Category", "Level", "Account");
        setColumnCollapsingAllowed(true);

        setFilterBarVisible(true);
        setFilterFieldVisible("selectedCb", false);

        for (String column : VISIBLE_COLUMNS) {
            addFilterChangeListeners(column);
        }

        setSelectable(true);
        setImmediate(true);

        addScrollListener(this);
        addHeaderClickListener((HeaderClickEvent event) -> {
            String column = (String) event.getPropertyId();
            if ("selectedCb".equals(column)) {
                selectAll();
            } else {
                controller.setSortColumn(event.getPropertyId().toString());
                reset();
            }
        });
        loadRecords();
    }

    private void addFilterChangeListeners(String field) {
        getFilterField(field).addListener((Event event) -> {
            if (event.getComponent() instanceof TextField) {
                TextField tf = (TextField) event.getComponent();
                if (tf.getValue().length() > 0) {
                    reset();
                }
            }
        });
    }

    public void toggleVisibleColumns() {
        allColumns = !allColumns;
        if (allColumns) {
            setVisibleColumns("selectedCb", "companyName", "firstName", "lastName",
                    "email", "workPhone", "cellPhone", "address", "city",
                    "state", "zip", "categoryName", "levelName", "accountName");
        } else {
            setVisibleColumns("companyName", "firstName", "lastName", "email");
        }
    }

    private void selectAll() {
        selected = !selected;
        container.getItemIds().stream().forEach((cr) -> {
            ((ContactRecord) cr).setSelected(selected);
        });
        refreshRowCache();
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

    class LookupColumnGenerator implements FilterTable.ColumnGenerator {

        public LookupColumnGenerator() {
        }

        /**
         * Generates the cell containing the Double value. The column is
         * irrelevant in this use case.
         */
        @Override
        public Object generateCell(CustomTable source, Object record, Object column) {
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
