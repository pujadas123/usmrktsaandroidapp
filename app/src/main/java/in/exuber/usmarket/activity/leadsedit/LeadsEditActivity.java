package in.exuber.usmarket.activity.leadsedit;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.activeleadsdetails.LeadsDetailActivity;
import in.exuber.usmarket.activity.leadsadd.LeadsAddActivity;
import in.exuber.usmarket.adapter.ProductHomeListAdapter;
import in.exuber.usmarket.apimodels.addlead.addleadinput.Product;
import in.exuber.usmarket.apimodels.addlead.addleadinput.ProductList;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.apimodels.editlead.editleadinput.AssignedTo;
import in.exuber.usmarket.apimodels.editlead.editleadinput.Category;
import in.exuber.usmarket.apimodels.editlead.editleadinput.CategoryList;
import in.exuber.usmarket.apimodels.editlead.editleadinput.CreatedBy;
import in.exuber.usmarket.apimodels.editlead.editleadinput.EditLeadInput;
import in.exuber.usmarket.apimodels.editlead.editleadinput.LeadOwner;
import in.exuber.usmarket.apimodels.editlead.editleadinput.LeadSource;
import in.exuber.usmarket.apimodels.editlead.editleadinput.Source;
import in.exuber.usmarket.apimodels.editlead.editleadinput.UpdatedBy;
import in.exuber.usmarket.apimodels.editlead.editleadinput.UserId;
import in.exuber.usmarket.apimodels.login.loginoutput.LoginOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.dialog.EditLeadsLeadSourceFilterDialog;
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

