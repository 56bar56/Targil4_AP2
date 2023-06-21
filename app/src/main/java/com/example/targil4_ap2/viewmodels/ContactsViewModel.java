package com.example.targil4_ap2.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.targil4_ap2.items.Contact;

import java.util.List;

public class ContactsViewModel  extends ViewModel {
    //private ContactsRepository cRepository;
    private LiveData<List<Contact>> contacts;

    /*
    public ContactsViewModel(){
        cRepository = new ContactsRepository();
        contacts = cRepository.getAll;
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        cRepository.add(contact);
    }

    public void delete(Contact contact) {
        cRepository.delete(contact);
    }

    public void reload() {
        cRepository.reload();
    }

     */
}
