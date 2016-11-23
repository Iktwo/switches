package com.iktwo.switcha.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iktwo.switcha.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database helper.
 */

public class SwitchDbHelper  extends SQLiteOpenHelper {
    private static final String DB_NAME = "switch.db";
    private static final int DB_VERSION = 1;

    private static final String TAG = SwitchDbHelper.class.getSimpleName();

    private static final String SQL_CREATE_CLIPBOARD = String.format("CREATE TABLE IF NOT " +
                    "EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL," +
                    "%s TEXT NOT NULL, %s TEXT NOT NULL)",
            SwitchDatabaseContract.TABLE_SWITCH,
            SwitchDatabaseContract.SwitchColumns._ID,
            SwitchDatabaseContract.SwitchColumns.NAME,
            SwitchDatabaseContract.SwitchColumns.ON,
            SwitchDatabaseContract.SwitchColumns.OFF
    );

    private Resources resources;

    public SwitchDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        resources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLIPBOARD);
        prefillDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SwitchDatabaseContract.TABLE_SWITCH);

        onCreate(db);
    }

    private void prefillDatabase(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readEntriesFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database " + e.getMessage());
        }
    }

    private void readEntriesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = resources.openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject root = new JSONObject(builder.toString());
        JSONArray entries = root.getJSONArray(SwitchDatabaseContract.TABLE_SWITCH);

        for (int i = 0; i < entries.length(); i++) {
            JSONObject item = entries.getJSONObject(i);
            ContentValues values = new ContentValues(3);

            values.put(SwitchDatabaseContract.SwitchColumns.NAME, item.getString(SwitchDatabaseContract.SwitchColumns.NAME));
            values.put(SwitchDatabaseContract.SwitchColumns.ON, item.getString(SwitchDatabaseContract.SwitchColumns.ON));
            values.put(SwitchDatabaseContract.SwitchColumns.OFF, item.getString(SwitchDatabaseContract.SwitchColumns.OFF));

            db.insert(SwitchDatabaseContract.TABLE_SWITCH, null, values);
        }
    }
}