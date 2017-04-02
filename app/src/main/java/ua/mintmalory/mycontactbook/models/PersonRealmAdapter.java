package ua.mintmalory.mycontactbook.models;

import io.realm.Realm;
import ua.mintmalory.mycontactbook.models.dao.RealmUser;

public class PersonRealmAdapter {
    private Realm realm = Realm.getDefaultInstance();

    public void writeOrUpdateUser(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(new RealmUser(user));
            }
        });
    }

    public User getUser(String email) {
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("email", email).findFirst();
        return new User(realmUser);
    }
}
