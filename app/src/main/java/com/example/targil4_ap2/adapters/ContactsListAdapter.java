package com.example.targil4_ap2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.Contact;

import java.util.List;


public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder> {

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView dissplayName;
        private final TextView date;
        private final ImageView profileImg;

        private ContactViewHolder(View itemView) {
            super(itemView);
            dissplayName = itemView.findViewById(R.id.displayName);
            date = itemView.findViewById(R.id.date);
            profileImg = itemView.findViewById(R.id.profileImg);
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts;
    private OnContactClickListener contactClickListener;


    public ContactsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_layout, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            holder.dissplayName.setText(current.getUser().getDisplayName());
            holder.date.setText(current.getLastMessage().getCreated());
            String profilePicName = current.getUser().getProfilePic();
            try {
                byte[] decodedBytes = android.util.Base64.decode(profilePicName, android.util.Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                // Set the decodedBitmap to your ImageView
                holder.profileImg.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the error if decoding or setting the image fails
            }
            //TODO change to the user img
            //int resourceId = holder.itemView.getContext().getResources().getIdentifier("messi", "drawable", holder.itemView.getContext().getPackageName());
            //holder.profileImg.setImageResource(resourceId);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactClickListener != null) {
                        contactClickListener.onContactClick(current);
                    }
                }
            });

        }
    }

    public void setContacts(List<Contact> c) {
        contacts = c;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null)
            return contacts.size();
        else return 0;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        contactClickListener = listener;
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }
}
