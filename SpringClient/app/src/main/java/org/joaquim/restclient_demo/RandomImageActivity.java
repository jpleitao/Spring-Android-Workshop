package org.joaquim.restclient_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.joaquim.restclient_demo.server.RESTHandler;
import org.joaquim.restclient_demo.server.RESTHandlerReturn;
import org.joaquim.restclient_demo.utils.Utils;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class RandomImageActivity extends AppCompatActivity {

    private ImageView imageView1;
    private TextView memeTextView;
    private Drawable[] drawables = null;
    private String drawableName;
    private String[] quotes;
    private Random random;
    private Drawable drawable;
    private Bitmap bitmapProcessed;
    private String bitmapProcessedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_image);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button spinButton = (Button) findViewById(R.id.spin);
        imageView1 = (ImageView) findViewById(R.id.imageView);
        memeTextView = (TextView) findViewById(R.id.memeText);

        drawables = new Drawable[]{
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.jabba11),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.prenos1),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.robot1),
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.yoda)
        };

        drawableName = "";
        random = new Random();

        quotes = getResources().getStringArray(R.array.star_wars_quotes);

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get random number
                int rand = random.nextInt(drawables.length);

                // Select the image and the quote and assign them to their respective fields
                drawable = drawables[rand];
                imageView1.setImageDrawable(drawable);
                bitmapProcessed = null;
                bitmapProcessedTitle = null;

                // Get the name of the selected image
                String drawableNames[] = getResources().getStringArray(R.array.drawable_names);
                drawableName = drawableNames[rand];

                // Generate new random number for the quote
                rand = random.nextInt(quotes.length);

                String quote = quotes[rand];
                char quoteArray[] = quote.toCharArray();
                memeTextView.setText(quoteArray, 0, quoteArray.length);
            }
        });
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

    public void uploadImage(View view) {
        RESTHandlerReturn result;
        String memeText = memeTextView.getText().toString();
        String drawablePath = getApplicationContext().getFilesDir().getPath() + "/" + drawableName;

        bitmapProcessed = null;
        bitmapProcessedTitle = null;

        if (drawableName.length() == 0) {
            // No image was selected
            Utils.createToast(getApplicationContext(), "No image was selected");
            return ;
        } else if (memeText.length() == 0) {
            // No meme text was assigned
            Utils.createToast(getApplicationContext(), "Missing meme text");
            return ;
        }

        try {
            // Upload the file to the REST Service and get the processed image
            RESTHandler restHandler = new RESTHandler(drawablePath);
            result = restHandler.execute(memeText).get();

            bitmapProcessed = result.getBitmap();
            bitmapProcessedTitle = result.getBitmapTitle();

        } catch (InterruptedException | ExecutionException e) {
            // If there is an exception during the process just set the result variable to
            // null, signalling that something went wrong
            e.printStackTrace();
            result = null;
        }

        if (result != null) {
            if (result.getErrorMessage() == null) {
                // If nothing went wrong simply apply the received bitmap
                imageView1.setImageBitmap(result.getBitmap());
            }else {
                Utils.createToast(getApplicationContext(), result.getErrorMessage());
            }
        } else {
            Utils.createToast(getApplicationContext(), "Exception while requesting the operation " +
                    "to the server");
        }
    }
}
