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

public class RecycleBinAdapter extends RecyclerView.Adapter<RecycleBinAdapter.RBinViewHolder> {

    private List<Note> noteList;
    private NoteListener noteListener;

    public RecycleBinAdapter(List<Note> noteList, NoteListener noteListener) {
        this.noteList = noteList;
        this.noteListener = noteListener;
    }

    @NonNull
    @Override
    public RBinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RBinViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_rbin_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RBinViewHolder holder, int position) {
        holder.setNote(noteList.get(position));
        holder.layoutRBinNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onNoteClicked(noteList.get(position),position);
            }
        });
        holder.btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onDelClicked(noteList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class RBinViewHolder extends RecyclerView.ViewHolder {

        private TextView txtRBinTitle, itemRBinDate;
        private ConstraintLayout layoutRBinNote;
        private ImageView btnRec;
        public RBinViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRBinTitle = itemView.findViewById(R.id.txtRBinTitle);
            layoutRBinNote = itemView.findViewById(R.id.layoutRBinNote);
            itemRBinDate = itemView.findViewById(R.id.itemRBinDate);
            btnRec = itemView.findViewById(R.id.btnRec);
        }

        void setNote(Note note) {
            txtRBinTitle.setText(note.getTitle());
            itemRBinDate.setText(note.getDate());
        }
    }
}