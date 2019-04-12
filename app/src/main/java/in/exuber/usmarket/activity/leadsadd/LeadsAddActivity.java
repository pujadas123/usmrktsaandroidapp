package in.exuber.usmarket.activity.leadsadd;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rilixtech.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.ProductHomeListAdapter;
import in.exuber.usmarket.apimodels.addlead.addleadinput.AddLeadInput;
import in.exuber.usmarket.apimodels.addlead.addleadinput.AssignedTo;
import in.exuber.usmarket.apimodels.addlead.addleadinput.Category;
import in.exuber.usmarket.apimodels.addlead.addleadinput.CategoryList;
import in.exuber.usmarket.apimodels.addlead.addleadinput.CreatedBy;
import in.exuber.usmarket.apimodels.addlead.addleadinput.LeadOwner;
import in.exuber.usmarket.apimodels.addlead.addleadinput.LeadSource;
import in.exuber.usmarket.apimodels.addlead.addleadinput.Product;
import in.exuber.usmarket.apimodels.addlead.addleadinput.ProductList;
import in.exuber.usmarket.apimodels.addlead.addleadinput.Source;
import in.exuber.usmarket.apimodels.addlead.addleadinput.UserId;
import in.exuber.usmarket.apimodels.login.loginoutput.LoginOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.dialog.AddLeadsLeadSourceFilterDialog;
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
import static in.exuber.usmarket.utils.UtilMethods.isValidEmail;

