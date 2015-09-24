/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.util;

import com.github.moscaville.contactsdb.dto.CategoryRecord;
import com.vaadin.data.util.converter.Converter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author moscac
 */
public class CategoryUIConverter implements Converter<Object, Set> {

    private final List<CategoryRecord> categoryRecords;

    public CategoryUIConverter(List<CategoryRecord> categoryRecords) {
        this.categoryRecords = categoryRecords;
    }

    @Override
    public Object convertToPresentation(Set value, Class<? extends Object> targetType, Locale locale) throws ConversionException {
        List<String> names = new ArrayList<>();
        if (value != null) {
            for (Object s : value) {
                for (CategoryRecord categoryRecord : categoryRecords) {
                    if (categoryRecord.getId().equals(s)) {
                        names.add(categoryRecord.getName());
                        //return categoryRecord.getName();
                    }
                }
            }
        }
//        if (names.isEmpty()) {
//            return null;
//        }
        return new HashSet<>(names);
    }

    @Override
    public Set convertToModel(Object value, Class<? extends Set> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Object s : (Set) value) {
            categoryRecords.stream().filter((categoryRecord) -> (categoryRecord.getName().equals(s))).forEach((categoryRecord) -> {
                names.add(categoryRecord.getId());
            });
        }
        if (names.isEmpty()) {
            return null;
        }
        return new HashSet<>(names);
    }

    @Override
    public Class<Set> getModelType() {
        return Set.class;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }

}
