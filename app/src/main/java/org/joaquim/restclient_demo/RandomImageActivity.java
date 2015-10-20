package org.joaquim.restclient_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

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

    public void saveImage(View view) {
    }

    public void uploadImage(View view) {
    }
}
