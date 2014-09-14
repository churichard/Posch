package pennapps2014f.posch;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class HomeFragment extends Fragment {
    private Activity mainActivity;
    // Resources
    private Resources res;
    // Random generator
    private Random randomGen;
    // Challenges
    private String[] challenges;
    // Colors
    private String[] colors;
    // Challenge text
    private View fragmentView;
    private TextView textView;
    // Root view
    private View root;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.mainActivity = activity;
        // Initialization
        randomGen = new Random();
        res = mainActivity.getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView) fragmentView.findViewById(R.id.textView1);

        final Button button2 = (Button) fragmentView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!MainActivity.storage.getBoolean("challengeSkipped", false)) {
                    // Generates a random number
                    int randNum = randomGen.nextInt(challenges.length);
                    // Sets the challenge
                    textView.setText("Today, positively challenge yourself by " + challenges[randNum]);
                    MainActivity.editor.putString("currentChallenge", challenges[randNum]);
                    MainActivity.editor.putBoolean("challengeSkipped", true);
                    // Generates a random number
                    randNum = randomGen.nextInt(colors.length);
                    // Set the background color
                    root.setBackgroundColor(Color.parseColor(colors[randNum]));
                    MainActivity.editor.putString("currentColor", colors[randNum]);
                    MainActivity.editor.commit();
                }
                else{
                    Toast.makeText(MainActivity.applicationContext, "You've already used up your skip for today. Just do the challenge already.", Toast.LENGTH_LONG).show();
                }
            }
        });

        setColor();
        setText();
        setButtons();

        return fragmentView;
    }

    // Generates a random challenge to display
    public void setText(){
        // Creates new font
        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/HansKendrick-Regular.ttf");
        if (MainActivity.storage.getBoolean("newDay", true)) {
            // Initializes string array
            challenges = res.getStringArray(R.array.challenges_array);
            // Sets textView properties
            textView = (TextView) fragmentView.findViewById(R.id.textView1);
            textView.setTypeface(font);
            // Generates a random number
            int randNum = randomGen.nextInt(challenges.length);
            // Sets the challenge
            textView.setText("Today, positively challenge yourself by " + challenges[randNum]);
            MainActivity.editor.putString("currentChallenge", challenges[randNum]);
            MainActivity.editor.putBoolean("newDay", false);
            MainActivity.editor.commit();
        }
        else{
            textView.setText("Today, positively challenge yourself by " + MainActivity.storage.getString("currentChallenge", "N/A"));
        }
    }

    // Generates random colors to display
    public void setColor(){
        // Find root view
        View buttonView = fragmentView.findViewById(R.id.button1);
        root = buttonView.getRootView();
        if (MainActivity.storage.getBoolean("newDay", true)) {
            // Initializes color array
            colors = res.getStringArray(R.array.colors_array);
            // Generates a random number
            int randNum = randomGen.nextInt(colors.length);
            // Set the background color
            root.setBackgroundColor(Color.parseColor(colors[randNum]));
            MainActivity.editor.putString("currentColor", colors[randNum]);
            MainActivity.editor.commit();
        }
        else{
            root.setBackgroundColor(Color.parseColor(MainActivity.storage.getString("currentColor", "N/A")));
        }
    }

    // Manages the buttons
    public void setButtons(){
        View buttonView = fragmentView.findViewById(R.id.button1);
        TextView textView = (TextView) fragmentView.findViewById(R.id.textView2);

        // Sets challenge finished button visibility
        if (!MainActivity.storage.getBoolean("buttonVisibility", true)) {
            // Hide button and display a message
            buttonView.setVisibility(View.INVISIBLE);
        }
        else {
            buttonView.setVisibility(View.VISIBLE);
        }
        // Sets the challenge finished text visibility
        if (MainActivity.storage.getBoolean("challengeFinishText", false)) {
            textView.setText("Nice, you've completed your challenge for today.");
        }
        else{
            textView.setText("");
        }
    }
}