public class LeadsEditActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsEditActivityContainer;
    private TextView toolbarHeader, toolbarHeaderDone;

    LinearLayout ll_toolbarHeaderDone;


    private EditText firstName, lastName, otherDetails, referralName;
    private TextView firstNameError, lastNameError, leadSourceError, otherDetailsError, referralError, interestError, categoriesError, phoneNumberError, emailError;
    private LinearLayout otherDetailsLayout, referralLayout;

    private LinearLayout leadSourceClick;
    private TextView leadSource;

    private EditText contactFacebook, contactInstagram, contactTwitter,contactWebsite, contactEmail, contactPhone;
    private CountryCodePicker phoneCodePicker;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Progress dialog
    private ProgressDialog progressDialog;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private AllLeadsOutput allLeadsOutput;
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
        setContentView(R.layout.activity_leads_edit);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Initialising variables
        productOutputList = new ArrayList<>();

        //Initialising progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Initialising views
        leadsEditActivityContainer = findViewById(R.id.activity_leads_edit);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        ll_toolbarHeaderDone=findViewById(R.id.ll_editLeads_toolBar_action);
        toolbarHeaderDone=findViewById(R.id.iv_editLeads_toolBar_done);
        ll_toolbarHeaderDone.setOnClickListener(this);


        firstName = findViewById(R.id.et_editLeads_firstName);
        lastName =findViewById(R.id.et_editLeads_lastName);
        otherDetails = findViewById(R.id.et_editLeads_otherDetails);
        referralName = findViewById(R.id.et_addLeads_referralName);
        interests = findViewById(R.id.et_editLeads_interests);
        categories = findViewById(R.id.et_editLeads_categories);

        otherDetailsLayout = findViewById(R.id.ll_editLeads_otherDetailsLayout);
        referralLayout=findViewById(R.id.ll_editLeads_ReferralLayout);

        firstNameError = findViewById(R.id.tv_editLeads_firstNameError);
        lastNameError = findViewById(R.id.tv_editLeads_lastNameError);
        leadSourceError = findViewById(R.id.tv_editLeads_leadSourceError);
        otherDetailsError = findViewById(R.id.tv_editLeads_otherDetailsError);
        referralError=findViewById(R.id.tv_editLeads_ReferralNameError);
        interestError = findViewById(R.id.tv_editLeads_interestsError);
        categoriesError = findViewById(R.id.tv_editLeads_categoriesError);
        phoneNumberError = findViewById(R.id.tv_editLeads_phoneNumberError);
        emailError = findViewById(R.id.tv_editLeads_emailError);

        leadSourceClick = findViewById(R.id.ll_editLeads_leadSourceClick);
        leadSource = findViewById(R.id.tv_editLeads_leadSource);

        contactFacebook = findViewById(R.id.et_editLeads_facebook);
        contactInstagram = findViewById(R.id.et_editLeads_instagram);
        contactTwitter = findViewById(R.id.et_editLeads_twitter);
        contactWebsite=findViewById(R.id.et_editLeads_website);
        contactEmail = findViewById(R.id.et_editLeads_email);
        contactPhone = findViewById(R.id.et_editLeads_phoneNumber);

        phoneCodePicker = findViewById(R.id.cpp_editLeads_phoneNumberPicker);

        //Hiding views
        otherDetailsLayout.setVisibility(View.GONE);
        firstNameError.setVisibility(View.GONE);
        lastNameError.setVisibility(View.GONE);
        leadSourceError.setVisibility(View.GONE);
        otherDetailsLayout.setVisibility(View.GONE);
        referralLayout.setVisibility(View.GONE);
        interestError.setVisibility(View.GONE);
        categoriesError.setVisibility(View.GONE);
        phoneNumberError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);

        //Autocomplete Text view
        interests.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LeadsEditActivity.this,
                android.R.layout.select_dialog_item, catogoryInputList);

        interests.setThreshold(1);
        interests.setAdapter(arrayAdapter);

        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(contactPhone);


        //Calling Service
        callGetProductService();

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        String leadItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_LEAD);


        //Converting string to Object
        Gson gson = new Gson();
        allLeadsOutput = gson.fromJson(leadItemString, AllLeadsOutput.class);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.edit_lead_caps));
        toolbarHeaderDone.setText(getResources().getString(R.string.done));

        //Setting values
        firstName.setText(allLeadsOutput.getName());
        lastName.setText(allLeadsOutput.getLastName());

        //Set LeadSource
        if (allLeadsOutput.getLeadSource() != null)
        {
            String selectedLeadSourceId = allLeadsOutput.getLeadSource().getId();

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_SOCIAL_NETWORK_ID))
            {
                selectedLeadSourcePosition = 0;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());
            }

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_WEBSITE_ID))
            {
                selectedLeadSourcePosition = 1;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());
            }

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_EMAIL_ID))
            {
                selectedLeadSourcePosition = 2;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());
            }

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_PHONE_ID))
            {
                selectedLeadSourcePosition = 3;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());
            }

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_REFEREL_ID))
            {
                selectedLeadSourcePosition = 4;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());

                referralLayout.setVisibility(View.VISIBLE);
                referralError.setVisibility(View.GONE);

                if (allLeadsOutput.getUserName() != null)
                {
                    referralName.setText(allLeadsOutput.getUserName());
                }
            }

            if (selectedLeadSourceId.equals(Constants.LEADSOURCE_OTHER_ID))
            {
                selectedLeadSourcePosition = 5;
                leadSource.setText(allLeadsOutput.getLeadSource().getName());

                otherDetailsLayout.setVisibility(View.VISIBLE);
                otherDetailsError.setVisibility(View.GONE);

                if (allLeadsOutput.getUserName() != null)
                {
                    otherDetails.setText(allLeadsOutput.getUserName());
                }
            }

        }

        if (allLeadsOutput.getFacebook() != null)
        {
            contactFacebook.setText(allLeadsOutput.getFacebook());
        }

        if (allLeadsOutput.getInstagram() != null)
        {
            contactInstagram.setText(allLeadsOutput.getInstagram());
        }

        if (allLeadsOutput.getTwitter() != null)
        {
            contactTwitter.setText(allLeadsOutput.getTwitter());
        }

        if (allLeadsOutput.getWebsite() != null)
        {
            contactWebsite.setText(allLeadsOutput.getWebsite());
        }

        if (allLeadsOutput.getEmail() != null)
        {
            contactEmail.setText(allLeadsOutput.getEmail());
        }

        if (allLeadsOutput.getPhoneNo() != null)
        {
            contactPhone.setText(allLeadsOutput.getPhoneNo());
        }

        if (allLeadsOutput.getPhoneNo() != null)
        {
            contactPhone.setText(allLeadsOutput.getPhoneNo());

            if (allLeadsOutput.getCountryCode() != null)
            {
                try {
                    phoneCodePicker.setCountryForPhoneCode(Integer.parseInt(allLeadsOutput.getCountryCode()));
                    phoneCodePicker.setCountryForNameCode(allLeadsOutput.getFlagCode());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }

        if (allLeadsOutput.getCategoryList() != null) {
            String categoryTagString = "";

            for (int index = 0; index < allLeadsOutput.getCategoryList().size(); index++) {
                String categoryName = allLeadsOutput.getCategoryList().get(index).getCategory().getName();

                if (categoryTagString.isEmpty()) {
                    categoryTagString = categoryName;
                } else {
                    categoryTagString = categoryTagString + "," + categoryName;
                }

            }

            interests.setText(categoryTagString);
        }

        if (allLeadsOutput.getProductList() != null) {
            String productTagString = "";

            for (int i = 0; i < allLeadsOutput.getProductList().size(); i++) {
                String productName = allLeadsOutput.getProductList().get(i).getProduct().getProductName();

                if (productTagString.isEmpty()) {
                    productTagString = productName;
                } else {
                    productTagString = productTagString + "," + productName;
                }

            }

            categories.setText(productTagString);
        }

        //Setting onclick
        leadSourceClick.setOnClickListener(this);

        //Adding data to multiautocomplete textview...........
        categories.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(LeadsEditActivity.this,
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
    public void onBackPressed() {

        finish();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_editLeads_leadSourceClick:

                //Calling dialog
                FragmentManager filterManager = getFragmentManager();
                EditLeadsLeadSourceFilterDialog filterDialog = new EditLeadsLeadSourceFilterDialog(selectedLeadSourcePosition);
                filterDialog.show(filterManager, "FILTER_DIALOG");

                break;

            case R.id.ll_editLeads_toolBar_action:
                //Hiding Keyboard
                hideKeyBoard(LeadsEditActivity.this);


                //Hiding views
                otherDetailsLayout.setVisibility(View.GONE);
                firstNameError.setVisibility(View.GONE);
                lastNameError.setVisibility(View.GONE);
                leadSourceError.setVisibility(View.GONE);
                interestError.setVisibility(View.GONE);
                categoriesError.setVisibility(View.GONE);
                phoneNumberError.setVisibility(View.GONE);
                emailError.setVisibility(View.GONE);

                String firstNameText = firstName.getText().toString().trim();
                String lastNameText = lastName.getText().toString().trim();
                String otherDetailsText = otherDetails.getText().toString().trim();
                String referralText = referralName.getText().toString().trim();
                String interestsText = interests.getText().toString().trim();
                String categoriesText = categories.getText().toString().trim();
                String contactPhoneText = contactPhone.getText().toString().trim();
                String contactEmailText = contactEmail.getText().toString().trim();

                boolean validFlag = validateTextFields(firstNameText,lastNameText,otherDetailsText,interestsText,categoriesText,contactPhoneText,contactEmailText);

                if (validFlag)
                {
                    editLeads(firstNameText,lastNameText,otherDetailsText,referralText,interestsText,categoriesText);

                }

                break;
        }

    }

    //Func - Select Lead Source
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
    private boolean validateTextFields(String firstNameText, String lastNameText, String otherDetailsText, String interestsText, String categoriesText, String contactPhoneText, String contactEmailText) {

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

    //Func - Edit Leads
    private void editLeads(String firstNameText, String lastNameText, String otherDetailsText, String referralText, String interestsText, String categoryText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            callEditLeadsService(firstNameText,lastNameText,otherDetailsText,referralText,interestsText,categoryText);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(leadsEditActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Service - Edit Leads
    private void callEditLeadsService(String firstNameText, String lastNameText, String otherDetailsText, String referralText, String interestsText, String categoryText) {

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


        EditLeadInput editLeadInput = new EditLeadInput();

        editLeadInput.setId(allLeadsOutput.getId().toString());
        editLeadInput.setLeadId(allLeadsOutput.getLeadId());
        editLeadInput.setName(firstNameText);
        editLeadInput.setLastName(lastNameText);

        editLeadInput.setFacebook(contactFacebookText);
        editLeadInput.setInstagram(contactInstagramText);
        editLeadInput.setTwitter(contactTwitterText);
        editLeadInput.setWebsite(contactWebsiteText);
        editLeadInput.setEmail(contactEmailText);
        editLeadInput.setFlagCode(phoneCodePicker.getSelectedCountryNameCode());
        editLeadInput.setPhoneNo(contactPhoneText);

        if (contactPhoneText.isEmpty())
        {
            editLeadInput.setCountryCode("");
        }
        else
        {
            editLeadInput.setCountryCode(phoneCodePicker.getSelectedCountryCodeWithPlus());
        }


        LeadSource leadSourceObject = new LeadSource();
        switch (selectedLeadSourcePosition)
        {
            case 0:

                leadSourceObject.setId(Constants.LEADSOURCE_SOCIAL_NETWORK_ID);
                editLeadInput.setUserName("");

                break;

            case 1:

                leadSourceObject.setId(Constants.LEADSOURCE_WEBSITE_ID);
                editLeadInput.setUserName("");

                break;

            case 2:

                leadSourceObject.setId(Constants.LEADSOURCE_EMAIL_ID);
                editLeadInput.setUserName("");

                break;

            case 3:

                leadSourceObject.setId(Constants.LEADSOURCE_PHONE_ID);
                editLeadInput.setUserName("");

                break;

            case 4:

                leadSourceObject.setId(Constants.LEADSOURCE_REFEREL_ID);
                editLeadInput.setUserName(referralName.getText().toString());

                break;

            case 5:

                leadSourceObject.setId(Constants.LEADSOURCE_OTHER_ID);
                editLeadInput.setUserName(otherDetails.getText().toString());

                break;
        }

        editLeadInput.setLeadSource(leadSourceObject);

        Source sourceObject = new Source();
        sourceObject.setId("4");
        editLeadInput.setSource(sourceObject);

        AssignedTo assignedToObject = new AssignedTo();
        assignedToObject.setUserId(userId);
        editLeadInput.setSource(sourceObject);

        LeadOwner leadOwnerObject = new LeadOwner();
        leadOwnerObject.setUserId(userId);
        editLeadInput.setLeadOwner(leadOwnerObject);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);
        editLeadInput.setCreatedBy(createdByObject);

        UserId userIdObject = new UserId();
        userIdObject.setUserId(userId);
        editLeadInput.setUserId(userIdObject);

        UpdatedBy updatedByObject = new UpdatedBy();
        updatedByObject.setUserId(userId);
        editLeadInput.setUpdatedBy(updatedByObject);


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
            editLeadInput.setCategoryList(categoryListUpload);
        }



        List<ProductList> productListUpload = new ArrayList<>();
        String[] parts2 = categoryText.split(",");
        for (int index=0;index<parts2.length;index++){
            SelecetedPName.add(parts2[index].trim());


            Log.e("Selected",SelecetedPName.get(index));
        }

        for (int j=0;j<SelecetedPName.size();j++){
            for (int k=0; k<productOutputList.size();k++){
                if (SelecetedPName.get(j).equals(productOutputList.get(k).getProductId().getProductName()))
                {
                    if (SelecetedPId.size()!=0) {
                        int flag  =0;
                        for (int p = 0; p < SelecetedPId.size(); p++) {
                            if (productOutputList.get(k).getProductId().getProductId().equals(SelecetedPId.get(p))) {
                                flag=0;
                                break;

                            } else {
                                flag=1;
                            }
                        }
                        if (flag==1)
                        {
                            SelecetedPId.add(productOutputList.get(k).getProductId().getProductId());
                        }
                    }
                    else {
                        SelecetedPId.add(productOutputList.get(k).getProductId().getProductId());
                    }

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
            editLeadInput.setProductList(productListUpload);
        }




        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.editLeads(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_EDIT_LEADS,
                editLeadInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 200 ) {

                    //Finishing Activity
                    LeadsDetailActivity.leadsDetailActivityActivityInstance.finish();

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
                //If status code is not 200
                else
                {

                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(leadsEditActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(leadsEditActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
