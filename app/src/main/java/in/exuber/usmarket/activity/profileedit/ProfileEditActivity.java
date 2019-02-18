package in.exuber.usmarket.activity.profileedit;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.libraries.circularimageview.CircleImageView;
import in.exuber.usmarket.models.ProfilePicModel.ProfileImageModel;
import in.exuber.usmarket.models.user.UserOutput;
import in.exuber.usmarket.utils.Api;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;
import in.exuber.usmarket.utils.UtilMethods;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.exuber.usmarket.utils.UtilMethods.ShrinkBitmap;
import static in.exuber.usmarket.utils.UtilMethods.bitmapToFile;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;
import static in.exuber.usmarket.utils.UtilMethods.isValidEmail;

public class ProfileEditActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout profileEditActivityContainer;
    private TextView toolbarHeader;

    private CircleImageView userImage;
    private RelativeLayout userImageClick;

    private EditText firstName, lastName, email, phoneNumber, password, confirmPassword;
    private RadioButton englishLanguageSelect, spanishLanguageSelect;
    private CountryCodePicker phoneCodePicker;
    private TextView firstNameError, lastNameError, emailError, phoneNumberError, prefLanguangeError, passwordError, confirmPasswordError;

    private EditText addressOne, addressTwo, aptUnit, city, state, postalCode;
    private TextView addressOneError, addressTwoError, aptUnitError, cityError, stateError, postalCodeError, countryRegionError;

    private CountryCodePicker countryPicker;

    RadioGroup radiogrp;

    private Bitmap imageForUpload;
    private String captured_image;
    private File file;
    ProgressDialog pd;
    String url;
    private static final int  PERMISSIONS_REQUEST_ACCOUNTS = 1090;
    private static OkHttpClient.Builder builder;

    private static final int SELECT_SINGLE_PICTURE = 101;

    private static final int SELECT_MULTIPLE_PICTURE = 201;

    public static final String IMAGE_TYPE = "image/*";

    //Sharedpreferences
    private SharedPreferences marketPreference;
    SharedPreferences.Editor preferenceEditor;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Declaring variables
    private UserOutput userOutput;
    private boolean isEnglishSelected = false, isSpanishSelected = false;

    String Language;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  ProfileEditActivity.this.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        preferenceEditor=marketPreference.edit();
        preferenceEditor.apply();

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);


        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising views
        profileEditActivityContainer = findViewById(R.id.activity_profile_edit);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);
        toolbarHeader.setText("PROFILE");


        userImage = findViewById(R.id.iv_profileEdit_userImage);
        userImage.setOnClickListener(this);
        userImageClick = findViewById(R.id.rl_profileEdit_userImageClick);

        firstName = findViewById(R.id.et_profileEdit_firstName);
        lastName = findViewById(R.id.et_profileEdit_lastName);
        email = findViewById(R.id.et_profileEdit_email);
        phoneNumber = findViewById(R.id.et_profileEdit_phoneNumber);
        password = findViewById(R.id.et_profileEdit_password);
        confirmPassword = findViewById(R.id.et_profileEdit_confirmPassword);

        phoneCodePicker = findViewById(R.id.cpp_profileEdit_phoneNumberPicker);

        radiogrp=findViewById(R.id.radiogrp);

        englishLanguageSelect = findViewById(R.id.cb_profileEdit_languageEnglish);
        spanishLanguageSelect = findViewById(R.id.cb_profileEdit_languageSpanish);

        firstNameError = findViewById(R.id.tv_profileEdit_firstNameError);
        lastNameError = findViewById(R.id.tv_profileEdit_lastNameError);
        emailError = findViewById(R.id.tv_profileEdit_emailError);
        phoneNumberError = findViewById(R.id.tv_profileEdit_phoneNumberError);
        prefLanguangeError = findViewById(R.id.tv_profileEdit_prefLanguageError);
        passwordError = findViewById(R.id.tv_profileEdit_passwordError);
        confirmPasswordError = findViewById(R.id.tv_profileEdit_confirmPasswordError);

        addressOne = findViewById(R.id.et_profileEdit_addressOne);
        addressTwo = findViewById(R.id.et_profileEdit_addressTwo);
        aptUnit = findViewById(R.id.et_profileEdit_aptUnit);
        city = findViewById(R.id.et_profileEdit_city);
        state = findViewById(R.id.et_profileEdit_state);
        postalCode = findViewById(R.id.et_profileEdit_postalCode);

        countryPicker = findViewById(R.id.cpp_profileEdit_countryRegion);

        addressOneError = findViewById(R.id.tv_profileEdit_addressOneError);
        addressTwoError = findViewById(R.id.tv_profileEdit_addressTwoError);
        aptUnitError = findViewById(R.id.tv_profileEdit_aptUnitError);
        cityError = findViewById(R.id.tv_profileEdit_cityError);
        stateError = findViewById(R.id.tv_profileEdit_stateError);
        postalCodeError = findViewById(R.id.tv_profileEdit_postalCodeError);
        countryRegionError = findViewById(R.id.tv_profileEdit_countryRegionError);

        //Hiding views
        firstNameError.setVisibility(View.GONE);
        lastNameError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        phoneNumberError.setVisibility(View.GONE);
        prefLanguangeError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
        confirmPasswordError.setVisibility(View.GONE);

        addressOneError.setVisibility(View.GONE);
        addressTwoError.setVisibility(View.GONE);
        aptUnitError.setVisibility(View.GONE);
        cityError.setVisibility(View.GONE);
        stateError.setVisibility(View.GONE);
        postalCodeError.setVisibility(View.GONE);
        countryRegionError.setVisibility(View.GONE);

        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(phoneNumber);





        //Setting values
        firstName.setText(marketPreference.getString("userName", ""));
        lastName.setText(marketPreference.getString("userLastName", ""));
        email.setText(marketPreference.getString("email", ""));

        Log.e("Code",marketPreference.getString("code", ""));
        try {
            phoneCodePicker.setCountryForPhoneCode(Integer.parseInt(marketPreference.getString("phoneNoCode", "")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        phoneNumber.setText(marketPreference.getString("phoneNo", ""));

        password.setText(marketPreference.getString("password", ""));
        confirmPassword.setText(marketPreference.getString("password", ""));

        addressOne.setText(marketPreference.getString("streetaddress1", ""));
        addressTwo.setText(marketPreference.getString("streetaddress2", ""));
        aptUnit.setText(marketPreference.getString("Unit", ""));
        city.setText(marketPreference.getString("City", ""));
        state.setText(marketPreference.getString("State", ""));
        postalCode.setText(marketPreference.getString("PostalCode", ""));
        countryPicker.setCountryForNameCode(marketPreference.getString("Country", ""));

        Log.e("Country profile edit",marketPreference.getString("Country", ""));

        Log.e("Language",marketPreference.getString("prefLanguage", ""));

        /*Language=marketPreference.getString("prefLanguage", "");

        if (Language.equals("English")){
            radiogrp.check(R.id.cb_profileEdit_languageEnglish);
        }*/

        radiogrp.clearCheck();
        if (marketPreference.getString("prefLanguage", "").equals("English")){
            radiogrp.check(R.id.cb_profileEdit_languageEnglish);
            //englishLanguageSelect.setSelected(true);
            Log.e("Language","1");
        }
        if (marketPreference.getString("prefLanguage", "").equals("Spanish")){
            radiogrp.check(R.id.cb_profileEdit_languageSpanish);
            //spanishLanguageSelect.setChecked(true);
            Log.e("Language","2");
        }



        englishLanguageSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                             @Override
                                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                                 if (isChecked)
                                                                 {
                                                                     isEnglishSelected = true;

                                                                 }
                                                                 else {
                                                                     isEnglishSelected = false;
                                                                 }

                                                             }

                                                         }
        );

        spanishLanguageSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                             @Override
                                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                                 if (isChecked)
                                                                 {
                                                                     isSpanishSelected = true;

                                                                 }
                                                                 else {
                                                                     isSpanishSelected = false;
                                                                 }

                                                             }

                                                         }
        );

        /*isEnglishSelected = userOutput.isEnglishSelected();
        isSpanishSelected = userOutput.isSpanishSelected();*/

        /*englishLanguageSelect.setChecked(isEnglishSelected);
        spanishLanguageSelect.setChecked(isSpanishSelected);*/

        //phoneCodePicker.setCountryForNameCode(userOutput.getCountryCode());
        //countryPicker.setCountryForNameCode(userOutput.getCountryCode());


        /////////////Profile Image.....................

        url=marketPreference.getString("userProfilePic","");

        if (url == null)
        {

            userImage.setImageResource(R.drawable.user_profile);

        }
        else
        {
            if (url.isEmpty())
            {
                userImage.setImageResource(R.drawable.user_profile);

            }
            else
            {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.user_profile)
                        .error(R.drawable.user_profile)
                        .into(userImage);

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_done_menu_done:

                //Hiding Keyboard
                hideKeyBoard(ProfileEditActivity.this);

                //Hiding views
                firstNameError.setVisibility(View.GONE);
                lastNameError.setVisibility(View.GONE);
                emailError.setVisibility(View.GONE);
                phoneNumberError.setVisibility(View.GONE);
                prefLanguangeError.setVisibility(View.GONE);
                passwordError.setVisibility(View.GONE);
                confirmPasswordError.setVisibility(View.GONE);

                addressOneError.setVisibility(View.GONE);
                addressTwoError.setVisibility(View.GONE);
                aptUnitError.setVisibility(View.GONE);
                cityError.setVisibility(View.GONE);
                stateError.setVisibility(View.GONE);
                postalCodeError.setVisibility(View.GONE);
                countryRegionError.setVisibility(View.GONE);


                String firstNameText = firstName.getText().toString().trim();
                String lastNameText = lastName.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String phoneNumberText = phoneNumber.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String confirmPasswordText = confirmPassword.getText().toString().trim();


                String addressOneText = addressOne.getText().toString().trim();
                String addressTwoText = addressTwo.getText().toString().trim();
                String aptUnitText = aptUnit.getText().toString().trim();
                String cityText = city.getText().toString().trim();
                String stateText = state.getText().toString().trim();
                String postalCodeText = postalCode.getText().toString().trim();


                boolean validFlag = validateTextFields(firstNameText,lastNameText,emailText,phoneNumberText,passwordText,confirmPasswordText,
                        addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText);

                if (validFlag)
                {
                    updateProfile(firstNameText,lastNameText,emailText,phoneNumberText,passwordText,confirmPasswordText,
                            addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText);
                    MyProfileApiCall();
                }


                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }


    private void MyProfileApiCall() {

        JSONObject jObject = new JSONObject();
        try {

            JSONObject updatedby = new JSONObject();
            updatedby.put("userId", marketPreference.getString("userid", ""));
            jObject.put("updatedBy",updatedby);
            Log.e("updatedBy",marketPreference.getString("userid", ""));

            jObject.put("id", marketPreference.getString("userserialid", ""));
            Log.e("updatedByID",marketPreference.getString("userserialid", ""));

            jObject.put("name", firstName.getText().toString().trim());
            Log.e("FirstName",firstName.getText().toString().trim());

            jObject.put("lastName", lastName.getText().toString().trim());
            Log.e("LastName",lastName.getText().toString().trim());

            jObject.put("email", email.getText().toString().trim());
            Log.e("EmailID",email.getText().toString().trim());

            jObject.put("phoneNo", phoneNumber.getText().toString().trim());
            Log.e("Phone_No",phoneNumber.getText().toString().trim());

            jObject.put("code", phoneCodePicker.getSelectedCountryCodeWithPlus());
            Log.e("Code",phoneCodePicker.getSelectedCountryCodeWithPlus());



            jObject.put("password", password.getText().toString().trim());
            Log.e("updatedByPassword",password.getText().toString().trim());

            jObject.put("address1", addressOne.getText().toString().trim());
            Log.e("address1",addressOne.getText().toString().trim());

            jObject.put("address2", addressTwo.getText().toString().trim());
            Log.e("address2",addressTwo.getText().toString().trim());

            jObject.put("unit", aptUnit.getText().toString().trim());
            Log.e("Unit",aptUnit.getText().toString().trim());

            jObject.put("city", city.getText().toString().trim());
            Log.e("City",city.getText().toString().trim());

            jObject.put("state", state.getText().toString().trim());
            Log.e("State",state.getText().toString().trim());

            jObject.put("postalCode", postalCode.getText().toString().trim());
            Log.e("postalCode",postalCode.getText().toString().trim());

            jObject.put("country", countryPicker.getSelectedCountryNameCode());
            Log.e("country",countryPicker.getSelectedCountryNameCode());


            String lang = null;
            if (isEnglishSelected)
            {
                lang = "English";
            }
            if (isSpanishSelected)
            {
                if (isEnglishSelected)
                {
                    lang = lang+",Spanish";
                }
                else
                {
                    lang = "Spanish";
                }

            }

            jObject.put("prefLanguage",lang );

            Log.e("Preffered_Language",lang);


        }
        catch (Exception e){
            e.printStackTrace();
        }



        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        StringEntity entity = null;
        try {
            entity = new StringEntity(jObject.toString());
            Log.d("Object", String.valueOf(jObject));
        } catch (Exception e) {
            e.printStackTrace();
        }


        asyncHttpClient.addHeader("accept", "application/json;charset=UTF-8");

        asyncHttpClient.addHeader("auth-token", marketPreference.getString("tokenid", ""));

        asyncHttpClient.addHeader("user-id", marketPreference.getString("userid", ""));

        asyncHttpClient.addHeader("role-id", marketPreference.getString("roleid", ""));

        asyncHttpClient.addHeader("service", Constants.APP_UPDATE_PROFILE_SERVICENAME);
        Log.e("Profileservicename", Constants.APP_UPDATE_PROFILE_SERVICENAME);

        asyncHttpClient.put(null, Constants.APP_UPDATE_PROFILE_LIST, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                pd=new ProgressDialog(ProfileEditActivity.this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(true);
                pd.setIndeterminate(false);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final String Response = new String(responseBody);
                Log.v("MYProfile_Response", Response);

                Log.v("Status_code", statusCode+"");


                if (statusCode==200) {

                    try {
                        JSONObject job = new JSONObject(Response);

                        preferenceEditor.putString("userid",job.getString("userId"));

                        preferenceEditor.putString("userName",job.getString("name"));

                        preferenceEditor.putString("userLastName",job.getString("lastName"));

                        preferenceEditor.putString("email", job.getString("email"));

                        preferenceEditor.putString("phoneNo",job.getString("phoneNo"));

                        preferenceEditor.putString("phoneNoCode",job.getString("code"));

                        preferenceEditor.putString("prefLanguage",job.getString("prefLanguage"));

                        preferenceEditor.putString("password", job.getString("password"));

                        preferenceEditor.putString("streetaddress1",job.getString("address1"));

                        preferenceEditor.putString("streetaddress2",job.getString("address2"));

                        preferenceEditor.putString("Unit",job.getString("unit"));

                        preferenceEditor.putString("City",job.getString("city"));

                        preferenceEditor.putString("State",job.getString("state"));

                        preferenceEditor.putString("PostalCode",job.getString("postalCode"));

                        preferenceEditor.putString("Country",job.getString("country"));

                        Log.e("Pref_LANG",marketPreference.getString("prefLanguage", ""));

                        preferenceEditor.commit();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ProfileEditActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(ProfileEditActivity.this, HomeActivity.class);
                    startActivity(intent);*/
                    finish();
                }

                else {
                    Toast.makeText(ProfileEditActivity.this, "Profile not updated", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Error", String.valueOf(error));
                Log.e("ErrorStatus", String.valueOf(statusCode));
                pd.dismiss();

            }
        });

    }






    /*@Override
    public void onBackPressed() {

        finish();

    }*/


    //Func - Validating text fields
    private boolean validateTextFields(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText,
                                       String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        boolean validFlag = true;

        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(phoneNumber);


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


        if (confirmPasswordText.isEmpty()) {

            confirmPasswordError.setText(getString(R.string.error_confirm_password_empty));
            confirmPasswordError.setVisibility(View.VISIBLE);
            confirmPassword.requestFocus();
            validFlag = false;

        }

        if (passwordText.isEmpty()) {

            passwordError.setText(getString(R.string.error_password_empty));
            passwordError.setVisibility(View.VISIBLE);
            password.requestFocus();
            validFlag = false;

        }

        if (validFlag)
        {
            if (!confirmPasswordText.equals(passwordText))
            {
                confirmPasswordError.setText(getString(R.string.error_confirm_password_mismatch));
                confirmPasswordError.setVisibility(View.VISIBLE);
                confirmPassword.requestFocus();
                validFlag = false;
            }

        }

        /*if (!isEnglishSelected && !isSpanishSelected) {

            prefLanguangeError.setText(getString(R.string.error_pref_language_empty));
            prefLanguangeError.setVisibility(View.VISIBLE);
            validFlag = false;

        }*/

        if (phoneNumberText.isEmpty()) {

            phoneNumberError.setText(getString(R.string.error_phone_number_empty));
            phoneNumberError.setVisibility(View.VISIBLE);
            phoneNumber.requestFocus();

            validFlag = false;

        }
        else
        {

            if (!phoneCodePicker.isValid()) {
                phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
                phoneNumberError.setVisibility(View.VISIBLE);
                phoneNumber.requestFocus();

                validFlag = false;
            }
        }

        if (passwordText.length()<6) {
            passwordError.setText(getString(R.string.password_placeholder));
            passwordError.setVisibility(View.VISIBLE);
            password.requestFocus();
            validFlag = false;
        }

        if (passwordText.length()>=10) {
            passwordError.setText(getString(R.string.password_placeholder));
            passwordError.setVisibility(View.VISIBLE);
            password.requestFocus();
            validFlag = false;
        }

        if (phoneNumberText.length()<10)
        {
            phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
            phoneNumberError.setVisibility(View.VISIBLE);
            phoneNumber.requestFocus();

            validFlag = false;
        }

        if (phoneNumberText.length()>=11)
        {
            phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
            phoneNumberError.setVisibility(View.VISIBLE);
            phoneNumber.requestFocus();

            validFlag = false;
        }

        /*else
        {

            if (!phoneCodePicker.isValid())
            {
                phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
                phoneNumberError.setVisibility(View.VISIBLE);
                phoneNumber.requestFocus();

                validFlag = false;
            }
        }*/


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


    //Func - Update Profile
    private void updateProfile(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText, String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            callUpdateProfileService(firstNameText,lastNameText,emailText,phoneNumberText,passwordText,confirmPasswordText,
                    addressOneText,addressTwoText,aptUnitText,cityText,stateText,postalCodeText);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(profileEditActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Service - Update Profile
    private void callUpdateProfileService(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText, String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_profileEdit_userImage:

                //Hiding Keyboard
                hideKeyBoard(ProfileEditActivity.this);

                Log.e("Came","Click");

                //Checking permission Marshmallow
                if (Build.VERSION.SDK_INT >= 23) {

                    if (checkPermissionCameraGallery())
                    {
                        Log.e("Came","permission Granted");

                        final Dialog dialog = new Dialog(ProfileEditActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.chooser_dialouge);
                        LinearLayout CameraBtn = (LinearLayout) dialog.findViewById(R.id.ll_camera_parent);
                        LinearLayout GalleryBtn = (LinearLayout) dialog.findViewById(R.id.ll_gallery_parent);
                        CameraBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, 1);


                            }

                        });

                        GalleryBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select File"), 2);*/
                                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, 2);
                            }
                        });

                        dialog.show();
                    } else {

                        //Request permission
                        requestPermissionCameraGallery();
                    }
                } else {

                    final Dialog dialog = new Dialog(ProfileEditActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.chooser_dialouge);
                    LinearLayout CameraBtn = (LinearLayout) dialog.findViewById(R.id.ll_camera_parent);
                    LinearLayout GalleryBtn = (LinearLayout) dialog.findViewById(R.id.ll_gallery_parent);
                    CameraBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            captured_image = System.currentTimeMillis() + ".jpg";
                            file = new File(Environment.getExternalStorageDirectory(), captured_image);
                            captured_image = file.getAbsolutePath();
                            Uri outputFileUri = Uri.fromFile(file);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(cameraIntent, 1);
                        }

                    });

                    GalleryBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select File"), 2);*/
                            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 2);
                        }
                    });

                    dialog.show();

                }

                break;
        }
    }


    //Func - Checking permission granted
    private boolean checkPermissionCameraGallery() {

        boolean isPermissionGranted = true;

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionSTORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Permission Camera",isPermissionGranted+"");

            isPermissionGranted = false;
        }
        if (permissionSTORAGE != PackageManager.PERMISSION_GRANTED) {

            Log.e("Permission Camera",isPermissionGranted+"");

            isPermissionGranted = false;
        }


        return isPermissionGranted;
    }

    //Func - Requesting permission
    private void requestPermissionCameraGallery() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.add(Manifest.permission.CAMERA);
        listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_ACCOUNTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCOUNTS:

                boolean permissionCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionSTORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                if(permissionCAMERA && permissionSTORAGE)
                {
                    final Dialog dialog = new Dialog(ProfileEditActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.chooser_dialouge);
                    LinearLayout CameraBtn = (LinearLayout) dialog.findViewById(R.id.ll_camera_parent);
                    LinearLayout GalleryBtn = (LinearLayout) dialog.findViewById(R.id.ll_gallery_parent);
                    CameraBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            captured_image = System.currentTimeMillis() + ".jpg";
                            file = new File(Environment.getExternalStorageDirectory(), captured_image);
                            captured_image = file.getAbsolutePath();
                            Uri outputFileUri = Uri.fromFile(file);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(cameraIntent, 1);
                        }

                    });

                    GalleryBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select File"), 2);*/
                            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 2);

                        }
                    });

                    dialog.show();
                }


                break;
        }
    }

    private void uploadImage(Bitmap bitmap) {

        //start
        pd=new ProgressDialog(ProfileEditActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(true);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        File fileimage = bitmapToFile(this, bitmap, true);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), fileimage);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileimage.getName(), reqFile);

        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(marketPreference.getString("userid", "")));


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DOMAIN).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        Api api = retrofit.create(Api.class);

        Call<ProfileImageModel> call = (Call<ProfileImageModel>) api.uploadImage(
                body,
                userIdBody);
        call.enqueue(new Callback<ProfileImageModel>() {
            @Override
            public void onResponse(Call<ProfileImageModel> call, Response<ProfileImageModel> response) {

                Log.d("ProfileAPICHECK","ProfileCheck");

                // close
                pd.dismiss();
                ProfileImageModel uploadImageOutput = response.body();

                //Checking for response code
                if (response.code() == 200 ) {


                    String uploadImageUrl = uploadImageOutput.getPicId();

                    //es.remove("userProfilePic");

                    Log.e("Uploaded url",uploadImageUrl);

                    preferenceEditor.putString("userProfilePic",uploadImageUrl);
                    preferenceEditor.commit();

                    if (uploadImageUrl == null)
                    {

                        userImage.setImageResource(R.drawable.user_profile);

                    }
                    else
                    {
                        if (uploadImageUrl.isEmpty())
                        {
                            userImage.setImageResource(R.drawable.user_profile);

                        }
                        else
                        {
                            Picasso.get()
                                    .load(uploadImageUrl)
                                    .placeholder(R.drawable.user_profile)
                                    .error(R.drawable.user_profile)
                                    .into(userImage);

                        }
                    }
                }
                else
                {
                    Log.e("Uploaded url","yyy");
                }

            }

            @Override
            public void onFailure(Call<ProfileImageModel> call, Throwable t) {
                //close
                pd.dismiss();

                Log.e("pic","fail");

            }
        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == this.RESULT_OK) {

            Bitmap capturedBitmap = (Bitmap) data.getExtras().get("data");
            /*Bitmap resizeBitmapImg = ShrinkBitmap(capturedBitmap, 300, 600);

            File capturedImageFile = convertBitmapToFile(resizeBitmapImg, this);
            uploadProfileImage(capturedImageFile, resizeBitmapImg);

            String imagePath = compressImage(captured_image);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageForUpload = bitmap;*/
            //ProfileImageApiCall();
            uploadImage(capturedBitmap);
            //profilePicIV.setImageBitmap(imageForUpload);
        }
        if (requestCode == 2 && resultCode == this.RESULT_OK) {
            /*Uri selectedImageUri = data.getData();
            String pathforcalculatesize = getRealPathFromURI(selectedImageUri);
            File file = new File(pathforcalculatesize);
            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constants.CHAT_IMAGES_DIRECTORY;
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            File f = new File(fDir, String.valueOf(System.currentTimeMillis()) + ".png");
            boolean fileCopied = copy(file, f);
            String imaptah = compressImage(f.getAbsolutePath());
            Bitmap bitmap = BitmapFactory.decodeFile(imaptah);
            imageForUpload = bitmap;
            //ProfileImageApiCall();
            uploadImage(bitmap);
            //profilePicIV.setImageBitmap(imageForUpload);
            f.delete();*/

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = BitmapFactory.decodeFile(picturePath);
            uploadImage(bmp);



        }

        /*-------*/

        /*if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                String pathforcalculatesize = getRealPathFromURI(selectedImageUri);
                File file = new File(pathforcalculatesize);
                final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + Constants.CHAT_IMAGES_DIRECTORY;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                File f = new File(fDir, String.valueOf(System.currentTimeMillis()) + ".png");
                boolean fileCopied = copy(file, f);
                String imaptah = compressImage(f.getAbsolutePath());
                Bitmap bitmap = BitmapFactory.decodeFile(imaptah);
                imageForUpload = bitmap;
                //ProfileImageApiCall();
                uploadImage(bitmap);
                //profilePicIV.setImageBitmap(imageForUpload);
                f.delete();

                //Uri selectedImageUri = data.getData();
                try {
                    userImage.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                } catch (IOException e) {
                    Log.e(ProfileEditActivity.class.getSimpleName(), "Failed to load image", e);
                }
                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }
        } else {
            // report failure
            Toast.makeText(getApplicationContext(), "Failed to get intent data", Toast.LENGTH_LONG).show();
            Log.d(ProfileEditActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
*/

        /*--------*/

    }

    //Func - Upload Image
    private void uploadProfileImage(File capturedImageFile, Bitmap resizeBitmapImg) {

        //Checking internet connection
        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            uploadImage(resizeBitmapImg);

        } else
        {

            Snackbar snackbar = Snackbar
                    .make(profileEditActivityContainer, getResources().getString(R.string.error_internet), Snackbar.LENGTH_LONG);

            snackbar.show();

        }
    }

    //Util - creating file
    public static File convertBitmapToFile(Bitmap bitmap, Activity context) {

        //Sharedpreferences
        SharedPreferences quotePreference =  context.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        String userId = quotePreference.getString(Constants.USER_ID, null);
        long millis = System.currentTimeMillis();

        String newCreatedFileName = "IMG" + userId + millis + ".png";


        File f = new File(context.getCacheDir(), newCreatedFileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;

    }

    public String compressImage(String filePath) {

//        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;

    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public boolean copy(File source, File target) {
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
