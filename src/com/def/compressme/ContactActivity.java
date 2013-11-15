/*******************************************************************************
 * Copyright (c) [2013], [Serdyuk Evgen]
 * 
 *  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * Neither the name of the {organization} nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.def.compressme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.def.compressme.adapter.ListViewAdapter;
import com.def.compressme.model.Contact;

public class ContactActivity extends Activity {

    private ListView list;
    private ListViewAdapter adapter;
    private EditText editSearch;
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private HashMap<String, String> contactsMap;
    private final int ADDRESS = 2;
    private final String SMS_SEND = "content://sms/sent/";
    private final String SMS_INBOX = "content://sms/inbox/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_main);
        list = (ListView) findViewById(R.id.listview);
        contactsMap = new HashMap<String, String>();
        readContacts();
          
        contacts.add(new Contact("+", ""));
        for(Entry<String, String> entry : contactsMap.entrySet()){
            Contact contact = new Contact(entry.getValue(), entry.getKey());
            contacts.add(contact);
        }

        contacts = mySort(contacts);
        
        adapter = new ListViewAdapter(this, contacts);
        list.setAdapter(adapter);
        findWord();

    }

    private void findWord() {
        editSearch = (EditText) findViewById(R.id.search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                    int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
            }
        });
    }

    public void readContacts() {

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor
                        .getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer
                        .parseInt(cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[] { id }, null);
                    while (phoneCursor.moveToNext()) {
                        int phoneType = phoneCursor
                                .getInt(phoneCursor
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNumber = phoneCursor
                                .getString(phoneCursor
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        switch (phoneType) {
                        case Phone.TYPE_MOBILE:
                            contactsMap.put(getOriginalNumber(phoneNumber), name + " (Mobile)");
                            break;
                        case Phone.TYPE_HOME:
                            contactsMap.put(getOriginalNumber(phoneNumber), name + " (Home)");
                            break;
                        case Phone.TYPE_WORK:
                            contactsMap.put(getOriginalNumber(phoneNumber), name + " (Work)");
                            break;
                        case Phone.TYPE_OTHER:
                            contactsMap.put(getOriginalNumber(phoneNumber), name + " (Other)");
                            break;
                        default:
                            break;
                        }
                    }
                    phoneCursor.close();
                }
            }
        }

        readSMS(SMS_INBOX);
        readSMS(SMS_SEND);
    }

    public static String getOriginalNumber(String number) {
        if (number.length() >= 10)
            return number.substring(number.length() - 10, number.length());
        return number;
    }
    
    
    private void readSMS(String smsPath) {
        Cursor cursor = getContentResolver().query(Uri.parse(smsPath), null,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(ADDRESS);
                if (number != null) {
                    number = getOriginalNumber(cursor.getString(ADDRESS));
                    if (contactsMap.get(number) == null) {
                        contactsMap.put(number, number);
                    }
                }

            } while (cursor.moveToNext());
        }
    }

    private ArrayList<Contact> mySort(ArrayList<Contact> arrayContact){
        Collections.sort(contacts, Contact.contactNameComparator);
        ArrayList<Contact> result = new ArrayList<Contact>();
        result.add(arrayContact.get(0));
        arrayContact.remove(0);
        for(Contact contact:arrayContact){
            if(isWord(contact.getName().toCharArray()[0]))
                result.add(contact);
        }
        for(Contact contact:arrayContact){
            if(!isWord(contact.getName().toCharArray()[0]))
                result.add(contact);
        }
        return result;
    }
    
    private boolean isWord(char symbol) {
        return (symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') 
            || (symbol >= 'à' && symbol <= 'ÿ') || (symbol >= 'À' && symbol <= 'ß') ;
    }
}
