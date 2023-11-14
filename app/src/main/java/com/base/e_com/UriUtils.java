package com.base.e_com;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

public class UriUtils {

    public static String getPathFromUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getFilePathForQ(context, uri);
        } else {
            return getFilePathForPreQ(context, uri);
        }
    }

    private static String getFilePathForPreQ(Context context, Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private static String getFilePathForQ(Context context, Uri uri) {
        String path = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                path = contentResolver.openFileDescriptor(uri, "r").getFileDescriptor().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}


