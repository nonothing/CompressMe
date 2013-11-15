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


import static com.def.compressme.MessageActivity.SMS_INBOX;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.def.compressme.model.Chiper;

public class SmsService extends Service {
   private Context context;
   private String body;
   private String date;
   private String address;
   
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        body = intent.getExtras().getString("body");
        date =  intent.getExtras().getString("date");
        address = intent.getExtras().getString("address");
        new Thread(updateMessage).start();
        return START_STICKY;
    }
    
    private void showNotification(String message, String address) {
        Intent notificationIntent = new Intent(context, MessageActivity.class);
        notificationIntent.putExtra("number", address);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.small_icon)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentText(message)
                .setContentIntent(
                        PendingIntent.getActivity(context, 0,
                                notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("CompressMe")
                .setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Notification notification = nb.getNotification();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }
    
    public static void restoreSms(Context context,String address,String body, String date, String smsPath) {
        ContentValues values = new ContentValues();
        values.put("address", address);//sender name
        values.put("date", date);
        values.put("body", body);
        context.getContentResolver().insert(Uri.parse(smsPath), values);
    }
    
    
    
    
    Runnable updateMessage = new Runnable() {
        public void run() {
            Log.v("HANDLE", "update");
            body = check(body);
            showNotification(body,address);
            restoreSms(context,address, body, date,SMS_INBOX);
        }
      };
    
    private String check(String body) {
        if(body.charAt(0) == '&'){
            return new Chiper().translateToRus(body);
        }
        return body;
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
