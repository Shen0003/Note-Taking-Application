package com.goldenringstu.note.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.goldenringstu.note.Dao.NoteDao;
import com.goldenringstu.note.Entities.Note;

@Database(entities = Note.class, version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase noteDB;

    public static synchronized NoteDatabase getDB(Context context) {
        if(noteDB == null) {
            noteDB = Room.databaseBuilder(
                    context,
                    NoteDatabase.class,
                    "notes_db1"
            ).build();
        }
        return noteDB;
    }

    public abstract NoteDao noteDao();
}
