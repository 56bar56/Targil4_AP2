package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.targil4_ap2.adapters.ContactsListAdapter;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageLast;
import com.example.targil4_ap2.items.UserToGet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class contacts_pageActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_page);

        RecyclerView lstContacts = findViewById(R.id.lstContacts);
        final ContactsListAdapter adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));

        List<Contact> contacts = new ArrayList<>();
        UserToGet u1 = new UserToGet("ariel", "osCAR", "1");
        MessageLast m1 = new MessageLast("1", "12/06/23", "hi");
        contacts.add(new Contact("1", u1, m1));
        UserToGet u2 = new UserToGet("Bar", "Barben", "1");
        MessageLast m2 = new MessageLast("2", "18/06/23", "hi");
        contacts.add(new Contact("2", u2, m2));
        UserToGet u3 = new UserToGet("Ofek", "Ofekul", "1");
        MessageLast m3 = new MessageLast("1", "21/06/23", "hi");
        contacts.add(new Contact("3", u3, m3));
        UserToGet u4 = new UserToGet("Maya", "maya", "1");
        MessageLast m4 = new MessageLast("1", "16/04/23", "hi");
        contacts.add(new Contact("4", u4, m4));
        UserToGet u5 = new UserToGet("Shoam", "Shoameni", "1");
        MessageLast m5 = new MessageLast("1", "03/06/23", "hi");
        contacts.add(new Contact("5", u5, m5));
        UserToGet u6 = new UserToGet("Tohar", "Tohar", "1");
        MessageLast m6 = new MessageLast("1", "04/03/23", "hi");
        contacts.add(new Contact("6", u6, m6));
        adapter.setContacts(contacts);

        adapter.setOnContactClickListener(new ContactsListAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                // Handle the click event for the contact item
                // Start the chat activity
                Intent intent = new Intent(getApplicationContext(), chatActivity.class);

                // Pass any necessary data to the chat activity using intent extras
                intent.putExtra("contactId", contact.getId());
                intent.putExtra("contactName", contact.getUser().getDisplayName());
                intent.putExtra("contactImg", contact.getUser().getProfilePic());
                startActivity(intent);
            }
        });

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddCon = new Intent(getApplicationContext(), ChooseConNameActivity.class);
                //intent.putExtra("username", contact.getId());
                //intent.putExtra("password", contact.getId());

                startActivity(intentAddCon);
            }
        });

        btnLogout= findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}