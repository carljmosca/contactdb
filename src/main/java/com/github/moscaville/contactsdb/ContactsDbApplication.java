package com.github.moscaville.contactsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

/**
 *
 * @author moscac
 */

@EnableAutoConfiguration
@EnableSideBar
@ComponentScan
public class ContactsDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactsDbApplication.class, args);
    }
}


