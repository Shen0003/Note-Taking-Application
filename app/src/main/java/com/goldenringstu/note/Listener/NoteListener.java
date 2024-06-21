package com.goldenringstu.note.Listener;

import com.goldenringstu.note.Entities.Note;

//***to prevent work in the Adapter: so fetch the targeted item from the adapter and do it on that targeted place.
public interface NoteListener { //to get the note3 and the position of note3 from the RecView item thru adapter
    void onNoteClicked(Note note, int position);
    void onDelClicked(Note note, int position);
}
