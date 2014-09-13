package pennapps2014f.posichallenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.sql.Date;

public class DateChangeReceiver extends BroadcastReceiver {

    public DateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if(action.equals(Intent.ACTION_DATE_CHANGED)) {
            Date date = new Date(System.currentTimeMillis() - 3600000);
            try {
                MainActivity.dateCache.getString(date.toString()).getString();
            } catch (IOException e) {
                ProgressActivity.setDateIncomplete(date);
            }
        }
    }
}
