package pennapps2014f.posichallenge;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends Activity {
    // Storage of dates and completions/incompletions
    public static SharedPreferences dateStorage;
    public static SharedPreferences.Editor editor;

    // Alarm manager
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    // Resources
    private Resources res;
    // Random generator
    private Random randomGen;
    // Challenges
    private String[] challenges;
    // Challenge text
    private TextView textView;

    // Drawer stuff
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private ArrayList<String> drawerListItems;
    private DrawerListAdapter drawerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialization
        randomGen = new Random();
        res = getResources();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerList = (ListView) findViewById(R.id.drawerListMain);
        drawerListItems = new ArrayList<String>();
        drawerListItems.add("Home");
        drawerListItems.add("My Progress");
        drawerListAdapter = new DrawerListAdapter(getApplicationContext(), drawerListItems);
        drawerList.setAdapter(drawerListAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        dateStorage = getPreferences(Context.MODE_PRIVATE);
        editor = dateStorage.edit();

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DateChangeReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set to broadcast to DateChangeReceiver every midnight
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        // Sets challenge and background color
        setText();
        setColor();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        drawerToggle.onConfigurationChanged(config);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.commit();
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

    public void viewProgress(View v) {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }

    public void completeChallenge(View v) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());

        editor.putString(dateFormat.format(today), "complete");
        ProgressActivity.setDateComplete(today);
        Toast.makeText(this, "You're awesome, keep up the positivity.", Toast.LENGTH_LONG).show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }

        private void displayView(int position) {
            switch(position) {
                case 0:
                    drawerLayout.closeDrawers();
                    break;
                case 1:
                    startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}