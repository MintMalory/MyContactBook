package ua.mintmalory.mycontactbook.presenters;

import java.util.Comparator;

import ua.mintmalory.mycontactbook.models.Contact;


class ContactsAscendingComparator implements Comparator<Contact> {
    @Override
    public int compare(Contact contact, Contact t1) {
        if (contact != null) {
            return contact.getName().compareTo(t1.getName());
        } else if (t1 != null) {
            return -1;
        }

        return 0;
    }
}
