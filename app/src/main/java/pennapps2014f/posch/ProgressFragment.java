package pennapps2014f.posch;

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
