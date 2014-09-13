package pennapps2014f.posichallenge;

import android.os.Bundle;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Date;
import java.util.Map;

public class ProgressFragment extends CaldroidFragment {

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redisplay complete/incomplete challenges
        for(Map.Entry<String, ?> entry : MainActivity.dateStorage.getAll().entrySet()) {
            java.sql.Date date = java.sql.Date.valueOf(entry.getKey());

            if((entry.getValue()).equals("complete")) {
                setDateComplete(date);
            } else if((entry.getValue()).equals("incomplete")) {
                setDateIncomplete(date);
            }
        }
        refreshView();
    }

    public void setDateComplete(Date date) {
        setBackgroundResourceForDate(R.color.complete, date);
        setTextColorForDate(R.color.white, date);
        refreshView();
    }

    public void setDateIncomplete(Date date) {
        setBackgroundResourceForDate(R.color.incomplete, date);
        setTextColorForDate(R.color.white, date);
        refreshView();
    }
}
