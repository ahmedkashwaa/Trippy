package com.example.Project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class history extends AppCompatActivity {
    private ArrayList<TripModel> trips = new ArrayList<>();
    private TripAdapterHistory adapter;
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    boolean s ;
    DatabaseReference databaseReference ;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");




    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrips();  // loads data from SQLite to the recycler view
    }

    private void getTrips(){
        // to fix duplicated values
        trips.clear();
        DBHistory helper = new DBHistory(this);
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
        }
        listTrips();  //to check if there is trips or no to show the empty trip layout
    }

    private void listTrips(){
        View view = findViewById(R.id.layout_no_trips);
        if(trips.size()==0)
            view.setVisibility(View.VISIBLE);
        else{
            view.setVisibility(View.INVISIBLE);
            adapter = new TripAdapterHistory(this,trips);
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
        DBHistory helper = new DBHistory(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] args = {trips.get(position).getId()+""};
        // DELETE FROM HISTORY WHERE _id == notes.get(position).getID()
        int deletedRows=  db.delete("TRIPS","_id ==?",args);
        if(deletedRows !=0)
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

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sync,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId()==R.id.sync) {

           DBHistory helper = new DBHistory(this);
            SQLiteDatabase db = helper.getReadableDatabase();
           String [] args = {"Firebase"};
           // Cursor cursor= db.rawQuery("SELECT * FROM TRIPS where status !=?",args);
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


                databaseReference = firebaseDatabase.getReference().child("History").child(firebaseAuth.getUid()).child(String.valueOf(id));

                TripModel tripModel = new TripModel(id,date,time,title,start,end,status);
                databaseReference.setValue(tripModel);
            }
        }

        return super.onOptionsItemSelected(item);
    } */





}