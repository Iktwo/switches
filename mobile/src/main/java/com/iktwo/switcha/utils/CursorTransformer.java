package com.iktwo.switcha.utils;

import android.database.Cursor;

/**
 * Interface to transform a cursor back to an object.
 */

public interface CursorTransformer {
    Object transform(Cursor cursor);
}