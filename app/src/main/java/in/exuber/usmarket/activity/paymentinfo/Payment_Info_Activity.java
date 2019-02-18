package in.exuber.usmarket.activity.paymentinfo;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.faq.FaqActivity;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.adapter.FaqListAdapter;
import in.exuber.usmarket.apimodels.editpaymentinfo.editpaymentinfoinput.CreatedBy;
import in.exuber.usmarket.apimodels.editpaymentinfo.editpaymentinfoinput.EditPaymentInfoInput;
import in.exuber.usmarket.apimodels.faq.faqoutput.FAQOutput;
import in.exuber.usmarket.apimodels.paymentinfo.paymentinfooutput.PaymentInfoOutput;
import in.exuber.usmarket.models.faq.FaqOutput;
import in.exuber.usmarket.models.paymentinfogetmodel.PaymentInfoGetOutputList;
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

public class Payment_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout paymentInfoActivityContainer;
    private ScrollView paymentInfoLayout;
    private TextView toolbarHeader;

    private EditText firstName, lastName, email, addressOne, addressTwo, aptUnit, city, state, postalCode;
    private EditText routingNumber, accountNumber, SWIFTCode, fullName, paypalEmail;

    private TextView firstNameError, lastNameError, emailError,addressOneError, addressTwoError, aptUnitError, cityError, stateError, postalCodeError, countryRegionError;
    private TextView routingNumberError, accountNumberError, SWIFTCodeError,fullNameError, paypalEmailError;

    private CountryCodePicker countryPicker;

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


    //De4claring views
    private PaymentInfoOutput paymentInfoOutput;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  Payment_Info_Activity.this.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Initialising progress dialog
        progressDialogSecond = new ProgressDialog(this);
        progressDialogSecond.setMessage(getString(R.string.loader_caption));
        progressDialogSecond.setCancelable(true);
        progressDialogSecond.setIndeterminate(false);
        progressDialogSecond.setCancelable(false);

        //Initialising variables
        paymentInfoOutput = new PaymentInfoOutput();


        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising views
        paymentInfoActivityContainer = findViewById(R.id.activity_payment_info);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);
        paymentInfoLayout = findViewById(R.id.sv_paymentInfo_paymentInfoLayout);

        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);

        firstName = findViewById(R.id.et_paymentInfo_firstName);
        lastName = findViewById(R.id.et_paymentInfo_lastName);
        email = findViewById(R.id.et_paymentInfo_email);
        addressOne = findViewById(R.id.et_paymentInfo_addressOne);
        addressTwo = findViewById(R.id.et_paymentInfo_addressTwo);
        aptUnit = findViewById(R.id.et_paymentInfo_aptUnit);
        city = findViewById(R.id.et_paymentInfo_city);
        state = findViewById(R.id.et_paymentInfo_state);
        postalCode = findViewById(R.id.et_paymentInfo_postalCode);

        routingNumber = findViewById(R.id.et_paymentInfo_routingNumber);
        accountNumber = findViewById(R.id.et_paymentInfo_accountNumber);
        SWIFTCode = findViewById(R.id.et_paymentInfo_SWIFTCode);
        fullName = findViewById(R.id.et_paymentInfo_fullName);
        paypalEmail= findViewById(R.id.et_paymentInfo_paypalEmail);
        countryPicker = findViewById(R.id.cpp_paymentInfo_countryRegion);

        firstNameError = findViewById(R.id.tv_paymentInfo_firstNameError);
        lastNameError = findViewById(R.id.tv_paymentInfo_lastNameError);
        emailError = findViewById(R.id.tv_paymentInfo_emailError);
        addressOneError = findViewById(R.id.tv_paymentInfo_addressOneError);
        addressTwoError = findViewById(R.id.tv_paymentInfo_addressTwoError);
        aptUnitError = findViewById(R.id.tv_paymentInfo_aptUnitError);
        cityError = findViewById(R.id.tv_paymentInfo_cityError);
        stateError = findViewById(R.id.tv_paymentInfo_stateError);
        postalCodeError = findViewById(R.id.tv_paymentInfo_postalCodeError);
        countryRegionError = findViewById(R.id.tv_paymentInfo_countryRegionError);

        routingNumberError = findViewById(R.id.tv_paymentInfo_routingNumberError);
        accountNumberError = findViewById(R.id.tv_paymentInfo_accountNumberError);
        SWIFTCodeError = findViewById(R.id.tv_paymentInfo_SWIFTCodeError);
        fullNameError = findViewById(R.id.tv_paymentInfo_fullNameError);
        paypalEmailError = findViewById(R.id.tv_paymentInfo_paypalEmailError);

        //Hiding views
        firstNameError.setVisibility(View.GONE);
        lastNameError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        addressOneError.setVisibility(View.GONE);
        addressTwoError.setVisibility(View.GONE);
        aptUnitError.setVisibility(View.GONE);
        cityError.setVisibility(View.GONE);
        stateError.setVisibility(View.GONE);
        postalCodeError.setVisibility(View.GONE);
        countryRegionError.setVisibility(View.GONE);
        routingNumberError.setVisibility(View.GONE);
        accountNumberError.setVisibility(View.GONE);
        SWIFTCodeError.setVisibility(View.GONE);
        fullNameError.setVisibility(View.GONE);
        paypalEmailError.setVisibility(View.GONE);

        //Setting toolbar header
        toolbarHeader.setText(getString(R.string.payment_info_caps));

        firstName.requestFocus();

        //Get Payment Info
        getPaymentInfo();

        //setting onclick
        errorDisplayTryClick.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_done_menu_done:

                //Hiding Keyboard
                hideKeyBoard(Payment_Info_Activity.this);

                //Hiding views
                firstNameError.setVisibility(View.GONE);
                lastNameError.setVisibility(View.GONE);
                emailError.setVisibility(View.GONE);

                addressOneError.setVisibility(View.GONE);
                addressTwoError.setVisibility(View.GONE);
                aptUnitError.setVisibility(View.GONE);
                cityError.setVisibility(View.GONE);
                stateError.setVisibility(View.GONE);
                postalCodeError.setVisibility(View.GONE);
                countryRegionError.setVisibility(View.GONE);
                routingNumberError.setVisibility(View.GONE);
                accountNumberError.setVisibility(View.GONE);
                SWIFTCodeError.setVisibility(View.GONE);
                fullNameError.setVisibility(View.GONE);
                paypalEmailError.setVisibility(View.GONE);

                String firstNameText = firstName.getText().toString().trim();
                String lastNameText = lastName.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String addressOneText = addressOne.getText().toString().trim();
                String addressTwoText = addressTwo.getText().toString().trim();
                String aptUnitText = aptUnit.getText().toString().trim();
                String cityText = city.getText().toString().trim();
                String stateText = state.getText().toString().trim();
                String postalCodeText = postalCode.getText().toString().trim();

                String routingNumberText = routingNumber.getText().toString().trim();
                String accountNumberText = accountNumber.getText().toString().trim();
                String SWIFTCodeText = SWIFTCode.getText().toString().trim();
                String fullNameText = fullName.getText().toString().trim();
                String paypalEmailText = paypalEmail.getText().toString().trim();

                boolean validFlag = validateTextFields(firstNameText,lastNameText,emailText,addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText,
                                                        routingNumberText,accountNumberText,SWIFTCodeText,fullNameText,paypalEmailText);

                if (validFlag)
                {
                    editPaymentInfo(firstNameText,lastNameText,emailText,addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText,
                                    routingNumberText,accountNumberText,SWIFTCodeText,fullNameText,paypalEmailText);

                }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(Payment_Info_Activity.this);

                //Get Payment Info
                getPaymentInfo();

                break;

        }

    }

    //Func - Validating text fields
    private boolean validateTextFields(String firstNameText, String lastNameText, String emailText, String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText,
                                       String routingNumberText, String accountNumberText, String SWIFTCodeText, String fullNameText, String paypalEmailText){

        boolean validFlag = true;

        if (paypalEmailText.isEmpty()) {

            paypalEmailError.setText(getString(R.string.error_email_empty));
            paypalEmailError.setVisibility(View.VISIBLE);
            paypalEmail.requestFocus();
            validFlag = false;

        }
        else
        {
            if (!isValidEmail(paypalEmailText))
            {
                paypalEmailError.setText(getString(R.string.error_email_invalid));
                paypalEmailError.setVisibility(View.VISIBLE);
                paypalEmail.requestFocus();

                validFlag = false;
            }
        }

        if (fullNameText.isEmpty()) {

            fullNameError.setText(getString(R.string.error_full_name_empty));
            fullNameError.setVisibility(View.VISIBLE);
            fullName.requestFocus();
            validFlag = false;

        }

        if (SWIFTCodeText.isEmpty()) {

            SWIFTCodeError.setText(getString(R.string.error_SWIFT_code_empty));
            SWIFTCodeError.setVisibility(View.VISIBLE);
            SWIFTCode.requestFocus();
            validFlag = false;

        }

        if (accountNumberText.isEmpty()) {

            accountNumberError.setText(getString(R.string.error_account_number_empty));
            accountNumberError.setVisibility(View.VISIBLE);
            accountNumber.requestFocus();
            validFlag = false;

        }

        if (routingNumberText.isEmpty()) {

            routingNumberError.setText(getString(R.string.error_routing_number_empty));
            routingNumberError.setVisibility(View.VISIBLE);
            routingNumber.requestFocus();
            validFlag = false;

        }

        if (emailText.isEmpty()) {

            emailError.setText(getString(R.string.error_email_empty));
            emailError.setVisibility(View.VISIBLE);
            email.requestFocus();

            validFlag = false;

        }
        else
        {
            if (!isValidEmail(emailText))
            {
                emailError.setText(getString(R.string.error_email_invalid));
                emailError.setVisibility(View.VISIBLE);
                email.requestFocus();

                validFlag = false;
            }
        }

        if (postalCodeText.isEmpty()) {

            postalCodeError.setText(getString(R.string.error_postal_code_empty));
            postalCodeError.setVisibility(View.VISIBLE);
            postalCode.requestFocus();
            validFlag = false;

        }

        if (stateText.isEmpty()) {

            stateError.setText(getString(R.string.error_state_empty));
            stateError.setVisibility(View.VISIBLE);
            state.requestFocus();
            validFlag = false;

        }

        if (cityText.isEmpty()) {

            cityError.setText(getString(R.string.error_city_empty));
            cityError.setVisibility(View.VISIBLE);
            city.requestFocus();
            validFlag = false;

        }

        if (aptUnitText.isEmpty()) {

            aptUnitError.setText(getString(R.string.error_apt_unit_empty));
            aptUnitError.setVisibility(View.VISIBLE);
            aptUnit.requestFocus();
            validFlag = false;

        }

        if (addressTwoText.isEmpty()) {

            addressTwoError.setText(getString(R.string.error_address_empty));
            addressTwoError.setVisibility(View.VISIBLE);
            addressTwo.requestFocus();
            validFlag = false;

        }

        if (addressOneText.isEmpty()) {

            addressOneError.setText(getString(R.string.error_address_empty));
            addressOneError.setVisibility(View.VISIBLE);
            addressOne.requestFocus();
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

    //Func - Get Payment Info
    private void getPaymentInfo() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetPaymentInfoService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            paymentInfoLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func - Edit Payment Info
    private void editPaymentInfo(String firstNameText, String lastNameText, String emailText, String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText,
                                 String routingNumberText, String accountNumberText, String swiftCodeText, String fullNameText, String paypalEmailText)
    {
        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            callEditPaymentInfoService(firstNameText,lastNameText,emailText,addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText,
                    routingNumberText,accountNumberText,swiftCodeText,fullNameText,paypalEmailText);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(paymentInfoActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }



    //Service - Get Payment Info
    private void callGetPaymentInfoService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<PaymentInfoOutput> call = (Call<PaymentInfoOutput>) api.getPaymentInfo(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_PAYMENT_INFO,
                userId);
        call.enqueue(new Callback<PaymentInfoOutput>() {
            @Override
            public void onResponse(Call<PaymentInfoOutput> call, Response<PaymentInfoOutput> response) {

                //Checking for response code
                if (response.code() == 200 ) {


                    paymentInfoOutput = response.body();

                    firstName.setText(paymentInfoOutput.getFirstName());
                    lastName.setText(paymentInfoOutput.getLastName());
                    addressOne.setText(paymentInfoOutput.getAddress1());
                    addressTwo.setText(paymentInfoOutput.getAddress2());
                    aptUnit.setText(paymentInfoOutput.getApt());
                    city.setText(paymentInfoOutput.getCity());
                    state.setText(paymentInfoOutput.getState());
                    postalCode.setText(paymentInfoOutput.getPostalCode());
                    countryPicker.setCountryForNameCode(paymentInfoOutput.getCountry());
                    email.setText(paymentInfoOutput.getEmail());
                    routingNumber.setText(paymentInfoOutput.getRoutingNumber());
                    accountNumber.setText(paymentInfoOutput.getAccountNo());
                    SWIFTCode.setText(paymentInfoOutput.getSwiftCode());
                    fullName.setText(paymentInfoOutput.getPaypalName());
                    paypalEmail.setText(paymentInfoOutput.getPaypalEmail());


                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    errorDisplay.setVisibility(View.GONE);

                    paymentInfoLayout.setVisibility(View.VISIBLE);



                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    paymentInfoLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<PaymentInfoOutput> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    errorDisplay.setVisibility(View.GONE);

                    paymentInfoLayout.setVisibility(View.VISIBLE);



                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    paymentInfoLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }





            }

        });

    }



    //Service - Edit Payment Info
    private  void callEditPaymentInfoService(String firstNameText, String lastNameText, String emailText, String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText,
                                             String routingNumberText, String accountNumberText, String swiftCodeText, String fullNameText, String paypalEmailText) {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        EditPaymentInfoInput editPaymentInfoInput = new EditPaymentInfoInput();

        editPaymentInfoInput.setFirstName(firstNameText);
        editPaymentInfoInput.setLastName(lastNameText);
        editPaymentInfoInput.setAddress1(addressOneText);
        editPaymentInfoInput.setAddress2(addressTwoText);
        editPaymentInfoInput.setApt(aptUnitText);
        editPaymentInfoInput.setCity(cityText);
        editPaymentInfoInput.setState(stateText);
        editPaymentInfoInput.setPostalCode(postalCodeText);
        editPaymentInfoInput.setCountry(countryPicker.getSelectedCountryNameCode());
        editPaymentInfoInput.setEmail(emailText);


        editPaymentInfoInput.setRoutingNumber(routingNumberText);
        editPaymentInfoInput.setAccountNo(accountNumberText);
        editPaymentInfoInput.setSwiftCode(swiftCodeText);
        editPaymentInfoInput.setPaypalName(fullNameText);
        editPaymentInfoInput.setPaypalEmail(paypalEmailText);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);

        editPaymentInfoInput.setCreatedBy(createdByObject);


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.editPaymentInfo(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_EDIT_PAYMENT_INFO,
                editPaymentInfoInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialogSecond.dismiss();


                    finish();


                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(paymentInfoActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(paymentInfoActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
