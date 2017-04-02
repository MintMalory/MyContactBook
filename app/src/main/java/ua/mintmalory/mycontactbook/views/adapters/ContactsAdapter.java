package ua.mintmalory.mycontactbook.views.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ua.mintmalory.mycontactbook.R;
import ua.mintmalory.mycontactbook.models.Contact;
import ua.mintmalory.mycontactbook.views.fragments.ContactDetailedFragment;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    public static final String SINGLE_CONTACT = "contact";
    private List<Contact> contacts;
    private FragmentActivity hostActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container)
        ViewGroup container;

        @BindView(R.id.user_avatar)
        CircleImageView avatar;

        @BindView(R.id.user_info)
        TextView name;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ContactsAdapter(List<Contact> contacts, FragmentActivity hostActivity) {
        this.contacts = contacts;
        this.hostActivity = hostActivity;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact person = contacts.get(position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactDetailedFragment dialog = new ContactDetailedFragment();
                Bundle b = new Bundle();
                b.putSerializable(SINGLE_CONTACT, person);
                dialog.setArguments(b);

                dialog.show(hostActivity.getSupportFragmentManager(), dialog.getTag());
            }
        });

        if (person != null) {
            holder.name.setText(person.getName());
            Picasso.with(hostActivity).load(person.getAvatarURI()).error(R.drawable.user_place_holder).into(holder.avatar);
        }
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void resetItems(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }
}