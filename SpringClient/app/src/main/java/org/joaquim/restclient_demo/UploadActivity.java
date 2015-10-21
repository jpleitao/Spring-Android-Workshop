package org.joaquim.restclient_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.joaquim.restclient_demo.server.RESTHandler;
import org.joaquim.restclient_demo.server.RESTHandlerReturn;
import org.joaquim.restclient_demo.utils.Utils;

import java.util.concurrent.ExecutionException;

public class UploadActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private String picturePath;
    private Bitmap bitmapProcessed;
    private String bitmapProcessedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button bFind = (Button) findViewById(R.id.find);
        bFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Create the intent to pick the image from the gallery:
                 * Set the intent's type to "image/*"
                 * Set the intent's action to "ACTION_GET_CONTENT"
                 * Use the startActivityForResult to start the activity and get its result (the
                 * selected image from the gallery)
                 */
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check for the result of the intent created when the button was pressed
        // (and check if we have data for that intent)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            // Use the Utils package to get the path of the selected image from its URI
            picturePath = Utils.getFullPath(selectedImage, getApplicationContext());

            bitmapProcessed = null;
            bitmapProcessedTitle = null;

            if (picturePath == null) {
                // Could not get the path
                Utils.createToast(getApplicationContext(), "Could not get the selected image's path");
                return;
            }

            // Set the selected image as the content for the ImageView in this Activity's view
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                // Back arrow on top
                Intent nazaj = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nazaj);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the onClick event of the upload button of this activity's view, sending the selected
     * image to the server, receiving its response image and displaying it on the screen
     * @param view The activity's view
     */
    public void uploadImage(View view) {
        RESTHandlerReturn result;
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

        bitmapProcessed = null;
        bitmapProcessedTitle = null;

        // Get the text from the edit text
        EditText editText = (EditText) findViewById(R.id.memeText2);
        String memeText = editText.getText().toString();

        if (picturePath.equals("")) {
            // No image selected -- Return the method
            Utils.createToast(getApplicationContext(), "No image was selected");
            return ;
        } else if (memeText.equals("")) {
            // No meme text selected -- Return the method
            Utils.createToast(getApplicationContext(), "Missing meme text");
            return ;
        }

        try {
            // Upload the file to the REST Service and get the processed image
            RESTHandler restHandler = new RESTHandler(picturePath);
            result = restHandler.execute(memeText).get();
        } catch (InterruptedException | ExecutionException e) {
            // If there is an exception during the process just set the result variable to
            // null, signalling that something went wrong
            e.printStackTrace();
            result = null;
        }

        if (result != null) {
            if (result.getErrorMessage() == null) {
                // If nothing went wrong simply apply the received bitmap

                bitmapProcessed = result.getBitmap();
                bitmapProcessedTitle = result.getBitmapTitle();
                imageView.setImageBitmap(bitmapProcessed);

            } else {
                Utils.createToast(getApplicationContext(), result.getErrorMessage());
            }
        } else {
            Utils.createToast(getApplicationContext(), "Exception while requesting the operation " +
                    "to the server");
        }

    }

    /**
     * Saves the selected image into the phone's memory
     * @param view The activity view
     */
    public void saveImage(View view) {
        if (bitmapProcessed != null) {
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmapProcessed,
                                                bitmapProcessedTitle, "");
        } else {
            Utils.createToast(getApplicationContext(), "Could not save the image");
        }
    }

}
