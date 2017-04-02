package ua.mintmalory.mycontactbook.presenters.interfaces;

public interface IContactBookPresenter {
    void downloadPersonInfoFromServer();
    void selectPersonInfoFromDB(String email);
    void sortContacts();
    void onDestroy();
}
