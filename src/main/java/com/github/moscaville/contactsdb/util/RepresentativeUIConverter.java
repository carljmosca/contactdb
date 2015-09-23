/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.util;

import com.github.moscaville.contactsdb.dto.LookupBase;
import com.vaadin.data.util.converter.Converter;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author moscac
 */
public class RepresentativeUIConverter implements Converter<Object, String[]> {

    private final List<LookupBase> representativeRecords;

    public RepresentativeUIConverter(List<LookupBase> representativeRecords) {
        this.representativeRecords = representativeRecords;
    }

    @Override
    public String convertToPresentation(String[] value, Class<? extends Object> targetType, Locale locale) throws ConversionException {
        if (value != null) {

            for (LookupBase representativeRecord : representativeRecords) {
                if (representativeRecord.getId().equals(value[0])) {
                    return representativeRecord.getName();
                }
            }
        }
        return null;
    }

    @Override
    public String[] convertToModel(Object value, Class<? extends String[]> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        for (LookupBase representativeRecord : representativeRecords) {
            if (representativeRecord.getName().equals(value)) {
                return new String[]{representativeRecord.getId()};
            }
        }
        return null;
    }

    @Override
    public Class<String[]> getModelType() {
        return String[].class;
    }

    @Override
    public Class<Object> getPresentationType() {
        return Object.class;
    }

}
