/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.ui;

import com.github.moscaville.contactsdb.dto.LookupBase;
import com.vaadin.data.util.converter.Converter;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author moscac
 * @param <T>
 */
public class LookupConverter<T extends LookupBase> implements Converter {

    private final List<LookupBase> list;

    public LookupConverter(List<LookupBase> list) {
        this.list = list;
    }

    @Override
    public Object convertToModel(Object value, Class targetType, Locale locale) throws ConversionException {
        return new String[]{"test"};
    }

    @Override
    public Object convertToPresentation(Object value, Class targetType, Locale locale) throws ConversionException {
        StringBuilder result = new StringBuilder();
        if (value != null) {
            String[] ids = (String[]) value;
            for (String id : ids) {
                for (LookupBase r : list) {
                    if (r.getName() != null && id.equals(r.getId())) {
                        if (result.length() > 0) {
                            result.append(",");
                        }
                        result.append(r.getName());
                        break;
                    }
                }
            }
        }
        return result.toString();
    }

    @Override
    public Class getModelType() {
        return String[].class;
    }

    @Override
    public Class getPresentationType() {
        return String.class;
    }

}
