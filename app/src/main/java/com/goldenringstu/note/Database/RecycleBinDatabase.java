package com.goldenringstu.note.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.goldenringstu.note.Dao.NoteDao;
import com.goldenringstu.note.Entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class RecycleBinDatabase extends RoomDatabase {
    private static RecycleBinDatabase RBinDB;

    public static synchronized RecycleBinDatabase getDB(Context context) {
        if(RBinDB == null) {
            RBinDB = Room.databaseBuilder(
                    context,
                    RecycleBinDatabase.class,
                    "RBin_db1"
            ).build();
        }
        return RBinDB;
    }

    public abstract NoteDao noteDao();
}
