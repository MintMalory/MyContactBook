package ua.mintmalory.mycontactbook.presenters;

import java.util.Comparator;

import ua.mintmalory.mycontactbook.models.Contact;


public class ContactsDescendingComparator implements Comparator<Contact> {
    @Override
    public int compare(Contact contact, Contact t1) {
        if (t1 != null) {
            return t1.getName().compareTo(contact.getName());
        } else if (contact != null) {
            return -1;
        }

        return 0;
    }
}
