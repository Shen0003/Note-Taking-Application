package com.goldenringstu.note.Adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.goldenringstu.note.Entities.Note;
import com.goldenringstu.note.Listener.NoteListener;
import com.goldenringstu.note.R;

import java.util.List;

public class NoteAdapter extends  RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private NoteListener noteListener;

    public NoteAdapter(List<Note> noteList, NoteListener noteListener) {
        this.noteList = noteList;
        this.noteListener = noteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(noteList.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() { //when click on the ITEM in the RecView
            @Override
            public void onClick(View v) {
                noteListener.onNoteClicked(noteList.get(position),position);
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onDelClicked(noteList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, itemDate;
        ConstraintLayout layoutNote;
        ImageView btnDel;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            btnDel = itemView.findViewById(R.id.btnDel);
            itemDate = itemView.findViewById(R.id.itemDate);
        }

        void setNote(Note note) {
            txtTitle.setText(note.getTitle());
            itemDate.setText(note.getDate());
        }
    }
}