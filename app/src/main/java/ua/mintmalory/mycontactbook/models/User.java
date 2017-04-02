package ua.mintmalory.mycontactbook.models;

import java.util.ArrayList;
import java.util.List;

import ua.mintmalory.mycontactbook.models.dao.RealmContact;
import ua.mintmalory.mycontactbook.models.dao.RealmUser;

public class User {
    private String email = "";
    private List<Contact> contacts = new ArrayList<>();

    public User() {
    }

    public User(RealmUser realmUser) {
        if (realmUser == null) {
            return;
        }

        this.email = realmUser.getEmail();
        Contact contact;
        for (RealmContact rc : realmUser.getRealmContacts()) {
            contact = new Contact();
            contact.setAvatarURI(rc.getAvatarURI());
            contact.setName(rc.getName());
            contact.setEmailsFromRealm(rc.getEmails());
            contact.setPhonesNumbersFromRealm(rc.getPhonesNumbers());

            contacts.add(contact);
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
    }
}
