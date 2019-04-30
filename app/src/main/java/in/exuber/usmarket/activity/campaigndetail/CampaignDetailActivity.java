package in.exuber.usmarket.activity.campaigndetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaignproductdetails.CampaignProductDetailActivity;
import in.exuber.usmarket.activity.campaigntraining.CampaignTrainingActivity;
import in.exuber.usmarket.adapter.CampaignShareLogListAdapter;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CampaignId;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CreatedBy;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.ShareCampaignInput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.Type;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.UserId;
import in.exuber.usmarket.apimodels.sharecampaignlog.sharecampaignlogoutput.ShareCampaignLogOutput;
import in.exuber.usmarket.utils.Api;
import in.exuber.usmarket.utils.Config;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

public class CampaignDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout campaignDetailActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_shareLogList;
    private TextView toolbarHeader;

    private TextView campaignName, campaignDate, campaignCategory, campaignProduct, campaignLanguage;
    private LinearLayout campaignProductClick,campaignTrainingClick;
    private ImageView facebookClick, instagramClick, twitterClick, whatsappClick;

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

    //Progress dialog
    private ProgressDialog progressDialogSecond;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private CampaignOutput campaignOutput;


    //Adapter
    private CampaignShareLogListAdapter campaignShareLogListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(CampaignDetailActivity.this);

        //Initialising progress dialog
        progressDialogSecond = new ProgressDialog(this);
        progressDialogSecond.setMessage(getString(R.string.loader_caption));
        progressDialogSecond.setCancelable(true);
        progressDialogSecond.setIndeterminate(false);
        progressDialogSecond.setCancelable(false);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);

        //Initialising views
        campaignDetailActivityContainer = findViewById(R.id.activity_campaign_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        campaignName = findViewById(R.id.tv_campaignDetail_campaignName);
        campaignDate = findViewById(R.id.tv_campaignDetail_campaignDate);
        campaignCategory = findViewById(R.id.tv_campaignDetail_campaignCategory);
        campaignProduct = findViewById(R.id.tv_campaignDetail_campaignProduct);
        campaignLanguage = findViewById(R.id.tv_campaignDetail_campaignLanguage);

        campaignTrainingClick = findViewById(R.id.ll_campaignDetail_campaignTrainingClick);
        campaignProductClick = findViewById(R.id.ll_campaignDetail_productClick);

        campaignImage = findViewById(R.id.iv_campaignDetail_campaignThumbnail);
        campaignDescripton = findViewById(R.id.tv_campaignDetail_campaignDescription);

        facebookClick = findViewById(R.id.iv_campaignDetails_facebookShareClick);
        instagramClick = findViewById(R.id.iv_campaignDetails_instagramShareClick);
        twitterClick = findViewById(R.id.iv_campaignDetails_twitterShareClick);
        whatsappClick = findViewById(R.id.iv_campaignDetails_whatsappShareClick);

        shareLogLayout = findViewById(R.id.ll_campaignDetail_shareLogLayout);

        //Recyclerview
        shareLogList = findViewById(R.id.rv_campaignDetail_shareLogList);
        shareLogList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerShareLog = new LinearLayoutManager(this);
        linearLayoutManagerShareLog.setOrientation(LinearLayoutManager.VERTICAL);
        shareLogList.setLayoutManager(linearLayoutManagerShareLog);

        swipeRefreshLayout_shareLogList = findViewById(R.id.srl_campaignDetail_pullToRefresh);
        swipeRefreshLayout_shareLogList.setColorSchemeResources(
                R.color.colorPrimary);

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
        campaignOutput = gson.fromJson(campaignItemString, CampaignOutput.class);

        //Setting values
        campaignName.setText(campaignOutput.getCompaignName());


        campaignCategory.setText(campaignOutput.getCategory().getName());
        campaignProduct.setText(campaignOutput.getProduct().getProductName());
        campaignLanguage.setText(campaignOutput.getLanguage().getName());

        campaignDescripton.setText(campaignOutput.getCompaignDesc());

        Date campaignDateOutput = new Date(campaignOutput.getCreatedOn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String campaignDateString = simpleDateFormat.format(campaignDateOutput);

        campaignDate.setText(campaignDateString);

        if (campaignOutput.getCompaignImage() == null)
        {

            campaignImage.setVisibility(View.GONE);

        }
        else
        {
            String campaignImageUrl = campaignOutput.getCompaignImage();

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

        Log.e("Campaign Id",campaignOutput.getCompaignId());

        //ShareLog
        getShareLog(null);

        swipeRefreshLayout_shareLogList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //ShareLog
                getShareLog(null);

                swipeRefreshLayout_shareLogList.setRefreshing(false);
            }
        });

        //Setting onclick
        campaignTrainingClick.setOnClickListener(this);
        campaignProductClick.setOnClickListener(this);
        facebookClick.setOnClickListener(this);
        instagramClick.setOnClickListener(this);
        twitterClick.setOnClickListener(this);
        whatsappClick.setOnClickListener(this);
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

        Gson gson = new Gson();

        //Converting to string
        String campaignItemString = gson.toJson(campaignOutput);

        switch (view.getId())
        {

            case R.id.tv_errorShareLog_errorTryAgain:


                //ShareLog
                getShareLog(null);


                break;

            case R.id.ll_campaignDetail_productClick:

                //Preparing Intent
                Intent campaignProductIntent = new Intent(CampaignDetailActivity.this, CampaignProductDetailActivity.class);

                //Create the bundle
                Bundle campaignProductBundle = new Bundle();

                //Add your data to bundle
                campaignProductBundle.putString(Constants.INTENT_KEY_SELECTED_CAMPAIGN,campaignItemString);

                //Add the bundle to the intent
                campaignProductIntent.putExtras(campaignProductBundle);

                startActivity(campaignProductIntent);

                break;



            case R.id.ll_campaignDetail_campaignTrainingClick:



                //Preparing Intent
                Intent campaignTrainingIntent = new Intent(CampaignDetailActivity.this, CampaignTrainingActivity.class);

                //Create the bundle
                Bundle campaignTrainingBundle = new Bundle();

                //Add your data to bundle
                campaignTrainingBundle.putString(Constants.INTENT_KEY_SELECTED_CAMPAIGN,campaignItemString);

                //Add the bundle to the intent
                campaignTrainingIntent.putExtras(campaignTrainingBundle);

                startActivity(campaignTrainingIntent);

                break;


            case R.id.iv_campaignDetails_facebookShareClick:

                shareCampaign(Constants.SHARE_FACEBOOK_ID);


                break;

            case R.id.iv_campaignDetails_instagramShareClick:

                shareCampaign(Constants.SHARE_INSTAGRAM_ID);



                break;

            case R.id.iv_campaignDetails_twitterShareClick:

                shareCampaign(Constants.SHARE_TWITTER_ID);


                break;

            case R.id.iv_campaignDetails_whatsappShareClick:

                shareCampaign(Constants.SHARE_WHATSAPP_ID);


                break;
        }
    }

    private void shareToMedia(String shareId) {

        if (shareId.equals(Constants.SHARE_FACEBOOK_ID))
        {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.orca");

            if (campaignOutput.getCompaignImage() == null)
            {

                facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = campaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                }
                else
                {
                    facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }


            try {

                startActivity(facebookIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignDetailActivityContainer, "Facebook have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }
        }

        if (shareId.equals(Constants.SHARE_INSTAGRAM_ID))
        {
            Intent instagramIntent = new Intent(Intent.ACTION_SEND);
            instagramIntent.setType("text/plain");
            instagramIntent.setPackage("com.instagram.android");

            if (campaignOutput.getCompaignImage() == null)
            {

                instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = campaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                }
                else
                {
                    instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }



            try {

                startActivity(instagramIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignDetailActivityContainer, "Instagram have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_TWITTER_ID))
        {
            Intent twitterIntent = new Intent(Intent.ACTION_SEND);
            twitterIntent.setType("text/plain");
            twitterIntent.setPackage("com.twiter.android");

            if (campaignOutput.getCompaignImage() == null)
            {

                twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = campaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                }
                else
                {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }



            try {

                startActivity(twitterIntent);

            } catch (Exception e) {

                Intent twitterWebIntent = new Intent();

                if (campaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                }
                else
                {
                    String campaignImageUrl = campaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                    }
                    else
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                    }
                }

                twitterWebIntent.setAction(Intent.ACTION_VIEW);

                if (campaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+campaignOutput.getCompaignName())));

                }
                else
                {
                    String campaignImageUrl = campaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+campaignOutput.getCompaignName())));

                    }
                    else
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Link : "+campaignImageUrl)));

                    }
                }


                startActivity(twitterWebIntent);

                Snackbar snackbar = Snackbar
                        .make(campaignDetailActivityContainer, "Twitter have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_WHATSAPP_ID))
        {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");

            if (campaignOutput.getCompaignImage() == null)
            {

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = campaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+campaignOutput.getCompaignName());

                }
                else
                {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }

            try {

                startActivity(whatsappIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignDetailActivityContainer, "Whatsapp have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();
            }
        }
    }

    //Func - Encode Url
    private String urlEncode(String s) {

        try {

            return URLEncoder.encode(s, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return "";
        }
    }

    //Func - Share Log
    private void getShareLog(String shareId) {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            shareLogLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetShareLogService(shareId);

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



    //Func - Share Campaign
    public void shareCampaign(String shareId) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callShareCampaignService(shareId);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(campaignDetailActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Func - Share Log
    private void callGetShareLogService(final String shareId) {


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
                campaignOutput.getCompaignId());
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
                        campaignShareLogListAdapter = new CampaignShareLogListAdapter(CampaignDetailActivity.this,shareCampaignLogOutputList);
                        shareLogList.setAdapter(campaignShareLogListAdapter);
                        campaignShareLogListAdapter.notifyDataSetChanged();
                    }


                    if (shareId !=null)
                    {
                        //Share to Medis
                        shareToMedia(shareId);

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




    //Service - Share Campaign
    private void callShareCampaignService(final String shareId) {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        ShareCampaignInput shareCampaignInput = new ShareCampaignInput();

        UserId userIdObject = new UserId();
        userIdObject.setUserId(userId);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);

        CampaignId campaignIdObject = new CampaignId();
        campaignIdObject.setCompaignId(campaignOutput.getCompaignId());

        Type typeObject = new Type();
        typeObject.setId(shareId);

        shareCampaignInput.setUserId(userIdObject);
        shareCampaignInput.setCreatedBy(createdByObject);
        shareCampaignInput.setCampaignId(campaignIdObject);
        shareCampaignInput.setType(typeObject);



        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.shareCampaigns(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_SHARE_CAMPAIGNS,
                shareCampaignInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {


                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    //Share Log
                    getShareLog(shareId);

                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(campaignDetailActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(campaignDetailActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

                snackbar.show();



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
