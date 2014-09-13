package pennapps2014f.posichallenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateChangeReceiver extends BroadcastReceiver {

    public DateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DateChange", "Midnight has passed");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date yesterday = calendar.getTime();

        if(MainActivity.dateStorage.getString(dateFormat.format(yesterday), "no entry").equals("no entry")) {
            ProgressActivity.setDateIncomplete(yesterday);
            MainActivity.editor.putString(dateFormat.format(yesterday), "incomplete");
        }
    }
}
