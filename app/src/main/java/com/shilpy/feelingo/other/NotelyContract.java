package com.shilpy.feelingo.other;

import android.provider.BaseColumns;

/**
 * Created by shilpysamaddar on 22/01/18.
 */

public final class NotelyContract {
    public NotelyContract() {
    }
    public static abstract class NotesEntry implements BaseColumns{

        // Notely table name
        public static final String TABLE_NAME = "noteTable";

        // Notely Table Columns names
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TYPE="type";
        public static final String COLUMN_CREATEDAT="cretedat";
        public static final String COLUMN_FAVOURITE="favourite";
        public static final String COLUMN_HEARTED="hearted";
    }
}
