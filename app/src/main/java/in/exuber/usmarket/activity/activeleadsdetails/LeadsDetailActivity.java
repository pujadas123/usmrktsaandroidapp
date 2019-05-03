package in.exuber.usmarket.activity.activeleadsdetails;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsedit.LeadsEditActivity;
import in.exuber.usmarket.adapter.ActiveLeadsInterestCategoryAdapter;
import in.exuber.usmarket.adapter.ActiveLeadsInterestProductAdapter;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.utils.Constants;

public class LeadsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsDetailActivityContainer;
    private TextView toolbarHeader, toolbarActionText;

    private LinearLayout toolbarActionClick;

    private LinearLayout leadSourceLayout;
    private TextView leadName, leadSource;

    private LinearLayout contactFaceBookLayout, contactInstagramLayout, contactTwitterLayout, contactWebsiteLayout, contactEmailLayout,contactPhoneLayout;
    private TextView contactFaceBook, contactInstagram, contactTwitter, contactWebsite, contactEmail,contactPhone;

    //private TextView leadInterestInvestment, leadInterestRealEstate;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring variables
    private AllLeadsOutput allLeadsOutput;

    public static Activity leadsDetailActivityActivityInstance;

    RecyclerView recyclerList_Active_interestProductList,recyclerList_ActiveLeads_interestCategoryList;
    ActiveLeadsInterestProductAdapter acticeLeadsInterestProductAdapter;
    ActiveLeadsInterestCategoryAdapter acticeLeadsInterestCategoryAdapter;


    TextView Product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Setting activity instance
        leadsDetailActivityActivityInstance = this;

        //Setting Toolbar
        Toolbar toolbar =  findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);

        //Initialising views
        leadsDetailActivityContainer = findViewById(R.id.activity_leads_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);
        toolbarActionClick = findViewById(R.id.ll_main_toolBar_actionClick);
        toolbarActionText = findViewById(R.id.iv_main_toolBar_actionText);

        recyclerList_Active_interestProductList=findViewById(R.id.recyclerList_ActiveLeads_interestProductList);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerList_Active_interestProductList.setLayoutManager(layoutManager);

        recyclerList_ActiveLeads_interestCategoryList=findViewById(R.id.recyclerList_ActiveLeads_interestCategoryList);

        RecyclerView.LayoutManager layoutManagerCategory=new GridLayoutManager(getApplicationContext(),2);
        recyclerList_ActiveLeads_interestCategoryList.setLayoutManager(layoutManagerCategory);


        leadName = findViewById(R.id.tv_leadsDetail_leadName);
        leadSourceLayout = findViewById(R.id.ll_leadsDetail_leadSourceLayout);
        leadSource = findViewById(R.id.tv_leadsDetail_leadSource);

        contactFaceBookLayout = findViewById(R.id.ll_leadsDetail_contactFacebookLayout);
        contactInstagramLayout = findViewById(R.id.ll_leadsDetail_contactInstagramLayout);
        contactTwitterLayout = findViewById(R.id.ll_leadsDetail_contactTwitterLayout);
        contactWebsiteLayout = findViewById(R.id.ll_leadsDetail_contactWebsiteLayout);
        contactEmailLayout = findViewById(R.id.ll_leadsDetail_contactEmailLayout);
        contactPhoneLayout = findViewById(R.id.ll_leadsDetail_contactPhoneLayout);


        contactFaceBook = findViewById(R.id.tv_leadsDetail_contactFacebook);
        contactInstagram = findViewById(R.id.tv_leadsDetail_contactInstagram);
        contactTwitter = findViewById(R.id.tv_leadsDetail_contactTwitter);
        contactWebsite = findViewById(R.id.tv_leadsDetail_contactWebsite);
        contactEmail = findViewById(R.id.tv_leadsDetail_contactEmail);
        contactPhone = findViewById(R.id.tv_leadsDetail_contactPhone);


        /*leadInterestInvestment = findViewById(R.id.tv_leadsDetail_interest_investment);
        leadInterestRealEstate = findViewById(R.id.tv_leadsDetail_interest_realEstate);*/

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String leadItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_LEAD);


        //Converting string to Object
        Gson gson = new Gson();
        allLeadsOutput = gson.fromJson(leadItemString, AllLeadsOutput.class);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.lead_details_caps));
        toolbarActionText.setText(getResources().getString(R.string.edit));

        leadName.setText(allLeadsOutput.getName()+" "+allLeadsOutput.getLastName());

        if (allLeadsOutput.getLeadSource() == null)
        {

            leadSourceLayout.setVisibility(View.GONE);
        }
        else
        {
            leadSource.setText(allLeadsOutput.getLeadSource().getName());

            leadSourceLayout.setVisibility(View.VISIBLE);
        }

        if (allLeadsOutput.getFacebook() == null)
        {

            contactFaceBookLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getFacebook().isEmpty())
            {
                contactFaceBookLayout.setVisibility(View.GONE);
            }
            else
            {
                contactFaceBook.setText(allLeadsOutput.getFacebook());
                contactFaceBookLayout.setVisibility(View.VISIBLE);
            }

        }

        if (allLeadsOutput.getInstagram() == null)
        {

            contactInstagramLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getInstagram().isEmpty())
            {
                contactInstagramLayout.setVisibility(View.GONE);
            }
            else
            {
                contactInstagram.setText(allLeadsOutput.getInstagram());
                contactInstagramLayout.setVisibility(View.VISIBLE);
            }

        }

        if (allLeadsOutput.getTwitter() == null)
        {

            contactTwitterLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getTwitter().isEmpty())
            {
                contactTwitterLayout.setVisibility(View.GONE);
            }
            else
            {
                contactTwitter.setText(allLeadsOutput.getTwitter());
                contactTwitterLayout.setVisibility(View.VISIBLE);
            }

        }

        if (allLeadsOutput.getWebsite() == null)
        {

            contactWebsiteLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getWebsite().isEmpty())
            {
                contactWebsiteLayout.setVisibility(View.GONE);
            }
            else
            {
                contactWebsite.setText(allLeadsOutput.getWebsite());
                contactWebsiteLayout.setVisibility(View.VISIBLE);
            }

        }

        if (allLeadsOutput.getEmail() == null)
        {

            contactEmailLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getEmail().isEmpty())
            {
                contactEmailLayout.setVisibility(View.GONE);
            }
            else
            {
                contactEmail.setText(allLeadsOutput.getEmail());
                contactEmailLayout.setVisibility(View.VISIBLE);
            }

        }

        if (allLeadsOutput.getPhoneNo() == null)
        {

            contactPhoneLayout.setVisibility(View.GONE);
        }
        else
        {
            if (allLeadsOutput.getPhoneNo().isEmpty())
            {
                contactPhoneLayout.setVisibility(View.GONE);
            }
            else
            {

                contactPhone.setText(allLeadsOutput.getCountryCode() + " " +allLeadsOutput.getPhoneNo());
                contactPhoneLayout.setVisibility(View.VISIBLE);
            }

        }

        /*if (allLeadsOutput.getCategoryList() != null)
        {
            boolean isRealEstatePresent = false;
            boolean isInvestmentPresent = false;

            for (int index = 0; index<allLeadsOutput.getCategoryList().size(); index++)
            {
                String categoryId = allLeadsOutput.getCategoryList().get(index).getCategory().getId();

                if (categoryId.equals(Constants.PRODUCT_CATEGORY_REALESTATE_ID))
                {
                    isRealEstatePresent = true;
                }

                if (categoryId.equals(Constants.PRODUCT_CATEGORY_INVESTMENT_ID))
                {
                    isInvestmentPresent = true;
                }

            }

            if (isRealEstatePresent)
            {
                leadInterestRealEstate.setVisibility(View.VISIBLE);
            }
            else
            {
                leadInterestRealEstate.setVisibility(View.GONE);
            }

            if (isInvestmentPresent)
            {
                leadInterestInvestment.setVisibility(View.VISIBLE);
            }
            else
            {
                leadInterestInvestment.setVisibility(View.GONE);
            }
        }*/


        //Setting adapter for Product
        acticeLeadsInterestProductAdapter = new ActiveLeadsInterestProductAdapter(LeadsDetailActivity.this,allLeadsOutput.getProductList());
        recyclerList_Active_interestProductList.setAdapter(acticeLeadsInterestProductAdapter);
        acticeLeadsInterestProductAdapter.notifyDataSetChanged();


        //Setting adapter for Category
        acticeLeadsInterestCategoryAdapter = new ActiveLeadsInterestCategoryAdapter(LeadsDetailActivity.this,allLeadsOutput.getCategoryList());
        recyclerList_ActiveLeads_interestCategoryList.setAdapter(acticeLeadsInterestCategoryAdapter);
        acticeLeadsInterestCategoryAdapter.notifyDataSetChanged();


        //Setting onclick
        toolbarActionClick.setOnClickListener(this);



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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_main_toolBar_actionClick:

                Gson gson = new Gson();

                //Converting to string
                String leadItemString = gson.toJson(allLeadsOutput);

                //Preparing Intent
                Intent leadEditIntent = new Intent(LeadsDetailActivity.this, LeadsEditActivity.class);

                //Create the bundle
                Bundle leadEditBundle = new Bundle();

                //Add your data to bundle
                leadEditBundle.putString(Constants.INTENT_KEY_SELECTED_LEAD,leadItemString);

                //Add the bundle to the intent
                leadEditIntent.putExtras(leadEditBundle);

                startActivity(leadEditIntent);

                break;
        }
    }
}
