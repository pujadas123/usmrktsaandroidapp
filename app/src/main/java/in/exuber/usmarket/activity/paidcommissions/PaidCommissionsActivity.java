package in.exuber.usmarket.activity.paidcommissions;

import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.PaidCommissionListAdapter;
import in.exuber.usmarket.apimodels.paidcommision.PaidCommissionOutput;
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

public class PaidCommissionsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout paidCommissionsActivityContainer;

    private TextView toolbarHeader;

    private NestedScrollView paidCommissionLayout;

    private LinearLayout searchLayout;
    private SearchView searchView;

    private TextView currentDateText, totalCommissionText;
    private LinearLayout dataLayout;
    private RecyclerView paidCommissionList;


    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;
    private LinearLayout errorDisplayPaidCommissions;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    private ImageView errorDisplayIconPaidCommissions;
    private TextView errorDisplayTextPaidCommissions;
    private TextView errorDisplayTryClickPaidCommissions;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;


    //Adapter
    private PaidCommissionListAdapter paidCommissionListAdapter;


    private List<PaidCommissionOutput> paidCommissionOutputList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_commissions);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        paidCommissionOutputList = new ArrayList<>();

        //Initialising variables
        paidCommissionsActivityContainer = findViewById(R.id.activity_paid_commissions);

        paidCommissionLayout = findViewById(R.id.nsv_paidCommissions_commissionLayout);
        searchLayout=findViewById(R.id.ll_paidCommissions_searchLayout);

        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);
        errorDisplayPaidCommissions =  findViewById(R.id.ll_errorPaidCommissions_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);

        errorDisplayIconPaidCommissions = findViewById(R.id.iv_errorPaidCommissions_errorIcon);
        errorDisplayTextPaidCommissions =  findViewById(R.id.tv_errorPaidCommissions_errorText);


        currentDateText = findViewById(R.id.tv_paidCommissions_currentDate);
        totalCommissionText = findViewById(R.id.tv_paidCommissions_totalCommission);

        searchView = findViewById(R.id.et_paidCommissions_search);

        dataLayout = findViewById(R.id.ll_paidCommissions_dataLayout);

        paidCommissionList = findViewById(R.id.rv_paidCommissions_paidCommissionsList);
        paidCommissionList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerPaidCommission = new LinearLayoutManager(this);
        linearLayoutManagerPaidCommission.setOrientation(LinearLayoutManager.VERTICAL);
        paidCommissionList.setLayoutManager(linearLayoutManagerPaidCommission);


        //Setting Current date
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = simpleDateFormat.format(date);
        currentDateText.setText(dateString);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.paid_commissions_caps));



        //Get Paid Commissions
        getPaidCommissions();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (paidCommissionListAdapter !=  null) {
                    paidCommissionListAdapter.getFilter().filter(query);
                }

                return false;
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
                hideKeyBoard(PaidCommissionsActivity.this);

                //Get Paid Commissions
                getPaidCommissions();

                break;

        }
    }

    //Func - Get Paid Commissions
    private void getPaidCommissions() {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            errorDisplayPaidCommissions.setVisibility(View.GONE);
            paidCommissionLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetPaidCommissionsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            paidCommissionLayout.setVisibility(View.GONE);
            errorDisplayPaidCommissions.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func - Get Paid Commissions
    private void callGetPaidCommissionsService() {

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<PaidCommissionOutput>> call = (Call<List<PaidCommissionOutput>>) api.getPaidCommissions(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_PAID_COMMISSIONS,
                userId);
        call.enqueue(new Callback<List<PaidCommissionOutput>>() {
            @Override
            public void onResponse(Call<List<PaidCommissionOutput>> call, Response<List<PaidCommissionOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    paidCommissionOutputList = response.body();


                    if (paidCommissionOutputList.size() == 0)
                    {


                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        paidCommissionLayout.setVisibility(View.VISIBLE);
                        searchLayout.setVisibility(View.GONE);
                        dataLayout.setVisibility(View.GONE);

                        errorDisplayPaidCommissions.setVisibility(View.VISIBLE);

                        errorDisplayIconPaidCommissions.setImageResource(R.drawable.ic_error_paid_commissions);
                        errorDisplayTextPaidCommissions.setText(getString(R.string.error_no_data_paid_commissions));

                        totalCommissionText.setText("$ 0.00");

                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);
                        errorDisplayPaidCommissions.setVisibility(View.GONE);

                        paidCommissionLayout.setVisibility(View.VISIBLE);
                        dataLayout.setVisibility(View.VISIBLE);

                        Log.e("Total Amount",""+paidCommissionOutputList.get(0).getTotalAmount());

                        DecimalFormat formatter = new DecimalFormat("$#,###,###.00");
                        totalCommissionText.setText(formatter.format(paidCommissionOutputList.get(0).getTotalAmount()));



                        paidCommissionListAdapter = new PaidCommissionListAdapter(PaidCommissionsActivity.this, paidCommissionOutputList);
                        paidCommissionList.setAdapter(paidCommissionListAdapter);
                        paidCommissionListAdapter.notifyDataSetChanged();



                    }


                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    paidCommissionLayout.setVisibility(View.GONE);
                    errorDisplayPaidCommissions.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<PaidCommissionOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {



                    progressDialog.setVisibility(View.GONE);
                    errorDisplay.setVisibility(View.GONE);

                    paidCommissionLayout.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.GONE);
                    dataLayout.setVisibility(View.GONE);

                    errorDisplayPaidCommissions.setVisibility(View.VISIBLE);

                    errorDisplayIconPaidCommissions.setImageResource(R.drawable.ic_error_paid_commissions);
                    errorDisplayTextPaidCommissions.setText(getString(R.string.error_no_data_paid_commissions));

                    totalCommissionText.setText("$ 0.00");




                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    paidCommissionLayout.setVisibility(View.GONE);
                    errorDisplayPaidCommissions.setVisibility(View.GONE);


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





//    //Func - Set Adapter Data
//    private void callPaidCommissionService() {
//
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setTimeout(60*1000);
//        StringEntity entity = null;
//        try {
//            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        } catch (Exception cart) {
//            cart.printStackTrace();
//        }
//
//        asyncHttpClient.addHeader("accept", "application/json;charset=UTF-8");
//
//        asyncHttpClient.addHeader("auth-token", marketPreference.getString("tokenid", ""));
//
//        asyncHttpClient.addHeader("user-id", marketPreference.getString("userid", ""));
//
//        asyncHttpClient.addHeader("role-id", marketPreference.getString("roleid", ""));
//
//        asyncHttpClient.addHeader("service", Constants.APP_PAID_COMISSION_SERVICE_NAME);
//        Log.e("comission_service_name", Constants.APP_PAID_COMISSION_SERVICE_NAME);
//
//        asyncHttpClient.get(null, Constants.APP_PAID_COMISSION_LIST_API + marketPreference.getString("userid", "") + "/", null, "application/json", new AsyncHttpResponseHandler() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                pd=new ProgressDialog(PaidCommissionsActivity.this);
//                pd.setMessage("Please Wait...");
//                pd.setCancelable(true);
//                pd.setIndeterminate(false);
//                pd.setCancelable(false);
//                pd.show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                final String Response = new String(responseBody);
//                Log.v("comission_Response", Response);
//                pd.dismiss();
//
//                Log.v("comission_Status_code", statusCode+"");
//
//                if (statusCode==200) {
//
//                    if (Response.equals("")) {
//                        fl_paidcommission_frameLayout.setVisibility(View.GONE);
//                        Log.e("Paidcommission", "CommissionMSG");
//                        pd.dismiss();
//                       /* Snackbar snackbar = Snackbar
//                                .make(ll_paidCommission_Parent, R.string.paid_commission, Snackbar.LENGTH_LONG);
//
//                        snackbar.show();*/
//                    } else {
//                        fl_paidcommission_frameLayout.setVisibility(View.VISIBLE);
//                        try {
//
//                            JSONArray faqjsonArray = new JSONArray(Response);
//                            for (int i = 0; i < faqjsonArray.length(); i++) {
//                                JSONObject jsonobject = faqjsonArray.getJSONObject(i);
//
//                                JSONObject productObject = jsonobject.getJSONObject("productId");
//
//                                String productId = productObject.getString("id");
//                                String productName = productObject.getString("productName");
//                                int paid = jsonobject.getInt("paid");
//                                int totalAmount = jsonobject.getInt("totalAmount");
//
//                                if (totalAmount == 0) {
//                                    txt_total.setVisibility(View.GONE);
//                                } else {
//                                    double amount = Double.parseDouble(String.valueOf(totalAmount));
//                                    DecimalFormat formatter = new DecimalFormat("$#,###,###.00");
//                                    txt_TotalPrice.setText(formatter.format(amount));
//
//                                    txt_total.setVisibility(View.VISIBLE);
//                                }
//
//                                //txt_TotalPrice.setText("$" + totalAmount + ".00");
//
//
//                                paidCommissionOutputs.add(new PaidCommissionOutput(productId, productName, paid, totalAmount));
//
//                                Log.d("TotalCommission", String.valueOf(totalAmount));
//                                Log.d("Commission", String.valueOf(paid));
//
//                            }
//
//                            Log.e("PAidMsg", String.valueOf(paidCommissionOutputs.size()));
//                            if (paidCommissionOutputs.size() != 0) {
//                                //Setting adapter
//                                paidCommissionListAdapter = new PaidCommissionListAdapter(PaidCommissionsActivity.this, paidCommissionOutputs);
//                                recycList_PaidCommision.setAdapter(paidCommissionListAdapter);
//                                paidCommissionListAdapter.notifyDataSetChanged();
//                                pd.dismiss();
//                            } else {
//                                Log.e("Paidcommission", "CommissionMSG");
//                                pd.dismiss();
//                                Snackbar snackbar = Snackbar
//                                        .make(ll_paidCommission_Parent, R.string.paid_commission, Snackbar.LENGTH_LONG);
//
//                                snackbar.show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                //If status code is not 200
//                else
//                {
//                    pd.dismiss();
//
//                    Snackbar snackbar = Snackbar
//                            .make(ll_paidCommission_Parent, R.string.error_response_code + statusCode, Snackbar.LENGTH_LONG);
//
//                    snackbar.show();
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                pd.dismiss();
//                Log.e("comission_Error",error.toString());
//                Log.e("comission_Errorstatus", String.valueOf(statusCode));
//
//            }
//
//        });
//
//    }





}
