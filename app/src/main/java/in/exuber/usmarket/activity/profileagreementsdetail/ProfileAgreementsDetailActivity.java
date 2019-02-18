package in.exuber.usmarket.activity.profileagreementsdetail;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import in.exuber.usmarket.R;
import in.exuber.usmarket.models.agreement.AgreementOutput;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

public class ProfileAgreementsDetailActivity extends AppCompatActivity {

    //Declaring views
    private LinearLayout profileAgreementsDetailActivityContainer;
    private TextView toolbarHeader;

    private TextView agreementDescription;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_agreements_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);


        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising views
        profileAgreementsDetailActivityContainer = findViewById(R.id.activity_profile_agreements_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        agreementDescription = findViewById(R.id.tv_profileAgreementsDetail_agreementDescription);

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String agreementItemString = passedBundle.getString("selectedAgreement");

        //Converting string to Object
        Gson gson = new Gson();
        AgreementOutput agreementOutput = gson.fromJson(agreementItemString, AgreementOutput.class);

        //Setting toolbar header
        toolbarHeader.setText(agreementOutput.getAgreementHeader());

        //Setting values
        agreementDescription.setText(agreementOutput.getAgreementDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dummy, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {

        finish();

    }

}
