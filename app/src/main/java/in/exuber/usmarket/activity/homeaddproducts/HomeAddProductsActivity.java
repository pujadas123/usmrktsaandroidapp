package in.exuber.usmarket.activity.homeaddproducts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.adapter.AddProductHomeListAdapter;
import in.exuber.usmarket.apimodels.addproduct.addproductinput.AddProductInput;
import in.exuber.usmarket.apimodels.addproduct.addproductinput.CreatedBy;
import in.exuber.usmarket.apimodels.addproduct.addproductinput.ProductId;
import in.exuber.usmarket.apimodels.addproduct.addproductinput.UserId;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
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

public class HomeAddProductsActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private RelativeLayout homeAddProductsActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_productList;

    private LinearLayout toolbarDoneClick;
    private RelativeLayout toolberNotificationClick;


    private SearchView productSearch;
    private RecyclerView productList;

    private NestedScrollView parentLayout;
    private LinearLayout realEstateClick, investmentClick;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;


    //Connection Detector
    private ConnectionDetector connectionDetector;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Progress dialog
    private ProgressDialog progressDialogSecond;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;


    //Declaring variables
    private List<ProductOutput> productOutputList;
    private List<ProductOutput> selectedProductOutputList;
    private boolean isRealEstateSelected = false;
    private boolean isInvestmentSelected = false;


    //Adapter
    private AddProductHomeListAdapter addProductHomeListAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add_products);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(HomeAddProductsActivity.this);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising progress dialog
        progressDialogSecond = new ProgressDialog(this);
        progressDialogSecond.setMessage(getString(R.string.loader_caption));
        progressDialogSecond.setCancelable(true);
        progressDialogSecond.setIndeterminate(false);
        progressDialogSecond.setCancelable(false);




        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.homeAddProduct_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //Initialising variables
        productOutputList = new ArrayList<>();
        selectedProductOutputList = new ArrayList<>();


        //Initialising views
        homeAddProductsActivityContainer = findViewById(R.id.activity_home_add_products);
        toolbarDoneClick = findViewById(R.id.ll_homeAddProduct_toolBar_doneClick);
        toolberNotificationClick=findViewById(R.id.rl_homeProfile_toolBar_notificationLayout);


        parentLayout = findViewById(R.id.nsv_homeAddProducts_parentlayout);

        realEstateClick = findViewById(R.id.ll_homeAddProducts_realEstateClick);
        investmentClick = findViewById(R.id.ll_homeAddProducts_investmentClick);

        productSearch = findViewById(R.id.et_homeAddProducts_search);



        //Recyclerview
        productList = findViewById(R.id.rv_homeAddProducts_productList);
        productList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProducts = new LinearLayoutManager(this);
        linearLayoutManagerProducts.setOrientation(LinearLayoutManager.VERTICAL);
        productList.setLayoutManager(linearLayoutManagerProducts);

        ///Swipe Refresh Layout
        swipeRefreshLayout_productList = findViewById(R.id.srl_homeAddProducts_pullToRefresh);
        swipeRefreshLayout_productList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);


        addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,productOutputList);
        productList.setAdapter(addProductHomeListAdapter);
        addProductHomeListAdapter.notifyDataSetChanged();

        boolean isShowcaseShown = marketPreference.getBoolean(Constants.IS_SHOWCASE_SHOWN, false);

        if (isShowcaseShown)
        {

            //Getting Products
            getProduct();
        }
        else
        {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_select_product);

            LinearLayout showcaseGetStartedClick = dialog.findViewById(R.id.ll_homeAddProducts_getStartedClick);


            showcaseGetStartedClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Hiding Keyboard
                    hideKeyBoard(HomeAddProductsActivity.this);

                    SharedPreferences.Editor preferenceEditor = marketPreference.edit();

                    //Preference Editor
                    preferenceEditor.putBoolean(Constants.IS_SHOWCASE_SHOWN, true);
                    preferenceEditor.commit();

                    dialog.dismiss();
                }
            });

            dialog.show();

            //Getting Products
            getProduct();
        }



        productSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (addProductHomeListAdapter != null)
                {
                    addProductHomeListAdapter.getFilter().filter(query.toLowerCase());
                }
                return false;
            }
        });

        swipeRefreshLayout_productList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Getting Products
                getProduct();

                swipeRefreshLayout_productList.setRefreshing(false);
            }
        });



        //Setting onclick
        realEstateClick.setOnClickListener(this);
        investmentClick.setOnClickListener(this);
        toolbarDoneClick.setOnClickListener(this);
        toolberNotificationClick.setOnClickListener(this);
        errorDisplayTryClick.setOnClickListener(this);


    }


    @Override
    public void onBackPressed() {

        boolean isShowcaseShown = marketPreference.getBoolean(Constants.IS_SHOWCASE_SHOWN, false);

        if (isShowcaseShown)
        {
            finish();
        }


    }



    @Override
    public void onClick(View view) {

        SharedPreferences.Editor preferenceEditor = marketPreference.edit();

        switch (view.getId())
        {

            case R.id.ll_homeAddProducts_realEstateClick:

                //Hiding Keyboard
                hideKeyBoard(HomeAddProductsActivity.this);

                if (isRealEstateSelected)
                {
                    realEstateClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                    isRealEstateSelected = false;
                }
                else
                {
                    realEstateClick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isRealEstateSelected = true;
                }

                //Filter data
                filterProductData();



                break;

            case R.id.ll_homeAddProducts_investmentClick:

                //Hiding Keyboard
                hideKeyBoard(HomeAddProductsActivity.this);

                if (isInvestmentSelected)
                {
                    investmentClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                    isInvestmentSelected = false;
                }
                else
                {
                    investmentClick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isInvestmentSelected = true;
                }

                //Filter data
                filterProductData();



                break;


            case R.id.ll_homeAddProduct_toolBar_doneClick:

                //Hiding Keyboard
                hideKeyBoard(HomeAddProductsActivity.this);

                selectedProductOutputList.clear();
                List<ProductOutput> selectedProductTempOutputList = addProductHomeListAdapter.getFilteredProductOutputList();

                for (int index = 0; index< selectedProductTempOutputList.size();index++)
                {
                    if (selectedProductTempOutputList.get(index).isSelected())
                    {
                        selectedProductOutputList.add(selectedProductTempOutputList.get(index));
                    }
                }

                Log.e("Selected Size", selectedProductOutputList.size()+"");

                if (selectedProductOutputList.size() == 0)
                {
                    //Preference Editor
                    preferenceEditor.putBoolean(Constants.IS_PRODUCT_ADDED, true);

                    preferenceEditor.commit();

                    //Going to Home Activity
                    startActivity(new Intent(HomeAddProductsActivity.this, HomeActivity.class));
                    finish();


                }
                else
                {

                    //Adding Product
                    addProduct();

                }




                break;

            case R.id.rl_homeProfile_toolBar_notificationLayout:

                //Hiding Keyboard
                hideKeyBoard(HomeAddProductsActivity.this);

                startActivity(new Intent(HomeAddProductsActivity.this, NotificationActivity.class));


                break;

            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(HomeAddProductsActivity.this);

                //Getting Products
                getProduct();


                break;


        }

    }

    //Func - Getting Product
    private void filterProductData() {

        if (isRealEstateSelected && isInvestmentSelected)
        {
            addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,productOutputList);
            productList.setAdapter(addProductHomeListAdapter);
            addProductHomeListAdapter.notifyDataSetChanged();
        }
        else if (isRealEstateSelected)
        {
            List<ProductOutput> filteredProductOutputList  = new ArrayList<>();

            for (int i=0;i<productOutputList.size();i++)
            {
                if (productOutputList.get(i).getProductCategory() != null)
                {

                    if (productOutputList.get(i).getProductCategory().getId().equals(Constants.PRODUCT_CATEGORY_REALESTATE_ID))
                    {
                        filteredProductOutputList.add(productOutputList.get(i));
                    }
                }
            }

            addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,filteredProductOutputList);
            productList.setAdapter(addProductHomeListAdapter);
            addProductHomeListAdapter.notifyDataSetChanged();
        }
        else if (isInvestmentSelected)
        {
            List<ProductOutput> filteredProductOutputList  = new ArrayList<>();

            for (int i=0;i<productOutputList.size();i++)
            {
                if (productOutputList.get(i).getProductCategory() != null)
                {
                    if (productOutputList.get(i).getProductCategory().getId().equals(Constants.PRODUCT_CATEGORY_INVESTMENT_ID))
                    {
                        filteredProductOutputList.add(productOutputList.get(i));
                    }
                }

            }

            addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,filteredProductOutputList);
            productList.setAdapter(addProductHomeListAdapter);
            addProductHomeListAdapter.notifyDataSetChanged();
        }
        else
        {
            addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,productOutputList);
            productList.setAdapter(addProductHomeListAdapter);
            addProductHomeListAdapter.notifyDataSetChanged();

        }
    }


    //Func - Getting Product
    private void getProduct() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        realEstateClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
        isRealEstateSelected = false;

        investmentClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
        isInvestmentSelected = false;


        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            parentLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);


            //Calling Service
            callGetProductService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            parentLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func - Add Product
    private void addProduct() {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callAddProductService();

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(homeAddProductsActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }




    //Service - Getting Product
    private void callGetProductService() {



        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<ProductOutput>> call = (Call<List<ProductOutput>>) api.getProduct(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_PRODUCT_LIST);
        call.enqueue(new Callback<List<ProductOutput>>() {
            @Override
            public void onResponse(Call<List<ProductOutput>> call, Response<List<ProductOutput>> response) {

                List<ProductOutput> productOutputListTemp = response.body();

                //Checking for response code
                if (response.code() == 200 ) {


                    if (productOutputListTemp.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        parentLayout.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_product);
                        errorDisplayText.setText(getString( R.string.error_no_data_products));
                        errorDisplayTryClick.setVisibility(View.GONE);
                    }
                    else {

                        productOutputList = productOutputListTemp;

                        addProductHomeListAdapter = new AddProductHomeListAdapter(HomeAddProductsActivity.this,productOutputList);
                        productList.setAdapter(addProductHomeListAdapter);
                        addProductHomeListAdapter.notifyDataSetChanged();

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        parentLayout.setVisibility(View.VISIBLE);


                    }

                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ProductOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                    errorDisplayText.setText(getString( R.string.error_no_data_products));
                    errorDisplayTryClick.setVisibility(View.GONE);
                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    parentLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }




                }

        });



    }

    //Service - Add Product
    private void callAddProductService() {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        List<AddProductInput> addProductInputList = new ArrayList<>();

        for (int index = 0; index<selectedProductOutputList.size();index++)
        {
            ProductOutput productOutput = selectedProductOutputList.get(index);

            AddProductInput addProductInput = new AddProductInput();

            UserId userIdObject = new UserId();
            userIdObject.setUserId(userId);

            CreatedBy createdByObject = new CreatedBy();
            createdByObject.setUserId(userId);

            ProductId productIdObject = new ProductId();
            productIdObject.setProductId(productOutput.getProductId());

            addProductInput.setUserId(userIdObject);
            addProductInput.setCreatedBy(createdByObject);
            addProductInput.setProductId(productIdObject);

            addProductInputList.add(addProductInput);

        }

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.addProduct(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_ADD_PRODUCT,
                addProductInputList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    SharedPreferences.Editor preferenceEditor = marketPreference.edit();

                    //Preference Editor
                    preferenceEditor.putBoolean(Constants.IS_PRODUCT_ADDED, true);

                    preferenceEditor.commit();

                    //Going to Home Activity
                    startActivity(new Intent(HomeAddProductsActivity.this, HomeActivity.class));
                    finish();


                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(homeAddProductsActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(homeAddProductsActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
