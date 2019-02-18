package in.exuber.usmarket.activity.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;
import in.exuber.usmarket.utils.Constants;

import static in.exuber.usmarket.utils.UtilMethods.getWindowSize;

public class SplashActivity extends AppCompatActivity {

    //Declaring views
    private ImageView splashImage;

    //Declaring Variables
    private static final int SPLASH_DURATION = 3;

    //Sharedpreferences
    private SharedPreferences marketPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising firebase
        FirebaseApp.initializeApp(SplashActivity.this);

        //Initialising Vies
        splashImage =  findViewById(R.id.iv_splash_splashImage);

        //Setting logo
        setResponsiveSplashLogo();

        //handler for delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Check and Proceed
                checkSessionAndProceed();

            }
        }, SPLASH_DURATION * 1000);




    }

    @Override
    public void onBackPressed() {

    }

    //Func - Setting Splash Screen
    private void setResponsiveSplashLogo() {


        //Setting responsive width
        splashImage.getLayoutParams().width = (int) (getWindowSize(this).x * 0.40);
        splashImage.getLayoutParams().height = (int) (getWindowSize(this).x * 0.40);
    }

    //Func - Checking Session
    private void checkSessionAndProceed()
    {

        boolean isLoggedIn = marketPreference.getBoolean(Constants.IS_LOGGED_IN, false);

        if (isLoggedIn)
        {
            boolean isAppIntroOver = marketPreference.getBoolean(Constants.IS_APPINTRO_OVER, false);

            if (isAppIntroOver) {

                boolean isProductAdded = marketPreference.getBoolean(Constants.IS_PRODUCT_ADDED, false);

                if (isProductAdded)
                {
                    Bundle extras = getIntent().getExtras();

                    if (extras == null) {

                        //Calling Home activity
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        if(extras.getBoolean(Constants.IS_NOTIFICATION_CLICK))
                        {
                            Intent homeIntent = new Intent(SplashActivity.this,HomeActivity.class);
                            homeIntent.putExtra(Constants.IS_NOTIFICATION_CLICK,true);
                            startActivity(homeIntent);
                            finish();
                        }
                        else
                        {
                            //Calling Home activity
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                        }

                    }

                }
                else
                {
                    //Calling Home Add product activity
                    startActivity(new Intent(SplashActivity.this, HomeAddProductsActivity.class));
                    finish();
                }




            }
            else
            {
                //Calling App Intro activity
                startActivity(new Intent(SplashActivity.this, AppIntroActivity.class));
                finish();
            }

        }
        else
        {

            //Calling Login Signup activity
            startActivity(new Intent(SplashActivity.this, LoginSignupActivity.class));
            finish();


        }

    }
}
