package com.goldenringstu.note.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.goldenringstu.note.Activities.GlobalVariableClass;
import com.goldenringstu.note.Entities.Theme;
import com.goldenringstu.note.Listener.ThemeListener;
import com.goldenringstu.note.R;

import java.util.ArrayList;
import java.util.List;


public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {
    List<Theme> tList = new ArrayList<>();

    private ThemeListener tListener;

    public ThemeAdapter(List<Theme> tList, ThemeListener tListener) {
        this.tList = tList;
        this.tListener = tListener;
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_theme,parent,false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        for(Theme t: tList) {
            if(GlobalVariableClass.getTHEME_CODE() == tList.get(position).getTHEME_CODE()) {
                holder.rBtnTheme.setChecked(true);
            }
        }
        holder.tItemTitle.setText(tList.get(position).getTitle());
        holder.tItemDesc.setText(tList.get(position).getDesc());
        holder.tItemIcon.setImageResource(tList.get(position).getImageRes());
        holder.rBtnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tListener != null) {
                    tListener.onThemeClicked(tList.get(position));
                }
            }
        });

    }




    @Override
    public int getItemCount() {
        return tList.size();
    }

    static class ThemeViewHolder extends RecyclerView.ViewHolder {
        private TextView tItemTitle, tItemDesc;
        private ImageView tItemIcon;
        private ConstraintLayout theme_item;
        private RadioButton rBtnTheme;
        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            tItemTitle = itemView.findViewById(R.id.tItemTitle);
            tItemDesc = itemView.findViewById(R.id.tItemDesc);
            tItemIcon = itemView.findViewById(R.id.tItemIcon);
            theme_item = itemView.findViewById(R.id.theme_item);
            rBtnTheme = itemView.findViewById(R.id.rBtnTheme);
        }
    }

}
