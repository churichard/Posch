package pennapps2014f.posch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pennapps2014f.posch.R;


public class MainActivity extends android.support.v4.app.FragmentActivity {
    // Calendar fragment
    public static HomeFragment homeFragment;
    public static ProgressFragment progressFragment;

    // Storage of dates and completions/incompletions
    public static SharedPreferences dateStorage;
    public static SharedPreferences.Editor editor;

    // Alarm manager
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    // Drawer stuff
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private ArrayList<String> drawerListItems;
    private DrawerListAdapter drawerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_frame_layout);

        homeFragment = new HomeFragment();
        progressFragment = new ProgressFragment();

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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else{
            super.onBackPressed();
        }
    }

    public void completeChallenge(View v) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());

        editor.putString(dateFormat.format(today), "complete");
        progressFragment.setDateComplete(today);
        Toast.makeText(this, "You're awesome, keep up the positivity.", Toast.LENGTH_LONG).show();
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