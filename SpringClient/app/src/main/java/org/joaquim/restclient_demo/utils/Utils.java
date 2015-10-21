package org.joaquim.restclient_demo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    /**
     * Get the full path from a Uri resource
     * @param selectedImage The Uri resource
     * @param context The context of the application
     * @return The full path to the resource, as a String object
     */
    public static String getFullPath(Uri selectedImage, Context context) {
        /**
         * Get the document ID, based on the given Uri. The format of a document ID is
         * "Document:COLUMN_DOCUMENT_ID" -- For images this is: "image:ID"
         * So, if we split the string by the ":" and get the first element we will have the type
         * of our document (and in the second the id)
         */
        String docId = DocumentsContract.getDocumentId(selectedImage);
        String[] split = docId.split(":");
        String type = split[0];
        String id = split[1];

        Uri contentUri;

        // Check if our content is an image
        if ("image".equals(type)) {
            /**
             * Here the idea is to query the given the URI, getting a cursor over the result set.
             * To do this, we will make use of the "ContentResolver.query" method. This method
             * receives the following parameters:
             * @param uri The URI to query (will identify the "database" where we will perform the
             *            query)
             * @param projection A list of which columns to return
             * @param selection A filter declaring which rows to return
             * @param selectionArgs You may include ?s in selection, which will be
             *         replaced by the values from selectionArgs, in the order that they
             *         appear in the selection. The values will be bound as Strings.
             * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
             *         clause (excluding the ORDER BY itself)
             *
             * Therefore, at this point we will set our URI as
             *          "MediaStore.Images.Media.EXTERNAL_CONTENT_URI"
             *          (The content:// style URI for the "primary" external storage volume)
             * We will also set our selection parameter to "_id=?", as we want to return the rows
             * with a given id (or in our case the row with the given id), and set our selectionArgs
             * as a String[] containing the id of our document
             */
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String selection = "_id=?";
            String[] selectionArgs = new String[] { id };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else {
            return null;
        }
    }

    /**
     * Query a given URI
     * @param context The context of the application
     * @param uri The URI to query
     * @param selection A filter declaring which rows to return
     * @param selectionArgs The arguments of the selection parameter
     * @return A string containing the result of the query
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;

        // We will only get the "_data" column, so make that our projection (Remember that the
        // projection is a list of the columns to return)
        String column = "_data";
        String[] projection = { column };

        try {
            // Make the query
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);

            // Check if cursors is not null (no entry found). If it is not null move it to the first
            // row. If "cursor.moveToFirst()" returns null then the cursor is empty, so we have a
            // problem
            if (cursor != null && cursor.moveToFirst()) {
                // Get the index based on the column we want, retrieve it and return it
                int index = cursor.getColumnIndexOrThrow(column);
                String result = cursor.getString(index);
                cursor.close();
                return result;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Create a Toast message, with a given content and the maximum duration allowed for Toasts
     * @param context The context of the application
     * @param text The message to display
     */
    public static void createToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Saves a bitmap, based on a drawable element, to a given path
     * @param path The path where the bitmap will be saved
     * @param drawable The drawable element to save
     * @return A boolean value, indicating the success or failure of the operation
     */
    public static boolean saveBitmap(String path, Drawable drawable) {
        File file = new File(path);
        FileOutputStream fileOutputStream;
        boolean result;
        Bitmap bitmap;

        try {
            if (!file.exists()) {
                // Create file
                result = file.createNewFile();
                if (!result) {
                    return false;
                }
                fileOutputStream = new FileOutputStream(file);

                if (drawable == null) {
                    return false;
                }
                bitmap = ((BitmapDrawable) drawable).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
