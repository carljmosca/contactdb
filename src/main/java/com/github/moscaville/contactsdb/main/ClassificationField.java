package com.github.moscaville.contactsdb.main;

import com.github.moscaville.contactsdb.dto.Classification;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;

/**
 * A custom Field implementation that allows selecting a set of categories using
 * checkboxes rather than multi-selection in a list/table or a TwinColSelect.
 */
public class ClassificationField extends CustomField<Set<Classification>> {

    private VerticalLayout options;
    private final Map<Classification, CheckBox> checkboxes = new HashMap<>();
    private boolean updatingField = false;
    private final Set<Classification> classifications;

    public ClassificationField() {
        options = new VerticalLayout();
        classifications = new HashSet<>();
    }

    public ClassificationField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        return options;
    }

    /**
     * Set the collection of categories among which the used can select a
     * subset.
     *
     * @param categories all available categories
     */
    public void setOptions(Collection<Classification> categories) {
        options.removeAllComponents();
        checkboxes.clear();
        categories.stream().map((category) -> {
            final CheckBox box = new CheckBox(category.getDescription());
            checkboxes.put(category, box);
            box.addValueChangeListener((com.vaadin.data.Property.ValueChangeEvent event) -> {
                if (!updatingField) {
                    Set<Classification> oldCategories = getValue();
                    Set<Classification> categories1;
                    if (oldCategories != null) {
                        categories1 = new HashSet<>(oldCategories);
                    } else {
                        categories1 = new HashSet<>();
                    }
                    if (box.getValue()) {
                        categories1.add(category);
                    } else {
                        removeValue(categories1, category);
                    }
                    setInternalValue(categories1);
                }
            });
            return box;
        }).forEach((box) -> {
            options.addComponent(box);
        });
    }

    @Override
    public Class getType() {
        return Set.class;
    }

    @Override
    protected void setInternalValue(Set<Classification> newValue) {
        classifications.clear();
        updatingField = true;
        super.setInternalValue(newValue);
        if (newValue != null) {
            classifications.addAll(newValue);
            for (Classification category : checkboxes.keySet()) {
                checkboxes.get(category).setValue(valueExists(newValue, category));
            }
        } else {
            for (Classification category : checkboxes.keySet()) {
                checkboxes.get(category).setValue(false);
            }
        }
        updatingField = false;
    }

    private boolean valueExists(Set<Classification> set, Classification value) {
        return set.stream().anyMatch((classification) -> (classification.getDescription().equals(value.getDescription())));
    }
    
    private void removeValue(Set<Classification> set, Classification value) {
        for (Classification classification : set) {
            if (classification.getDescription().equals(value.getDescription())) {
                set.remove(classification);
                return;
            }
        }        
    }

    @Override
    public Set<Classification> getInternalValue() {
        return classifications;
    }

    @Override
    public Set<Classification> getValue() {
        return getInternalValue();
    }
    
}
