package in.exuber.usmarket.activity.appintro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.fragment.appintro.AppIntroFourFragment;
import in.exuber.usmarket.fragment.appintro.AppIntroOneFragment;
import in.exuber.usmarket.fragment.appintro.AppIntroThreeFragment;
import in.exuber.usmarket.fragment.appintro.AppIntroTwoFragment;
import in.exuber.usmarket.utils.Constants;

public class AppIntroActivity extends AppCompatActivity {

    //declaring views
    private RelativeLayout appIntroActivityContainer;
    private ViewPager appIntroViewPager;
    private TextView appIntroNavigator;

    //Sharedpreferences
    private SharedPreferences marketPreference;


    //Declaring variables
    private int currentItem;

    //Adapter
    private AppIntroPagerAdapter appIntroPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Initialising variables
        currentItem = 0;

        //Initialising views
        appIntroActivityContainer = findViewById(R.id.activity_app_intro);
        appIntroViewPager = findViewById(R.id.vp_appIntro_viewPager);
        appIntroNavigator = findViewById(R.id.ll_appIntro_dots);

        appIntroPagerAdapter = new AppIntroPagerAdapter(getSupportFragmentManager());
        appIntroViewPager.setAdapter(appIntroPagerAdapter);
        appIntroViewPager.setCurrentItem(currentItem);
        setNavigator();

        appIntroViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {

                setNavigator();
            }
        });


    }

    @Override
    public void onBackPressed() {

    }

    //Func - Set navigator
    public void setNavigator() {
        String navigation = "";

        for (int i = 0; i < appIntroPagerAdapter.getCount(); i++) {
            if (i == appIntroViewPager.getCurrentItem()) {
                navigation += getString(R.string.icon_circle_full)
                        + "  ";
            } else {
                navigation += getString(R.string.icon_circle_empty)
                        + "  ";
            }
        }
        appIntroNavigator.setText(navigation);
    }

    //Func - Done click Appintro
    public void appIntroFinished() {

        //Preference Editor
        SharedPreferences.Editor preferenceEditor = marketPreference.edit();

        preferenceEditor.putBoolean(Constants.IS_APPINTRO_OVER, true);
        preferenceEditor.commit();



        boolean isProductAdded = marketPreference.getBoolean(Constants.IS_PRODUCT_ADDED, false);

        if (isProductAdded)
        {
            //Calling Home activity
            startActivity(new Intent(AppIntroActivity.this, HomeActivity.class));
            finish();
        }
        else
        {
            //Calling Home Add product activity
            startActivity(new Intent(AppIntroActivity.this, HomeAddProductsActivity.class));
            finish();
        }


    }


    //View pager Adapter
    public class AppIntroPagerAdapter extends FragmentPagerAdapter {

        public AppIntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0)
            {
                return new AppIntroOneFragment(AppIntroActivity.this);

            } else if (position == 1)
            {
                return new AppIntroTwoFragment(AppIntroActivity.this);

            }else if (position == 2)
            {
                return new AppIntroThreeFragment(AppIntroActivity.this);
            }
            else
            {

                return new AppIntroFourFragment(AppIntroActivity.this);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
