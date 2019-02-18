package in.exuber.usmarket.activity.loginsignup;

import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.fragment.loginsignup.LoginFragment;
import in.exuber.usmarket.fragment.loginsignup.SignupFragment;
import in.exuber.usmarket.utils.Constants;

public class LoginSignupActivity extends AppCompatActivity {

    //Declaring views
    private CoordinatorLayout loginSignupActivityContainer;
    private TabLayout loginSignupTabLayout;
    private ViewPager loginSignupViewPager;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising views
        loginSignupActivityContainer = findViewById(R.id.activity_login_signup);
        loginSignupViewPager = findViewById(R.id.vp_loginSignUp_viewPager);
        loginSignupTabLayout = findViewById(R.id.tl_loginSignUp_tabLayout);

        //Setting tabs
        setupViewPager(loginSignupViewPager);
        loginSignupTabLayout.setupWithViewPager(loginSignupViewPager);
    }


    //Func - Setting up viewpager
    private void setupViewPager(ViewPager viewPager) {

        LoginSignUpPagerAdapter adapter = new LoginSignUpPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), getResources().getString(R.string.log_in));
        adapter.addFragment(new SignupFragment(), getResources().getString(R.string.register));
        viewPager.setAdapter(adapter);
    }

    //Func - Register link Click
    public void registerLinkClick() {

        //Setting signup page
        loginSignupViewPager.setCurrentItem(1);
    }


    //Func - Login link Click
    public void loginLinkClick() {

        //Setting login page
        loginSignupViewPager.setCurrentItem(0);
    }



    //Adapter - Pager Adapter
    class LoginSignUpPagerAdapter extends FragmentPagerAdapter {

        //Declaring variables
        private final List<Fragment> pagerFragmentList = new ArrayList<>();
        private final List<String> pagerFragmentTitleList = new ArrayList<>();

        public LoginSignUpPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return pagerFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return pagerFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            pagerFragmentList.add(fragment);
            pagerFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagerFragmentTitleList.get(position);
        }
    }
}
