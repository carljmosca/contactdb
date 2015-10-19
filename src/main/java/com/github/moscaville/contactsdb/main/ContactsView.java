/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.ValoSideBarUI;
import com.github.moscaville.contactsdb.controller.CategoryController;
import com.github.moscaville.contactsdb.controller.ContactController;
import com.github.moscaville.contactsdb.controller.LevelController;
import com.github.moscaville.contactsdb.controller.RepresentativeController;
import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.dto.LevelRecord;
import com.github.moscaville.contactsdb.dto.LookupBase;
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import com.github.moscaville.contactsdb.ui.LookupConverter;
import com.github.moscaville.contactsdb.util.ExportOnDemandStreamResource;
import com.github.moscaville.contactsdb.util.OnDemandFileDownloader;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = ContactsView.VIEW_NAME)
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "All",
        order = 20)
@FontAwesomeIcon(FontAwesome.TABLE)
@ViewScope
public class ContactsView extends CssLayout implements View {

    private Grid contactGrid;
    private BeanContainer<String, ContactRecord> beans;
    final Panel pnlMain = new Panel();
    VerticalLayout vLayout;
    HorizontalLayout hLayout = new HorizontalLayout();
    HorizontalLayout vControls;
    private Button btnEdit;
    //private Button btnDuplicate;
    private Button btnExport;
    private Button btnColumns;
    private final String[] COLUMNS = {"companyName", "firstName", "lastName",
        "email", "workPhone", "cellPhone", "address", "city",
        "state", "zip", "category", "level", "account"};
    private final String[] FEWER_COLUMNS = {"companyName", "firstName", "lastName", "email"};
    private boolean allColumns;
    @Autowired
    ContactController controller;
    @Autowired
    RepresentativeController representativeController;
    @Autowired
    CategoryController categoryController;
    @Autowired
    LevelController levelController;
    private List<CategoryRecord> categories;
    private List<LevelRecord> levels;
    private List<RepresentativeRecord> representatives;
    public static final String VIEW_NAME = "contacts";

    public ContactsView() {
        this.allColumns = true;
    }

    @PostConstruct
    void init() {

        categories = categoryController.loadItems(100, 0, new CategoryRecord());
        levels = levelController.loadItems(100, 0, new LevelRecord());
        representatives = representativeController.loadItems(100, 0, new RepresentativeRecord());

        //contactTable = new ContactTable(controller, categories, levels, representatives);
        beans = new BeanContainer<>(ContactRecord.class);
        beans.setBeanIdProperty("id");
        beans.addAll(controller.loadAllItems(new ContactRecord()));
        contactGrid = new Grid(beans);
        contactGrid.setSizeFull();
        contactGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        addRenderersAndConverters();
        addFilters();
        //addGeneratedColumns();
        contactGrid.setColumns((Object[]) COLUMNS);
        contactGrid.setColumnOrder((Object[]) COLUMNS);

        btnEdit = new Button("Edit");
        btnExport = new Button("Export");
        btnColumns = new Button("Columns");

        vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        vControls = new HorizontalLayout();
        vControls.setSpacing(true);
        vControls.setMargin(true);
        vControls.addComponent(btnEdit);
        //vControls.addComponent(btnDuplicate);
        vControls.addComponent(btnExport);
        vControls.addComponent(btnColumns);
        hLayout.setSizeFull();

        //contactTable = new ContactTable();
        vLayout.addComponent(contactGrid);
        vLayout.setHeight("75%");
        addComponent(vControls);
        addComponent(vLayout);
        setSizeFull();

        contactGrid.addItemClickListener((ItemClickEvent event) -> {
            if (event.isDoubleClick()) {
                editContact(getSelectedContact());
            }
        });

        btnEdit.addClickListener((Button.ClickEvent event) -> {
            editContact(getSelectedContact());
        });

        btnColumns.addClickListener((Button.ClickEvent event) -> {
            toggleVisibleColumns();
        });

        OnDemandFileDownloader fd = new OnDemandFileDownloader(new ExportOnDemandStreamResource(contactGrid));
        fd.extend(btnExport);

    }

