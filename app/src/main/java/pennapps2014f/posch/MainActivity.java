package pennapps2014f.posch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends android.support.v4.app.FragmentActivity {
    // Calendar fragment
    public static HomeFragment homeFragment;
    public static ProgressFragment progressFragment;

    // Date completion storage
    public static SharedPreferences dateStorage;
    public static SharedPreferences.Editor dateEditor;

    // Drawer stuff
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private ArrayList<String> drawerListItems;
    private DrawerListAdapter drawerListAdapter;

    // Challenge maintenance
    public static Context applicationContext;
    public static SharedPreferences storage; // App storage
    public static SharedPreferences.Editor editor; // App storage editor

    // Facebook
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_frame_layout);

        homeFragment = new HomeFragment();
        progressFragment = new ProgressFragment();

        applicationContext = getApplicationContext();

        dateStorage = getSharedPreferences("DatePrefs", Context.MODE_PRIVATE);
        dateEditor = dateStorage.edit();
        storage = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        editor = storage.edit();

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
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

        drawerList = (ListView) findViewById(R.id.drawerList);
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();

        // Midnight tonight
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTime(new Date());
        calEnd.set(Calendar.DAY_OF_YEAR, calEnd.get(Calendar.DAY_OF_YEAR)+1);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int midnightTonight = Integer.parseInt(dateFormat.format(calEnd.getTime()).toString());
        Log.d("loggy", "Midnight: " + Integer.toString(midnightTonight));

        editor.putInt("midnight", midnightTonight);
        editor.commit();
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
    protected void onResume(){
        super.onResume();
        uiHelper.onResume();

        Calendar cal = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int currTime = Integer.parseInt(dateFormat.format(cal.getTime()).toString());
        Log.d("loggy", "Current Time: " + currTime);

        if (currTime >= storage.getInt("midnight", 999999999)){
            if (!MainActivity.storage.getBoolean("challengeFinished", false)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                cal.add(Calendar.DATE, -1);
                dateEditor.putString(format.format(cal.getTime()), "incomplete");
                MainActivity.progressFragment.setDateIncomplete(cal.getTime());
            }

            findViewById(R.id.shareChallengeButton).setVisibility(View.INVISIBLE);

            editor.putBoolean("newDay", true); // If the app is opened on a new day
            editor.putBoolean("challengeFinished", false); // If the challenge is finished or not
            editor.putBoolean("challengeSkipped", false); // If a challenge was skipped or not
            editor.putBoolean("challengeFinishText", false); // Text that appears when a challenge is finished
            editor.putBoolean("buttonVisibility", true); // Whether or not the finish challenge button is visible
            editor.putString("currentChallenge", "N/A"); // Challenge of the day
            editor.putString("currentColor", "N/A"); // Color of the day
            editor.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.commit();
        dateEditor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        uiHelper.onActivityResult(requestCode, resultCode, intent, new FacebookDialog.Callback() {
            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("FacebookAuth", "Success");
            }

            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("FacebookAuth", error.toString());
            }
        });
    }

    public void completeChallenge(View v) {
        if (!storage.getBoolean("challengeFinished", false)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date(System.currentTimeMillis());

            dateEditor.putString(dateFormat.format(today), "complete");
            progressFragment.setDateComplete(today);
            Toast.makeText(this, "You're awesome, keep up the positivity.", Toast.LENGTH_LONG).show();

            // Hide button and show text
            View buttonView = findViewById(R.id.button1);
            buttonView.setVisibility(View.INVISIBLE);
            findViewById(R.id.shareChallengeButton).setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.textView2);
            textView.setText("Nice, you've completed your challenge for today.");

            editor.putBoolean("buttonVisibility", false);
            editor.putBoolean("challengeFinishText", true);
            editor.putBoolean("challengeFinished", true);
            editor.commit();
        }
    }

    public void shareToFacebook(View v) {
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            String currentChallenge = storage.getString("currentChallenge", "N/A");

            if(!currentChallenge.equals("N/A")) {
                // Publish the post using the Share Dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setName("Posch")
                        .setDescription("I just completed a challenge on Posch! I challenged myself by " + currentChallenge)
                        .setLink("https://github.com/churichard/Posch")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } else {
                // Publish the post using the Share Dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                        .setName("Posch")
                        .setDescription("I just completed a challenge on Posch!")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            }

        } else {
            Toast.makeText(this, "Facebook app isn't installed on your phone", Toast.LENGTH_LONG).show();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }

        private void displayView(int position) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch(position) {
                case 0:
                    getSupportFragmentManager().popBackStack("progress", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    transaction.replace(R.id.fragmentContainer, homeFragment).commit();
                    break;
                case 1:
                    Bundle args = new Bundle();
                    Calendar cal = Calendar.getInstance();
                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                    if(progressFragment.getArguments() == null) {
                        progressFragment.setArguments(args);
                    }

                    transaction.replace(R.id.fragmentContainer, progressFragment);
                    transaction.addToBackStack("progress");
                    transaction.commit();
                    break;
                default:
                    break;
            }
            drawerLayout.closeDrawers();
        }
    }
}