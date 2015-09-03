/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.util;

/**
 *
 * @author moscac
 */
public class Utility {

    public static String toHumanName(String camelName) {
        if (camelName == null || camelName.isEmpty()) {
            return "";
        }
        if (camelName.length() == 1) {
            return camelName.toUpperCase();
        }
        String name = camelName.replaceAll("([A-Z][a-z]+)", " $1") // Words beginning with UC
                .replaceAll("([A-Z][A-Z]+)", " $1") // "Words" of only UC
                .replaceAll("([^A-Za-z ]+)", " $1") // "Words" of non-letters
                .trim();
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
        return result.toString();
    }
}
