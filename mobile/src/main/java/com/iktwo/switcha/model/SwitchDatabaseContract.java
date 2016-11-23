package com.iktwo.switcha.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Database contract for switch.
 */

public class SwitchDatabaseContract {
    public static final String TABLE_SWITCH = "switcha";
    public static final String CONTENT_AUTHORITY = "com.iktwo.switcha";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_SWITCH)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static final class SwitchColumns implements BaseColumns {
        public static final String NAME = "name";
        public static final String ON = "onCode";
        public static final String OFF = "offCode";
    }
}
