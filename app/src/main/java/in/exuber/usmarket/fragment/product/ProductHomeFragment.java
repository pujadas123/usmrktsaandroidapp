package in.exuber.usmarket.fragment.product;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.exuber.usmarket.activity.productedit.ProductEditActivity;
import in.exuber.usmarket.adapter.ProductHomeListAdapter;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
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

import static android.content.Context.MODE_PRIVATE;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductHomeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout productHomeFragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout_productList;
    private NestedScrollView productLayout;

    private RelativeLayout toolbarEditClick;
    private RelativeLayout toolbarAddClick;

    private RecyclerView productList;

    private SearchView searchProduct;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;
    private LinearLayout errorDisplayMyProduct;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    private ImageView errorDisplayIconMyProduct;
    private TextView errorDisplayTextMyProduct;
    private TextView errorDisplayTryClickMyProduct;



    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection Detector
    private ConnectionDetector connectionDetector;



    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private List<ProductUserOutput> productOutputList;

     //Adapter
    private ProductHomeListAdapter productHomeListAdapter;


    public ProductHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View productView = inflater.inflate(R.layout.fragment_product_home, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising variables
        productOutputList = new ArrayList<>();

        //Initialising views
        productHomeFragmentContainer = productView.findViewById(R.id.fragment_product_home);

        toolbarEditClick = productView.findViewById(R.id.rl_homeProducts_toolBar_editLayout);
        toolbarAddClick = productView.findViewById(R.id.rl_homeProducts_toolBar_addLayout);

        //Recyclerview
        productList = productView.findViewById(R.id.rv_homeProduct_productList);
        productList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProducts = new LinearLayoutManager(getActivity());
        linearLayoutManagerProducts.setOrientation(LinearLayoutManager.VERTICAL);
        productList.setLayoutManager(linearLayoutManagerProducts);

        productLayout = productView.findViewById(R.id.nsv_homeProduct_parentlayout);
        searchProduct = productView.findViewById(R.id.et_homeProduct_search);

        ///Swipe Refresh Layout
        swipeRefreshLayout_productList = productView.findViewById(R.id.srl_homeProduct_pullToRefresh);
        swipeRefreshLayout_productList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  productView.findViewById(R.id.ll_custom_dialog);
        errorDisplay =  productView.findViewById(R.id.ll_errorMain_layout);
        errorDisplayMyProduct =  productView.findViewById(R.id.ll_errorMyProduct_layout);

        errorDisplayIcon = productView.findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  productView.findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  productView.findViewById(R.id.tv_errorMain_errorTryAgain);

        errorDisplayIconMyProduct = productView.findViewById(R.id.iv_errorMyProduct_errorIcon);
        errorDisplayTextMyProduct =  productView.findViewById(R.id.tv_errorMyProduct_errorText);
        errorDisplayTryClickMyProduct =  productView.findViewById(R.id.tv_errorMyProduct_errorTryAgain);

        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (productHomeListAdapter != null)
                {
                    productHomeListAdapter.getFilter().filter(query.toLowerCase());
                }

                return false;
            }
        });

        //Calling Service
        getProduct();

        //Swipe Referesh listner
        swipeRefreshLayout_productList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Calling Service
                getProduct();

                swipeRefreshLayout_productList.setRefreshing(false);
            }
        });



        //Setting onclick listner
        toolbarEditClick.setOnClickListener(this);
        toolbarAddClick.setOnClickListener(this);
        errorDisplayTryClick.setOnClickListener(this);
        errorDisplayTryClickMyProduct.setOnClickListener(this);


        return productView;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isProductDataChanged = marketPreference.getBoolean(Constants.IS_PRODUCT_DATA_CHANGED, false);

        if (isProductDataChanged)
        {

            SharedPreferences.Editor preferenceEditor = marketPreference.edit();

            //Preference Editor
            preferenceEditor.putBoolean(Constants.IS_PRODUCT_DATA_CHANGED, false);

            preferenceEditor.commit();

            //Calling Service
            getProduct();
        }


    }

    public void onBackPressed() {

        getActivity().finish();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.rl_homeProducts_toolBar_editLayout:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Edit Product Activity
                startActivity(new Intent(getActivity(), ProductEditActivity.class));


                break;

            case R.id.rl_homeProducts_toolBar_addLayout:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Add Product Activity
                startActivity(new Intent(getActivity(), HomeAddProductsActivity.class));

                break;

            case R.id.tv_errorMyProduct_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Edit Product Activity
                startActivity(new Intent(getActivity(), HomeAddProductsActivity.class));


                break;

            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Service
                getProduct();



                break;
        }

    }

    //Func - Getting Product
    private void getProduct() {

        toolbarEditClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Hiding views
            errorDisplay.setVisibility(View.GONE);
            errorDisplayMyProduct.setVisibility(View.GONE);
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
            errorDisplayMyProduct.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
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



                //Checking for response code
                if (response.code() == 200 ) {


                    productOutputList = response.body();

                    if (productOutputList.size() == 0)
                    {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        productLayout.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        errorDisplayMyProduct.setVisibility(View.VISIBLE);

                        errorDisplayTextMyProduct.setText(getString( R.string.error_no_data_myproduct));

                        toolbarEditClick.setVisibility(View.INVISIBLE);


                    }
                    else {

                        progressDialog.setVisibility(View.GONE);
                        errorDisplayMyProduct.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);


                        productLayout.setVisibility(View.VISIBLE);

                        //Setting adapter
                        productHomeListAdapter = new ProductHomeListAdapter(getActivity(), productOutputList);
                        productList.setAdapter(productHomeListAdapter);
                        productHomeListAdapter.notifyDataSetChanged();

                    }



                }
                //If status code is not 200
                else
                {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productLayout.setVisibility(View.GONE);
                    errorDisplayMyProduct.setVisibility(View.GONE);

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
                    errorDisplay.setVisibility(View.GONE);

                    errorDisplayMyProduct.setVisibility(View.VISIBLE);

                    errorDisplayTextMyProduct.setText(getString( R.string.error_no_data_myproduct));

                    toolbarEditClick.setVisibility(View.INVISIBLE);



                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    productLayout.setVisibility(View.GONE);
                    errorDisplayMyProduct.setVisibility(View.GONE);

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
