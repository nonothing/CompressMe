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
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.def.compressme.R;
import com.def.compressme.adapter.AwesomeAdapter;
import com.def.compressme.model.Chiper;
import com.def.compressme.model.Message;

import static com.def.compressme.SmsService.restoreSms;
import static com.def.compressme.ContactActivity.getOriginalNumber;

public class MessageActivity extends ListActivity {
    public final static String SMS_INBOX = "content://sms/inbox/";
    private final String SMS_SEND = "content://sms/sent/";
    private final String SMS = "content://sms/";
    private final int ADRESS = 2;
    private final int DATE = 4;
    private final int BODY = 11;
    private final int COUNT_ONE = 160;
    private final int COUNT_TWO = 306;
    private ArrayList<Message> messages;
    private AwesomeAdapter adapter;
    private EditText text;
    private TextView textView;
    private String number = null;
    private Chiper chiper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        chiper = new Chiper();
        text = (EditText) this.findViewById(R.id.text);
        textView = (TextView) this.findViewById(R.id.textView1);
        messages = new ArrayList<Message>();
        onNewIntent(getIntent());
        changeOriginal();
        readSMS(SMS_SEND);
        readSMS(SMS_INBOX);

        Collections.sort(messages, Message.messageDateComparator);
        adapter = new AwesomeAdapter(this, messages);
        setListAdapter(adapter);
        
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                countSymbols();
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

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("number") != null) {
                number = extras.getString("number");
            }
        }
    }


    private void changeOriginal() {
        Cursor cursor = getContentResolver().query(Uri.parse(SMS_INBOX), null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(BODY) != null) {
                    if (cursor.getString(BODY).charAt(0) == '&') {
                        long id = cursor.getLong(0);
                        ContentValues values = new ContentValues();
                        values.put("_id", id);
                        values.put("thread_id", cursor.getLong(1));
                        values.put("address", cursor.getString(ADRESS));
                        values.put("person", cursor.getString(3));
                        values.put("date", cursor.getString(DATE));
                        values.put("protocol", cursor.getString(5));
                        values.put("read", cursor.getString(6));
                        values.put("status", cursor.getString(7));
                        values.put("type", cursor.getString(8));
                        values.put("reply_path_present", cursor.getString(9));
                        values.put("subject", cursor.getString(10));
                        values.put("body", new Chiper().translateToRus(cursor
                                .getString(BODY)));
                        values.put("service_center", cursor.getString(12));
                        getContentResolver().delete(
                                Uri.parse(SMS + id), null, null);
                        getContentResolver().insert(
                                Uri.parse(SMS_INBOX), values);
                    }
                }

            } while (cursor.moveToNext());
        }
    }
    
    private void readSMS(String adress) {
        boolean isMe = false;
        if (adress == SMS_SEND) {
            isMe = true;
        }
        Cursor cursor = getContentResolver().query(Uri.parse(adress), null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                    if (cursor.getString(ADRESS) != null) {
                        if (getOriginalNumber(cursor.getString(ADRESS)).equals(number)) {
                            messages.add(new Message(cursor.getString(BODY),
                                    isMe, cursor.getString(DATE)));
                        }
                    }
            } while (cursor.moveToNext());
        }

    }
    
    public void sendMessage(View view) {
        String newMessage = text.getText().toString().trim();
        if (newMessage.length() > 0) {
            if(number.length() == 0){
                showDialog(newMessage);
            }else{
                sendSMS(number,newMessage);
            }
        }
    }

    private void sendSMS(String number, String textMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null,
                    new Chiper().translateToTranslit(textMessage), null, null);
            restoreSms(this,number, textMessage, String.valueOf(new Date().getTime()),SMS_SEND);
            text.setText("");
            addNewMessage(new Message(textMessage, true,
                    String.valueOf(new Date().getTime())));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    private void showDialog(final String textMessage) {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¬ведите номер").setView(input)
                                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        number = input.getText().toString().trim();
                        sendSMS(number,textMessage);
                    }
                }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do something if Cancel
                            }
                        });
        builder.create().show();
    }

    void addNewMessage(Message message) {
        messages.add(message);
        adapter.notifyDataSetChanged();
        getListView().setSelection(messages.size() - 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    protected void countSymbols(){
        int result=0;
        String str = text.getText().toString().trim();
        int lenghtTranslate = chiper.lenghtTranslate(str);
        if(lenghtTranslate < COUNT_TWO){
            result = COUNT_TWO - lenghtTranslate;
            textView.setText(result+"/2" );
            }
        if(lenghtTranslate  < COUNT_ONE){
            result = COUNT_ONE - lenghtTranslate;
            textView.setText(result+"/1" );
            }
    }
}