public class LeadsAddActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsAddActivityContainer;
    private TextView toolbarHeader, toolbarHeaderDone;

    LinearLayout ll_toolbarHeaderDone;


    private EditText firstName, lastName, otherDetails, referralName;
    private TextView firstNameError, lastNameError, leadSourceError, otherDetailsError, interestError, categoriesError, referralError, phoneNumberError, emailError;
    private LinearLayout otherDetailsLayout,referralLayout;

    private EditText contactFacebook, contactInstagram, contactTwitter,contactWebsite, contactEmail, contactPhone;
    private CountryCodePicker phoneCodePicker;

    private LinearLayout leadSourceClick;
    private TextView leadSource;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Progress dialog
    private ProgressDialog progressDialog;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private int selectedLeadSourcePosition = -1;

    private MultiAutoCompleteTextView interests, categories;
    private String[] catogoryInputList = {"Investment", "Real Estate"};


    //Declaring variables
    private List<ProductUserOutput> productOutputList;

    ArrayList<String> productNameList=new ArrayList<>();
    ArrayList<String> productIdList=new ArrayList<>();

    ArrayList<String>SelecetedPName=new ArrayList<>();
    ArrayList<String>SelecetedPId=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_add);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Initialising progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        //Initialising variables
        productOutputList = new ArrayList<>();

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Initialising views
        leadsAddActivityContainer = findViewById(R.id.activity_leads_add);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        ll_toolbarHeaderDone=findViewById(R.id.ll_editLeads_toolBar_action);
        toolbarHeaderDone=findViewById(R.id.iv_editLeads_toolBar_done);
        ll_toolbarHeaderDone.setOnClickListener(this);

        otherDetailsLayout = findViewById(R.id.ll_addLeads_otherDetailsLayout);
        referralLayout=findViewById(R.id.ll_addLeads_referralLayout);

        firstName = findViewById(R.id.et_addLeads_firstName);
        lastName = findViewById(R.id.et_addLeads_lastName);
        otherDetails = findViewById(R.id.et_addLeads_otherDetails);
        referralName = findViewById(R.id.et_addLeads_referralName);
        categories = findViewById(R.id.et_addLeads_categories);
        interests = findViewById(R.id.et_addLeads_interests);


        contactFacebook = findViewById(R.id.et_addLeads_facebook);
        contactInstagram = findViewById(R.id.et_addLeads_instagram);
        contactTwitter = findViewById(R.id.et_addLeads_twitter);
        contactWebsite=findViewById(R.id.et_addLeads_website);
        contactEmail = findViewById(R.id.et_addLeads_email);
        contactPhone = findViewById(R.id.et_addLeads_phoneNumber);

        phoneCodePicker = findViewById(R.id.cpp_addLeads_phoneNumberPicker);

        firstNameError = findViewById(R.id.tv_addLeads_firstNameError);
        lastNameError = findViewById(R.id.tv_addLeads_lastNameError);
        leadSourceError = findViewById(R.id.tv_addLeads_leadSourceError);
        otherDetailsError = findViewById(R.id.tv_addLeads_otherDetailsError);
        interestError = findViewById(R.id.tv_addLeads_interestsError);
        categoriesError = findViewById(R.id.tv_addLeads_categoriesError);
        referralError = findViewById(R.id.tv_addLeads_referralNameError);
        phoneNumberError = findViewById(R.id.tv_addLeads_phoneNumberError);
        emailError = findViewById(R.id.tv_addLeads_emailError);


        leadSourceClick = findViewById(R.id.ll_addLeads_leadSourceClick);
        leadSource = findViewById(R.id.tv_addLeads_leadSource);


        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(contactPhone);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.add_lead_caps));
        toolbarHeaderDone.setText(getResources().getString(R.string.done));

        //Hiding views
        otherDetailsLayout.setVisibility(View.GONE);
        referralLayout.setVisibility(View.GONE);
        firstNameError.setVisibility(View.GONE);
        lastNameError.setVisibility(View.GONE);
        leadSourceError.setVisibility(View.GONE);
        otherDetailsError.setVisibility(View.GONE);
        interestError.setVisibility(View.GONE);
        categoriesError.setVisibility(View.GONE);
        referralError.setVisibility(View.GONE);
        phoneNumberError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);

        //Autocomplete Text view
        interests.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LeadsAddActivity.this,
                android.R.layout.select_dialog_item, catogoryInputList);
        interests.setThreshold(1);
        interests.setAdapter(arrayAdapter);

        //Setting onclick
        leadSourceClick.setOnClickListener(this);


        //Calling Service
        callGetProductService();


        categories.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(LeadsAddActivity.this,
                android.R.layout.select_dialog_item, productNameList);
        categories.setThreshold(1);
        categories.setAdapter(arrayAdapter2);


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }*/

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

                    if (productOutputList.size()!=0){
                        for (int i=0;i<productOutputList.size();i++){
                            productNameList.add(productOutputList.get(i).getProductId().getProductName());
                            productIdList.add(productOutputList.get(i).getProductId().getProductId());

                            Log.e("NameList",productNameList.get(i));
                            Log.e("NameIdList",productIdList.get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductUserOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());
            }
        });
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_addLeads_leadSourceClick:

                //Calling dialog
                FragmentManager filterManager = getFragmentManager();
                AddLeadsLeadSourceFilterDialog filterDialog = new AddLeadsLeadSourceFilterDialog(selectedLeadSourcePosition);
                filterDialog.show(filterManager, "FILTER_DIALOG");

                break;


            case R.id.ll_editLeads_toolBar_action:

                //Hiding Keyboard
                hideKeyBoard(LeadsAddActivity.this);

                //Hiding views
                firstNameError.setVisibility(View.GONE);
                lastNameError.setVisibility(View.GONE);
                leadSourceError.setVisibility(View.GONE);
                otherDetailsError.setVisibility(View.GONE);
                interestError.setVisibility(View.GONE);
                categoriesError.setVisibility(View.GONE);
                phoneNumberError.setVisibility(View.GONE);
                emailError.setVisibility(View.GONE);

                String firstNameText = firstName.getText().toString().trim();
                String lastNameText = lastName.getText().toString().trim();
                String otherDetailsText = otherDetails.getText().toString().trim();
                String referralText = referralName.getText().toString().trim();
                String categoriesText = categories.getText().toString().trim();
                String interestsText = interests.getText().toString().trim();
                String contactPhoneText = contactPhone.getText().toString().trim();
                String contactEmailText = contactEmail.getText().toString().trim();

                boolean validFlag = validateTextFields(firstNameText,lastNameText,otherDetailsText,categoriesText,interestsText,contactPhoneText,contactEmailText);

                if (validFlag)
                {
                    addLeads(firstNameText,lastNameText,otherDetailsText,referralText,categoriesText,interestsText);

                }

                break;
        }


    }



    public void setLeadSource(int clickPosition, String leadSourceName) {

        referralLayout.setVisibility(View.GONE);
        referralError.setVisibility(View.GONE);
        otherDetailsLayout.setVisibility(View.GONE);
        otherDetailsError.setVisibility(View.GONE);



        selectedLeadSourcePosition = clickPosition;
        leadSource.setText(leadSourceName);
        leadSourceError.setVisibility(View.GONE);


        String loginResponse = marketPreference.getString(Constants.LOGIN_RESPONSE, null);

        //Converting string to Object
        Gson gson = new Gson();
        LoginOutput loginOutput = gson.fromJson(loginResponse, LoginOutput.class);


        if (selectedLeadSourcePosition == 4)
        {
            referralLayout.setVisibility(View.VISIBLE);
            referralError.setVisibility(View.GONE);

            //referralName.setText(loginOutput.getData().getName() + " " + loginOutput.getData().getLastName());
        }
        else
        {
            referralLayout.setVisibility(View.GONE);
            referralError.setVisibility(View.GONE);
        }

        if (selectedLeadSourcePosition == 5)
        {
            otherDetailsLayout.setVisibility(View.VISIBLE);
            otherDetailsError.setVisibility(View.GONE);

            //otherDetails.setText(loginOutput.getData().getName() + " " + loginOutput.getData().getLastName());
        }



    }

    //Func - Validating text fields
    private boolean validateTextFields(String firstNameText, String lastNameText, String otherDetailsText, String interestsText,  String categoriesText, String contactPhoneText, String contactEmailText) {

        boolean validFlag = true;

        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(contactPhone);

        if (categoriesText.isEmpty()) {

            categoriesError.setText(getString(R.string.error_category_empty));
            categoriesError.setVisibility(View.VISIBLE);
            categories.requestFocus();
            validFlag = false;

        }

        if (interestsText.isEmpty()) {

            interestError.setText(getString(R.string.error_interests_empty));
            interestError.setVisibility(View.VISIBLE);
            interests.requestFocus();
            validFlag = false;

        }

        if (contactPhoneText.isEmpty()) {


            phoneNumberError.setText(getString(R.string.error_phone_number_empty));
            phoneNumberError.setVisibility(View.VISIBLE);
            contactPhone.requestFocus();

            validFlag = false;
        }
        else
        {
            if (!phoneCodePicker.isValid()) {

                phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
                phoneNumberError.setVisibility(View.VISIBLE);
                contactPhone.requestFocus();

                validFlag = false;
            }
        }

        if (contactEmailText.isEmpty()) {

            emailError.setText(getString(R.string.error_email_empty));
            emailError.setVisibility(View.VISIBLE);
            contactEmail.requestFocus();

            validFlag = false;
        }
        else
        {
            if (!isValidEmail(contactEmailText)) {

                emailError.setText(getString(R.string.error_email_invalid));
                emailError.setVisibility(View.VISIBLE);
                contactEmail.requestFocus();

                validFlag = false;
            }
        }




        if (selectedLeadSourcePosition == 5)
        {
            if (otherDetailsText.isEmpty())
            {
                otherDetailsError.setText(getString(R.string.error_details_empty));
                otherDetailsError.setVisibility(View.VISIBLE);
                otherDetails.requestFocus();
                validFlag = false;
            }

        }

        if (selectedLeadSourcePosition == -1)
        {
            leadSourceError.setText(getString(R.string.error_lead_source_empty));
            leadSourceError.setVisibility(View.VISIBLE);
            leadSourceClick.requestFocus();
            validFlag = false;
        }



        if (lastNameText.isEmpty()) {

            lastNameError.setText(getString(R.string.error_last_name_empty));
            lastNameError.setVisibility(View.VISIBLE);
            lastName.requestFocus();
            validFlag = false;

        }

        if (firstNameText.isEmpty()) {

            firstNameError.setText(getString(R.string.error_first_name_empty));
            firstNameError.setVisibility(View.VISIBLE);
            firstName.requestFocus();
            validFlag = false;

        }

        return validFlag;
    }

    //Func - Add Leads
    private void addLeads(String firstNameText, String lastNameText, String otherDetailsText, String referralText, String categoriesText, String interestsText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            callAddLeadsService(firstNameText,lastNameText,otherDetailsText,referralText,categoriesText,interestsText);
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(leadsAddActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Service - Add Leads
    private void callAddLeadsService(String firstNameText, String lastNameText, String otherDetailsText, String referralText,String categoriesText, String interestsText) {

        //Showing loading
        progressDialog.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        String contactFacebookText = contactFacebook.getText().toString().trim();
        String contactInstagramText = contactInstagram.getText().toString().trim();
        String contactTwitterText = contactTwitter.getText().toString().trim();
        String contactWebsiteText = contactWebsite.getText().toString().trim();
        String contactEmailText = contactEmail.getText().toString().trim();
        String contactPhoneText = contactPhone.getText().toString().trim();

        AddLeadInput addLeadInput = new AddLeadInput();

        addLeadInput.setName(firstNameText);
        addLeadInput.setLastName(lastNameText);


        addLeadInput.setFacebook(contactFacebookText);
        addLeadInput.setInstagram(contactInstagramText);
        addLeadInput.setTwitter(contactTwitterText);
        addLeadInput.setWebsite(contactWebsiteText);
        addLeadInput.setEmail(contactEmailText);
        addLeadInput.setPhoneNo(contactPhoneText);

        if (contactPhoneText.isEmpty())
        {
            addLeadInput.setCountryCode("");
        }
        else
        {
            addLeadInput.setCountryCode(phoneCodePicker.getSelectedCountryCodeWithPlus());
        }

        LeadSource leadSourceObject = new LeadSource();
        switch (selectedLeadSourcePosition)
        {
            case 0:

                leadSourceObject.setId(Constants.LEADSOURCE_SOCIAL_NETWORK_ID);
                addLeadInput.setUserName("");

                break;

            case 1:

                leadSourceObject.setId(Constants.LEADSOURCE_WEBSITE_ID);
                addLeadInput.setUserName("");

                break;

            case 2:

                leadSourceObject.setId(Constants.LEADSOURCE_EMAIL_ID);
                addLeadInput.setUserName("");

                break;

            case 3:

                leadSourceObject.setId(Constants.LEADSOURCE_PHONE_ID);
                addLeadInput.setUserName("");

                break;

            case 4:

                leadSourceObject.setId(Constants.LEADSOURCE_REFEREL_ID);
                addLeadInput.setUserName(referralName.getText().toString());

                break;

            case 5:

                leadSourceObject.setId(Constants.LEADSOURCE_OTHER_ID);
                addLeadInput.setUserName(otherDetails.getText().toString());

                break;
        }

        addLeadInput.setLeadSource(leadSourceObject);

        Source sourceObject = new Source();
        sourceObject.setId("4");
        addLeadInput.setSource(sourceObject);

        AssignedTo assignedToObject = new AssignedTo();
        assignedToObject.setUserId(userId);
        addLeadInput.setSource(sourceObject);

        LeadOwner leadOwnerObject = new LeadOwner();
        leadOwnerObject.setUserId(userId);
        addLeadInput.setLeadOwner(leadOwnerObject);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);
        addLeadInput.setCreatedBy(createdByObject);

        UserId userIdObject = new UserId();
        userIdObject.setUserId(userId);
        addLeadInput.setUserId(userIdObject);

        Log.e("INTERESTS",interestsText);

        String[] parts = interestsText.split(",");
        boolean isRealEstateSelected = false;
        boolean isInvestmentSelected = false;



        for (int index=0;index<parts.length;index++){

            if (parts[index].trim().equals("Real Estate"))
            {
                isRealEstateSelected = true;

                Log.e("Real Estate","YES");
            }

            if (parts[index].trim().equals("Investment"))
            {
                isInvestmentSelected = true;

                Log.e("INVESTMENT","YES");
            }
        }

        List<CategoryList> categoryListUpload = new ArrayList<>();

        if (isRealEstateSelected)
        {
            CategoryList categoryList = new CategoryList();
            Category category = new Category();
            category.setId(Constants.PRODUCT_CATEGORY_REALESTATE_ID);
            categoryList.setCategory(category);

            categoryListUpload.add(categoryList);
        }

        if (isInvestmentSelected)
        {
            CategoryList categoryList = new CategoryList();
            Category category = new Category();
            category.setId(Constants.PRODUCT_CATEGORY_INVESTMENT_ID);
            categoryList.setCategory(category);

            categoryListUpload.add(categoryList);
        }

        if (categoryListUpload.size()>0)
        {
            addLeadInput.setCategoryList(categoryListUpload);
        }

        List<ProductList> productListUpload = new ArrayList<>();
        String[] parts2 = categoriesText.split(",");
        for (int index=0;index<parts2.length;index++){
            SelecetedPName.add(parts2[index].trim());


            Log.e("Selected",SelecetedPName.get(index));
        }

        for (int j=0;j<SelecetedPName.size();j++){
            for (int k=0; k<productOutputList.size();k++){
                if (SelecetedPName.get(j).equals(productOutputList.get(k).getProductId().getProductName()))
                {
                    SelecetedPId.add(productOutputList.get(k).getProductId().getProductId());

                }
            }
        }




        for (int m=0;m<SelecetedPId.size();m++){
            ProductList productList=new ProductList();
            Product product=new Product();
            product.setProductId(SelecetedPId.get(m));
            productList.setProduct(product);
            productListUpload.add(productList);
        }




        if (productListUpload.size()>0)
        {
            addLeadInput.setProductList(productListUpload);
        }

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.addLeads(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_ADD_LEADS,
                addLeadInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    SharedPreferences.Editor preferenceEditor = marketPreference.edit();

                    //Preference Editor
                    preferenceEditor.putBoolean(Constants.IS_ACTIVE_LEAD_DATA_CHANGED, true);
                    preferenceEditor.putBoolean(Constants.IS_READY_LEAD_DATA_CHANGED, true);
                    preferenceEditor.putBoolean(Constants.IS_CONVERTED_LEAD_DATA_CHANGED, true);

                    preferenceEditor.commit();

                    finish();


                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(leadsAddActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(leadsAddActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
