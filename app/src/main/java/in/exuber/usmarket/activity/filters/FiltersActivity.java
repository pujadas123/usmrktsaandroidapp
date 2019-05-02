package in.exuber.usmarket.activity.filters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.paidcommissions.PaidCommissionsActivity;

public class FiltersActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_homeProfile_toolBar_doneClick;
    RelativeLayout rl_toolBar_clearAllLayout;

    LinearLayout ll_realEstateClick,ll_investmentClick,ll_januaryClick,ll_februaryClick,ll_marchClick,ll_aprilClick,ll_mayClick,
            ll_juneClick,ll_julyClick,ll_augustClick,ll_septemberClick,ll_octoberClick,ll_novemberClick,ll_decemberClick,ll_thisYearClick,
            ll_previousYearClick,ll_historicalClick;
    CheckBox chkb_realEstate,chkb_investment,chkb_january,chkb_february,chkb_march,chkb_april,chkb_may,chkb_june,chkb_july,chkb_august,
            chkb_september,chkb_october,chkb_november,chkb_december,chkb_thisYear,chkb_previousYear,chkb_historical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ll_homeProfile_toolBar_doneClick=findViewById(R.id.ll_homeProfile_toolBar_doneClick);
        ll_homeProfile_toolBar_doneClick.setOnClickListener(this);
        rl_toolBar_clearAllLayout=findViewById(R.id.rl_toolBar_clearAllLayout);
        rl_toolBar_clearAllLayout.setOnClickListener(this);

        ll_realEstateClick=findViewById(R.id.ll_realEstateClick);
        ll_investmentClick=findViewById(R.id.ll_investmentClick);
        ll_januaryClick=findViewById(R.id.ll_januaryClick);
        ll_februaryClick=findViewById(R.id.ll_februaryClick);
        ll_marchClick=findViewById(R.id.ll_marchClick);
        ll_aprilClick=findViewById(R.id.ll_aprilClick);
        ll_mayClick=findViewById(R.id.ll_mayClick);
        ll_juneClick=findViewById(R.id.ll_juneClick);
        ll_julyClick=findViewById(R.id.ll_julyClick);
        ll_augustClick=findViewById(R.id.ll_augustClick);
        ll_septemberClick=findViewById(R.id.ll_septemberClick);
        ll_octoberClick=findViewById(R.id.ll_octoberClick);
        ll_novemberClick=findViewById(R.id.ll_novemberClick);
        ll_decemberClick=findViewById(R.id.ll_decemberClick);
        ll_thisYearClick=findViewById(R.id.ll_thisYearClick);
        ll_previousYearClick=findViewById(R.id.ll_previousYearClick);
        ll_historicalClick=findViewById(R.id.ll_historicalClick);

        chkb_realEstate=findViewById(R.id.chkb_realEstate);
        chkb_investment=findViewById(R.id.chkb_investment);
        chkb_january=findViewById(R.id.chkb_january);
        chkb_february=findViewById(R.id.chkb_february);
        chkb_march=findViewById(R.id.chkb_march);
        chkb_april=findViewById(R.id.chkb_april);
        chkb_may=findViewById(R.id.chkb_may);
        chkb_june=findViewById(R.id.chkb_june);
        chkb_july=findViewById(R.id.chkb_july);
        chkb_august=findViewById(R.id.chkb_august);
        chkb_september=findViewById(R.id.chkb_september);
        chkb_october=findViewById(R.id.chkb_october);
        chkb_november=findViewById(R.id.chkb_november);
        chkb_december=findViewById(R.id.chkb_december);
        chkb_thisYear=findViewById(R.id.chkb_thisYear);
        chkb_previousYear=findViewById(R.id.chkb_previousYear);
        chkb_historical=findViewById(R.id.chkb_historical);
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
                chkb_realEstate.setChecked(false);
                chkb_investment.setChecked(false);
                chkb_january.setChecked(false);
                chkb_february.setChecked(false);
                chkb_march.setChecked(false);
                chkb_april.setChecked(false);
                chkb_may.setChecked(false);
                chkb_june.setChecked(false);
                chkb_july.setChecked(false);
                chkb_august.setChecked(false);
                chkb_september.setChecked(false);
                chkb_october.setChecked(false);
                chkb_november.setChecked(false);
                chkb_december.setChecked(false);
                chkb_thisYear.setChecked(false);
                chkb_previousYear.setChecked(false);
                chkb_historical.setChecked(false);
                break;
        }
    }
}
