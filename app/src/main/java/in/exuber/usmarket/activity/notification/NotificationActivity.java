package in.exuber.usmarket.activity.notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.adapter.NotificationListAdapter;
import in.exuber.usmarket.apimodels.notification.notificationoutput.NotificationOutput;
import in.exuber.usmarket.apimodels.updatenotificationseen.updatenotificationseeninput.UpdateNotificationSeenInput;
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

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout notificationActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_notificationList;
    private TextView toolbarHeader;

    private RecyclerView notificationList;

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

    //Progress dialog
    private ProgressDialog progressDialogSecond;

    //Declaring variables
    private List<NotificationOutput> notificationOutputList;

    //Adapter
    private NotificationListAdapter notificationListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

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
        notificationOutputList = new ArrayList<>();

        //Initialising views
        notificationActivityContainer = findViewById(R.id.activity_notification);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        //Recyclerview
        notificationList = findViewById(R.id.rv_notification_notificationList);
        notificationList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(this);
        linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
        notificationList.setLayoutManager(linearLayoutManagerNotification);

        ///Swipe Refresh Layout
        swipeRefreshLayout_notificationList = findViewById(R.id.srl_notification_pullToRefresh);
        swipeRefreshLayout_notificationList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);


        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.notifications_caps));


        //Get Notification
        getNotification();

        swipeRefreshLayout_notificationList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Notification
                getNotification();

                swipeRefreshLayout_notificationList.setRefreshing(false);
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
                hideKeyBoard(NotificationActivity.this);

                //Get Notification
                getNotification();

                break;

        }

    }

    //Func - Moving to home page
    public void moveToPage(NotificationOutput notificationOutput) {

        startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
        finish();
    }



    //Func - Get Notification
    private void getNotification() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            notificationList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetNotificationService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            notificationList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func -  Update Seen
    public void updateSeenAndProceed(NotificationOutput notificationOutput) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            callUpdateSeenService(notificationOutput);

        }
        else {
            Snackbar snackbar = Snackbar
                    .make(notificationActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }



    //Service - Get Notification
    private void callGetNotificationService() {

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<NotificationOutput>> call = (Call<List<NotificationOutput>>) api.getNotification(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_NOTIFICATIONS);
        call.enqueue(new Callback<List<NotificationOutput>>() {
            @Override
            public void onResponse(Call<List<NotificationOutput>> call, Response<List<NotificationOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    notificationOutputList = response.body();


                    if (notificationOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        notificationList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_notification);
                        errorDisplayText.setText(getString( R.string.error_no_data_notification));
                        errorDisplayTryClick.setVisibility(View.GONE);



                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        notificationList.setVisibility(View.VISIBLE);

                        //Setting adapter
                        notificationListAdapter = new NotificationListAdapter(NotificationActivity.this,notificationOutputList);
                        notificationList.setAdapter(notificationListAdapter);
                        notificationListAdapter.notifyDataSetChanged();
                    }


                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    notificationList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<NotificationOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    notificationList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_notification);
                    errorDisplayText.setText(getString( R.string.error_no_data_notification));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    notificationList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }





            }

        });
    }

    //Service -  Update Seen
    private void callUpdateSeenService(NotificationOutput notificationOutput) {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        UpdateNotificationSeenInput updateNotificationSeenInput = new UpdateNotificationSeenInput();

        updateNotificationSeenInput.setId(notificationOutput.getId().toString());


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.updateNotificationSeen(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_UPDATE_NOTIFICATION_SEEN,
                updateNotificationSeenInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
                    finish();


                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(notificationActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(notificationActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
