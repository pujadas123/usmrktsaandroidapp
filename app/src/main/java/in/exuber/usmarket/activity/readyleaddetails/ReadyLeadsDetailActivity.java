package in.exuber.usmarket.activity.readyleaddetails;

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
import in.exuber.usmarket.activity.activeleadsdetails.LeadsDetailActivity;
import in.exuber.usmarket.adapter.ActiveLeadsInterestProductAdapter;
import in.exuber.usmarket.adapter.LeadContactListAdapter;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.apimodels.readyleads.readyleadsoutput.ReadyLeadsOutput;
import in.exuber.usmarket.models.LeadsActivieCategoryOutput;
import in.exuber.usmarket.models.leads.LeadsOutput;
import in.exuber.usmarket.utils.Constants;

public class ReadyLeadsDetailActivity extends AppCompatActivity {

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
    private ReadyLeadsOutput readyLeadsOutput;

    //Declaring variables
    private AllLeadsOutput allLeadsOutput;

    RecyclerView recyclerList_Ready_interestProductList;
    ActiveLeadsInterestProductAdapter acticeLeadsInterestProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_leads_detail);

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

        recyclerList_Ready_interestProductList=findViewById(R.id.recyclerList_ReadyLeads_interestProductList);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),2);
        recyclerList_Ready_interestProductList.setLayoutManager(layoutManager);

        leadsDetailActivityContainer = findViewById(R.id.activity_ready_leads_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        leadName = findViewById(R.id.tv_readyLeadsDetail_leadName);
        leadSourceLayout = findViewById(R.id.ll_readyLeadsDetail_leadSourceLayout);
        leadSource = findViewById(R.id.tv_readyLeadsDetail_leadSource);

        leadInterestInvestment = findViewById(R.id.tv_readyLeadsDetail_interest_investment);
        leadInterestRealEstate = findViewById(R.id.tv_readyLeadsDetail_interest_realEstate);

        contactFaceBookLayout = findViewById(R.id.ll_readyLeadsDetail_contactFacebookLayout);
        contactInstagramLayout = findViewById(R.id.ll_readyLeadsDetail_contactInstagramLayout);
        contactTwitterLayout = findViewById(R.id.ll_readyLeadsDetail_contactTwitterLayout);
        contactWebsiteLayout = findViewById(R.id.ll_readyLeadsDetail_contactWebsiteLayout);
        contactEmailLayout = findViewById(R.id.ll_readyLeadsDetail_contactEmailLayout);
        contactPhoneLayout = findViewById(R.id.ll_readyLeadsDetail_contactPhoneLayout);


        contactFaceBook = findViewById(R.id.tv_readyLeadsDetail_contactFacebook);
        contactInstagram = findViewById(R.id.tv_readyLeadsDetail_contactInstagram);
        contactTwitter = findViewById(R.id.tv_readyLeadsDetail_contactTwitter);
        contactWebsite = findViewById(R.id.tv_readyLeadsDetail_contactWebsite);
        contactEmail = findViewById(R.id.tv_readyLeadsDetail_contactEmail);
        contactPhone = findViewById(R.id.tv_readyLeadsDetail_contactPhone);



        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String leadItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_LEAD);


        //Converting string to Object
        Gson gson = new Gson();
        readyLeadsOutput = gson.fromJson(leadItemString, ReadyLeadsOutput.class);

        //Converting string to Object
        Gson gson2 = new Gson();
        allLeadsOutput = gson2.fromJson(leadItemString, AllLeadsOutput.class);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.lead_details_caps));

        leadName.setText(readyLeadsOutput.getName()+" "+readyLeadsOutput.getLastName());

        if (readyLeadsOutput.getLeadSource() == null)
        {

            leadSourceLayout.setVisibility(View.GONE);
        }
        else
        {
            leadSource.setText(readyLeadsOutput.getLeadSource().getName());

            leadSourceLayout.setVisibility(View.VISIBLE);
        }

        if (readyLeadsOutput.getFacebook() == null)
        {

            contactFaceBookLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getFacebook().isEmpty())
            {
                contactFaceBookLayout.setVisibility(View.GONE);
            }
            else
            {
                contactFaceBook.setText(readyLeadsOutput.getFacebook());
                contactFaceBookLayout.setVisibility(View.VISIBLE);
            }

        }

        if (readyLeadsOutput.getInstagram() == null)
        {

            contactInstagramLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getInstagram().isEmpty())
            {
                contactInstagramLayout.setVisibility(View.GONE);
            }
            else
            {
                contactInstagram.setText(readyLeadsOutput.getInstagram());
                contactInstagramLayout.setVisibility(View.VISIBLE);
            }

        }

        if (readyLeadsOutput.getTwitter() == null)
        {

            contactTwitterLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getTwitter().isEmpty())
            {
                contactTwitterLayout.setVisibility(View.GONE);
            }
            else
            {
                contactTwitter.setText(readyLeadsOutput.getTwitter());
                contactTwitterLayout.setVisibility(View.VISIBLE);
            }

        }

        if (readyLeadsOutput.getWebsite() == null)
        {

            contactWebsiteLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getWebsite().isEmpty())
            {
                contactWebsiteLayout.setVisibility(View.GONE);
            }
            else
            {
                contactWebsite.setText(readyLeadsOutput.getWebsite());
                contactWebsiteLayout.setVisibility(View.VISIBLE);
            }

        }

        if (readyLeadsOutput.getEmail() == null)
        {

            contactEmailLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getEmail().isEmpty())
            {
                contactEmailLayout.setVisibility(View.GONE);
            }
            else
            {
                contactEmail.setText(readyLeadsOutput.getEmail());
                contactEmailLayout.setVisibility(View.VISIBLE);
            }

        }

        if (readyLeadsOutput.getPhoneNo() == null)
        {

            contactPhoneLayout.setVisibility(View.GONE);
        }
        else
        {
            if (readyLeadsOutput.getPhoneNo().isEmpty())
            {
                contactPhoneLayout.setVisibility(View.GONE);
            }
            else
            {
                contactPhone.setText(readyLeadsOutput.getCountryCode() + " " +readyLeadsOutput.getPhoneNo());
                contactPhoneLayout.setVisibility(View.VISIBLE);
            }

        }




        if (readyLeadsOutput.getCategoryList() != null)
        {
            boolean isRealEstatePresent = false;
            boolean isInvestmentPresent = false;

            for (int index = 0; index<readyLeadsOutput.getCategoryList().size(); index++)
            {
                String categoryId = readyLeadsOutput.getCategoryList().get(index).getCategory().getId();

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
        acticeLeadsInterestProductAdapter = new ActiveLeadsInterestProductAdapter(ReadyLeadsDetailActivity.this,allLeadsOutput.getProductList());
        recyclerList_Ready_interestProductList.setAdapter(acticeLeadsInterestProductAdapter);
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
