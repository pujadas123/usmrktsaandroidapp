package in.exuber.usmarket.activity.productedit;

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
import android.util.Log;
import android.view.View;
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
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.adapter.ProductEditListAdapter;
import in.exuber.usmarket.apimodels.editproduct.editproductinput.EditProductInput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
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

public class ProductEditActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout productEditActivityContainer;
    private SwipeRefreshLayout swipeRefreshLayout_productList;

    private RecyclerView productList;
    private SearchView productSearch;

    private RelativeLayout toolbarDoneClick;
    private RelativeLayout toolbarAddClick;

    private NestedScrollView productLayout;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection Detector
    private ConnectionDetector connectionDetector;

    //Progress dialog
    private ProgressDialog progressDialogSecond;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;


    //Declaring variables
    private List<ProductUserOutput> productOutputList;
    private List<ProductUserOutput> selectedProductOutputList;

    //Adapter
    private ProductEditListAdapter productEditListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(ProductEditActivity.this);

        //Initialising progress dialog
        progressDialogSecond = new ProgressDialog(this);
        progressDialogSecond.setMessage(getString(R.string.loader_caption));
        progressDialogSecond.setCancelable(true);
        progressDialogSecond.setIndeterminate(false);
        progressDialogSecond.setCancelable(false);


        //Initialising variables
        productOutputList = new ArrayList<>();
        selectedProductOutputList = new ArrayList<>();


        //Initialising views
        productEditActivityContainer = findViewById(R.id.activity_product_edit);
        productLayout = findViewById(R.id.nsv_productEdit_productLayout);

        toolbarDoneClick = findViewById(R.id.rl_homeProducts_toolBar_doneLayout);
        toolbarAddClick = findViewById(R.id.rl_homeProducts_toolBar_addLayout);

        //Recyclerview
        productList = findViewById(R.id.rv_productEdit_productList);
        productList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProducts = new LinearLayoutManager(this);
        linearLayoutManagerProducts.setOrientation(LinearLayoutManager.VERTICAL);
        productList.setLayoutManager(linearLayoutManagerProducts);

        swipeRefreshLayout_productList = findViewById(R.id.srl_productEdit_pullToRefresh);
        swipeRefreshLayout_productList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);


        productSearch = findViewById(R.id.et_productEdit_search);



        productSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (productEditListAdapter != null)
                {
                    productEditListAdapter.getFilter().filter(query.toLowerCase());
                }

                return false;
            }
        });

        //Calling Service
        getProduct();

        swipeRefreshLayout_productList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Getting Products
                getProduct();

                swipeRefreshLayout_productList.setRefreshing(false);
            }
        });

        //Setting onclick listner
        toolbarDoneClick.setOnClickListener(this);
        toolbarAddClick.setOnClickListener(this);
        errorDisplayTryClick.setOnClickListener(this);



    }


    @Override
    public void onBackPressed() {

        finish();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.rl_homeProducts_toolBar_doneLayout:

                //Hiding Keyboard
                hideKeyBoard(ProductEditActivity.this);



                selectedProductOutputList.clear();
                List<ProductUserOutput> selectedProductTempOutputList = productEditListAdapter.getFilteredProductOutputList();

                for (int index = 0; index< selectedProductTempOutputList.size();index++)
                {
                    if (!selectedProductTempOutputList.get(index).isSelected())
                    {
                        selectedProductOutputList.add(selectedProductTempOutputList.get(index));
                    }
                }

                Log.e("Selected Size", selectedProductOutputList.size()+"");

                if (selectedProductOutputList.size() == 0)
                {

                    finish();
                }
                else
                {

                    //Adding Product
                    editProduct();

                }


                break;

            case R.id.rl_homeProducts_toolBar_addLayout:

                //Hiding Keyboard
                hideKeyBoard(ProductEditActivity.this);

                //Calling Add Product Activity
                startActivity(new Intent(ProductEditActivity.this, HomeAddProductsActivity.class));
                finish();

                break;

            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(ProductEditActivity.this);

                //Getting Products
                getProduct();


                break;
        }
    }

    //Func - Getting Product
    private void getProduct() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            productLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetProductService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            productLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
        }
    }

    //Func - Edit Product
    private void editProduct() {


        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callEditProductService();

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(productEditActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

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

        Call<List<ProductUserOutput>> call = (Call<List<ProductUserOutput>>) api.getProductByUserId(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_PRODUCT_LIST_BY_USERID,
                userId);
        call.enqueue(new Callback<List<ProductUserOutput>>() {
            @Override
            public void onResponse(Call<List<ProductUserOutput>> call, Response<List<ProductUserOutput>> response) {

                List<ProductUserOutput> productOutputListTemp = response.body();

                //Checking for response code
                if (response.code() == 200 ) {


                    if (productOutputListTemp.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        productLayout.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_product);
                        errorDisplayText.setText(getString( R.string.error_no_data_myproduct));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {

                        productOutputList = productOutputListTemp;

                        for (int index = 0; index<productOutputList.size();index++)
                        {
                            ProductUserOutput productUserOutput = productOutputList.get(index);
                            productUserOutput.setSelected(true);
                            productOutputList.set(index,productUserOutput);
                        }

                        //Setting adapter
                        productEditListAdapter = new ProductEditListAdapter(ProductEditActivity.this, productOutputList);
                        productList.setAdapter(productEditListAdapter);
                        productEditListAdapter.notifyDataSetChanged();

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        productLayout.setVisibility(View.VISIBLE);

                    }

                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    productLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ProductUserOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_product);
                    errorDisplayText.setText(getString( R.string.error_no_data_myproduct));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }



            }

        });



    }

    //Service - Edit Product
    private void callEditProductService() {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        List<EditProductInput> editProductInputList = new ArrayList<>();

        for (int index = 0; index<selectedProductOutputList.size();index++)
        {
            ProductUserOutput productOutput = selectedProductOutputList.get(index);

            EditProductInput editProductInput = new EditProductInput();

            editProductInput.setId(productOutput.getId().toString());

            editProductInputList.add(editProductInput);

        }

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.editProduct(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_EDIT_PRODUCT,
                editProductInputList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    SharedPreferences.Editor preferenceEditor = marketPreference.edit();

                    //Preference Editor
                    preferenceEditor.putBoolean(Constants.IS_PRODUCT_DATA_CHANGED, true);

                    preferenceEditor.commit();

                    finish();


                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(productEditActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(productEditActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
