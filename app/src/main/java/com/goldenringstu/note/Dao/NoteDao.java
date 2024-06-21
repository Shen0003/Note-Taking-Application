package com.goldenringstu.note.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.goldenringstu.note.Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY id DESC") //DESC so the oldest note3 is the last, and the newest note3 is the first in the RECView/ Array
    List<Note> getAllNote();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}
