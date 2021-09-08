package com.example.Project;


import static com.example.Project.TripAdapter.*;
import static com.example.Project.helper.HelperMethods.showAlertDialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Project.helper.HelperMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlarmService extends Service {
    private MediaPlayer mp;



    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    if ((mp.isPlaying())) {
          mp.stop();

     }
       mp.release();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String title = (String)intent.getExtras().get("title");
        String start = (String)intent.getExtras().get("start");
        String end = (String)intent.getExtras().get("end");
        String date = (String)intent.getExtras().get("date");
        String time = (String)intent.getExtras().get("time");
        mp = MediaPlayer.create(AlarmService.this.getApplicationContext(), R.raw.loudalarm);
        mp.start();
        showAlertDialog(AlarmService.this, AlarmService.this::stopSelf, title, start, end, date, time);


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopService(intent);
        return super.onUnbind(intent);
    }


}
