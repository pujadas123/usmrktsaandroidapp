package in.exuber.usmarket.activity.filters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;

public class FiltersActivity extends AppCompatActivity {

    LinearLayout ll_homeProfile_toolBar_doneClick;
    RelativeLayout rl_toolBar_clearAllLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
    }
}
