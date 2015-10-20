package org.joaquim.restclient_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.joaquim.restclient_demo.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Try to save the base images to the phone's internal memory
        boolean result = saveImagesToInternalStorage();

        if (!result) {
            // Create Toast to display error message
            String message = "Could not save resource images to device. Random file upload may not" +
                    "function properly";
            Utils.createToast(getApplicationContext(), message);
        }

        TextView uploadTextView = (TextView) findViewById(R.id.textView);
        TextView randomTextView = (TextView) findViewById(R.id.textView2);


        uploadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(open);
            }
        });

        randomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRandom = new Intent(getApplicationContext(), RandomImageActivity.class);
                startActivity(openRandom);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean saveImagesToInternalStorage() {
        boolean result;

        // jabba11.png
        result = Utils.saveBitmap(getApplicationContext().getFilesDir().getPath() + "/jabba11.png",
                                  ContextCompat.getDrawable(getApplicationContext(),
                                          R.drawable.jabba11));
        if(!result)
            return false;

        // prenos1.png
        result = Utils.saveBitmap(getApplicationContext().getFilesDir().getPath() + "/prenos1.png",
                                  ContextCompat.getDrawable(getApplicationContext(),
                                                            R.drawable.prenos1));
        if(!result)
            return false;

        // robot1.png
        result = Utils.saveBitmap(getApplicationContext().getFilesDir().getPath() + "/robot1.png",
                                  ContextCompat.getDrawable(getApplicationContext(),
                                                            R.drawable.robot1));
        if(!result)
            return false;

        // yoda.png
        result = Utils.saveBitmap(getApplicationContext().getFilesDir().getPath() + "/yoda.png",
                                  ContextCompat.getDrawable(getApplicationContext(),
                                          R.drawable.yoda));
        return result;
    }
}
