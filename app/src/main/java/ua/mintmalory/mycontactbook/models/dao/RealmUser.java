package ua.mintmalory.mycontactbook.models.dao;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ua.mintmalory.mycontactbook.models.Contact;
import ua.mintmalory.mycontactbook.models.User;

public class RealmUser extends RealmObject {
    @PrimaryKey
    private String email = "";
    private RealmList<RealmContact> realmContacts = new RealmList<>();

    public RealmUser() {
    }

    public RealmUser(User user) {
        this.email = user.getEmail();
        RealmContact realmContact;
        for (Contact c : user.getContacts()) {
            realmContact = new RealmContact();
            realmContact.setAvatarURI(c.getAvatarURI());
            realmContact.setName(c.getName());
            realmContact.setEmails(c.getEmails());
            realmContact.setPhonesNumbers(c.getPhonesNumbers());

            realmContacts.add(realmContact);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<RealmContact> getRealmContacts() {
        return realmContacts;
    }

    public void setRealmContacts(List<RealmContact> realmContacts) {
        this.realmContacts.clear();
        this.realmContacts.addAll(realmContacts);
    }
}
