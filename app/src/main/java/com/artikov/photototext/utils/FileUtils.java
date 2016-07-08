package com.artikov.photototext.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 28/6/2016
 * Time: 12:35
 *
 * @author Artur Artikov
 */
public class FileUtils {
    static public byte[] readFile(Context context, Uri uri) throws IOException {
        InputStream stream = context.getContentResolver().openInputStream(uri);
        if (stream == null) {
            throw new IOException("Could not read file " + uri);
        }

        byte[] data = new byte[stream.available()];
        try {
            int offset = 0;
            while (true) {
                if (offset >= data.length) break;
                int count = stream.read(data, offset, data.length - offset);
                if (count < 0) break;
                offset += count;
            }
            if (offset < data.length) {
                throw new IOException("Could not read file " + uri);
            }
        } finally {
            stream.close();
        }
        return data;
    }

    static public String getNameByUri(Context context, Uri uri) {
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
