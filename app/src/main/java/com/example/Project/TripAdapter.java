package com.example.Project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Project.R;
import com.example.Project.helper.HelperMethods;



import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.content.Context.ALARM_SERVICE;
import static com.example.Project.R.color.*;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private Activity activity;
    private ArrayList<TripModel> trips;
    private int receivedID;
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    public TripAdapter(Activity activity, ArrayList<TripModel> trips) {
        this.activity = activity;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.trip_list,parent,false);
        TripViewHolder holder =new TripViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.TripViewHolder holder, int position) {
        Date date = new Date();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        currenttime = simpleDateFormat.format(calendar.getTime());
        DBHelper helper = new DBHelper(activity);
        SQLiteDatabase db = helper.getReadableDatabase();
        String [] whereArgs = {String.valueOf(trips.get(position).getId())};

        try {
            Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE _id=? ",whereArgs);
            cursor.moveToNext();
            String notes = cursor.getString(cursor.getColumnIndex("NOTES"));
            trips.get(position).setNotes(notes);
        } catch (Exception e){
            trips.get(position).setNotes("");
        }



        holder.startPoint.setText("From : "+trips.get(position).getStartPoint());
        holder.endPoint.setText("To : "+trips.get(position).getEndPoint());
        holder.tripTitle.setText(trips.get(position).getTitle());
        holder.date.setText(trips.get(position).getDate());
        holder.time.setText(trips.get(position).getTime());
        trips.get(position).setStatus("Upcoming");
        holder.status.setText(trips.get(position).getStatus());
      Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(trips.get(position).getDate()+" "+trips.get(position).getTime()));// all done
            if(date.before(cal.getTime())){

                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.quantum_googgreen200));
            }else {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.quantum_vanillared100));
                holder.status.setText("Delayed");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

     //
        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  DBHelper helper = new DBHelper(activity);
                SQLiteDatabase db = helper.getReadableDatabase();
                String [] whereArgs = {String.valueOf(trips.get(position).getId())};
                Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE _id=? ",whereArgs);
                    cursor.moveToNext();
                    String notes = cursor.getString(cursor.getColumnIndex("NOTES")); */

                PopupMenu pop = new PopupMenu(activity,v);
                pop.inflate(R.menu.notes_menu);
                try {
                    String [] a = trips.get(position).getNotes().split(",");
                    for (int i = 0; i < a.length; i++) {

                        pop.getMenu().add(a[i]);
                        pop.show();
                    }
                }catch (Exception e ){
                    Toast.makeText(activity, "There are no Notes", Toast.LENGTH_SHORT).show();
                }



            }
        });

        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.popup);
                popupMenu.inflate(R.menu.menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if(item.getItemId()==R.id.note){
                            Intent i = new Intent(activity,AddNoteActivity.class);
                            i.putExtra("id", trips.get(position).getId());
                            i.putExtra("title", trips.get(position).getTitle());
                            i.putExtra("startPoint", trips.get(position).getStartPoint());
                            i.putExtra("endPoint", trips.get(position).getEndPoint());
                            i.putExtra("date", trips.get(position).getDate());
                            i.putExtra("time", trips.get(position).getTime());
                            activity.startActivity(i);

                        }


                        if (item.getItemId() == R.id.cancel) {
                            DBHelper helper = new DBHelper(activity);
                            SQLiteDatabase db = helper.getReadableDatabase();
                            String [] whereArgs = {String.valueOf(trips.get(position).getId())};
                            Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE _id=? ",whereArgs);
                            cursor.moveToNext();
                            String notes = cursor.getString(cursor.getColumnIndex("NOTES"));
                            Toast.makeText(activity, "Canceled Trip!", Toast.LENGTH_LONG).show();
                            trips.get(position).setStatus("Canceled!");
                            //  holder.status.setText(trips.get(position).getStatus());
                            holder.status.setText("Canceled!");
                            ContentValues values = new ContentValues();
                            values.put("title", trips.get(position).getTitle());
                            values.put("startPoint", trips.get(position).getStartPoint());
                            values.put("endPoint", trips.get(position).getEndPoint());
                            values.put("date", trips.get(position).getDate());
                            values.put("time", trips.get(position).getTime());
                            values.put("status", "Canceled!");
                            values.put("NOTES", notes);
                            DBHistory history = new DBHistory(activity);
                            SQLiteDatabase db2 = history.getWritableDatabase();
                            long id2 = db2.insert("TRIPS", null, values);

                          //  DBHelper helper = new DBHelper(activity);
                          //  SQLiteDatabase db = helper.getWritableDatabase();
                            String[] args = {trips.get(position).getId() + ""};

                            // DELETE FROM TRIPS WHERE _id == trips.get(position).getID()
                            int deletedRows = db.delete("TRIPS", "_id ==?", args);

                            Intent intent = new Intent(activity, AlarmService.class);
                            PendingIntent pendingIntent = PendingIntent.getService(
                                    activity, (int)cal.getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
                            pendingIntent.cancel();
                          reload();
                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });


        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper helper = new DBHelper(activity);
                SQLiteDatabase db = helper.getReadableDatabase();
                String [] whereArgs = {String.valueOf(trips.get(position).getId())};
                Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE _id=? ",whereArgs);
                cursor.moveToNext();
                String notes = cursor.getString(cursor.getColumnIndex("NOTES"));



                trips.get(position).setStatus("Done!");
                //   holder.status.setText(trips.get(position).getStatus());
                holder.status.setText("Done!");
                ContentValues values = new ContentValues();
                values.put("title", trips.get(position).getTitle());
                values.put("startPoint", trips.get(position).getStartPoint());
                values.put("endPoint", trips.get(position).getEndPoint());
                values.put("date", trips.get(position).getDate());
                values.put("time", trips.get(position).getTime());
                values.put("status", "Done!");
                values.put("NOTES", notes);
                DBHistory history = new DBHistory(activity);
                SQLiteDatabase db2 = history.getWritableDatabase();
                long id2 = db2.insert("TRIPS", null, values);

              //  DBHelper helper = new DBHelper(activity);
             //   SQLiteDatabase db = helper.getWritableDatabase();
                String[] args = {trips.get(position).getId() + ""};

                // DELETE FROM TRIPS WHERE _id == trips.get(position).getID()
                int deletedRows = db.delete("TRIPS", "_id ==?", args);
                notifyDataSetChanged();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + trips.get(position).getEndPoint());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
                Intent i = new Intent(activity,FloatingWindow.class);
                i.putExtra("notes",notes);
                Intent intent = new Intent(activity, AlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(
                        activity, (int)cal.getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
                pendingIntent.cancel();
                activity.startService(i);

            //    DisplayTrack(trips.get(position).getStartPoint(), trips.get(position).getEndPoint());

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, TripDetails.class);
                i.putExtra("id", trips.get(position).getId());
                i.putExtra("title", trips.get(position).getTitle());
                i.putExtra("startPoint", trips.get(position).getStartPoint());
                i.putExtra("endPoint", trips.get(position).getEndPoint());
                i.putExtra("date", trips.get(position).getDate());
                i.putExtra("time", trips.get(position).getTime());
                activity.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView date,time,tripTitle,startPoint,endPoint,status;
        private final Button start,popup;
        private final CardView cardView;
        RecyclerView recyclerView;
        ImageView notes;



        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            date= itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            tripTitle =itemView.findViewById(R.id.tv_title);
            startPoint = itemView.findViewById(R.id.tv_startPoint);
            endPoint = itemView.findViewById(R.id.tv_endPoint);
            start = itemView.findViewById(R.id.startTrip);
            cardView = itemView.findViewById(R.id.card_view);
            status = itemView.findViewById(R.id.status);
            popup = itemView.findViewById(R.id.pop_menu_id);
            recyclerView=itemView.findViewById(R.id.recycler_view);
            notes=itemView.findViewById(R.id.show);
        }


    }

    /* private void DisplayTrack(String start, String end) {
        // if mobile doesn't have map installed , redirect to play store
        try {
            // when google map installed
            // intialize uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+start+"/"+end);
            // intialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            // set package
            intent.setPackage("com.google.android.apps.maps");
            //set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // start activity
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e){
            // when google map is not installed
            // intialize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US");
            // intialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //set flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
        


    }  */

public void reload(){

        trips.clear();
    DBHelper helper = new DBHelper(activity);
    SQLiteDatabase db = helper.getWritableDatabase();
    Cursor cursor= db.rawQuery("SELECT * FROM TRIPS ",null);
    while(cursor.moveToNext()) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String start = cursor.getString(2);
        String end = cursor.getString(3);
        String date = cursor.getString(4);
        String time = cursor.getString(5);
        String status = cursor.getString(6);

        trips.add(new TripModel(id, date, time, title, start, end, status));

        notifyDataSetChanged();
    }
    View view = activity.findViewById(R.id.layout_no_trips);
    if(trips.size()==0) {
        view.setVisibility(View.VISIBLE);
    }

    else {
        view.setVisibility(View.INVISIBLE);
    }



}




}
