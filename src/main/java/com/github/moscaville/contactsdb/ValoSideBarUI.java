/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.moscaville.contactsdb;

import com.github.moscaville.contactsdb.controller.UserController;
import com.github.moscaville.contactsdb.dto.ContactRecord;
import com.github.moscaville.contactsdb.dto.UserRecord;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

/**
 * UI that demonstrates the
 * {@link org.vaadin.spring.sidebar.components.ValoSideBar}.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringUI
@Theme("sidebar") // A custom theme based on Valo
@Widgetset("AppWidgetset")
@EnableI18N
public class ValoSideBarUI extends AbstractSideBarUI {

    @Autowired
    ValoSideBar sideBar;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserController userController;
    public static final String ERROR_ATTRIBUTE = "ERROR";
    public static final String EMAIL_ATTRIBUTE = "EMAIL";
    private boolean loggedIn;
    private String email;
    private String error;
    private ContactRecord contact;

    public ValoSideBarUI() {
        
    }
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        CssLayout header = new CssLayout();

        error = (String) httpSession.getAttribute(ERROR_ATTRIBUTE);
        email = (String) httpSession.getAttribute(EMAIL_ATTRIBUTE);
        loggedIn = false;
        if (email != null) {
            for (UserRecord userRecord : userController.loadAllItems(new UserRecord())) {
                if (email.equals(userRecord.getEmail())) {
                    loggedIn = true;
                    break;
                }
            }

        }

        Label lblTitle = new Label("ContactsDb");

        loggedIn = email != null && !email.isEmpty();

        lblTitle.addStyleName(ValoTheme.LABEL_H2);
        lblTitle.setSizeFull();

        header.addComponent(lblTitle);

//        MenuBar menuBar = new MenuBar();
//        header.addComponent(menuBar);
//
//        MenuBar.MenuItem settingsItem = menuBar.addItem("", FontAwesome.WRENCH, null);
        sideBar.setHeader(header);
        sideBar.setVisible(loggedIn);
        if (!loggedIn) {
            getUI().getPage().setLocation("/signin");
        }
    }

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }

    public ContactRecord getContact() {
        return contact;
    }

    public void setContactRecord(ContactRecord contact) {
        this.contact = contact;
    }

    public static ValoSideBarUI get() {
        return (ValoSideBarUI) UI.getCurrent();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
