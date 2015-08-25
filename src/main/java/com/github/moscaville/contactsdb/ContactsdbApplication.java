package com.github.moscaville.contactsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

@EnableAutoConfiguration
@EnableSideBar
@ComponentScan
public class ContactsdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactsdbApplication.class, args);
    }
}
