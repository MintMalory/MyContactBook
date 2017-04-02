package ua.mintmalory.mycontactbook.views.interfaces;

import java.util.List;

import ua.mintmalory.mycontactbook.models.Contact;


public interface IContactBookView {
    void refreshContacts(List<Contact> Contacts);
}
