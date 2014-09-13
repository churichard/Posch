package pennapps2014f.posichallenge;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ProgressActivity extends ActionBarActivity {
    private static CaldroidFragment caldroidFragment = new CaldroidFragment();

    // Drawer stuff
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private ArrayList<String> drawerListItems;
    private DrawerListAdapter drawerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress);

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        if(caldroidFragment.getArguments() == null) {
            caldroidFragment.setArguments(args);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendarContainer, caldroidFragment);
        transaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutProgress);
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
        drawerList = (ListView) findViewById(R.id.drawerListProgress);
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

        // Redisplay complete/incomplete challenges
        for(Map.Entry<String, ?> entry : MainActivity.dateStorage.getAll().entrySet()) {
            java.sql.Date date = java.sql.Date.valueOf(entry.getKey());

            if((entry.getValue()).equals("complete")) {
                ProgressActivity.setDateComplete(date);
            } else if((entry.getValue()).equals("incomplete")) {
                ProgressActivity.setDateIncomplete(date);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progress, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        caldroidFragment.onDestroy();
    }

    public static void setDateComplete(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.complete, date);
        caldroidFragment.setTextColorForDate(R.color.white, date);
        caldroidFragment.refreshView();
    }

    public static void setDateIncomplete(Date date) {
        caldroidFragment.setBackgroundResourceForDate(R.color.incomplete, date);
        caldroidFragment.setTextColorForDate(R.color.white, date);
        caldroidFragment.refreshView();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }

        private void displayView(int position) {
            switch(position) {
                case 0:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case 1:
                    drawerLayout.closeDrawers();
                    break;
                default:
                    break;
            }
        }
    }
}