package pennapps2014f.posichallenge;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import cz.fhucho.android.util.SimpleDiskCache;

public class MainActivity extends Activity {
    public static SimpleDiskCache dateCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 5; // 5 MB

    // Resources
    Resources res;
    // Random generator
    Random randomGen;
    // Challenges
    String[] challenges;
    // Challenge text
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // Initialization
        randomGen = new Random();
        res = getResources();

        // Create/open cache
        try {
            File cacheDir = getFilesDir();
            dateCache = SimpleDiskCache.open(cacheDir, 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sets challenge and background color
        setText();
        setColor();

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Does some stuff
            }
        });

        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Generates a random number
                int randNum = randomGen.nextInt(challenges.length);
                // Sets the challenge
                textView.setText("Today, positively challenge yourself by " + challenges[randNum]);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Generates a random challenge to display
    public void setText(){
        // Creates new font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/HansKendrick-Regular.ttf");
        // Initializes string array
        challenges = res.getStringArray(R.array.challenges_array);
        // Sets textView properties
        textView = (TextView) findViewById(R.id.textView1);
        textView.setTypeface(font);
        // Generates a random number
        int randNum = randomGen.nextInt(challenges.length);
        // Sets the challenge
        textView.setText("Today, positively challenge yourself by " + challenges[randNum]);

    }

    // Generates random colors to display
    public void setColor(){
        // Find root view
        View buttonView = findViewById(R.id.button1);
        View root = buttonView.getRootView();
        // Initializes color array
        String[] colors = res.getStringArray(R.array.colors_array);
        // Generates a random number
        int randNum = randomGen.nextInt(colors.length);
        // Set the background color
        root.setBackgroundColor(Color.parseColor(colors[randNum]));
    }
}