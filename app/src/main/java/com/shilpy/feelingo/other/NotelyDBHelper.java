package com.shilpy.feelingo.other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shilpysamaddar on 22/01/18.
 */

public class NotelyDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "notely_database";

    private static NotelyDBHelper NOTELY_DB_HELPER = null;
    private static final String COMMA_SEP = ",";

    private static final String CREATE_NOTELY_TABLE = "CREATE TABLE IF NOT EXISTS " +
            NotelyContract.NotesEntry.TABLE_NAME + "( " +
            NotelyContract.NotesEntry.COLUMN_ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_TITLE + " VARCHAR " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_CONTENT + " VARCHAR " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_TYPE + " VARCHAR " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_CREATEDAT + " VARCHAR " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_FAVOURITE + " boolean DEFAULT 0 " + COMMA_SEP +
            NotelyContract.NotesEntry.COLUMN_HEARTED + " boolean DEFAULT 0 " +
            ");";

    private static final String DROP_NOTELY_TABLE = "DROP TABLE IF EXISTS " + NotelyContract.NotesEntry.TABLE_NAME;

    synchronized public static NotelyDBHelper getInstance(Context context) {
        if (NOTELY_DB_HELPER == null) {
            NOTELY_DB_HELPER = new NotelyDBHelper(context);
        }
        return NOTELY_DB_HELPER;
    }

    private NotelyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void dropNotelyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        dropAllTables(db);
    }

    /**
     * Drop note table.
     *
     * @param db Writable databae.
     */
    public void dropAllTables(SQLiteDatabase db) {
        db.execSQL(DROP_NOTELY_TABLE);
        db.execSQL(CREATE_NOTELY_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTELY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//      drop table and create new table if required
    }

    //    add notes
    public int addNotes(NotelyDataModel notelyDataModel) {
        int rowId = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            // Inserting Notes Row
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotelyContract.NotesEntry.COLUMN_TITLE, notelyDataModel.getTitle());
            contentValues.put(NotelyContract.NotesEntry.COLUMN_CONTENT, notelyDataModel.getContent());
            contentValues.put(NotelyContract.NotesEntry.COLUMN_TYPE, notelyDataModel.getType());
            contentValues.put(NotelyContract.NotesEntry.COLUMN_FAVOURITE, notelyDataModel.isFavourite());
            contentValues.put(NotelyContract.NotesEntry.COLUMN_CREATEDAT, notelyDataModel.getCreatedAt());
            contentValues.put(NotelyContract.NotesEntry.COLUMN_HEARTED, notelyDataModel.isHearted());
            rowId = (int) db.insert(NotelyContract.NotesEntry.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public List<NotelyDataModel> getNotes() {
        List<NotelyDataModel> allNotestList = new ArrayList<NotelyDataModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + NotelyContract.NotesEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("size", "" + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotelyDataModel notelyDataModel = new NotelyDataModel();
                notelyDataModel.setId(Integer.parseInt(cursor.getString(0)));
                notelyDataModel.setTitle(cursor.getString(1));
                notelyDataModel.setContent(cursor.getString(2));
                notelyDataModel.setType(cursor.getString(3));
                notelyDataModel.setCreatedAt(cursor.getString(4));
                notelyDataModel.setFavourite(Boolean.parseBoolean(cursor.getString(5)));
                notelyDataModel.setHearted(Boolean.parseBoolean(cursor.getString(6)));
                // Adding notes to list
                allNotestList.add(notelyDataModel);
            } while (cursor.moveToNext());
        }

        // return notes list
        return allNotestList;
    }

    public NotelyDataModel getNotesById(int row_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Select Single Row Query
        Cursor cursor = db.query(NotelyContract.NotesEntry.TABLE_NAME, new String[]{NotelyContract.NotesEntry.COLUMN_ID,
                        NotelyContract.NotesEntry.COLUMN_TITLE, NotelyContract.NotesEntry.COLUMN_CONTENT, NotelyContract.NotesEntry.COLUMN_TYPE,
                        NotelyContract.NotesEntry.COLUMN_CREATEDAT, NotelyContract.NotesEntry.COLUMN_FAVOURITE, NotelyContract.NotesEntry.COLUMN_HEARTED},
                NotelyContract.NotesEntry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(row_id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        NotelyDataModel notelyDataModel = new NotelyDataModel();
        notelyDataModel.setId(Integer.parseInt(cursor.getString(0)));
        notelyDataModel.setTitle(cursor.getString(1));
        notelyDataModel.setContent(cursor.getString(2));
        notelyDataModel.setType(cursor.getString(3));
        notelyDataModel.setCreatedAt(cursor.getString(4));
        notelyDataModel.setFavourite(Boolean.parseBoolean(cursor.getString(5)));
        notelyDataModel.setHearted(Boolean.parseBoolean(cursor.getString(6)));
        // return notes
        return notelyDataModel;
    }

    public int updateNotes(NotelyDataModel notelyDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotelyContract.NotesEntry.COLUMN_TITLE, notelyDataModel.getTitle());
        values.put(NotelyContract.NotesEntry.COLUMN_CONTENT, notelyDataModel.getContent());
        values.put(NotelyContract.NotesEntry.COLUMN_TYPE, notelyDataModel.getType());
        values.put(NotelyContract.NotesEntry.COLUMN_FAVOURITE, String.valueOf(notelyDataModel.isFavourite()));
        values.put(NotelyContract.NotesEntry.COLUMN_CREATEDAT, notelyDataModel.getCreatedAt());
        values.put(NotelyContract.NotesEntry.COLUMN_HEARTED, String.valueOf(notelyDataModel.isHearted()));
        // updating row
        return db.update(NotelyContract.NotesEntry.TABLE_NAME, values, NotelyContract.NotesEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(notelyDataModel.getId())});
    }

//    delete a single row
    public void deleteNotes(NotelyDataModel notelyDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NotelyContract.NotesEntry.TABLE_NAME, NotelyContract.NotesEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(notelyDataModel.getId())});
        db.close();
    }

}
