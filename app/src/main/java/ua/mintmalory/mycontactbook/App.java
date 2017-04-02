package ua.mintmalory.mycontactbook;

import android.app.Application;

import io.realm.Realm;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        instance = this;
    }

    public static App getInstance(){
        return instance;
    }

}
