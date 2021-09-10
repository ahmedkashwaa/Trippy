package com.example.Project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {
    List<TextInputLayout> mNotesTextInputLayout = new ArrayList<>();
    List<String> notesList = new ArrayList<>();
    int increasedID = 0;
    TextInputLayout noteTextField;

    LinearLayout notesLinearLayout;

    String title,start,end,date,time;

    int id;
    private boolean openAsUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        notesLinearLayout=findViewById(R.id.notes_linearLayout);
        noteTextField=findViewById(R.id.note_text_field);
        mNotesTextInputLayout.add(noteTextField);



      id = getIntent().getIntExtra("id",0);
         title= getIntent().getStringExtra("title");
         start=getIntent().getStringExtra("startPoint");
         end=getIntent().getStringExtra("endPoint");
         date=getIntent().getStringExtra("date");
       time=getIntent().getStringExtra("time");
      String notess=getIntent().getStringExtra("NOTES");

     try {
         if (!notess.isEmpty()) {
             setTitle("Update Note");

             noteTextField.getEditText().setText(notess);
             openAsUpdate = true;
         } else {

             setTitle("Add Note");
         }
     }catch (Exception e){

     }



    }

    public void add(View view) {
        for (TextInputLayout txtLayout : mNotesTextInputLayout) {

            notesList.add(txtLayout.getEditText().getText().toString());
          //  Toast.makeText(AddNoteActivity.this, ""+txtLayout.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
        }

        String str = TextUtils.join(" , ", notesList);

        ContentValues values = new ContentValues();
        values.put("NOTES",str);
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] whereArgs = {String.valueOf(id)};
        int id = db.update("TRIPS",values,"_id==?",whereArgs);



    Intent i = new Intent(AddNoteActivity.this,MainActivity2.class);
      boolean  firstLogin=false;
       i.putExtra("two",firstLogin);
      finish();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);

    }

    private void generateNoteLayout(View view) {
        LinearLayout currentParent = findViewById(R.id.notes_parent_linear_Layout);

        View linearLayout = getLayoutInflater().inflate(R.layout.add_notes_sayout_sample, null);

        TextInputLayout noteTextInput = linearLayout.findViewById(R.id.note_text_field_input);
        mNotesTextInputLayout.add(noteTextInput);
        if(increasedID!=9) {
            ImageButton subImgBtn = linearLayout.findViewById(R.id.sub_note_img_btn);
            subImgBtn.setOnClickListener(v -> {
                currentParent.removeView(linearLayout);
                mNotesTextInputLayout.remove(noteTextInput);
                increasedID--;
            });

            currentParent.addView(linearLayout);
            increasedID++;
        }



    }

    public void addNote(View view) {
                generateNoteLayout(view);
    }
}