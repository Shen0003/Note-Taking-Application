package com.goldenringstu.note.Activities;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.goldenringstu.note.Adapters.RecycleBinAdapter;
import com.goldenringstu.note.Database.RecycleBinDatabase;
import com.goldenringstu.note.Entities.Note;
import com.goldenringstu.note.Listener.NoteListener;
import com.goldenringstu.note.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecycleBinActivity extends AppCompatActivity implements NoteListener {

    private RecyclerView RBinRecView;
    private RecycleBinAdapter RBinAdapter;
    private List<Note> noteList;
    private int onNoteClickPos = -1;
    private final static int SHOW_CODE = 0;
    private final static int UPDWHOLE_CODE = 1;
    ActivityResultLauncher<Intent> RBinItemActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getNotesFromRBDB(UPDWHOLE_CODE); //DO ADD Note FROM RBDB TO NoteDB; and REMOVE Note FROM RBDB here.
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
    );
    private AlertDialog recDialog;
    private List<Note> notesToFDel = new ArrayList<>();


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
        setContentView(R.layout.activity_recycle_bin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //REC VIEW AND ADAPTER THINGS:
        RBinRecView = findViewById(R.id.RBinRecView); //here
        noteList = new ArrayList<>();
        RBinAdapter = new RecycleBinAdapter(noteList, this);
        RBinRecView.setAdapter(RBinAdapter);
        RBinRecView.setLayoutManager(new LinearLayoutManager(this));


        getNotesFromRBDB(SHOW_CODE);
    }

    private void getNotesFromRBDB(final int CODE) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here  //////***ALL ABOUT DATABASE WORK HERE!!!
                List<Note> RBinDB_notes;
                RBinDB_notes = RecycleBinDatabase.getDB(getApplicationContext()).noteDao().getAllNote();

                handler.post(new Runnable() {
                    @Override
                    public void run() { //////***ALL ABOUT REC VIEW HERE, NOT DB!!!
                        //UI Thread work here
                        if (CODE == SHOW_CODE) {// //Just show all latest note3 on RecView
                            noteList.addAll(RBinDB_notes);
                            RBinAdapter.notifyDataSetChanged();
                        }
                        else if (CODE == UPDWHOLE_CODE) {
                            noteList.remove(onNoteClickPos);
                            RBinAdapter.notifyItemRemoved(onNoteClickPos);

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        onNoteClickPos = position;
        Intent intent2 = new Intent(RecycleBinActivity.this,ResNoteActivity.class);
        intent2.putExtra("note3", note);
        RBinItemActivityResultLauncher.launch(intent2);
    }

    @Override
    public void onDelClicked(Note note, int position) {
        notesToFDel.add(note);
        if (recDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecycleBinActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            recDialog = builder.create();
            if (recDialog.getWindow() != null) {
                recDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.txtCDel).setOnClickListener(v -> {
                // Delete notes in background (thread)
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    // Background work: delete notes from recycle bin database
                    for (Note noteToDelete : notesToFDel) {
                        RecycleBinDatabase.getDB(getApplicationContext()).noteDao().deleteNote(noteToDelete);
                    }
                    handler.post(() -> {
                        // UI Thread work: update RecyclerView
                        for (Note noteToFDelete : notesToFDel) {
                            int position1 = noteList.indexOf(noteToFDelete);
                            if (position1 != -1) {
                                noteList.remove(position1);
                                RBinAdapter.notifyItemRemoved(position1);
                            }
                        }
                        notesToFDel.clear();
                        recDialog.dismiss();
                    });
                });
            });

            view.findViewById(R.id.txtCancel).setOnClickListener(v -> {
                notesToFDel.clear();
                recDialog.dismiss();
            });
        }
        recDialog.show();
    }
}



