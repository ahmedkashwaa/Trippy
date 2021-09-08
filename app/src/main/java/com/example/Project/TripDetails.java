package com.example.Project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Project.helper.HelperMethods;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripDetails extends AppCompatActivity {
    EditText tripName,startPoint,endPoint;
    TextView textView_date;
    TextView textView_time;
    Button btnAdd;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    private int receivedID;
    private boolean openAsUpdate = false;

    Calendar mCalendar;
    Place place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        tripName = findViewById(R.id.trip_name);
        startPoint = findViewById(R.id.start_Point);
        endPoint = findViewById(R.id.end_Point);
        btnAdd = findViewById(R.id.btn_ADD);
        textView_date = findViewById(R.id.date_pick);
        textView_time = findViewById(R.id.time_picker);

        receivedID = getIntent().getIntExtra("id",-1);
        if (receivedID != -1){
            setTitle("Update Trip");
            tripName.setText(getIntent().getStringExtra("title"));
            startPoint.setText(getIntent().getStringExtra("startPoint"));
            endPoint.setText(getIntent().getStringExtra("endPoint"));
            textView_date.setText(getIntent().getStringExtra("date"));
            textView_time.setText(getIntent().getStringExtra("time"));

            Button update_btn = findViewById(R.id.btn_update);
            update_btn.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            openAsUpdate = true;
        } else {

            setTitle("Add Trip");
        }

        // intialize Places
      /* Places.initialize(getApplicationContext(),"AIzaSyDpxpdvO9vwD9Lw0KGq4gspzj7WrR2lUaE");
        PlacesClient placesClient = Places.createClient(this);
       startPoint.setFocusable(false);
        endPoint.setFocusable(false);
        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);
                // create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        ,fieldList).build(TripDetails.this);

                // start activity result
                startActivityForResult(intent,200);
            }
        });

        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);
                // create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        ,fieldList).build(TripDetails.this);
                // start activity result
                startActivityForResult(intent,100);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK){
            // when success
            // intialize place

             place = Autocomplete.getPlaceFromIntent(data);
            // set address on editText
            startPoint.setText(place.getAddress());

        }
        else if (requestCode==200){

            // set address on editText
                endPoint.setText(place.getAddress());

        }
        else if (resultCode== AutocompleteActivity.RESULT_ERROR){
           Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        } */
    }






    public void datePicker(View view) {
        showDataDialog(textView_date);

    }

    private void showDataDialog(TextView textView_date) {
      final   Calendar calendar =  Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy");

                textView_date.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        DatePickerDialog dialog=   new DatePickerDialog(TripDetails.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();

    }

    public void timePicker(View view) {
        showTimeDialog(textView_time);
    }

    private void showTimeDialog(TextView textView_time) {
        final Calendar calendar=Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                textView_time.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new TimePickerDialog(TripDetails.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();

    }


    public void UpdateNote(View view) {
        String title = tripName.getText().toString();
        String start = startPoint.getText().toString();
        String end = endPoint.getText().toString();
        String date = textView_date.getText().toString();
        String time = textView_time.getText().toString();
        if (title.isEmpty()){
            tripName.setError("Required Field");
        } else {
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("startPoint",start);
            values.put("endPoint",end);
            values.put("date",date);
            values.put("time",time);
            values.put("status","Upcoming");

            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String [] whereArgs = {String.valueOf(receivedID)};
            int id = db.update("TRIPS",values,"_id==?",whereArgs);


            Date datee = new Date();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(date+" "+time));// all done
                if(datee.before(cal.getTime())){
                    HelperMethods.startScheduling(TripDetails.this,date,time,title,start,end);
                }else {
                    Toast.makeText(TripDetails.this, "You have expired Trips please remove them", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (id != 0){

                Toast.makeText(this, "Trip Updated", Toast.LENGTH_SHORT).show();
                finish();
            }


        }
    }

    public void saveTrip(View view) {
        String title = tripName.getText().toString();
        String start = startPoint.getText().toString();
        String end = endPoint.getText().toString();
        String date = textView_date.getText().toString();
        String time = textView_time.getText().toString();
        if (title.isEmpty()) {
            tripName.setError("Required Field");
        } else if(start.isEmpty()){
            startPoint.setError("Required Field");
        } else if (end.isEmpty()){
            endPoint.setError("Required Field");
        }

        else if (date.isEmpty()){
            textView_date.setError("Required Field");
        }

        else if (time.isEmpty()){
            textView_time.setError("Required Field");
        }


        else {
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("startPoint",start);
            values.put("endPoint",end);
            values.put("date",date);
            values.put("time",time);
            values.put("status","Upcoming");

            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String [] whereArgs = {String.valueOf(receivedID)};
            long id=   db.insert("TRIPS", null,values);

            Date datee = new Date();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(date+" "+time));// all done
                if(datee.before(cal.getTime())){
                    HelperMethods.startScheduling(TripDetails.this,date,time,title,start,end);
                }else {
                    Toast.makeText(TripDetails.this, "You have expired Trips please remove them", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }




            if(id!= -1){
                Toast.makeText(this, "Trip Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




}