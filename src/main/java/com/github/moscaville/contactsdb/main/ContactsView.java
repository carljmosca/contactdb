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
import com.github.moscaville.contactsdb.dto.RepresentativeRecord;
import com.github.moscaville.contactsdb.util.ExportOnDemandStreamResource;
import com.github.moscaville.contactsdb.util.OnDemandFileDownloader;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = "")
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "All",
        order = 1)
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
    private final String[] COLUMNS = {"selected", "companyName", "firstName", "lastName",
        "email", "workPhone", "cellPhone", "address", "city",
        "state", "zip", "category", "level", "account"};
    @Autowired
    ContactController controller;
    @Autowired
    RepresentativeController representativeController;
    @Autowired
    CategoryController categoryController;
    @Autowired
    LevelController levelController;

    public ContactsView() {
    }

    @PostConstruct
    void init() {

        List<CategoryRecord> categories = categoryController.loadItems(100, 0, new CategoryRecord());
        List<LevelRecord> levels = levelController.loadItems(100, 0, new LevelRecord());
        List<RepresentativeRecord> representatives = representativeController.loadItems(100, 0, new RepresentativeRecord());

        //contactTable = new ContactTable(controller, categories, levels, representatives);
        beans = new BeanContainer<>(ContactRecord.class);
        beans.setBeanIdProperty("id");
        beans.addAll(controller.loadAllItems(new ContactRecord()));
        contactGrid = new Grid(beans);
        contactGrid.setSizeFull();
        addFilters();
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
            //contactTable.toggleVisibleColumns();
        });

        OnDemandFileDownloader fd = new OnDemandFileDownloader(new ExportOnDemandStreamResource(contactGrid.getContainerDataSource()));
        fd.extend(btnExport);

    }

    private void addFilters() {
        HeaderRow filterRow = contactGrid.appendHeaderRow();

        for (Object pid : contactGrid.getContainerDataSource()
                .getContainerPropertyIds()) {
            HeaderCell cell = filterRow.getCell(pid);

            // Have an input field to use for filter
            TextField filterField = new TextField();
            filterField.setColumns(COLUMNS.length);

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
            cell.setComponent(filterField);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private void editContact(ContactRecord contact) {
        MainUI.get().setContactRecord(contact);
        MainUI.getCurrent().getNavigator().navigateTo(DetailView.VIEW_NAME);
    }

    private ContactRecord getSelectedContact() {
        Object selected;
        if (contactGrid.getSelectedRow() == null) {
            selected = beans.getIdByIndex(0);
        } else {
            selected = contactGrid.getSelectedRow();
        }
        ContactRecord contact = null;
        if (selected != null) {
            contact = beans.getItem(selected).getBean();
        }
        return contact;
    }

}
