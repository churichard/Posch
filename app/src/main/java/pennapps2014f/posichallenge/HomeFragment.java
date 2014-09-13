package pennapps2014f.posichallenge;



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

import java.util.Random;

public class HomeFragment extends Fragment {
    private Activity mainActivity;
    // Resources
    private Resources res;
    // Random generator
    private Random randomGen;
    // Challenges
    private String[] challenges;
    // Challenge text
    private View fragmentView;
    private TextView textView;

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
                // Generates a random number
                int randNum = randomGen.nextInt(challenges.length);
                // Sets the challenge
                textView.setText("Today, positively challenge yourself by " + challenges[randNum]);
            }
        });

        setColor();
        setText();

        return fragmentView;
    }

    // Generates a random challenge to display
    public void setText(){
        // Creates new font
        Typeface font = Typeface.createFromAsset(mainActivity.getAssets(), "fonts/HansKendrick-Regular.ttf");
        // Initializes string array
        challenges = res.getStringArray(R.array.challenges_array);
        // Sets textView properties
        textView = (TextView) fragmentView.findViewById(R.id.textView1);
        textView.setTypeface(font);
        // Generates a random number
        int randNum = randomGen.nextInt(challenges.length);
        // Sets the challenge
        textView.setText("Today, positively challenge yourself by " + challenges[randNum]);
    }

    // Generates random colors to display
    public void setColor(){
        // Find root view
        View buttonView = fragmentView.findViewById(R.id.button1);
        View root = buttonView.getRootView();
        // Initializes color array
        String[] colors = res.getStringArray(R.array.colors_array);
        // Generates a random number
        int randNum = randomGen.nextInt(colors.length);
        // Set the background color
        root.setBackgroundColor(Color.parseColor(colors[randNum]));
    }
}
