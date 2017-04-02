package ua.mintmalory.mycontactbook.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.people.v1.PeopleScopes;

import ua.mintmalory.mycontactbook.R;
import ua.mintmalory.mycontactbook.views.fragments.ContactBookFragment;
import ua.mintmalory.mycontactbook.views.fragments.SignInFragment;
import ua.mintmalory.mycontactbook.views.interfaces.IGoogleApiClientHolder;

public class MainActivity extends AppCompatActivity implements IGoogleApiClientHolder,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String SERVER_AUTH_CODE = "SERVER_AUTH_CODE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    private OptionalPendingResult<GoogleSignInResult> opr;
    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (opr.isDone()) {
            processSignInResult(opr.get());
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    processSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void initGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.client_id))
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleScopes.CONTACTS_READONLY))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            processSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }

    private void processSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            acct = result.getSignInAccount();

            ContactBookFragment f = new ContactBookFragment();
            Bundle b = new Bundle();
            b.putString(SERVER_AUTH_CODE, acct.getServerAuthCode());
            b.putString(USER_EMAIL, acct.getEmail());
            f.setArguments(b);
            showFragment(f);
        } else {
            showFragment(new SignInFragment());
        }
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showFragment(new SignInFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onSignedOutListener() {
        showFragment(new SignInFragment());
    }
}
