package ua.mintmalory.mycontactbook.views.interfaces;

import com.google.android.gms.common.api.GoogleApiClient;

public interface IGoogleApiClientHolder {
    GoogleApiClient getGoogleApiClient();
    void onSignedOutListener();
}
