package org.joaquim.restclient_demo.server;

import android.graphics.Bitmap;

public class RESTHandlerReturn {

    private Bitmap bitmap;
    private String errorMessage;
    private String bitmapTitle;

    public RESTHandlerReturn(Bitmap bitmap, String errorMessage, String bitmapTitle) {
        this.bitmap = bitmap;
        this.errorMessage = errorMessage;
        this.bitmapTitle = bitmapTitle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getBitmapTitle() {
        return bitmapTitle;
    }
}