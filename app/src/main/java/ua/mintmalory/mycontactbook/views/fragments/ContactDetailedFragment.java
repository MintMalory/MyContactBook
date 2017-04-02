package ua.mintmalory.mycontactbook.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.mintmalory.mycontactbook.R;
import ua.mintmalory.mycontactbook.models.Contact;

import static ua.mintmalory.mycontactbook.views.adapters.ContactsAdapter.SINGLE_CONTACT;

public class ContactDetailedFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Contact contact = getContactInfo();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(getString(R.string.btn_ok_text), null);

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_detailed_contact, null);

        CircleImageView avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
        TextView name = (TextView) view.findViewById(R.id.user_info);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.container);


        if (contact != null) {
            name.setText(contact.getName());
            Picasso.with(getActivity().getApplicationContext()).load(contact.getAvatarURI()).error(R.drawable.user_place_holder).into(avatar);

            if (contact.getEmails() != null) {
                for (String email : contact.getEmails()) {
                    TextView mail = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_contact_info, null);
                    mail.setText(email);

                    container.addView(mail);
                }
            }

            if (contact.getPhonesNumbers() != null) {
                for (String phoneNumber : contact.getPhonesNumbers()) {
                    TextView phoneNumb = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_contact_info, null);
                    phoneNumb.setText(phoneNumber);

                    container.addView(phoneNumb);
                }
            }
        }

        builder.setView(view);
        return builder.create();
    }

    private Contact getContactInfo() {
        if (getArguments() != null && getArguments().getSerializable(SINGLE_CONTACT) != null) {
            return (Contact) getArguments().getSerializable(SINGLE_CONTACT);
        }

        return null;
    }
}