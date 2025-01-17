/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.MainUI;
import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.ValoSideBarUI;
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
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.CssLayout;
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

    private ContactTable contactTable;
    final Panel pnlMain = new Panel();
    VerticalLayout vLayout;
    HorizontalLayout hLayout = new HorizontalLayout();
    HorizontalLayout vControls;
    private Button btnEdit;
    //private Button btnDuplicate;
    private Button btnExport;
    private Button btnColumns;
    private TextField tfFilter;
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

        contactTable = new ContactTable(controller, categories, levels, representatives);

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
        vLayout.addComponent(contactTable);
        vLayout.setHeight("75%");
        addComponent(vControls);
        addComponent(vLayout);
        setSizeFull();

        contactTable.addItemClickListener((ItemClickEvent event) -> {
            if (event.isDoubleClick()) {
                editContact(getSelectedContact());
            }
        });

        btnEdit.addClickListener((Button.ClickEvent event) -> {
            editContact(getSelectedContact());
        });

        btnColumns.addClickListener((Button.ClickEvent event) -> {
            contactTable.toggleVisibleColumns();
        });

        OnDemandFileDownloader fd = new OnDemandFileDownloader(new ExportOnDemandStreamResource(contactTable.getContainerDataSource()));
        fd.extend(btnExport);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    private void editContact(ContactRecord contact) {
        MainUI.get().setContactRecord(contact);
        MainUI.getCurrent().getNavigator().navigateTo(DetailView.VIEW_NAME);
    }

    private ContactRecord getSelectedContact() {
        ContactRecord contact = null;
        Object selected = contactTable.getValue();
        if (selected instanceof ContactRecord) {
            contact = (ContactRecord) selected;
        }
        return contact;
    }

}
