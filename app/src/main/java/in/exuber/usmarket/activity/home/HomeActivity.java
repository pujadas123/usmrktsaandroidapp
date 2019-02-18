package in.exuber.usmarket.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.faq.FaqActivity;
import in.exuber.usmarket.activity.glossary.GlossaryActivity;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.activity.splash.SplashActivity;
import in.exuber.usmarket.fragment.campaign.CampaignHomeFragment;
import in.exuber.usmarket.fragment.leads.LeadsHomeFragment;
import in.exuber.usmarket.fragment.product.ProductHomeFragment;
import in.exuber.usmarket.fragment.ProfileHomeFragment;
import in.exuber.usmarket.utils.Constants;

import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private RelativeLayout homeActivityContainer;
    private FrameLayout homeFrameLayout;
    private BottomBar homeBotoomBar;

    private ImageView homeFab;

    private BottomBarTab productsTab, campaignTab;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Dialog
    public Dialog homeFabDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising firebase
        FirebaseApp.initializeApp(HomeActivity.this);

        //Initialising views
        homeActivityContainer =  findViewById(R.id.activity_home);
        homeFrameLayout =  findViewById(R.id.fl_home_frameLayout);
        homeBotoomBar =  findViewById(R.id.bb_home_bottomBar);

        homeFab = findViewById(R.id.fab_home_fabIcon);

        productsTab = homeBotoomBar.getTabWithId(R.id.tab_home_products);
        campaignTab = homeBotoomBar.getTabWithId(R.id.tab_home_campaigns);




        //Setting bottom bar
        for (int i = 0; i < homeBotoomBar.getTabCount(); i++) {
            homeBotoomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL);
        }

        homeBotoomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {


                switch (tabId) {

                    case R.id.tab_home_products:

                        //Hiding Keyboard
                        hideKeyBoard(HomeActivity.this);


                        replace_fragment(new ProductHomeFragment());

                        break;

                    case R.id.tab_home_campaigns:

                        //Hiding Keyboard
                        hideKeyBoard(HomeActivity.this);

                        replace_fragment(new CampaignHomeFragment());

                        break;

                    case R.id.tab_home_leads:

                        //Hiding Keyboard
                        hideKeyBoard(HomeActivity.this);

                        replace_fragment(new LeadsHomeFragment());

                        break;

                    case R.id.tab_home_profile:

                        //Hiding Keyboard
                        hideKeyBoard(HomeActivity.this);

                        replace_fragment(new ProfileHomeFragment());

                        break;



                }


            }
        });

        //Setting onclick
        homeFab.setOnClickListener(this);



        //Checking Notification Click
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            if(extras.getBoolean(Constants.IS_NOTIFICATION_CLICK))
            {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
            }

        }


        String generatedToken = FirebaseInstanceId.getInstance().getToken();
        if (generatedToken!=null)
        {
            Log.e("Firebase Token", generatedToken);
        }
        else
        {
            Log.e("Firebase Token", "Empty");
        }


    }

    @Override
    public void onBackPressed() {

        finish();


    }

    //Func - Replace Fragment
    public void replace_fragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_home_frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.fab_home_fabIcon:

                // creating custom Floating Action button
                showCustomChatDialog();

                break;
        }

    }

    //FUNC - Showing fab dialog
    public void showCustomChatDialog() {

        homeFabDialog = new Dialog(HomeActivity.this);
        homeFabDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        homeFabDialog.setContentView(R.layout.dialog_home_fab);
        homeFabDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = homeFabDialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        homeFabDialog.setCanceledOnTouchOutside(true);

        View clossDialog = homeFabDialog.findViewById(R.id.rl_homeFab_closs);
        clossDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeFabDialog.dismiss();
            }
        });


        View glossaryDialog = homeFabDialog.findViewById(R.id.ll_homeFab_glossaryClick);
        glossaryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, GlossaryActivity.class);
                startActivity(intent);

                homeFabDialog.dismiss();
            }
        });

        View faqDialog = homeFabDialog.findViewById(R.id.ll_homeFab_faqClick);
        faqDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, FaqActivity.class);
                startActivity(intent);

                homeFabDialog.dismiss();
            }
        });


        // it show the dialog box
        homeFabDialog.show();

    }
}
