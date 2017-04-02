package ua.mintmalory.mycontactbook.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.mintmalory.mycontactbook.models.dao.RealmString;


public class Contact implements Serializable {
    private String name = "";
    private String avatarURI = "";
    private List<String> phonesNumbers = new ArrayList<>();
    private List<String> emails = new ArrayList<>();

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

    public List<String> getPhonesNumbers() {
        return phonesNumbers;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setPhonesNumbers(List<String> phonesNumbers) {
        this.phonesNumbers = new ArrayList<>();
        this.phonesNumbers.addAll(phonesNumbers);
    }

    public void setEmails(List<String> emails) {
        this.emails = new ArrayList<>();
        this.emails.addAll(emails);

    }

    public void setPhonesNumbersFromRealm(List<RealmString> phonesNumbers) {
        this.phonesNumbers.clear();

        for (RealmString s : phonesNumbers) {
            this.emails.add(s.getValue());
        }
    }

    public void setEmailsFromRealm(List<RealmString> emails) {
        this.emails.clear();

        for (RealmString s : emails) {
            this.emails.add(s.getValue());
        }
    }

    public void addEmail(String newEmail) {
        emails.add(newEmail);
    }

    public void addPhoneNumber(String newPhoneNumber) {
        phonesNumbers.add(newPhoneNumber);
    }
}

