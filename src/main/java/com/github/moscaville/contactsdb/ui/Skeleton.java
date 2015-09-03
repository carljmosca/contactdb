/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.ui;

import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author moscac
 */
public class Skeleton extends VerticalLayout {

    public Skeleton() {
        setSizeFull();

        addComponent(new Label("Header component"));

        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        Panel leftComponent = new Panel();
        Panel rightComponent = new Panel();
        splitPanel.setFirstComponent(leftComponent);
        splitPanel.setSecondComponent(rightComponent);
        for (int i = 0; i < 200; i++) {
            leftComponent.setContent(new Label("left"));
            rightComponent.setContent(new Label("right"));
        }

        leftComponent.setSizeFull();
        rightComponent.setSizeFull();

        addComponent(splitPanel);
        setExpandRatio(splitPanel, 1);

        addComponent(new Label("Footer component"));
    }

}
