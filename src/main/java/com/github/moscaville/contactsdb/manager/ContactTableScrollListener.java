/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.manager;

import com.github.moscaville.contactsdb.ui.ScrollingTable;
import com.github.moscaville.contactsdb.ui.ScrollingTableEventListener;

/**
 *
 * @author moscac
 */
public class ContactTableScrollListener implements ScrollingTableEventListener {

    private final ScrollingTable table;
    
    public ContactTableScrollListener(ScrollingTable table) {
        this.table = table;
    }
    
    @Override
    public void processEvent(String event, Integer value) {

    }
    
}
