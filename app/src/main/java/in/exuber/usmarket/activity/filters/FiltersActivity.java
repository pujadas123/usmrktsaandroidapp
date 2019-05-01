package in.exuber.usmarket.activity.filters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.paidcommissions.PaidCommissionsActivity;

public class FiltersActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_homeProfile_toolBar_doneClick;
    RelativeLayout rl_toolBar_clearAllLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ll_homeProfile_toolBar_doneClick=findViewById(R.id.ll_homeProfile_toolBar_doneClick);
        ll_homeProfile_toolBar_doneClick.setOnClickListener(this);
        rl_toolBar_clearAllLayout=findViewById(R.id.rl_toolBar_clearAllLayout);
        rl_toolBar_clearAllLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ll_homeProfile_toolBar_doneClick:
                Intent intent =new Intent(FiltersActivity.this, PaidCommissionsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_toolBar_clearAllLayout:

                break;
        }
    }
}
