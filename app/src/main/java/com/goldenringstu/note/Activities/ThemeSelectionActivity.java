package com.goldenringstu.note.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldenringstu.note.Adapters.ThemeAdapter;
import com.goldenringstu.note.Entities.Theme;
import com.goldenringstu.note.Listener.ThemeListener;
import com.goldenringstu.note.R;
import com.jakewharton.processphoenix.ProcessPhoenix;


import java.util.ArrayList;
import java.util.List;

public class ThemeSelectionActivity extends AppCompatActivity implements ThemeListener {
    private ThemeAdapter adapter;
    private RecyclerView ThemeRecView;
    private List<Theme> tList = new ArrayList<>();


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
        setContentView(R.layout.activity_theme_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //here add the theme detail item
        tList.add(new Theme(0, "Default Dark", "A Default Good Looking Dark Theme!", R.drawable.note_icon));
        tList.add(new Theme(1, "Default Light", "A Default Good Looking Light Theme!", R.drawable.theme_icon_t1));
        tList.add(new Theme(2,"Kitty Tong", "A spirited and adventurous cat with charm.", R.drawable.theme_icon_cat_t2));

        //setting up the Theme Recycler View and Adapter
        ThemeRecView = findViewById(R.id.ThemeRecView);
        adapter = new ThemeAdapter(tList, this);
        ThemeRecView.setLayoutManager(new LinearLayoutManager(this));
        ThemeRecView.setAdapter(adapter);
    }

    @Override
    public void onThemeClicked(Theme theme) {
        GlobalVariableClass.initSP(this);
        GlobalVariableClass.setTHEME_CODE(theme.getTHEME_CODE());
        Toast.makeText(this, "Theme Changed!", Toast.LENGTH_SHORT).show();
        ProcessPhoenix.triggerRebirth(ThemeSelectionActivity.this);
    }
}