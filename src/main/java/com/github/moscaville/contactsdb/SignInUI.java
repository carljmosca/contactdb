/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb;

import com.github.moscaville.contactsdb.auth.OAuthProvider;
import com.github.moscaville.contactsdb.auth.OAuthUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.annotation.EnableI18N;

/**
 *
 * @author moscac
 */
@SpringUI(path = "/signin")
@Theme("sidebar") // A custom theme based on Valo
@Widgetset("AppWidgetset")
@EnableI18N
public class SignInUI extends UI {

    private final VerticalLayout mainLayout;
    private HorizontalLayout buttonLayout;
    private Button btnSignIn;
    @Autowired
    OAuthUtil oAuthUtil;

    public SignInUI() {
        mainLayout = new VerticalLayout();
    }

    private void addButtonLayout() {
        buttonLayout = new HorizontalLayout();
        btnSignIn = new Button("Sign In");
        btnSignIn.addClickListener((Button.ClickEvent event) -> {
            doLogin();
        });
        buttonLayout.addComponent(btnSignIn);
        mainLayout.addComponent(buttonLayout);
    }

    @Override
    protected void init(VaadinRequest request) {
        setContent(mainLayout);
        addButtonLayout();
    }

    private void doLogin() {
        OAuthProvider provider = oAuthUtil.getByName("GOOGLE");
        String url = provider.getOAuth().getAuthorizationUrl(provider.getScopes(),
                String.valueOf(System.currentTimeMillis()));
        getUI().getPage().setLocation(url);
    }

}
