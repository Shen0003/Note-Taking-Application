package com.goldenringstu.note.Activities;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.goldenringstu.note.Database.NoteDatabase;
import com.goldenringstu.note.Entities.Note;
import com.goldenringstu.note.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    private EditText inTitle, inContext;
    private TextView txtDate;
    private ImageButton btnSave;
    private Note alreadyAvailableNote;
    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    private AlertDialog saveDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalVariableClass.initSP(this);
        int themeCode = GlobalVariableClass.getTHEME_CODE();
        switch (themeCode) {
            case 1:
                setTheme(R.style.t1_theme);
                break;
            case 2:
                setTheme(R.style.t2_theme);
                break;
            default:
                setTheme(R.style.t0_theme);
                break;
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inTitle = findViewById(R.id.inTitle);
        inContext = findViewById(R.id.inContext);
        txtDate = findViewById(R.id.txtDate);
        txtDate.setText(new SimpleDateFormat("EEEE\n yyyy-MM-dd HH:mm aa", Locale.getDefault()).format(new Date()));

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //dialog here

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNoteActivity.this);
                    View view = LayoutInflater.from(AddNoteActivity.this).inflate(
                            R.layout.layout_save_note,
                            (ViewGroup) findViewById(R.id.layoutSaveNoteContainer)
                    );
                    builder.setView(view);
                    saveDialog = builder.create();
                    if (saveDialog.getWindow() != null) {
                        saveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    view.findViewById(R.id.txtDSave).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveNote();
                            saveDialog.dismiss();
                        }
                    });

                    view.findViewById(R.id.txtDNSave).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_CANCELED);
                            finish();
                            saveDialog.dismiss();
                        }
                    });

                    view.findViewById(R.id.txtDCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveDialog.dismiss();
                        }
                    });
                    saveDialog.show();


            }
        });

        if(getIntent().getBooleanExtra("isViewOrUpdate",false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

    }

    private void setViewOrUpdateNote() { //***for updating the note
        inTitle.setText(alreadyAvailableNote.getTitle());
        inContext.setText(alreadyAvailableNote.getContext());
        txtDate.setText(alreadyAvailableNote.getDate());
    }

    //*******THIS METHOD IS FOR ADD NOTE, and UPDATE NOTE!
    private void saveNote() { //**create and ADD the new note here with new attributes/variables/values
        final Note note = new Note(); ////create a new note using its empty arg constructor
        if ((inTitle.getText().toString()).isEmpty()) { //set
            note.setTitle("Untitled Note");
        } /////////HEREEEEE
        else {
            note.setTitle(inTitle.getText().toString()); //set
        }
        note.setContext(inContext.getText().toString());//set
        note.setDate(new SimpleDateFormat("EEEE\n yyyy-MM-dd HH:mm aa", Locale.getDefault()).format(new Date()));//set

        //**for UPDATE notes, to replace the note with same ID later in DB!!
        if (alreadyAvailableNote != null) { //if THERE is some text/context are set on the AddNotePage.xml, means it is an old notes, so is using UPDATE logic!
            note.setId(alreadyAvailableNote.getId()); //getID and so set again ALL the text, context, date, MOST IMPORTANT WE SET THE ID, but not letting the databse set the ID iteself, so will replace the onConflict(same ID) to update our note!!!! (******THAT'S WHY WE ADD onConflict = OnConflictStrategy.REPLACE in the UPDATE in dao class!)
        }

        //******insert notes into database and recView
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                NoteDatabase.getDB(getApplicationContext()).noteDao().insertNote(note);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

}