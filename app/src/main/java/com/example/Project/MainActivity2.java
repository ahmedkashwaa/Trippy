package com.example.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.Project.helper.HelperMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private ArrayList<TripModel> trips = new ArrayList<>();
    private TripAdapter adapter;
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference ;
    FirebaseDatabase firebaseDatabase;
    boolean s ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth= FirebaseAuth.getInstance();
        setTitle("Trippy");

        s = getIntent().getBooleanExtra("First",true);
      boolean  z = getIntent().getBooleanExtra("two",true);

      if(s==true){   // s= true it means that the user first time to log in , no data in SQLite
         if(z==true){  // z == false means the user entered the add note activity (to fix duplicate reading from firebase bug)
             loadData2();
             loadData();// loads data from Firebase and insert it into SQLite
         }
      }

        if (Build.VERSION.SDK_INT >= 23) {   // asks for permissions
            if (!Settings.canDrawOverlays(MainActivity2.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + MainActivity2.this.getPackageName()));
                startActivityForResult(intent, 1234);
            }
            }

        new Handler().postDelayed(new Runnable() {  // delay until the data be loaded from firebase into the SQL then getTrips
            @Override
            public void run() {
                getTrips();
            }
        }, 1200);


    }

    public void openAddTrip(View view) {   // floating button for add trips onClick action
        Intent intent = new Intent(MainActivity2.this,TripDetails.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    if(item.getItemId()==R.id.Log_out){  // when the user logs out all data in SQL will be deleted with the method logout();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder

                .setMessage("Would you like to sync data before you logout?")
                .setPositiveButton("OK", (dialog, which) -> {
                         sync();
                         syncHistory();
                         logout();

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    logout();
                })
                // avoid problem by clicking in any place outside the dialog or back button
                .setCancelable(false)
                .show();

        }
    else if (item.getItemId()==R.id.history)
          {
        Intent i = new Intent(MainActivity2.this,history.class);
        startActivity(i);

         }
    else if(item.getItemId()==R.id.sync)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Your Data Will be Synced now")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    sync();
                    syncHistory();
                })
                .setCancelable(false)
                .show();

    }
    else if(item.getItemId()==R.id.profile)
    {
     Toast.makeText(MainActivity2.this, "Email: " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getTrips();     //reading data
    }


    private void getTrips(){
        // to fix duplicated values
        trips.clear();
        adapter = new TripAdapter(this,trips);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM TRIPS ",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String start = cursor.getString(2);
            String end = cursor.getString(3);
            String date = cursor.getString(4);
            String time = cursor.getString(5);
            String status = cursor.getString(6);
            trips.add(new TripModel(id,date,time,title,start,end,status));


            Date datee = new Date();  //check trip time if expired
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(date+" "+time));
                if(datee.before(cal.getTime())){
                    HelperMethods.startScheduling(MainActivity2.this,date,time,title,start,end);
                    // passing all trip data for HelperMethods to pass it then to the alert dialog and the service
                }else {
                    Toast.makeText(MainActivity2.this, "You have expired Trips please remove them", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        listTrips();   //to check if there is trips or no to show the empty trip layout
    }

    private void listTrips(){
        View view = findViewById(R.id.layout_no_trips);
        if(trips.size()==0) {
            view.setVisibility(View.VISIBLE);
        }

        else{
            view.setVisibility(View.INVISIBLE);
            adapter = new TripAdapter(this,trips);
            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeToDelete();
        }
    }

    private void swipeToDelete(){
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0
                ,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteDialog(position);

            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }
    private void deleteFromDB(int position){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] args = {trips.get(position).getId()+""};
        // DELETE FROM TRIPS WHERE _id == trips.get(position).getID()
        int deletedRows=  db.delete("TRIPS","_id ==?",args);

        if(deletedRows !=0) //when there is data deleted
            Toast.makeText(this, "Trip Deleted", Toast.LENGTH_SHORT).show();

    }
    private void showDeleteDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Are you sure you want to delete this Trip?")
                .setMessage("This action cannot be undone")
                .setPositiveButton("OK", (dialog, which) -> {
                    deleteFromDB(position);
                    // To notify array list and adapter that some data deleted
                    trips.remove(position);
                    adapter.notifyItemRemoved(position);
                    if(trips.size()==0){
                        View view = findViewById(R.id.layout_no_trips);
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    adapter.notifyItemChanged(position);
                })
                // avoid problem by clicking in any place outside the dialog or back button
                .setCancelable(false)
                .show();
    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1234) {

            }
        }




    public void loadData(){  //load upcoming data from Firebase


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DBHelper helper = new DBHelper(MainActivity2.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] args = {"Firebase"};

      int deletedRows=  db.delete("TRIPS","status ==?",args);
      //delete all firebase data in SQL first to avoid duplicating data and then add all data from firebase to SQL again



        FirebaseDatabase    firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Trip tripModel = ds.getValue(Trip.class);
                    ContentValues values = new ContentValues();
                    values.put("title", tripModel.getTitle());
                    values.put("startPoint", tripModel.getStartPoint());
                    values.put("endPoint", tripModel.getEndPoint());
                    values.put("date", tripModel.getDate());
                    values.put("time", tripModel.getTime());
                    values.put("status", "Firebase");
                    values.put("NOTES", tripModel.getNotes());

                    db.insert("TRIPS", null, values);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void loadData2(){

        //loading history data from Firebase

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase    firebaseDatabase = FirebaseDatabase.getInstance();
        DBHistory history = new DBHistory(MainActivity2.this);
        SQLiteDatabase dbb = history.getWritableDatabase();
        String [] args = {"Firebase"};
        int deletedRows=  dbb.delete("TRIPS","status ==?",args);
        //delete all firebase data in SQL first to avoid duplicating data and then add all data from firebase to SQL again



        databaseReference = firebaseDatabase.getReference().child("History").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {

                    Trip tripModel = ds.getValue(Trip.class);
                    String [] args = {tripModel.getTitle()};
                    int deletedRows=  dbb.delete("TRIPS","title ==?",args);
                    ContentValues values = new ContentValues();
                    values.put("title", tripModel.getTitle());
                    values.put("startPoint", tripModel.getStartPoint());
                    values.put("endPoint", tripModel.getEndPoint());
                    values.put("date", tripModel.getDate());
                    values.put("time", tripModel.getTime());
                    values.put("status", tripModel.getStatus());
                    values.put("NOTES", tripModel.getNotes());

                    dbb.insert("TRIPS", null, values);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

public void sync(){
   //sync upcoming data ,first delete all firebase data then adding all data from SQL to firebase

    DBHelper helper = new DBHelper(this);
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor cursor= db.rawQuery("SELECT * FROM TRIPS ",null);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.getUid();
    firebaseDatabase= FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
    databaseReference.removeValue();
    while(cursor.moveToNext()){
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String start = cursor.getString(2);
        String end = cursor.getString(3);
        String date = cursor.getString(4);
        String time = cursor.getString(5);
        String status = cursor.getString(6);
        String notes = cursor.getString(7);


        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid()).child(String.valueOf(id));

        Trip tripModel = new Trip(id,date,time,title,start,end,status,notes);
        databaseReference.setValue(tripModel);
    }
}
public void logout(){
   //when the user logs out delete all data from SQLite
    mAuth.signOut();
    Intent intent = new Intent(MainActivity2.this,MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
    trips.clear();
    DBHelper helper = new DBHelper(this);
    SQLiteDatabase db = helper.getWritableDatabase();
    int deletedRows=  db.delete("TRIPS",null,null);
    DBHistory history = new DBHistory(this);
    SQLiteDatabase dbb = history.getWritableDatabase();
    int deleted=  dbb.delete("TRIPS",null,null);
    startActivity(intent);
}

public void syncHistory(){

    DBHistory helper = new DBHistory(this);
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor cursor= db.rawQuery("SELECT * FROM TRIPS ",null);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase= FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference().child("History").child(firebaseAuth.getCurrentUser().getUid());
    databaseReference.removeValue();
    while(cursor.moveToNext()){
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String start = cursor.getString(2);
        String end = cursor.getString(3);
        String date = cursor.getString(4);
        String time = cursor.getString(5);
        String status = cursor.getString(6);
        String notes = cursor.getString(7);


        databaseReference = firebaseDatabase.getReference().child("History").child(firebaseAuth.getUid()).child(String.valueOf(id));

        Trip tripModel = new Trip(id,date,time,title,start,end,status,notes);
        databaseReference.setValue(tripModel);
    }
}

}