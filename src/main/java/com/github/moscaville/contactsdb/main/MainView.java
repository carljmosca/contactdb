/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.main;


import com.github.moscaville.contactsdb.Sections;
import com.github.moscaville.contactsdb.auth.OAuthProvider;
import com.github.moscaville.contactsdb.auth.OAuthUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = MainView.VIEW_NAME)
@SideBarItem(sectionId = Sections.CONTACTS,
        caption = "",
        order = 10)
@FontAwesomeIcon(FontAwesome.SIGN_IN)
public class MainView extends BaseView {

    public static final String VIEW_NAME = "";
    private VerticalLayout mainLayout;
    private HorizontalLayout detailLayout;
    private HorizontalLayout buttonLayout;
    @Autowired
    OAuthUtil oAuthUtil;
    @Autowired
    private HttpSession httpSession;

    @PostConstruct
    void init() {

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);

        createDetail();
        createButtons();

        addComponent(mainLayout);
        setSizeFull();
        setResponsive(true);

    }

    private void createDetail() {
        detailLayout = new HorizontalLayout();

        mainLayout.addComponent(detailLayout);
    }

    private void createButtons() {
        buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button btnLogin = new Button("Login");
        btnLogin.setStyleName(ValoTheme.BUTTON_PRIMARY);

        btnLogin.addClickListener((Button.ClickEvent event) -> {
            doLogin();
        });


        buttonLayout.addComponent(btnLogin);
        mainLayout.addComponent(buttonLayout);
    }

    private void doLogin() {

        OAuthProvider provider = oAuthUtil.getByName("GOOGLE");
        String url = provider.getOAuth().getAuthorizationUrl(provider.getScopes(),
                String.valueOf(System.currentTimeMillis()));
        getUI().getPage().setLocation(url);
    }
}
