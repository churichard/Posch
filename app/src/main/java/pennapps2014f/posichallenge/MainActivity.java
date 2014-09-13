package pennapps2014f.posichallenge;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    // Resources
    Resources res;
    // Random generator
    Random randomGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialization
        randomGen = new Random();
        res = getResources();
        // Sets challenge and background color
        setText();
        setColor();
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
        String[] challenges = res.getStringArray(R.array.challenges_array);
        // Sets textView properties
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setTypeface(font);
        // Sets the challenge
        textView.append("Today, challenge yourself by " + challenges[randomGen.nextInt(challenges.length)]);

    }

    // Generates a random background color to display
    public void setColor(){
        // Find root view
        View someView = findViewById(R.id.textView1);
        View root = someView.getRootView();
        // Initializes color array
        String[] colors = res.getStringArray(R.array.colors_array);
        // Set the color
        root.setBackgroundColor(Color.parseColor(colors[randomGen.nextInt(colors.length)]));
    }
}