package com.goldenringstu.note.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.goldenringstu.note.Adapters.NoteAdapter;
import com.goldenringstu.note.Database.NoteDatabase;
import com.goldenringstu.note.Database.RecycleBinDatabase;
import com.goldenringstu.note.Entities.Note;
import com.goldenringstu.note.Listener.NoteListener;
import com.goldenringstu.note.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NoteListener {

    private ImageButton mainbtnAdd;
    private List<Note> noteList; //to store all the notes of RecView, from NoteDatabase "notes" list
    private RecyclerView notesRecView;
    private NoteAdapter noteAdapter;
    public static final int ADD_CODE = 1; ////CONSTANT
    public static final int UPD_CODE = 2; ////CONSTANT
    public static final int SHOW_CODE = 3; ////CONSTANT
    ActivityResultLauncher<Intent> addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // doSomeOperations() here
                        getNotes(ADD_CODE);
                    }
                }
            });

    ActivityResultLauncher<Intent> updActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // doSomeOperations() here
                        getNotes(UPD_CODE);
                    }
                }
            });

    ActivityResultLauncher<Intent> RBinActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getNotes(SHOW_CODE); //only show at here, becoz we already do add/remove on the RBin DB page
                        //******USE ADD CODE BECOZ WE JUST (INSERT THE NOTES from RBinDB)--> just input from DB but not user only, but same things of "Insert" note3 to DB!
                    }
                }
            }
    );

    private int noteClickedPos = -1;
    private int delClickedPos = -1;
    private AlertDialog delDialog;

    private ImageView rbinIcon,tIcon,hIcon;
    private List<Note> notesToDelete = new ArrayList<>();


    //////////////////////////FUNCTIONS/METHODS//////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme
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
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
///hhhhhhhhhhhhhhhaaa
        /////////
        mainbtnAdd = findViewById(R.id.mainbtnAdd);
        mainbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                addActivityResultLauncher.launch(intent);
            }
        });

        rbinIcon = findViewById(R.id.rbinIcon);
        rbinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecycleBinActivity.class);
                RBinActivityResultLauncher.launch(intent);
            }
        });

        tIcon = findViewById(R.id.tIcon);
        tIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThemeSelectionActivity.class);
                startActivity(intent);
            }
        });

        hIcon = findViewById(R.id.hIcon);
        hIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "The note3 is sorted by oldest to latest!", Toast.LENGTH_SHORT).show();
            }
        });


        //RecView and Adapter Things:
        notesRecView = findViewById(R.id.notesRecView);
        notesRecView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        notesRecView.setAdapter(noteAdapter);


        getNotes(SHOW_CODE);


    }

    private void getNotes(final int CODE) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here  //////***ALL ABOUT DATABASE WORK HERE!!!
                List<Note> notes; ///////***********************TO STORE ALL Notes FROM DB
                notes = NoteDatabase.getDB(getApplicationContext()).noteDao().getAllNote();

                handler.post(new Runnable() {
                    @Override
                    public void run() { //////***ALL ABOUT REC VIEW HERE, NOT DB!!!
                        //UI Thread work here
                        if (CODE == SHOW_CODE) { ////Just show all latest note3 on RecView
                            noteList.clear();          ////* Both of these code is TO REFRESH DB:
                            noteList.addAll(notes);    ////* clear the original List and add THE NEW UPDATED from DB!!!!!!!!!
                            noteAdapter.notifyDataSetChanged();
                        } else if (CODE == ADD_CODE) {                                ////Add only 1 new note3 to RecView then refresh the RecView
                            noteList.add(0, notes.get(0));  //MEANS: In the "notes" list of DB at 0th position, we have the new note3 added; then we add into "noteList" of RecView at 0th position too
                            noteAdapter.notifyItemInserted(0);
                            notesRecView.smoothScrollToPosition(0);
                        } else if (CODE == UPD_CODE) {                                ////Update the note3 in RecView then refresh the RecView
                            noteList.remove(noteClickedPos);
                            noteList.add(noteClickedPos, notes.get(noteClickedPos));
                            noteAdapter.notifyItemChanged(noteClickedPos);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onNoteClicked(Note note, int position) {

        noteClickedPos = position;
        Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        updActivityResultLauncher.launch(intent);

    }

    @Override
    public void onDelClicked(Note note, int position) {
        notesToDelete.add(note);

        if (delDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            delDialog = builder.create();
            if (delDialog.getWindow() != null) {
                delDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            // DELETE clickable on dialog
            view.findViewById(R.id.txtCDel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete notes in background (thread)
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // Background work here
                            for (Note noteToDelete : notesToDelete) {
                                NoteDatabase.getDB(getApplicationContext()).noteDao().deleteNote(noteToDelete);
                                RecycleBinDatabase.getDB(getApplicationContext()).noteDao().insertNote(noteToDelete);
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // UI Thread work here
                                    for (Note noteToDelete : notesToDelete) {
                                        int position = noteList.indexOf(noteToDelete);
                                        if (position != -1) {
                                            noteList.remove(position);
                                            noteAdapter.notifyItemRemoved(position);
                                        }
                                    }
                                    notesToDelete.clear();
                                }
                            });
                        }
                    });
                    delDialog.dismiss();
                }
            });

            view.findViewById(R.id.txtCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notesToDelete.clear(); // Clear the notesToDelete list when canceling
                    delDialog.dismiss();
                }
            });
        }
        delDialog.show();
    }


}
