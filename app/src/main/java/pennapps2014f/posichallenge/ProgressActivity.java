package pennapps2014f.posichallenge;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;

public class ProgressActivity extends ActionBarActivity {
    private static CaldroidFragment caldroidFragment = new CaldroidFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendarContainer, caldroidFragment);
        transaction.commit();

        setContentView(R.layout.activity_progress);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}