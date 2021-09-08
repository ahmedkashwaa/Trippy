package com.example.Project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapterHistory extends RecyclerView.Adapter<TripAdapterHistory.TripViewHolder> {
    private Activity activity;
    private ArrayList<TripModel> trips;

    public TripAdapterHistory(Activity activity, ArrayList<TripModel> trips) {
        this.activity = activity;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripAdapterHistory.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.trip_list,parent,false);
        TripViewHolder holder =new TripViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        DBHistory history = new DBHistory(activity);
        SQLiteDatabase db = history.getReadableDatabase();
        String [] whereArgs = {String.valueOf(trips.get(position).getId())};
        Cursor cursor= db.rawQuery("SELECT NOTES FROM TRIPS WHERE _id=? ",whereArgs);
        cursor.moveToNext();
        String notes = cursor.getString(cursor.getColumnIndex("NOTES"));
        trips.get(position).setNotes(notes);



        holder.startPoint.setText(trips.get(position).getStartPoint());
        holder.endPoint.setText(trips.get(position).getEndPoint());
        holder.tripTitle.setText(trips.get(position).getTitle());
        holder.date.setText(trips.get(position).getDate());
        holder.time.setText(trips.get(position).getTime());
        holder.start.setVisibility(View.INVISIBLE);
        holder.status.setText(trips.get(position).getStatus());
        holder.popup.setVisibility(View.INVISIBLE);
      //  holder.notes.setVisibility(View.INVISIBLE);
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(activity, R.color.quantum_vanillared100));
        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             try {
                 String[] a = trips.get(position).getNotes().split(",");
                 PopupMenu pop = new PopupMenu(activity, v);
                 pop.inflate(R.menu.notes_menu);
                 for (int i = 0; i < a.length; i++) {

                     pop.getMenu().add(a[i]);
                     pop.show();
                 }
             }catch (Exception e ){
                 Toast.makeText(activity, "There are no Notes", Toast.LENGTH_SHORT).show();
             }


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
            notes=itemView.findViewById(R.id.show);
        }


    }


}
