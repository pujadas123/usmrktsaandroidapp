package in.exuber.usmarket.activity.campaignshared;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.SharedCampaignListAdapter;
import in.exuber.usmarket.apimodels.sharedcampaign.sharedcampaignoutput.SharedCampaignOutput;
import in.exuber.usmarket.dialog.SharedCampaignCategoryFilterDialog;
import in.exuber.usmarket.dialog.SharedCampaignLanguageFilterDialog;
import in.exuber.usmarket.utils.Api;
import in.exuber.usmarket.utils.Config;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

public class CampaignSharedActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout campaignSharedActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_sharedCampaignList;
    private NestedScrollView campaignLayout;

    private TextView toolbarHeader;

    private TextView categoryClick;
    private LinearLayout languageClick;
    private TextView languageText;

    private RecyclerView sharedCampaignList;

    private SearchView searchCampaign;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;


    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Connection detector class
    private ConnectionDetector connectionDetector;


    //Declaring variables
    private List<SharedCampaignOutput> sharedCampaignOutputList;
    private List<SharedCampaignOutput> filteredSharedCampaignOutputList;

    private int selectedCategoryPosition = 0;
    private int selectedLanguagePosition = 0;

    //Adapter
    private SharedCampaignListAdapter sharedCampaignListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_shared);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);


        //Initialising variables
        sharedCampaignOutputList = new ArrayList<>();
        filteredSharedCampaignOutputList = new ArrayList<>();

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);


        //Initialising views
        campaignSharedActivityContainer = findViewById(R.id.activity_campaign_shared);
        campaignLayout = findViewById(R.id.nsv_campaignShared_campaignLayout);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        //Recyclerview
        sharedCampaignList = findViewById(R.id.rv_campaignShared_campaignList);
        sharedCampaignList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerSharedCampaigns = new LinearLayoutManager(this);
        linearLayoutManagerSharedCampaigns.setOrientation(LinearLayoutManager.VERTICAL);
        sharedCampaignList.setLayoutManager(linearLayoutManagerSharedCampaigns);

        categoryClick = findViewById(R.id.tv_campaignShared_categoryClick);
        languageClick = findViewById(R.id.ll_campaignShared_languageClick);
        languageText = findViewById(R.id.tv_campaignShared_languageText);

        searchCampaign = findViewById(R.id.et_campaignShared_search);

        ///Swipe Refresh Layout
        swipeRefreshLayout_sharedCampaignList = findViewById(R.id.srl_campaignShared_pullToRefresh);
        swipeRefreshLayout_sharedCampaignList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);

        searchCampaign.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (sharedCampaignListAdapter !=  null) {
                    sharedCampaignListAdapter.getFilter().filter(query);
                }

                return false;
            }
        });

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.shared_campaigns_caps));

        categoryClick.setText("All Categories");
        languageText.setText("ALL");


        //Get Shared Campaigns
        getSharedCampaigns();

        swipeRefreshLayout_sharedCampaignList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Shared Campaigns
                getSharedCampaigns();

                swipeRefreshLayout_sharedCampaignList.setRefreshing(false);
            }
        });


        //setting onclick
        categoryClick.setOnClickListener(this);
        languageClick.setOnClickListener(this);
        errorDisplayTryClick.setOnClickListener(this);


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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_campaignShared_categoryClick:


                //Calling dialog
                FragmentManager filterCategoryManager = getFragmentManager();
                SharedCampaignCategoryFilterDialog filterCategoryDialog = new SharedCampaignCategoryFilterDialog(CampaignSharedActivity.this,selectedCategoryPosition);
                filterCategoryDialog.show(filterCategoryManager, "FILTER_CATEGORY_DIALOG");

                break;

            case R.id.ll_campaignShared_languageClick:


                //Calling dialog
                FragmentManager infoLanguageManager = getFragmentManager();
                SharedCampaignLanguageFilterDialog filterLanguageDialog = new SharedCampaignLanguageFilterDialog(CampaignSharedActivity.this,selectedLanguagePosition);
                filterLanguageDialog.show(infoLanguageManager, "FILTER_LANGUAGE_DIALOG");

                break;

            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(CampaignSharedActivity.this);

                //Get Shared Campaigns
                getSharedCampaigns();


                break;
        }

    }


    //Func - Set Category
    public void setCategory(int clickPosition, String selectedCategory) {

        selectedCategoryPosition = clickPosition;
        categoryClick.setText(selectedCategory);

        fillterAndSetData();
    }

    //Func - Set Language
    public void setLanguage(int clickPosition) {

        selectedLanguagePosition = clickPosition;

        if (clickPosition == 0)
        {
            languageText.setText("ALL");

        } else if (clickPosition == 1)
        {
            languageText.setText("EN");

        } else if (clickPosition == 2)
        {
            languageText.setText("SP");

        } else if (clickPosition == 3)
        {
            languageText.setText("FR");
        }

        fillterAndSetData();

    }


    //Func - Filter and Set Data
    private void fillterAndSetData() {

        //Hiding views
        progressDialog.setVisibility(View.GONE);
        errorDisplay.setVisibility(View.GONE);

        campaignLayout.setVisibility(View.VISIBLE);

        boolean isAllCategorySelected = false;
        boolean isAllLanguageSelected = false;

        filteredSharedCampaignOutputList = new ArrayList<>();


        if (selectedCategoryPosition == 0)
        {
            isAllCategorySelected = true;
        }
        if (selectedLanguagePosition == 0)
        {
            isAllLanguageSelected = true;
        }

        if (isAllCategorySelected && isAllLanguageSelected)
        {
            filteredSharedCampaignOutputList = sharedCampaignOutputList;
        }
        else if (isAllCategorySelected)
        {
            String selectedLanguageId = null;

            switch (selectedLanguagePosition)
            {

                case 1:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_ENGLISH_ID;

                    break;

                case 2:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_SPANISH_ID;

                    break;

                case 3:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_FRENCH_ID;

                    break;


            }


            for (int index = 0; index < sharedCampaignOutputList.size(); index++)
            {

                SharedCampaignOutput sharedCampaignOutput = sharedCampaignOutputList.get(index);


                if (sharedCampaignOutput.getCampaignId().getLanguage().getId().equals(selectedLanguageId))
                {
                    filteredSharedCampaignOutputList.add(sharedCampaignOutput);
                }

            }
        }
        else if (isAllLanguageSelected)
        {

            String selectedCategoryId = null;

            switch (selectedCategoryPosition)
            {

                case 1:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_REALESTATE_ID;

                    break;

                case 2:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_INVESTMENT_ID;

                    break;

            }

            for (int index = 0; index < sharedCampaignOutputList.size(); index++)
            {

                SharedCampaignOutput sharedCampaignOutput = sharedCampaignOutputList.get(index);

                if (sharedCampaignOutput.getCampaignId().getCategory() != null)
                {
                    if (sharedCampaignOutput.getCampaignId().getCategory().getId().equals(selectedCategoryId))
                    {
                        filteredSharedCampaignOutputList.add(sharedCampaignOutput);
                    }
                }


            }

        }
        else {

            String selectedCategoryId = null;
            String selectedLanguageId = null;

            switch (selectedCategoryPosition) {

                case 1:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_REALESTATE_ID;

                    break;

                case 2:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_INVESTMENT_ID;

                    break;

            }

            switch (selectedLanguagePosition)
            {

                case 1:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_ENGLISH_ID;

                    break;

                case 2:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_SPANISH_ID;

                    break;

                case 3:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_FRENCH_ID;

                    break;


            }

            for (int index = 0; index < sharedCampaignOutputList.size(); index++)
            {

                SharedCampaignOutput sharedCampaignOutput = sharedCampaignOutputList.get(index);

                if (sharedCampaignOutput.getCampaignId().getCategory() != null)
                {
                    if (sharedCampaignOutput.getCampaignId().getCategory().getId().equals(selectedCategoryId) && sharedCampaignOutput.getCampaignId().getLanguage().getId().equals(selectedLanguageId))
                    {
                        filteredSharedCampaignOutputList.add(sharedCampaignOutput);
                    }
                }



            }

        }

        //Setting adapter
        sharedCampaignListAdapter = new SharedCampaignListAdapter(CampaignSharedActivity.this,filteredSharedCampaignOutputList);
        sharedCampaignList.setAdapter(sharedCampaignListAdapter);
        sharedCampaignListAdapter.notifyDataSetChanged();

    }


    //Func - Getting Shared Campaigns
    private void getSharedCampaigns() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            campaignLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);


            //Calling Service
            callGetSharedCampaignsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            campaignLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }



    //Service - Getting Shared Campaigns
    private void callGetSharedCampaignsService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<SharedCampaignOutput>> call = (Call<List<SharedCampaignOutput>>) api.getSharedCampaigns(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_SHARED_CAMPAIGNS,
                userId);
        call.enqueue(new Callback<List<SharedCampaignOutput>>() {
            @Override
            public void onResponse(Call<List<SharedCampaignOutput>> call, Response<List<SharedCampaignOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {


                    sharedCampaignOutputList = response.body();


                    if (sharedCampaignOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        campaignLayout.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                        errorDisplayText.setText(getString( R.string.error_no_data_shared_campaigns));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {


                        fillterAndSetData();

                    }



                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    campaignLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<SharedCampaignOutput>> call, Throwable t) {


                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    campaignLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                    errorDisplayText.setText(getString( R.string.error_no_data_shared_campaigns));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    campaignLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }



            }

        });



    }

    //Retrofit log
    public OkHttpClient.Builder getHttpClient() {

        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);
            client.writeTimeout(60000, TimeUnit.MILLISECONDS);
            client.readTimeout(60000, TimeUnit.MILLISECONDS);
            client.connectTimeout(60000, TimeUnit.MILLISECONDS);
            return client;
        }
        return builder;
    }





}
