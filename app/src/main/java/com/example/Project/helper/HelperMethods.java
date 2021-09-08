package com.example.Project.helper;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.WindowManager;

import com.example.Project.AlarmService;
import com.example.Project.DBHelper;
import com.example.Project.DBHistory;
import com.example.Project.FloatingWindow;
import com.example.Project.NonRemovableNotification;
import com.example.Project.TripModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HelperMethods {
    public static String ACTION_PENDING = "action";

    public static void startScheduling(Context context, String date, String time,String title,String start,String end) {
        int timeInSec = 2;

      Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra("title",title);
        intent.putExtra("start",start);
        intent.putExtra("end",end);
        intent.putExtra("date",date);
        intent.putExtra("time",time);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date+" "+time));// all done
             } catch (ParseException e) {
            e.printStackTrace();
             }

        PendingIntent pendingIntent = PendingIntent.getService(
                context.getApplicationContext(), (int)cal.getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

     //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
     //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 168*60*60*1000 , pendingIntent);  //set repeating every 168 hours
     //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 730*60*60*1000 , pendingIntent);  //set repeating every 730 hours

    }

    public interface OnButton{
        void onClicked();
    }
    public static void showAlertDialog(Context context, OnButton onButton,String title,String start,String end,String date,String time) {

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Reminder for your trip!!!")
                .setCancelable(false)
                .setMessage(title+"  Trip Time has come!")
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                      try {
                            // when google map installed
                            // intialize uri
                          Uri uri = Uri.parse("google.navigation:q=" + end);
                            // intialize intent with action view
                          Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                            // set package
                          mapIntent.setPackage("com.google.android.apps.maps");
                            //set flag
                          mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // start activity
                          context.startActivity(mapIntent);
                        } catch (ActivityNotFoundException e){
                            // when google map is not installed
                            // intialize uri
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US");
                            // intialize intent with action view
                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                            //set flags
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                        DBHelper helper = new DBHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String[] args = {title};
                        String [] whereArgs = {String.valueOf(title)};
                        Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE title=? ",whereArgs);
                        cursor.moveToNext();
                        String notes = cursor.getString(cursor.getColumnIndex("NOTES"));

                      ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("startPoint", start);
                        values.put("endPoint", end);
                        values.put("date", date);
                        values.put("time", time);
                        values.put("status", "Done!");
                        values.put("NOTES", notes);
                        DBHistory history = new DBHistory(context);
                        SQLiteDatabase db2 = history.getWritableDatabase();
                        long id2 = db2.insert("TRIPS", null, values);


                        // DELETE FROM TRIPS WHERE title == trip title
                        int deletedRows = db.delete("TRIPS", "title ==?", args);
                        Intent i = new Intent(context, FloatingWindow.class);
                        i.putExtra("notes",notes);
                        context.startService(i);
                        dialog.dismiss();
                        onButton.onClicked();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBHelper helper = new DBHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String[] args = {title};
                        String [] whereArgs = {String.valueOf(title)};
                        Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE title=? ",whereArgs);
                        cursor.moveToNext();
                        String notes = cursor.getString(cursor.getColumnIndex("NOTES"));

                        ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("startPoint", start);
                        values.put("endPoint", end);
                        values.put("date", date);
                        values.put("time", time);
                        values.put("status", "Canceled!");
                        values.put("NOTES", notes);
                        DBHistory history = new DBHistory(context);
                        SQLiteDatabase db2 = history.getWritableDatabase();
                        long id2 = db2.insert("TRIPS", null, values);

                        // DELETE FROM TRIPS WHERE title == trip title
                        int deletedRows = db.delete("TRIPS", "title ==?", args);

                        dialog.dismiss();
                        onButton.onClicked();
                    }
                }).setNeutralButton("Snooze", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        NonRemovableNotification.notification(context,title,start,end,date,time);
                        dialog.dismiss();
                        onButton.onClicked();
                    }
                }).create();
        alertDialog.getWindow().setType(LAYOUT_FLAG);
        alertDialog.show();
    }



}
