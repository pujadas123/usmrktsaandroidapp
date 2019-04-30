package in.exuber.usmarket.activity.campaigntraining;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.CampaignTrainingListAdapter;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.apimodels.campaigntraining.campaigntrainingoutput.CampaignTrainingOutput;
import in.exuber.usmarket.service.DownloadService;
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

public class CampaignTrainingActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout campaignTrainingActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_trainingList;
    private TextView toolbarHeader;

    private TextView campaignName, campaignDate, campaignProduct, campaignCategory, campaignLanguage, campaignDescription;

    private RecyclerView trainingList;

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

    private CampaignOutput campaignOutput;
    private List<CampaignTrainingOutput> campaignTrainingOutputList;

    //Adapter
    private CampaignTrainingListAdapter campaignTrainingListAdapter;


    private CustomTabsClient customTabsClient;
    private CustomTabsSession customTabsSession;
    private CustomTabsServiceConnection customTabsServiceConnection;
    private CustomTabsIntent customTabsIntent;

    private static final int PERMISSIONS_REQUEST_ACCOUNTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_training);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);

        //Initialising variables
        campaignTrainingOutputList = new ArrayList<>();

        //Initialising views
        campaignTrainingActivityContainer = findViewById(R.id.activity_campaign_training);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        campaignName = findViewById(R.id.tv_campaignTraining_campaignName);
        campaignDate = findViewById(R.id.tv_campaignTraining_campaignDate);
        campaignProduct = findViewById(R.id.tv_campaignTraining_campaignProduct);
        campaignCategory = findViewById(R.id.tv_campaignTraining_campaignCategory);
        campaignLanguage = findViewById(R.id.tv_campaignTraining_campaignLanguage);
        campaignDescription = findViewById(R.id.tv_campaignTraining_campaignDescription);

        //Recyclerview
        trainingList = findViewById(R.id.rv_campaignTraining_trainingList);
        trainingList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerCampaignTraining = new LinearLayoutManager(this);
        linearLayoutManagerCampaignTraining.setOrientation(LinearLayoutManager.VERTICAL);
        trainingList.setLayoutManager(linearLayoutManagerCampaignTraining);

        swipeRefreshLayout_trainingList = findViewById(R.id.srl_campaignTraining_pullToRefresh);
        swipeRefreshLayout_trainingList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog_training);
        errorDisplay =  findViewById(R.id.ll_errorTraining_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorTraining_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorTraining_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorTraining_errorTryAgain);


        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                customTabsClient= customTabsClient;
                customTabsClient.warmup(0L);
                customTabsSession = customTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                customTabsClient= null;
            }
        };

        CustomTabsClient.bindCustomTabsService(this, Constants.CUSTOM_TAB_PACKAGE_NAME, customTabsServiceConnection);

        customTabsIntent = new CustomTabsIntent.Builder(customTabsSession)
                .setShowTitle(true)
                .build();

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.campaign_training_caps));

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String campaignItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_CAMPAIGN);


        //Converting string to Object
        Gson gson = new Gson();
        campaignOutput = gson.fromJson(campaignItemString, CampaignOutput.class);

        Log.e("Campaign ID", campaignOutput.getCompaignId());

        //Setting values
        campaignName.setText(campaignOutput.getCompaignName());


        campaignCategory.setText(campaignOutput.getCategory().getName());
        campaignProduct.setText(campaignOutput.getProduct().getProductName());
        campaignLanguage.setText(campaignOutput.getLanguage().getName());

        campaignDescription.setText(campaignOutput.getCompaignDesc());

        Date campaignDateOutput = new Date(campaignOutput.getCreatedOn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String campaignDateString = simpleDateFormat.format(campaignDateOutput);

        campaignDate.setText(campaignDateString);


        //Get campaign training
        getCampaignTraining();

        swipeRefreshLayout_trainingList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get campaign training
                getCampaignTraining();

                swipeRefreshLayout_trainingList.setRefreshing(false);
            }
        });

        //setting onclick
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

        switch (view.getId())
        {
            case R.id.tv_errorTraining_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(CampaignTrainingActivity.this);

                //Get campaign training
                getCampaignTraining();


                break;

        }


    }

    //Func  - Open Document Url
    public void openDocumentUrl(CampaignTrainingOutput selectedCampaignTrainingOutput) {

        //Opening Url
        customTabsIntent.launchUrl(this, Uri.parse(selectedCampaignTrainingOutput.getUrl()));
    }

    //Func  - Download Document Url
    public void downloadDocumentUrl(CampaignTrainingOutput selectedCampaignTrainingOutput) {

        //Checking permission Marshmallow
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermissionStorage())
            {

                downloadFile(selectedCampaignTrainingOutput);

            } else {

                //Request permission
                requestPermissionStorage();
            }
        } else {


            downloadFile(selectedCampaignTrainingOutput);
        }
    }

    //Func  - Open Link Url
    public void openLinkUrl(CampaignTrainingOutput selectedCampaignTrainingOutput) {

        //Opening to Link
        customTabsIntent.launchUrl(this, Uri.parse(selectedCampaignTrainingOutput.getLink()));
    }




    //Func - Checking permission granted
    private boolean checkPermissionStorage() {

        boolean isPermissionGranted = true;

        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStorage != PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted = false;
        }

        return isPermissionGranted;
    }

    //Func - Requesting permission
    private void requestPermissionStorage() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_ACCOUNTS);

    }

    //Func - Getting Campaign Training
    private void getCampaignTraining() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            trainingList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            calletCampaignTrainingService();

        } else {

            //Hiding views
            progressDialog.setVisibility(View.GONE);
            trainingList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func  - Download File
    private void downloadFile(CampaignTrainingOutput selectedCampaignTrainingOutput) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            Intent intent = new Intent(this, DownloadService.class);
            intent.putExtra(Constants.INTENT_KEY_SELECTED_FILE_URL, selectedCampaignTrainingOutput.getLink());
            startService(intent);

        } else {
            Snackbar snackbar = Snackbar
                    .make(campaignTrainingActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }


    //Service - Getting Campaign Training
    private void calletCampaignTrainingService() {

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        String campaignId = campaignOutput.getCompaignId();




        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<CampaignTrainingOutput>> call = (Call<List<CampaignTrainingOutput>>) api.getCampaignTraining(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_CAMPAIGN_TRAINING,
                campaignId);
        call.enqueue(new Callback<List<CampaignTrainingOutput>>() {
            @Override
            public void onResponse(Call<List<CampaignTrainingOutput>> call, Response<List<CampaignTrainingOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {


                    campaignTrainingOutputList = response.body();


                    if (campaignTrainingOutputList.size() == 0)
                    {
                        progressDialog.setVisibility(View.GONE);
                        trainingList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_training);
                        errorDisplayText.setText(getString( R.string.error_no_data_campaign_training));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        trainingList.setVisibility(View.VISIBLE);

                        campaignTrainingListAdapter = new CampaignTrainingListAdapter(CampaignTrainingActivity.this,campaignTrainingOutputList);
                        trainingList.setAdapter(campaignTrainingListAdapter);
                        campaignTrainingListAdapter.notifyDataSetChanged();

                    }


                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    trainingList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<CampaignTrainingOutput>> call, Throwable t) {


                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    progressDialog.setVisibility(View.GONE);
                    trainingList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_training);
                    errorDisplayText.setText(getString( R.string.error_no_data_campaign_training));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    trainingList.setVisibility(View.GONE);


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
