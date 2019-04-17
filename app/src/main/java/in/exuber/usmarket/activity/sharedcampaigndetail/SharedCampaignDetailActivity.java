package in.exuber.usmarket.activity.sharedcampaigndetail;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.CampaignShareLogListAdapter;
import in.exuber.usmarket.apimodels.sharecampaignlog.sharecampaignlogoutput.ShareCampaignLogOutput;
import in.exuber.usmarket.apimodels.sharedcampaign.sharedcampaignoutput.SharedCampaignOutput;
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

public class SharedCampaignDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout sharedCampaignDetailActivityContainer;
    private TextView toolbarHeader;

    private TextView campaignName, campaignDate, campaignCategory, campaignProduct, campaignLanguage;

    private ImageView campaignImage;
    private TextView campaignDescripton;

    private LinearLayout shareLogLayout;
    private RecyclerView shareLogList;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private SharedCampaignOutput sharedcampaignOutput;

    //Adapter
    private CampaignShareLogListAdapter campaignShareLogListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_campaign_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);



        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising views
        sharedCampaignDetailActivityContainer = findViewById(R.id.activity_shared_campaign_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        campaignName = findViewById(R.id.tv_sharedCampaignDetail_campaignName);
        campaignDate = findViewById(R.id.tv_sharedCampaignDetail_campaignDate);
        campaignCategory = findViewById(R.id.tv_sharedCampaignDetail_campaignCategory);
        campaignProduct = findViewById(R.id.tv_sharedCampaignDetail_campaignProduct);
        campaignLanguage = findViewById(R.id.tv_sharedCampaignDetail_campaignLanguage);


        campaignImage = findViewById(R.id.iv_sharedCampaignDetail_campaignThumbnail);
        campaignDescripton = findViewById(R.id.tv_sharedCampaignDetail_campaignDescription);

        shareLogLayout = findViewById(R.id.ll_sharedCampaignDetail_shareLogLayout);

        //Recyclerview
        shareLogList = findViewById(R.id.rv_sharedCampaignDetail_shareLogList);
        shareLogList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerShareLog = new LinearLayoutManager(this);
        linearLayoutManagerShareLog.setOrientation(LinearLayoutManager.VERTICAL);
        shareLogList.setLayoutManager(linearLayoutManagerShareLog);

        progressDialog =  findViewById(R.id.ll_custom_dialog_sharelog);
        errorDisplay =  findViewById(R.id.ll_errorShareLog_layout);

        errorDisplayIcon = findViewById(R.id.iv_errorShareLog_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorShareLog_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorShareLog_errorTryAgain);


        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.campaign_details_caps));

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String campaignItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_CAMPAIGN);


        //Converting string to Object
        Gson gson = new Gson();
        sharedcampaignOutput = gson.fromJson(campaignItemString, SharedCampaignOutput.class);

        campaignName.setText(sharedcampaignOutput.getCampaignId().getCompaignName());
        Log.e("CampName",sharedcampaignOutput.getCampaignId().getCompaignName());
        campaignCategory.setText(sharedcampaignOutput.getCampaignId().getCategory().getName());
        campaignProduct.setText(sharedcampaignOutput.getCampaignId().getProduct().getProductName());
        campaignLanguage.setText(sharedcampaignOutput.getCampaignId().getLanguage().getName());

        campaignDescripton.setText(sharedcampaignOutput.getCampaignId().getCompaignDesc());

        Date campaignDateOutput = new Date(sharedcampaignOutput.getCampaignId().getCreatedOn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String campaignDateString = simpleDateFormat.format(campaignDateOutput);

        campaignDate.setText(campaignDateString);

        if (sharedcampaignOutput.getCampaignId().getCompaignImage() == null)
        {

            campaignImage.setVisibility(View.GONE);

        }
        else
        {
            String campaignImageUrl = sharedcampaignOutput.getCampaignId().getCompaignImage();

            if (campaignImageUrl.isEmpty())
            {
                campaignImage.setVisibility(View.GONE);

            }
            else
            {
                Picasso.get()
                        .load(campaignImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(campaignImage);

                campaignImage.setVisibility(View.VISIBLE);

            }
        }

        Log.e("Campaign Id",sharedcampaignOutput.getCampaignId().getCompaignId());

        //ShareLog
        getShareLog();



        //Setting onclick
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

            case R.id.tv_errorShareLog_errorTryAgain:

                //ShareLog
                getShareLog();


                break;
        }

    }

    //Func - Share Log
    private void getShareLog() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            shareLogLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetShareLogService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            shareLogLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Service - Share Log
    private void callGetShareLogService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<ShareCampaignLogOutput>> call = (Call<List<ShareCampaignLogOutput>>) api.getShareCampaignLog(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_SHARE_CAMPAIGNS_LOGS,
                userId,
                sharedcampaignOutput.getCampaignId().getCompaignId());
        call.enqueue(new Callback<List<ShareCampaignLogOutput>>() {
            @Override
            public void onResponse(Call<List<ShareCampaignLogOutput>> call, Response<List<ShareCampaignLogOutput>> response) {


                //Checking for response code
                if (response.code() == 200 ) {


                    List<ShareCampaignLogOutput> shareCampaignLogOutputList = response.body();

                    if (shareCampaignLogOutputList.size() == 0)
                    {
                        progressDialog.setVisibility(View.GONE);
                        shareLogLayout.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_share);
                        errorDisplayText.setText(getString( R.string.error_no_data_sharelog_campaign));
                        errorDisplayTryClick.setVisibility(View.GONE);
                    }
                    else
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        shareLogLayout.setVisibility(View.VISIBLE);

                        //Setting adapter
                        campaignShareLogListAdapter = new CampaignShareLogListAdapter(SharedCampaignDetailActivity.this,shareCampaignLogOutputList);
                        shareLogList.setAdapter(campaignShareLogListAdapter);
                        campaignShareLogListAdapter.notifyDataSetChanged();
                    }


                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    shareLogLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ShareCampaignLogOutput>> call, Throwable t) {


                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    progressDialog.setVisibility(View.GONE);
                    shareLogLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_share);
                    errorDisplayText.setText(getString( R.string.error_no_data_sharelog_campaign));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    shareLogLayout.setVisibility(View.GONE);


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
