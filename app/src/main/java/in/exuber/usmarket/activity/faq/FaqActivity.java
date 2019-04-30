package in.exuber.usmarket.activity.faq;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.FaqListAdapter;
import in.exuber.usmarket.apimodels.faq.faqoutput.FAQOutput;
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

public class FaqActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout faqActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_faqList;
    private TextView toolbarHeader;

    private RecyclerView faqList;

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

    private List<FAQOutput> faqOutputList;

    //Adapter
    private FaqListAdapter faqListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


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
        faqOutputList = new ArrayList<>();

        //Initialising views
        faqActivityContainer = findViewById(R.id.activity_faq);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        faqList = findViewById(R.id.rv_faq_faqLIst);
        faqList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerFaq = new LinearLayoutManager(this);
        linearLayoutManagerFaq.setOrientation(LinearLayoutManager.VERTICAL);
        faqList.setLayoutManager(linearLayoutManagerFaq);

        ///Swipe Refresh Layout
        swipeRefreshLayout_faqList = findViewById(R.id.srl_faq_pullToRefresh);
        swipeRefreshLayout_faqList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);



        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.faq_caps));

        //Get FAQ
        getFaq();

        swipeRefreshLayout_faqList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get FAQ
                getFaq();

                swipeRefreshLayout_faqList.setRefreshing(false);
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
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(FaqActivity.this);

                //Get FAQ
                getFaq();

                break;

        }

    }

    //Func - Get FAQ
    private void getFaq() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            faqList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetFaqService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            faqList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Service - Get FAQ
    private void callGetFaqService() {

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<FAQOutput>> call = (Call<List<FAQOutput>>) api.getFAQ(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_FAQ);
        call.enqueue(new Callback<List<FAQOutput>>() {
            @Override
            public void onResponse(Call<List<FAQOutput>> call, Response<List<FAQOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    faqOutputList = response.body();


                    if (faqOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        faqList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_faq);
                        errorDisplayText.setText(getString( R.string.error_no_data_faq));
                        errorDisplayTryClick.setVisibility(View.GONE);



                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        faqList.setVisibility(View.VISIBLE);

                        //Setting adapter
                        faqListAdapter = new FaqListAdapter(FaqActivity.this,faqOutputList);
                        faqList.setAdapter(faqListAdapter);
                        faqListAdapter.notifyDataSetChanged();
                    }


                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    faqList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<FAQOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    faqList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_faq);
                    errorDisplayText.setText(getString( R.string.error_no_data_faq));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    faqList.setVisibility(View.GONE);


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
