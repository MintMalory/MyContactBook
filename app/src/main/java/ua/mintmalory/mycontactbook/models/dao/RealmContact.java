package ua.mintmalory.mycontactbook.models.dao;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by mintmalory on 02.04.17.
 */

public class RealmContact extends RealmObject implements Serializable {
    private String name = "";
    private String avatarURI = "";
    private RealmList<RealmString> phonesNumbers = new RealmList<>();
    private RealmList<RealmString> emails = new RealmList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarURI() {
        return avatarURI;
    }

    public void setAvatarURI(String avatarURI) {
        this.avatarURI = avatarURI;
    }

    public List<RealmString> getPhonesNumbers() {
        return phonesNumbers;
    }

    public void setPhonesNumbers(List<String> phonesNumbers) {
        this.phonesNumbers.clear();

        for (String s : phonesNumbers) {
            this.phonesNumbers.add(new RealmString(s));
        }
    }

    public RealmList<RealmString> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails.clear();

        for (String s : emails) {
            this.emails.add(new RealmString(s));
        }
    }

    public void addEmail(RealmString newEmail) {
        emails.add(newEmail);
    }

    public void addPhoneNumber(RealmString newPhoneNumber) {
        phonesNumbers.add(newPhoneNumber);
    }
}
