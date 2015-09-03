/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.ui;

import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author moscac
 */
public class ScrollingTable extends Table {

    List<ScrollingTableEventListener> listeners = new ArrayList();

    private void fireEvent(String event, Integer value) {
        listeners.stream().forEach((listener) -> {
            listener.processEvent(event, value);
        });
    }

    public void addScrollListener(ScrollingTableEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        Map<String, Object> map = (Map<String, Object>)variables;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            switch (entry.getKey()) {
                case "lastToBeRendered" :
                    if (entry.getValue() instanceof Integer) {
                        fireEvent(entry.getKey(), (Integer)entry.getValue());
                    }
                    break;
            }
        }
    }
}
