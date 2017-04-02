package ua.mintmalory.mycontactbook.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ua.mintmalory.mycontactbook.R;
import ua.mintmalory.mycontactbook.models.Contact;
import ua.mintmalory.mycontactbook.presenters.ContactBookPresenter;
import ua.mintmalory.mycontactbook.presenters.interfaces.IContactBookPresenter;
import ua.mintmalory.mycontactbook.views.adapters.ContactsAdapter;
import ua.mintmalory.mycontactbook.views.interfaces.IContactBookView;
import ua.mintmalory.mycontactbook.views.interfaces.IGoogleApiClientHolder;

import static ua.mintmalory.mycontactbook.views.MainActivity.SERVER_AUTH_CODE;
import static ua.mintmalory.mycontactbook.views.MainActivity.USER_EMAIL;

public class ContactBookFragment extends Fragment implements IContactBookView {
    @BindView(R.id.contacts_list)
    RecyclerView contactsRv;

    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;

    private ContactsAdapter adapter;
    private volatile String code = "";
    private volatile String email = "";
    private Unbinder unbinder;
    private IContactBookPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().getString(SERVER_AUTH_CODE) != null) {
                code = getArguments().getString(SERVER_AUTH_CODE);
            }

            if (getArguments().getString(USER_EMAIL) != null) {
                email = getArguments().getString(USER_EMAIL);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_book, container, false);
        unbinder = ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        contactsRv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        presenter = new ContactBookPresenter(this, code);

        presenter.selectPersonInfoFromDB(email);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

    }

    @Override
    public void refreshContacts(List<Contact> contacts) {
        if (contacts.size() == 0) {
            switchToLayout(R.id.no_data_screen);
            return;
        } else {
            switchToLayout(R.id.contacts_list);
        }


        if (adapter == null) {
            adapter = new ContactsAdapter(contacts, getActivity());
            contactsRv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter.resetItems(contacts);
        }


    }

    private void switchToLayout(long layoutId) {
        if (viewSwitcher.getCurrentView().getId() != layoutId) {
            viewSwitcher.showNext();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                presenter.sortContacts();
                break;
            case R.id.synchronize:
                presenter.downloadPersonInfoFromServer();
                break;
            case R.id.sign_out:
                if (getActivity() instanceof IGoogleApiClientHolder) {
                    GoogleApiClient googleApiClient = ((IGoogleApiClientHolder) getActivity()).getGoogleApiClient();

                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    ((IGoogleApiClientHolder) getActivity()).onSignedOutListener();
                                }
                            });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
