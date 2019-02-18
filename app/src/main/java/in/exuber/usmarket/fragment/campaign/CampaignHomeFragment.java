package in.exuber.usmarket.fragment.campaign;


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

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaignshared.CampaignSharedActivity;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampaignHomeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout campaignHomeFragmentContainer;
    private RelativeLayout toolbarNotificationClick;

    private LinearLayout toolbarSharedClick;

    private TabLayout campaignTabLayout;
    private ViewPager campaignViewPager;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;


    public CampaignHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View campaignView = inflater.inflate(R.layout.fragment_campaign_home, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());


        //Initialising views
        campaignHomeFragmentContainer = campaignView.findViewById(R.id.fragment_campaign_home);

        toolbarNotificationClick = campaignView.findViewById(R.id.rl_homeCampaign_toolBar_notificationLayout);
        toolbarSharedClick = campaignView.findViewById(R.id.ll_homeCampaign_toolBar_sharedClick);

        campaignViewPager = campaignView.findViewById(R.id.vp_campaignHomeFragment_viewPager);
        campaignTabLayout = campaignView.findViewById(R.id.tl_campaignHomeFragment_tabLayout);

        //Setting tabs
        setupViewPager(campaignViewPager);
        campaignTabLayout.setupWithViewPager(campaignViewPager);


        //Setting onclick listner
        toolbarNotificationClick.setOnClickListener(this);
        toolbarSharedClick.setOnClickListener(this);



        return campaignView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.rl_homeCampaign_toolBar_notificationLayout:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Notification Activity
                startActivity(new Intent(getActivity(), NotificationActivity.class));

                break;

            case R.id.ll_homeCampaign_toolBar_sharedClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Shared Campaign Activity
                startActivity(new Intent(getActivity(), CampaignSharedActivity.class));

                break;
        }

    }

    //Func - Setting up viewpager
    private void setupViewPager(ViewPager viewPager) {

        CampaignPagerAdapter adapter = new CampaignPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new CampaignNewFragment(), getResources().getString(R.string.new_campaigns_caps));
        adapter.addFragment(new CampaignExistingFragment(), getResources().getString(R.string.existing_campaigns_caps));
        viewPager.setAdapter(adapter);
    }


    //Adapter - Pager Adapter
    class CampaignPagerAdapter extends FragmentPagerAdapter {

        //Declaring variables
        private final List<Fragment> pagerFragmentList = new ArrayList<>();
        private final List<String> pagerFragmentTitleList = new ArrayList<>();

        public CampaignPagerAdapter(FragmentManager manager) {
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


    public void onBackPressed() {

        getActivity().finishAffinity();


    }



}