    private void addFilters() {
        if (contactGrid.getHeaderRowCount() > 1) {
            contactGrid.removeHeaderRow(1);
        }
        HeaderRow filterRow = contactGrid.appendHeaderRow();
        contactGrid.getContainerDataSource()
                .getContainerPropertyIds().stream().forEach((pid) -> {
                    if (contactGrid.getColumn(pid) != null) {
                        HeaderCell cell = filterRow.getCell(pid);

                        // Have an input field to use for filter
                        if ("selected".equals(pid)) {
                            CheckBox checkBox = new CheckBox("", false);
                            checkBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
                                beans.removeContainerFilters(pid);
                                if (checkBox.getValue()) {
                                    beans.addContainerFilter(new Compare.Equal(pid, checkBox.getValue()));
                                }
                            });
                            cell.setComponent(checkBox);
                        } else if ("category".equals(pid) || "level".equals(pid) || "account".equals(pid)) {
                            TextField filterField = new TextField();
                            filterField.setColumns(8);
                            // Update filter When the filter input is changed
                            filterField.addTextChangeListener(change -> {
                                // Can't modify filters so need to replace
                                beans.removeContainerFilters(pid);
                                // (Re)create the filter if necessary
                                if (!change.getText().isEmpty()) {
                                    beans.addContainerFilter(
                                            new CategoryFilter((String) pid,
                                                    change.getText()));
                                }
                            });
                            filterField.setImmediate(true);
                            cell.setComponent(filterField);
                        } else {
                            TextField filterField = new TextField();
                            filterField.setColumns(8);
                            // Update filter When the filter input is changed
                            filterField.addTextChangeListener(change -> {
                                // Can't modify filters so need to replace
                                beans.removeContainerFilters(pid);
                                // (Re)create the filter if necessary
                                if (!change.getText().isEmpty()) {
                                    beans.addContainerFilter(
                                            new SimpleStringFilter(pid,
                                                    change.getText(), true, false));
                                }
                            });
                            filterField.setImmediate(true);
                            cell.setComponent(filterField);
                        }
                    }
                });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private void editContact(ContactRecord contact) {
        ValoSideBarUI.get().setContactRecord(contact);
        ValoSideBarUI.getCurrent().getNavigator().navigateTo(DetailView.VIEW_NAME);
    }

    private ContactRecord getSelectedContact() {
        Object selected;
        if (contactGrid.getSelectedRows().isEmpty()) {
            selected = beans.getIdByIndex(0);
        } else {
            selected = contactGrid.getSelectedRows().toArray()[0];
        }
        ContactRecord contact = null;
        if (selected != null) {
            contact = beans.getItem(selected).getBean();
        }
        return contact;
    }

    private void addRenderersAndConverters() {
        Grid.Column categoryColumn = contactGrid.getColumn("category");
        if (categoryColumn != null) {
            categoryColumn.setRenderer(new TextRenderer(), new LookupConverter(categories));
        }
        Grid.Column levelColumn = contactGrid.getColumn("level");
        if (levelColumn != null) {
            levelColumn.setRenderer(new TextRenderer(), new LookupConverter(levels));
        }
        Grid.Column representativeColumn = contactGrid.getColumn("account");
        if (representativeColumn != null) {
            representativeColumn.setRenderer(new TextRenderer(), new LookupConverter(representatives));
        }

    }

    private void toggleVisibleColumns() {
        allColumns = !allColumns;
        if (allColumns) {
            contactGrid.setColumns((Object[]) COLUMNS);
            contactGrid.setColumnOrder((Object[]) COLUMNS);
        } else {
            contactGrid.setColumns((Object[]) FEWER_COLUMNS);
            contactGrid.setColumnOrder((Object[]) FEWER_COLUMNS);
        }
        addFilters();
        addRenderersAndConverters();
    }

    public class CategoryFilter implements Container.Filter {

        protected String propertyId;
        protected String value;

        public CategoryFilter(String propertyId, String value) {
            this.propertyId = propertyId;
            this.value = value;
        }

        /**
         * Tells if this filter works on the given property.
         */
        @Override
        public boolean appliesToProperty(Object propertyId) {
            return propertyId != null
                    && propertyId.equals(this.propertyId);
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            BeanItem beanItem = (BeanItem) item;
            ContactRecord contactRecord = (ContactRecord) beanItem.getBean();
            if (null != propertyId) {
                switch (propertyId) {
                    case "category":
                        return compare(categories, contactRecord.getCategory());
                    case "level":
                        return compare(levels, contactRecord.getLevel());
                    case "account":
                        return compare(representatives, contactRecord.getAccount());
                }
            }
            return false;
        }

        private boolean compare(List<? extends LookupBase> list, String[] ids) {
            if (ids == null) {
                return false;
            }
            for (LookupBase r : list) {
                for (String s : ids) {
                    if (r.getId().equals(s) && r.getName().toLowerCase().contains(value.toLowerCase())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
