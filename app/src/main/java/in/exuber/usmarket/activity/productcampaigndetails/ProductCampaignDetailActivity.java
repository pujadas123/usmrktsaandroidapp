package in.exuber.usmarket.activity.productcampaigndetails;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.CampaignProductListAdapter;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CampaignId;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CreatedBy;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.ShareCampaignInput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.Type;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.UserId;
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

public class ProductCampaignDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout productCampaignDetailContainer;
    private SwipeRefreshLayout swipeRefreshLayout_productCampaignList;
    private TextView toolbarHeader;

    private RecyclerView productCampaignList;

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
    private List<CampaignOutput> campaignOutputList;
    private ProductOutput productOutput;
    private ProductUserOutput productUserOutput;

    private boolean isUserProductPassed;
    private String productItemString;

    //Adapter
    private CampaignProductListAdapter campaignProductListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_campaign_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

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

        //Initialising variables
        productOutput = new ProductOutput();
        productUserOutput = new ProductUserOutput();
        campaignOutputList = new ArrayList<>();

        //Initialising views
        productCampaignDetailContainer = findViewById(R.id.activity_product_campaign_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        //Recyclerview
        productCampaignList=findViewById(R.id.rv_productCampaignDetail_productCampaignLIst);
        productCampaignList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerCampaigns = new LinearLayoutManager(ProductCampaignDetailActivity.this);
        linearLayoutManagerCampaigns.setOrientation(LinearLayoutManager.VERTICAL);
        productCampaignList.setLayoutManager(linearLayoutManagerCampaigns);

        ///Swipe Refresh Layout
        swipeRefreshLayout_productCampaignList = findViewById(R.id.srl_productCampaignDetail_pullToRefresh);
        swipeRefreshLayout_productCampaignList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);

        //Setting toolbar header
        toolbarHeader.setText(getString(R.string.product_campaigns_caps));


        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        isUserProductPassed = passedBundle.getBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT);
        productItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_PRODUCT);

        Log.e("Is User Product",isUserProductPassed+"");

        if (isUserProductPassed)
        {
            //Converting string to Object
            Gson gson = new Gson();
            productUserOutput = gson.fromJson(productItemString, ProductUserOutput.class);
        }
        else
        {
            //Converting string to Object
            Gson gson = new Gson();
            productOutput = gson.fromJson(productItemString, ProductOutput.class);
        }


        //Get Product campaigns
        getProductCampaigns();

        swipeRefreshLayout_productCampaignList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Product campaigns
                getProductCampaigns();

                swipeRefreshLayout_productCampaignList.setRefreshing(false);
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
            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(ProductCampaignDetailActivity.this);

                //Get Product campaigns
                getProductCampaigns();



                break;

        }


    }

    private void shareToMedia(String shareId, CampaignOutput shareCampaignOutput) {

        if (shareId.equals(Constants.SHARE_FACEBOOK_ID))
        {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.orca");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

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
                        .make(productCampaignDetailContainer, "Facebook have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }
        }

        if (shareId.equals(Constants.SHARE_INSTAGRAM_ID))
        {
            Intent instagramIntent = new Intent(Intent.ACTION_SEND);
            instagramIntent.setType("text/plain");
            instagramIntent.setPackage("com.instagram.android");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

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
                        .make(productCampaignDetailContainer, "Instagram have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_TWITTER_ID))
        {
            Intent twitterIntent = new Intent(Intent.ACTION_SEND);
            twitterIntent.setType("text/plain");
            twitterIntent.setPackage("com.twiter.android");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

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

                if (shareCampaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                    }
                    else
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                    }
                }

                twitterWebIntent.setAction(Intent.ACTION_VIEW);

                if (shareCampaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+shareCampaignOutput.getCompaignName())));

                }
                else
                {
                    String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+shareCampaignOutput.getCompaignName())));

                    }
                    else
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Link : "+campaignImageUrl)));

                    }
                }


                startActivity(twitterWebIntent);

                Snackbar snackbar = Snackbar
                        .make(productCampaignDetailContainer, "Twitter have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_WHATSAPP_ID))
        {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

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
                        .make(productCampaignDetailContainer, "Whatsapp have not been installed", Snackbar.LENGTH_LONG);

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

    //Func - Getting Product Campaigns
    private void getProductCampaigns() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            productCampaignList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetProductCampaignsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            productCampaignList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func - Share Campaign
    public void shareCampaign(String shareId, CampaignOutput shareCampaignOutput) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callShareCampaignService(shareId,shareCampaignOutput);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(productCampaignDetailContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }


    //Func - Getting Product Campaigns
    private void callGetProductCampaignsService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        String productId = null;

        if (isUserProductPassed)
        {
            productId = productUserOutput.getProductId().getProductId();
        }
        else
        {

            productId = productOutput.getProductId();
        }


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<CampaignOutput>> call = (Call<List<CampaignOutput>>) api.getCampaignsByProductId(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_CAMPAIGNS_BY_PRODUCT_ID,
                productId);
        call.enqueue(new Callback<List<CampaignOutput>>() {
            @Override
            public void onResponse(Call<List<CampaignOutput>> call, Response<List<CampaignOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    campaignOutputList = response.body();


                    if (campaignOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        productCampaignList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                        errorDisplayText.setText(getString( R.string.error_no_data_campaigns));
                        errorDisplayTryClick.setVisibility(View.GONE);



                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        productCampaignList.setVisibility(View.VISIBLE);

                        campaignProductListAdapter = new CampaignProductListAdapter(ProductCampaignDetailActivity.this,campaignOutputList);
                        productCampaignList.setAdapter(campaignProductListAdapter);
                        campaignProductListAdapter.notifyDataSetChanged();

                    }


                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    productCampaignList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<CampaignOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productCampaignList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                    errorDisplayText.setText(getString( R.string.error_no_data_campaigns));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productCampaignList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }





            }

        });

    }


    //Service - Share Campaign
    private void callShareCampaignService(final String shareId, final CampaignOutput shareCampaignOutput) {

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
        campaignIdObject.setCompaignId(shareCampaignOutput.getCompaignId());

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

                    //Share to Medis
                    shareToMedia(shareId,shareCampaignOutput);

                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(productCampaignDetailContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(productCampaignDetailContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
