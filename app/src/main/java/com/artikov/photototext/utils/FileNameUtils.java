package com.artikov.photototext.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

/**
 * Date: 28/6/2016
 * Time: 12:35
 *
 * @author Artur Artikov
 */
public class FileNameUtils {
    static public String getName(Context context, Uri uri) {
        if(uri.getScheme().equals("file")) {
            return trimExtension(uri.getLastPathSegment());
        }

        String[] projection = new String[]{OpenableColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        String name = "";
        if (cursor != null && cursor.moveToFirst()) {
            name = trimExtension(cursor.getString(0));
        }
        if(cursor != null) cursor.close();
        return name;
    }

    static private String trimExtension(String name) {
        int dotIndex = name.lastIndexOf('.');
        return dotIndex != -1 ? name.substring(0, dotIndex) : name;
    }
}
