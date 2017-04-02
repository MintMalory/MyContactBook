package ua.mintmalory.mycontactbook.presenters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.mintmalory.mycontactbook.models.Contact;
import ua.mintmalory.mycontactbook.models.PeopleApiDownloader;
import ua.mintmalory.mycontactbook.models.PersonRealmAdapter;
import ua.mintmalory.mycontactbook.models.User;
import ua.mintmalory.mycontactbook.presenters.interfaces.IContactBookPresenter;
import ua.mintmalory.mycontactbook.views.interfaces.IContactBookView;

public class ContactBookPresenter implements IContactBookPresenter,
        PeopleApiDownloader.OnPeopleDownloadedListener {
    private PeopleApiDownloader peopleApiDownloader;
    private PersonRealmAdapter personRealmAdapter;
    private IContactBookView view;
    private List<Contact> connections = new ArrayList<>();
    private String email = "";
    private boolean sortedAscending = false;

    public ContactBookPresenter(IContactBookView view, String code) {
        personRealmAdapter = new PersonRealmAdapter();
        this.view = view;
        peopleApiDownloader = new PeopleApiDownloader(code, this);
    }

    @Override
    public void downloadPersonInfoFromServer() {
        peopleApiDownloader.downloadContacts();
    }

    @Override
    public void selectPersonInfoFromDB(String email) {
        User user = personRealmAdapter.getUser(email);
        this.email = email;
        connections.clear();
        if (user != null) {
            view.refreshContacts(user.getContacts());
            connections.addAll(user.getContacts());
        } else {
            view.refreshContacts(new ArrayList<Contact>());
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void sortContacts() {
        if (sortedAscending) {
            Collections.sort(connections, new ContactsDescendingComparator());
            sortedAscending = false;
        } else {
            Collections.sort(connections, new ContactsAscendingComparator());
            sortedAscending = true;
        }

        view.refreshContacts(connections);
    }

    @Override
    public void onDownloaded(List<Contact> contacts) {
        User user = new User();
        user.setEmail(email);
        user.setContacts(contacts);

        connections.clear();
        connections.addAll(contacts);

        personRealmAdapter.writeOrUpdateUser(user);

        if (ContactBookPresenter.this.view != null) {
            ContactBookPresenter.this.view.refreshContacts(contacts);
        }
    }
}
