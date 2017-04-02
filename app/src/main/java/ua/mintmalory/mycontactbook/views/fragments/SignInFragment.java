package ua.mintmalory.mycontactbook.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ua.mintmalory.mycontactbook.R;
import ua.mintmalory.mycontactbook.views.interfaces.IGoogleApiClientHolder;

import static ua.mintmalory.mycontactbook.views.MainActivity.RC_SIGN_IN;

public class SignInFragment extends Fragment {
    @BindView(R.id.sign_in_btn)
    SignInButton signInButton;

    private Unbinder unbinder;

    @OnClick(R.id.sign_in_btn)
    public void onSignInBtnClicked() {
        if (getActivity() instanceof IGoogleApiClientHolder) {
            GoogleApiClient googleApiClient = ((IGoogleApiClientHolder) getActivity()).getGoogleApiClient();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sing_in, container, false);
        unbinder = ButterKnife.bind(this, v);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
