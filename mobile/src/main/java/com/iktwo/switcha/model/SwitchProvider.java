package com.iktwo.switcha.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Switch content provider.
 */

public class SwitchProvider extends ContentProvider {
    private static final int CLIPBOARD = 100;
    private static final int CLIPBOARD_WITH_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(SwitchDatabaseContract.CONTENT_AUTHORITY,
                SwitchDatabaseContract.TABLE_SWITCH, CLIPBOARD);

        uriMatcher.addURI(SwitchDatabaseContract.CONTENT_AUTHORITY,
                SwitchDatabaseContract.TABLE_SWITCH + "/#", CLIPBOARD_WITH_ID);
    }

    private SwitchDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SwitchDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SwitchDatabaseContract.TABLE_SWITCH);

        switch (uriMatcher.match(uri)) {
            case CLIPBOARD:
                break;
            case CLIPBOARD_WITH_ID:
                qb.appendWhere(String.format("%s = %s", SwitchDatabaseContract.SwitchColumns._ID,
                        uri.getLastPathSegment()));
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI received");
        }

        String defaultSort = SwitchDatabaseContract.SwitchColumns._ID;
        String orderBy = (TextUtils.isEmpty(sortOrder)) ? defaultSort : sortOrder;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        if (cursor != null && getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != CLIPBOARD) {
            throw new IllegalArgumentException("Illegal insert URI");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insertWithOnConflict(SwitchDatabaseContract.TABLE_SWITCH, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);

        if (rowId == -1) {
            return null;
        }

        Uri result = ContentUris.withAppendedId(uri, rowId);

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case CLIPBOARD:
                selection = (selection == null) ? "1" : selection;
                break;
            case CLIPBOARD_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", SwitchDatabaseContract.SwitchColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(SwitchDatabaseContract.TABLE_SWITCH, selection, selectionArgs);

        if (count > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case CLIPBOARD:
                break;
            case CLIPBOARD_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", SwitchDatabaseContract.SwitchColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal update URI");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(SwitchDatabaseContract.TABLE_SWITCH, values, selection, selectionArgs);

        if (count > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
