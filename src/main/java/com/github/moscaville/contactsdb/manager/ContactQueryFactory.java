/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.manager;

import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

/**
 *
 * @author moscac
 */
public class ContactQueryFactory implements QueryFactory {

    
    @Override
    public Query constructQuery(QueryDefinition queryDefinition) {
        return new ContactQuery(queryDefinition);
    }
    
}
