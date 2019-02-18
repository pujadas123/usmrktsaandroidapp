package in.exuber.usmarket.activity.producttraining;

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
import in.exuber.usmarket.adapter.ProductTrainingListAdapter;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.apimodels.producttraining.producttrainingoutput.ProductTrainingOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
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

public class ProductTrainingActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout productTrainingActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_trainingList;
    private TextView toolbarHeader;

    private TextView productName, productDate, productCategory, productDescription;

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


    //Declaring variables
    private ProductOutput productOutput;
    private ProductUserOutput productUserOutput;

    private boolean isUserProductPassed;
    private String productItemString;

    private List<ProductTrainingOutput> productTrainingOutputList;

    private static final int PERMISSIONS_REQUEST_ACCOUNTS = 1;

    //Adapter
    private ProductTrainingListAdapter productTrainingListAdapter;

    private CustomTabsClient customTabsClient;
    private CustomTabsSession customTabsSession;
    private CustomTabsServiceConnection customTabsServiceConnection;
    private CustomTabsIntent customTabsIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_training);

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

        //Initialising variables
        productTrainingOutputList = new ArrayList<>();

        //Initialising views
        productTrainingActivityContainer = findViewById(R.id.activity_campaign_training);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        productName = findViewById(R.id.tv_productTraining_productName);
        productDate = findViewById(R.id.tv_productTraining_productDate);
        productCategory = findViewById(R.id.tv_productTraining_productCategory);
        productDescription = findViewById(R.id.tv_productTraining_productDescription);

        //Recyclerview
        trainingList = findViewById(R.id.rv_productTraining_trainingList);
        trainingList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProductTraining = new LinearLayoutManager(this);
        linearLayoutManagerProductTraining.setOrientation(LinearLayoutManager.VERTICAL);
        trainingList.setLayoutManager(linearLayoutManagerProductTraining);

        ///Swipe Refresh Layout
        swipeRefreshLayout_trainingList = findViewById(R.id.srl_productTraining_pullToRefresh);
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
        toolbarHeader.setText(getResources().getString(R.string.product_training_caps));

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        isUserProductPassed = passedBundle.getBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT);
        productItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_PRODUCT);

        Log.e("Is User Product", isUserProductPassed + "");

        if (isUserProductPassed)
        {
            setUserProductData();
        }
        else
        {
            setProductData();
        }

        //Get product training
        getProductTraining();

        swipeRefreshLayout_trainingList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get product training
                getProductTraining();

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
                hideKeyBoard(ProductTrainingActivity.this);

                //Get product training
                getProductTraining();


                break;

        }

    }

    //Func - Setting User Product Data
    private void setUserProductData() {

        //Converting string to Object
        Gson gson = new Gson();
        productUserOutput = gson.fromJson(productItemString, ProductUserOutput.class);

        //Setting values
        productName.setText(productUserOutput.getProductId().getProductName());

        if (productUserOutput.getProductId().getProductCategory() == null)
        {
            productCategory.setVisibility(View.GONE);
        }
        else
        {
            productCategory.setText(productUserOutput.getProductId().getProductCategory().getName());
            productCategory.setVisibility(View.VISIBLE);
        }

        Date productDateOutput = new Date(productUserOutput.getProductId().getCreatedOn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String productDateString = simpleDateFormat.format(productDateOutput);

        productDate.setText(productDateString);



        if (productUserOutput.getProductId().getDesc() == null)
        {
            productDescription.setVisibility(View.GONE);
        }
        else
        {
            productDescription.setText(productUserOutput.getProductId().getDesc());
            productDescription.setVisibility(View.VISIBLE);
        }




    }

    //Func - Setting Product Data
    private void setProductData() {

        //Converting string to Object
        Gson gson = new Gson();
        productOutput = gson.fromJson(productItemString, ProductOutput.class);

        //Setting values
        productName.setText(productOutput.getProductName());

        if (productOutput.getProductCategory() == null)
        {
            productCategory.setVisibility(View.GONE);
        }
        else
        {
            productCategory.setText(productOutput.getProductCategory().getName());
            productCategory.setVisibility(View.VISIBLE);
        }

        Date productDateOutput = new Date(productOutput.getCreatedOn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String productDateString = simpleDateFormat.format(productDateOutput);

        productDate.setText(productDateString);

        if (productOutput.getDesc() == null)
        {
            productDescription.setVisibility(View.GONE);
        }
        else
        {
            productDescription.setText(productOutput.getDesc());
            productDescription.setVisibility(View.VISIBLE);
        }


    }

    //Func  - Open Document Url
    public void openDocumentUrl(ProductTrainingOutput selectedProductTrainingOutput) {

        //Opening Url
        customTabsIntent.launchUrl(this, Uri.parse(selectedProductTrainingOutput.getUrl()));
    }

    //Func  - Download Document Url
    public void downloadDocumentUrl(ProductTrainingOutput selectedProductTrainingOutput) {

        //Checking permission Marshmallow
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermissionStorage())
            {

                downloadFile(selectedProductTrainingOutput);

            } else {

                //Request permission
                requestPermissionStorage();
            }
        } else {


            downloadFile(selectedProductTrainingOutput);
        }
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


    //Func  - Open Link Url
    public void openLinkUrl(ProductTrainingOutput selectedProductTrainingOutput) {

        //Opening to Link
        customTabsIntent.launchUrl(this, Uri.parse(selectedProductTrainingOutput.getLink()));
    }

    //Func - Getting Product Training
    private void getProductTraining() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            trainingList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);


            //Calling Service
            callGetProductTrainingService();

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
    private void downloadFile(ProductTrainingOutput selectedProductTrainingOutput) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            Intent intent = new Intent(this, DownloadService.class);
            intent.putExtra(Constants.INTENT_KEY_SELECTED_FILE_URL, selectedProductTrainingOutput.getLink());
            startService(intent);

        } else {

            Snackbar snackbar = Snackbar
                    .make(productTrainingActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Func - Getting Product Training
    private void callGetProductTrainingService() {


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

        Call<List<ProductTrainingOutput>> call = (Call<List<ProductTrainingOutput>>) api.getProductTraining(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_PRODUCT_TRAINING,
                productId);
        call.enqueue(new Callback<List<ProductTrainingOutput>>() {
            @Override
            public void onResponse(Call<List<ProductTrainingOutput>> call, Response<List<ProductTrainingOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {



                    productTrainingOutputList = response.body();


                    if (productTrainingOutputList.size() == 0)
                    {
                        progressDialog.setVisibility(View.GONE);
                        trainingList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_training);
                        errorDisplayText.setText(getString( R.string.error_no_data_training));
                        errorDisplayTryClick.setVisibility(View.GONE);

                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        trainingList.setVisibility(View.VISIBLE);

                        Log.e("Training Size",productTrainingOutputList.size()+"");

                        productTrainingListAdapter = new ProductTrainingListAdapter(ProductTrainingActivity.this,productTrainingOutputList);
                        trainingList.setAdapter(productTrainingListAdapter);
                        productTrainingListAdapter.notifyDataSetChanged();

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
            public void onFailure(Call<List<ProductTrainingOutput>> call, Throwable t) {



                Log.e("Failure",t.toString());


                if (t instanceof IOException) {

                    progressDialog.setVisibility(View.GONE);
                    trainingList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_training);
                    errorDisplayText.setText(getString( R.string.error_no_data_training));
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
