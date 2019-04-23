package in.exuber.usmarket.fragment.loginsignup;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.activity.forgotpassword.ForgotPasswordActivity;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;
import in.exuber.usmarket.apimodels.login.logininput.LoginInput;
import in.exuber.usmarket.apimodels.login.loginoutput.LoginOutput;
import in.exuber.usmarket.service.GPSTrackerService;
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
import static in.exuber.usmarket.utils.UtilMethods.isValidEmail;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private NestedScrollView loginFragmentContainer;

    private EditText email,password;
    private TextView emailError, passwordError;

    private TextView forgotPasswordClick;
    private CheckBox rememberMeSelect;
    private TextView registerClickText;

    private LinearLayout loginClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Dialog
    private ProgressDialog progressDialog;

    //Declaring variables
    private boolean isRememberMeSelected = false;
    double latitude,longitude;
    private GPSTrackerService gpsTrackerService;

    private static final int PERMISSIONS_REQUEST_ACCOUNTS = 1;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView =  inflater.inflate(R.layout.fragment_login, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);


        //Initialising views
        loginFragmentContainer = loginView.findViewById(R.id.fragment_login);

        email = loginView.findViewById(R.id.et_login_email);
        password = loginView.findViewById(R.id.et_login_password);

        emailError = loginView.findViewById(R.id.tv_login_emailError);
        passwordError = loginView.findViewById(R.id.tv_login_passwordError);

        forgotPasswordClick = loginView.findViewById(R.id.tv_login_forgotClick);
        rememberMeSelect = loginView.findViewById(R.id.cb_login_rememberSelect);
        registerClickText = loginView.findViewById(R.id.tv_login_registerClickText);

        loginClick = loginView.findViewById(R.id.ll_login_loginClick);

        //Hiding views
        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);


        //Checking Remember me
        boolean rememberMePrevoius = marketPreference.getBoolean(Constants.IS_REMEMBER_ME_SELECTED, false);

        if (rememberMePrevoius)
        {
            String storedEmail = marketPreference.getString(Constants.REMEMBER_ME_EMAIL, null);
            String storedPassword = marketPreference.getString(Constants.REMEMBER_ME_PASSWORD, null);

            email.setText(storedEmail);
            password.setText(storedPassword);

            rememberMeSelect.setChecked(true);
            isRememberMeSelected = true;


        }

        rememberMeSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    isRememberMeSelected = true;
                }
                else
                {
                    isRememberMeSelected = false;
                }

                }

        }
        );

        ClickableSpan registerLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                ((LoginSignupActivity)getActivity()).registerLinkClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }

        };




        makeLinks(registerClickText,
                new String[] {"REGISTER HERE"},
                new ClickableSpan[] {registerLinkClickSpan}
        );

        //Getting Location
        getLocation();


        //Setting onclick
        forgotPasswordClick.setOnClickListener(this);
        loginClick.setOnClickListener(this);

        return loginView;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.tv_login_forgotClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Forgot password activity
                startActivity(new Intent(getActivity(),ForgotPasswordActivity.class));

                break;

            case R.id.ll_login_loginClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Hiding views
                emailError.setVisibility(View.GONE);
                passwordError.setVisibility(View.GONE);

                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                boolean validFlag = validateTextFields(emailText,passwordText);

                if (validFlag)
                {
                    //Calling Login
                    login();

                }


                break;
        }

    }

    //Func  - Get Location
    public void getLocation() {

        //Checking permission Marshmallow
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermissionLocation())
            {
                setLocation();


            } else {

                //Request permission
                requestPermissionLocation();
            }
        } else {


            setLocation();
        }
    }

    private void checkPermissionLocationSecond() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermissionLocation())
            {

                setLocation();
            }
            else
            {
                Snackbar snackbar = Snackbar
                        .make(loginFragmentContainer, R.string.permission_request, Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.go_settings), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);

                            }
                        });

                snackbar.show();

            }

        }
        else
        {
            setLocation();
        }
    }

    //Func  - Get Location
    public void setLocation() {

        gpsTrackerService = new GPSTrackerService(getActivity());

        // Check if GPS enabled
        if (gpsTrackerService.canGetLocation()) {

            latitude = gpsTrackerService.getLatitude();
            longitude = gpsTrackerService.getLongitude();

            Log.e("LATITUDE", String.valueOf(latitude));
            Log.e("LONGITUDE", String.valueOf(longitude));

        } else {
            gpsTrackerService.showSettingsAlert();
        }

    }


    //Func - Checking permission granted
    private boolean checkPermissionLocation() {

        boolean isPermissionGranted = true;

        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionLocation != PackageManager.PERMISSION_GRANTED)
        {
            isPermissionGranted = false;
        }

        return isPermissionGranted;
    }

    //Func - Requesting permission
    private void requestPermissionLocation() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_ACCOUNTS);

    }



    //Func - Making links
    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {

        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    //Func - Validating text fields
    private boolean validateTextFields(String emailText, String passwordText) {

        //Hiding Keyboard
        hideKeyBoard(getActivity());

        boolean validFlag = true;

        if (passwordText.isEmpty()) {

            passwordError.setText(getString(R.string.error_password_empty));
            passwordError.setVisibility(View.VISIBLE);
            password.requestFocus();
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


        return validFlag;

    }

    //Func - Login
    private void login() {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callLoginService();

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(loginFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }

    //Service - Login
    private void callLoginService() {

        //Showing loading
        progressDialog.show();



        final String emailText = email.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();

        LoginInput loginInput = new LoginInput();
        loginInput.setEmail(emailText);
        loginInput.setPassword(passwordText);

        String generatedToken = FirebaseInstanceId.getInstance().getToken();
        if (generatedToken!=null)
        {
            Log.e("Firebase Token", generatedToken);
            loginInput.setMobileId(generatedToken);
        }
        else
        {
            Log.e("Firebase Token", "Empty");
        }


        loginInput.setLocationLatId(latitude);
        loginInput.setLocationLongId(longitude);


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<LoginOutput> call = (Call<LoginOutput>) api.validateUser(loginInput);
        call.enqueue(new Callback<LoginOutput>() {
            @Override
            public void onResponse(Call<LoginOutput> call, Response<LoginOutput> response) {

                LoginOutput loginOutput = response.body();

                //Checking for response code
                if (response.code() == 200 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    if (loginOutput.getError())
                    {

                        Snackbar snackbar = Snackbar
                                .make(loginFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

                        snackbar.show();


                    }
                    else
                    {

                        //Preference Editor
                        SharedPreferences.Editor preferenceEditor = marketPreference.edit();

                        Gson gson = new Gson();
                        String loginItemString = gson.toJson(loginOutput);

                        String userId = loginOutput.getData().getUserId();
                        String roleId = loginOutput.getData().getRole().getId();
                        String accessTokenId = loginOutput.getData().getAccessId().getAccessTokenId();

                        Log.e("Access Token Id - LOGIN", accessTokenId);


                        if (roleId.equals(Constants.SALES_ASSOCIATE_ROLE_ID) &&
                                loginOutput.getData().getDataStatus().equals(Constants.LOGIN_DATA_STATUS))
                        {

                            //Storing details
                            preferenceEditor.putString(Constants.LOGIN_RESPONSE, loginItemString);
                            preferenceEditor.putString(Constants.LOGIN_USER_ID, userId);
                            preferenceEditor.putString(Constants.LOGIN_ROLE_ID, roleId);
                            preferenceEditor.putString(Constants.LOGIN_ACCESSTOKEN_ID, accessTokenId);

                            //Storing details
                            if (isRememberMeSelected)
                            {
                                preferenceEditor.putBoolean(Constants.IS_REMEMBER_ME_SELECTED, true);
                                preferenceEditor.putString(Constants.REMEMBER_ME_EMAIL,emailText);
                                preferenceEditor.putString(Constants.REMEMBER_ME_PASSWORD,passwordText);

                            }
                            else
                            {
                                preferenceEditor.remove(Constants.IS_REMEMBER_ME_SELECTED);
                                preferenceEditor.remove(Constants.REMEMBER_ME_EMAIL);
                                preferenceEditor.remove(Constants.REMEMBER_ME_PASSWORD);
                            }

                            preferenceEditor.putString("email", loginOutput.getData().getEmail());
                            preferenceEditor.putString("password", loginOutput.getData().getPassword());

                            /*preferenceEditor.putString("mobileId", loginOutput.getData().getPassword());
                            preferenceEditor.putString("gLatitude", loginOutput.getData().getPassword());
                            preferenceEditor.putString("gLongitude", loginOutput.getData().getPassword());*/

                            preferenceEditor.putString("userid",loginOutput.getData().getUserId());
                            //preferenceEditor.putString("userkey",loginOutput.getData().getUserKey().toString());
                            preferenceEditor.putString("tokenid",accessTokenId);
                            preferenceEditor.putString("roleid",roleId);
                            preferenceEditor.putString("userName",loginOutput.getData().getName());
                            preferenceEditor.putString("userLastName",loginOutput.getData().getLastName());
                            preferenceEditor.putString("userserialid",loginOutput.getData().getId().toString());
                            //preferenceEditor.putString("companyNameId",companyId.getString("companyId"));

                            preferenceEditor.putString("prefLanguage",loginOutput.getData().getPrefLanguage());
                            preferenceEditor.putString("phoneNoCode",loginOutput.getData().getCode());
                            preferenceEditor.putString("phoneNo",loginOutput.getData().getPhoneNo());
                            preferenceEditor.putString("streetaddress1",loginOutput.getData().getAddress1());
                            preferenceEditor.putString("streetaddress2",loginOutput.getData().getAddress2());
                            preferenceEditor.putString("Unit",loginOutput.getData().getUnit());
                            preferenceEditor.putString("City",loginOutput.getData().getCity());
                            preferenceEditor.putString("State",loginOutput.getData().getState());
                            preferenceEditor.putString("PostalCode",loginOutput.getData().getPostalCode());
                            preferenceEditor.putString("Country",loginOutput.getData().getCountry());
                            Log.e("Country",loginOutput.getData().getCountry());

                            preferenceEditor.putString("userProfilePic",loginOutput.getData().getPicId());

                            preferenceEditor.putString("appIntroDone",loginOutput.getData().getAppIntro());
                            Log.e("APP_INTRO",loginOutput.getData().getAppIntro()+"");

                            preferenceEditor.putString("flagCodeName",loginOutput.getData().getFlagCode());
                            Log.e("FLAG_CODE_NAME",loginOutput.getData().getFlagCode()+"");

                            preferenceEditor.putBoolean(Constants.IS_LOGGED_IN, true);

                            preferenceEditor.commit();

                            //Going Home activity

                            if (loginOutput.getData().getAppIntro()== null) {
                                startActivity(new Intent(getActivity(), AppIntroActivity.class));
                                getActivity().finish();
                            }
                            else if (loginOutput.getData().getAppIntro().equals("false")) {
                                startActivity(new Intent(getActivity(), AppIntroActivity.class));
                                getActivity().finish();
                            }
                            else {

                                startActivity(new Intent(getActivity(), HomeAddProductsActivity.class));
                                getActivity().finish();

                               /* //Preference Editor
                                preferenceEditor.putBoolean(Constants.IS_APPINTRO_OVER, true);
                                preferenceEditor.commit();

                                boolean isProductAdded = marketPreference.getBoolean(Constants.IS_PRODUCT_ADDED, false);

                                if (isProductAdded)
                                {
                                    //Calling Home activity
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                    *//*Intent intent=new Intent(getActivity(),HomeActivity.class);
                                    intent.putExtra("Login","ProductAvailable");
                                    startActivity(intent);*//*
                                }
                                else
                                {
                                    //Calling Home Add product activity
                                    startActivity(new Intent(getActivity(), HomeAddProductsActivity.class));
                                    getActivity().finish();
                                }*/
                            }
                        }
                        else
                        {

                            Snackbar snackbar = Snackbar
                                    .make(loginFragmentContainer, R.string.error_role_login, Snackbar.LENGTH_LONG);

                            snackbar.show();

                        }



                    }

                }
                //If status code is not 200
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    if (response.code() == 404)
                    {

                        Snackbar snackbar = Snackbar
                                .make(loginFragmentContainer, getString(R.string.error_login), Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else
                    {


                        Snackbar snackbar = Snackbar
                                .make(loginFragmentContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }





                }
            }

            @Override
            public void onFailure(Call<LoginOutput> call, Throwable t) {

                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());


                Snackbar snackbar = Snackbar
                        .make(loginFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
            client.writeTimeout(600000, TimeUnit.MILLISECONDS);
            client.readTimeout(600000, TimeUnit.MILLISECONDS);
            client.connectTimeout(600000, TimeUnit.MILLISECONDS);
            return client;
        }
        return builder;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCOUNTS:

                boolean permissionSTORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                if(permissionSTORAGE)
                {
                    setLocation();
                }
                else
                {
                    checkPermissionLocationSecond();
                }

                break;
        }
    }




}
