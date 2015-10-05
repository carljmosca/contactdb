package com.github.moscaville.contactsdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.spring.sidebar.annotation.EnableSideBar;

@EnableSideBar
@SpringBootApplication
@ComponentScan("com.github.moscaville")

public class ContactsdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactsdbApplication.class, args);
    }
}
