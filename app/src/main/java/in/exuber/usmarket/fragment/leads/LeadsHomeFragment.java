package in.exuber.usmarket.fragment.leads;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsadd.LeadsAddActivity;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.models.leads.LeadsOutput;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadsHomeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsHomeFragmentContainer;

    private RelativeLayout toolbarNotificationClick;
    private TextView toolbarAddClick;

    private TabLayout leadsTabLayout;
    private ViewPager leadsViewPager;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;




    public LeadsHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View leadsView = inflater.inflate(R.layout.fragment_leads_home, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());


        //Initialising views
        leadsHomeFragmentContainer = leadsView.findViewById(R.id.fragment_leads_home);

        toolbarNotificationClick = leadsView.findViewById(R.id.rl_homeLeads_toolBar_notificationLayout);
        toolbarAddClick = leadsView.findViewById(R.id.iv_homeLeads_toolBar_add);

        leadsViewPager = leadsView.findViewById(R.id.vp_leadsHomeFragment_viewPager);
        leadsTabLayout = leadsView.findViewById(R.id.tl_leadsHomeFragment_tabLayout);

        //Setting tabs
        setupViewPager(leadsViewPager);
        leadsTabLayout.setupWithViewPager(leadsViewPager);


        //Setting onclick listner
        toolbarNotificationClick.setOnClickListener(this);
        toolbarAddClick.setOnClickListener(this);



        return leadsView;
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.rl_homeLeads_toolBar_notificationLayout:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Notification Activity
                startActivity(new Intent(getActivity(), NotificationActivity.class));

                break;

            case R.id.iv_homeLeads_toolBar_add:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Add leads Activity
                startActivity(new Intent(getActivity(), LeadsAddActivity.class));


                break;
        }

    }

    //Func - Setting up viewpager
    private void setupViewPager(ViewPager viewPager) {

        LeadsPagerAdapter adapter = new LeadsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LeadsActiveFragment(LeadsHomeFragment.this), getResources().getString(R.string.active_caps));
        adapter.addFragment(new LeadsReadyFragment(LeadsHomeFragment.this), getResources().getString(R.string.ready_caps));
        adapter.addFragment(new LeadsConvertedFragment(LeadsHomeFragment.this), getResources().getString(R.string.converted_caps));
        viewPager.setAdapter(adapter);
    }

    //Func - Setting up first tab title
    public void setFirstTabTitleText() {

        leadsTabLayout.getTabAt(0).setText( getResources().getString(R.string.active_caps));
    }

    //Func - Setting up first tab title
    public void setFirstTabTitle(int size) {

        leadsTabLayout.getTabAt(0).setText( getResources().getString(R.string.active_caps)+ "("+size+")");
    }

    //Func - Setting up second tab title
    public void setSecondTabTitleText() {

        leadsTabLayout.getTabAt(1).setText( getResources().getString(R.string.ready_caps));
    }

    //Func - Setting up second tab title
    public void setSecondTabTitle(int size) {

        leadsTabLayout.getTabAt(1).setText( getResources().getString(R.string.ready_caps)+ "("+size+")");
    }

    //Func - Setting up third tab title
    public void setThirdTabTitleText() {

        leadsTabLayout.getTabAt(2).setText( getResources().getString(R.string.converted_caps));
    }


    //Func - Setting up third tab title
    public void setThirdTabTitle(int size) {

        leadsTabLayout.getTabAt(2).setText( getResources().getString(R.string.converted_caps)+ "("+size+")");
    }



    //Adapter - Pager Adapter
    class LeadsPagerAdapter extends FragmentPagerAdapter {

        //Declaring variables
        private final List<Fragment> pagerFragmentList = new ArrayList<>();
        private final List<String> pagerFragmentTitleList = new ArrayList<>();

        public LeadsPagerAdapter(FragmentManager manager) {
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
