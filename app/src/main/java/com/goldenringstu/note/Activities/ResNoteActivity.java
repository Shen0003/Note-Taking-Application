package com.goldenringstu.note.Activities;



import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.goldenringstu.note.Database.NoteDatabase;
import com.goldenringstu.note.Database.RecycleBinDatabase;
import com.goldenringstu.note.Entities.Note;
import com.goldenringstu.note.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResNoteActivity extends AppCompatActivity {


    private TextView resDate;
    private EditText resTitle, resContext;
    private ImageView btnRes;
    private Note note;
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
        setContentView(R.layout.activity_res_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resTitle = findViewById(R.id.resTitle);
        resDate = findViewById(R.id.resDate);
        resContext = findViewById(R.id.resContext);
        btnRes = findViewById(R.id.btnRes);

        setResNote();

        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resNote();
            }
        });

    }



    public void setResNote() { //set On UI views by getting the Note from RBinDB
        note = (Note) getIntent().getSerializableExtra("note3"); //get the Note object and CAST to Note datatype (cause we transfer to other activity thru SERIALIZABLE [which is implemented in the Note entitiy modal class before])!!!!
        resTitle.setText(note.getTitle());
        resTitle.setFocusable(false);
        resDate.setText(note.getDate());
        resContext.setText(note.getContext());
        resContext.setFocusable(false);
    }

    public void resNote() { //get the Notes detail from the UI views and save to orginal Note DB
        //******insert notes into database and recView
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                NoteDatabase.getDB(getApplicationContext()).noteDao().insertNote(note);
                RecycleBinDatabase.getDB(getApplicationContext()).noteDao().deleteNote(note);

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
        Toast.makeText(this, "Restored!", Toast.LENGTH_SHORT).show();
    }
}







