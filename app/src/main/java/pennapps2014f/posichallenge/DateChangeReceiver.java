package pennapps2014f.posichallenge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateChangeReceiver extends BroadcastReceiver {
    public DateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if(action.equals(Intent.ACTION_DATE_CHANGED)) {

        }
    }
}
