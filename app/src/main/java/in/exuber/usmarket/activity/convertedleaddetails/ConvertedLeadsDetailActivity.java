package in.exuber.usmarket.activity.convertedleaddetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsedit.LeadsEditActivity;
import in.exuber.usmarket.activity.readyleaddetails.ReadyLeadsDetailActivity;
import in.exuber.usmarket.adapter.ActiveLeadsInterestProductAdapter;
import in.exuber.usmarket.adapter.LeadContactListAdapter;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput.ConvertedLeadsOutput;
import in.exuber.usmarket.models.LeadsActivieCategoryOutput;
import in.exuber.usmarket.models.leads.LeadsOutput;
import in.exuber.usmarket.utils.Constants;

public class ConvertedLeadsDetailActivity extends AppCompatActivity {

    //Declaring views
    private LinearLayout leadsDetailActivityContainer;
    private TextView toolbarHeader;

    private LinearLayout leadSourceLayout;
    private TextView leadName, leadSource;

    private LinearLayout contactFaceBookLayout, contactInstagramLayout, contactTwitterLayout, contactWebsiteLayout, contactEmailLayout,contactPhoneLayout;
    private TextView contactFaceBook, contactInstagram, contactTwitter, contactWebsite, contactEmail,contactPhone;

    private TextView leadInterestInvestment, leadInterestRealEstate;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring variables
    private ConvertedLeadsOutput convertedLeadsOutput;

    //Declaring variables
    private AllLeadsOutput allLeadsOutput;

    RecyclerView recyclerList_Converted_interestProductList;
    ActiveLeadsInterestProductAdapter acticeLeadsInterestProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converted_leads_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Setting Toolbar
        Toolbar toolbar =  findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising views

        recyclerList_Converted_interestProductList=findViewById(R.id.recyclerList_ConvertedLeads_interestProductList);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerList_Converted_interestProductList.setLayoutManager(layoutManager);

        leadsDetailActivityContainer = findViewById(R.id.activity_converted_leads_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        leadName = findViewById(R.id.tv_convertedLeadsDetail_leadName);
        leadSourceLayout = findViewById(R.id.ll_convertedLeadsDetail_leadSourceLayout);
        leadSource = findViewById(R.id.tv_convertedLeadsDetail_leadSource);

        leadInterestInvestment = findViewById(R.id.tv_convertedLeadsDetail_interest_investment);
        leadInterestRealEstate = findViewById(R.id.tv_convertedLeadsDetail_interest_realEstate);

        contactFaceBookLayout = findViewById(R.id.ll_convertedLeadsDetail_contactFacebookLayout);
        contactInstagramLayout = findViewById(R.id.ll_convertedLeadsDetail_contactInstagramLayout);
        contactTwitterLayout = findViewById(R.id.ll_convertedLeadsDetail_contactTwitterLayout);
        contactWebsiteLayout = findViewById(R.id.ll_convertedLeadsDetail_contactWebsiteLayout);
        contactEmailLayout = findViewById(R.id.ll_convertedLeadsDetail_contactEmailLayout);
        contactPhoneLayout = findViewById(R.id.ll_convertedLeadsDetail_contactPhoneLayout);


        contactFaceBook = findViewById(R.id.tv_convertedLeadsDetail_contactFacebook);
        contactInstagram = findViewById(R.id.tv_convertedLeadsDetail_contactInstagram);
        contactTwitter = findViewById(R.id.tv_convertedLeadsDetail_contactTwitter);
        contactWebsite = findViewById(R.id.tv_convertedLeadsDetail_contactWebsite);
        contactEmail = findViewById(R.id.tv_convertedLeadsDetail_contactEmail);
        contactPhone = findViewById(R.id.tv_convertedLeadsDetail_contactPhone);


        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String leadItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_LEAD);


        //Converting string to Object
        Gson gson = new Gson();
        convertedLeadsOutput = gson.fromJson(leadItemString, ConvertedLeadsOutput.class);

        //Converting string to Object
        Gson gson2 = new Gson();
        allLeadsOutput = gson2.fromJson(leadItemString, AllLeadsOutput.class);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.lead_details_caps));

        leadName.setText(convertedLeadsOutput.getName()+" "+convertedLeadsOutput.getLastName());

        if (convertedLeadsOutput.getLeadSource() == null)
        {

            leadSourceLayout.setVisibility(View.GONE);
        }
        else
        {
            leadSource.setText(convertedLeadsOutput.getLeadSource().getName());

            leadSourceLayout.setVisibility(View.VISIBLE);
        }

        if (convertedLeadsOutput.getFacebook() == null)
        {

            contactFaceBookLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getFacebook().isEmpty())
            {
                contactFaceBookLayout.setVisibility(View.GONE);
            }
            else
            {
                contactFaceBook.setText(convertedLeadsOutput.getFacebook());
                contactFaceBookLayout.setVisibility(View.VISIBLE);
            }

        }

        if (convertedLeadsOutput.getInstagram() == null)
        {

            contactInstagramLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getInstagram().isEmpty())
            {
                contactInstagramLayout.setVisibility(View.GONE);
            }
            else
            {
                contactInstagram.setText(convertedLeadsOutput.getInstagram());
                contactInstagramLayout.setVisibility(View.VISIBLE);
            }

        }

        if (convertedLeadsOutput.getTwitter() == null)
        {

            contactTwitterLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getTwitter().isEmpty())
            {
                contactTwitterLayout.setVisibility(View.GONE);
            }
            else
            {
                contactTwitter.setText(convertedLeadsOutput.getTwitter());
                contactTwitterLayout.setVisibility(View.VISIBLE);
            }

        }

        if (convertedLeadsOutput.getWebsite() == null)
        {

            contactWebsiteLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getWebsite().isEmpty())
            {
                contactWebsiteLayout.setVisibility(View.GONE);
            }
            else
            {
                contactWebsite.setText(convertedLeadsOutput.getWebsite());
                contactWebsiteLayout.setVisibility(View.VISIBLE);
            }

        }

        if (convertedLeadsOutput.getEmail() == null)
        {

            contactEmailLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getEmail().isEmpty())
            {
                contactEmailLayout.setVisibility(View.GONE);
            }
            else
            {
                contactEmail.setText(convertedLeadsOutput.getEmail());
                contactEmailLayout.setVisibility(View.VISIBLE);
            }

        }


        if (convertedLeadsOutput.getPhoneNo() == null)
        {

            contactPhoneLayout.setVisibility(View.GONE);
        }
        else
        {
            if (convertedLeadsOutput.getPhoneNo().isEmpty())
            {
                contactPhoneLayout.setVisibility(View.GONE);
            }
            else
            {
                contactPhone.setText(convertedLeadsOutput.getCountryCode() + " " +convertedLeadsOutput.getPhoneNo());
                contactPhoneLayout.setVisibility(View.VISIBLE);
            }

        }



        if (convertedLeadsOutput.getCategoryList() != null)
        {
            boolean isRealEstatePresent = false;
            boolean isInvestmentPresent = false;

            for (int index = 0; index<convertedLeadsOutput.getCategoryList().size(); index++)
            {
                String categoryId = convertedLeadsOutput.getCategoryList().get(index).getCategory().getId();

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


        }


        //Setting adapter
        acticeLeadsInterestProductAdapter = new ActiveLeadsInterestProductAdapter(ConvertedLeadsDetailActivity.this,allLeadsOutput.getProductList());
        recyclerList_Converted_interestProductList.setAdapter(acticeLeadsInterestProductAdapter);
        acticeLeadsInterestProductAdapter.notifyDataSetChanged();




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
