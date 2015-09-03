package com.github.moscaville.contactsdb.main;


import com.github.moscaville.contactsdb.dto.Contact;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the contact editor form and the data source, including
 * fetching and saving contacts.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class ContactLogic {

    protected ContactView view;

    public ContactLogic(ContactView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editContact(null);
        // Hide and disable if not admin
//        if (!ContactDbUI.get().getAccessControl().isUserInRole("admin")) {
//            view.setNewContactEnabled(false);
//        }

        refreshTable();
    }

    public void cancelContact() {
        setFragmentParameter("");
        //view.clearSelection();
        //view.editContact(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String contactId) {
        String fragmentParameter;
        if (contactId == null || contactId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = contactId;
        }

//        Page page = ContactDbUI.get().getPage();
//        page.setUriFragment("!" + ContactView.VIEW_NAME + "/"
//                + fragmentParameter, false);
    }

//    public void enter(String contactId) {
//        if (contactId != null && !contactId.isEmpty()) {
//            if (contactId.equals("new")) {
//                newContact();
//            } else {
//                // Ensure this is selected even if coming directly here from
//                // login
//                try {
//                    int pid = Integer.parseInt(contactId);
//                    Contact contact = findContact(pid);
//                    view.selectRow(contact);
//                } catch (NumberFormatException e) {
//                }
//            }
//        }
//    }

//    private Contact findContact(int contactId) {
//        return DataService.get().getContactById(contactId);
//    }

    public void saveContact(Contact contact) {
        view.showSaveNotification(contact.getLastName() + " updated");
        //view.clearSelection();
        //view.editContact(null);
        refreshTable();
        setFragmentParameter("");
    }

//    public void deleteProduct(Contact contact) {
//        DataService.get().deleteContact(contact.getId());
//        view.showSaveNotification(contact.getProductName() + " ("
//                + contact.getId() + ") removed");
//
//        view.clearSelection();
//        view.editContact(null);
//        refreshTable();
//        setFragmentParameter("");
//    }

    public void editContact(Contact contact) {
        if (contact == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(contact.getId() + "");
        }
        //view.editContact(contact);
    }

    private void refreshTable() {
        //Contact oldSelection = view.getSelectedRow();
//        view.showContacts(DataService.get().getAllContacts());
        //view.selectRow(oldSelection);
    }

    public void newContact() {
        //view.clearSelection();
        setFragmentParameter("new");
        view.editContact(new Contact());
    }

    public void rowSelected(Contact contact) {
//        if (ContactDbUI.get().getAccessControl().isUserInRole("admin")) {
//            view.editContact(contact);
//        }
    }
}
