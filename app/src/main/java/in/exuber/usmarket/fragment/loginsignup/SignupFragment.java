package in.exuber.usmarket.fragment.loginsignup;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.activity.forgotpassword.ForgotPasswordActivity;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;
import in.exuber.usmarket.activity.welcomescreen.WelcomeScreen;
import in.exuber.usmarket.apimodels.signup.signupoutput.SignupInput;
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

import static android.content.Context.MODE_PRIVATE;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;
import static in.exuber.usmarket.utils.UtilMethods.isValidEmail;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private NestedScrollView signupFragmentContainer;

    private EditText firstName, lastName, email, phoneNumber, password, confirmPassword;
    private RadioGroup languageSelectGroup;
    private RadioButton englishLanguageSelect, spanishLanguageSelect;
    private CountryCodePicker phoneCodePicker, countryRegionPicker;
    private TextView firstNameError, lastNameError, emailError, phoneNumberError, prefLanguangeError, passwordError, confirmPasswordError;

    private EditText addressOne, addressTwo, aptUnit, city, state, postalCode;
    private TextView addressOneError, addressTwoError, aptUnitError, cityError, stateError, postalCodeError, countryRegionError;

    private CheckBox agreementSelect;
    private TextView agreementText;
    private CheckBox termsSelect;
    private CheckBox citizenSelect;
    private TextView termsText, citizenText;
    private TextView loginClickText;

    private LinearLayout registerClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Progress Dialog
    private ProgressDialog progressDialog;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private boolean isEnglishSelected = false, isSpanishSelected = false;
    private boolean isTermsSelected = false;
    private boolean isAgreementSelected = false;
    private boolean isCitizenSelected = false;

    String selectedLangId;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View signupView = inflater.inflate(R.layout.fragment_signup, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        //Initialising views
        signupFragmentContainer = signupView.findViewById(R.id.fragment_signup);

        firstName = signupView.findViewById(R.id.et_signup_firstName);
        lastName = signupView.findViewById(R.id.et_signup_lastName);
        email = signupView.findViewById(R.id.et_signup_email);
        phoneNumber = signupView.findViewById(R.id.et_signup_phoneNumber);
        password = signupView.findViewById(R.id.et_signup_password);
        confirmPassword = signupView.findViewById(R.id.et_signup_confirmPassword);

        phoneCodePicker = signupView.findViewById(R.id.cpp_signup_phoneNumberPicker);
        countryRegionPicker = signupView.findViewById(R.id.cpp_signup_countryRegion);

        languageSelectGroup = signupView.findViewById(R.id.rg_signup_languageGroup);
        englishLanguageSelect = signupView.findViewById(R.id.rb_signup_languageEnglish);
        spanishLanguageSelect = signupView.findViewById(R.id.rb_signup_languageSpanish);

        firstNameError = signupView.findViewById(R.id.tv_signup_firstNameError);
        lastNameError = signupView.findViewById(R.id.tv_signup_lastNameError);
        emailError = signupView.findViewById(R.id.tv_signup_emailError);
        phoneNumberError = signupView.findViewById(R.id.tv_signup_phoneNumberError);
        prefLanguangeError = signupView.findViewById(R.id.tv_signup_prefLanguageError);
        passwordError = signupView.findViewById(R.id.tv_signup_passwordError);
        confirmPasswordError = signupView.findViewById(R.id.tv_signup_confirmPasswordError);

        addressOne = signupView.findViewById(R.id.et_signup_addressOne);
        addressTwo = signupView.findViewById(R.id.et_signup_addressTwo);
        aptUnit = signupView.findViewById(R.id.et_signup_aptUnit);
        city = signupView.findViewById(R.id.et_signup_city);
        state = signupView.findViewById(R.id.et_signup_state);
        postalCode = signupView.findViewById(R.id.et_signup_postalCode);

        addressOneError = signupView.findViewById(R.id.tv_signup_addressOneError);
        addressTwoError = signupView.findViewById(R.id.tv_signup_addressTwoError);
        aptUnitError = signupView.findViewById(R.id.tv_signup_aptUnitError);
        cityError = signupView.findViewById(R.id.tv_signup_cityError);
        stateError = signupView.findViewById(R.id.tv_signup_stateError);
        postalCodeError = signupView.findViewById(R.id.tv_signup_postalCodeError);
        countryRegionError = signupView.findViewById(R.id.tv_signup_countryRegionError);

        agreementSelect = signupView.findViewById(R.id.cb_signup_agreementSelect);

        agreementText = signupView.findViewById(R.id.tv_signup_agreementText);
        agreementText.setClickable(true);
        agreementText.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Check here to indicate that you read and agree with the <a href='https://s3-us-west-2.amazonaws.com/usmrktrepowalmart/USMRKT+Sales+Associate+Agreement.pdf'>US MRKT Sales Associate Agreement</a>.";
        agreementText.setText(Html.fromHtml(text));

        termsSelect = signupView.findViewById(R.id.cb_signup_termsSelect);
        citizenSelect = signupView.findViewById(R.id.cb_signup_citizenSelect);

        termsText = signupView.findViewById(R.id.tv_signup_termsText);
        termsText.setClickable(true);
        termsText.setMovementMethod(LinkMovementMethod.getInstance());
        String term_text = "Check here to indicate that you read and agree with the <a href='https://s3-us-west-2.amazonaws.com/usmrktrepowalmart/USM+Terms+of+Use+(USA).pdf'>Terms and Conditions</a> of US MRKT.";
        termsText.setText(Html.fromHtml(term_text));


        citizenText = signupView.findViewById(R.id.tv_signup_citizenText);
        loginClickText = signupView.findViewById(R.id.tv_signup_loginClickText);

        registerClick = signupView.findViewById(R.id.ll_signup_registerClick);

        //Registering validation
        phoneCodePicker.registerPhoneNumberTextView(phoneNumber);

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

        languageSelectGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.d("Selected Id", "" + checkedId);

                        if (checkedId == R.id.rb_signup_languageEnglish) {

                            isEnglishSelected = true;
                            isSpanishSelected = false;
                        }
                        else if (checkedId == R.id.rb_signup_languageSpanish) {

                            isSpanishSelected = true;
                            isEnglishSelected = false;
                        }

                    }

                });



        agreementSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        isAgreementSelected = true;
                        agreementText.setTextColor(getResources().getColor(R.color.colorText));

                    } else {

                        isAgreementSelected = false;

                    }

                }

            }
        );

        termsSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                      if (isChecked) {
                          isTermsSelected = true;
                          termsText.setTextColor(getResources().getColor(R.color.colorText));
                      } else {
                          isTermsSelected = false;
                      }
            }
        });

        citizenSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    isCitizenSelected = true;
                    citizenText.setTextColor(getResources().getColor(R.color.colorText));

                } else {

                    isCitizenSelected = false;
                }
            }
        }
        );


        ClickableSpan agreementsLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "Clicked US MRKT Sales Associate Agreement", Toast.LENGTH_SHORT).show();
               /* agreementText.setClickable(true);
                agreementText.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href='https://s3-us-west-2.amazonaws.com/usmrktrepowalmart/US+MRKT+Sales+Associate+Agreement.pdf'>Check here to indicate that you read and agree with the US MRKT Sales Associate Agreement.\n</a>";
                agreementText.setText(Html.fromHtml(text));*/

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }

        };


        ClickableSpan termsLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Clicked Terms and Conditions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }

        };

        ClickableSpan loginLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                //Hiding Keyboard
                hideKeyBoard(getActivity());

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

                firstName.setText("");
                lastName.setText("");
                email.setText("");
                phoneNumber.setText("");
                password.setText("");
                confirmPassword.setText("");

                addressOne.setText("");
                addressTwo.setText("");
                aptUnit.setText("");
                state.setText("");
                postalCode.setText("");

                firstName.clearFocus();
                lastName.clearFocus();
                email.clearFocus();
                phoneNumber.clearFocus();
                password.clearFocus();
                confirmPassword.clearFocus();

                addressOne.clearFocus();
                addressTwo.clearFocus();
                aptUnit.clearFocus();
                city.clearFocus();
                state.clearFocus();
                postalCode.clearFocus();

                isEnglishSelected = false;
                isSpanishSelected = false;
                isAgreementSelected = false;
                isTermsSelected = false;
                isCitizenSelected = false;

                englishLanguageSelect.setChecked(false);
                spanishLanguageSelect.setChecked(false);
                agreementSelect.setChecked(false);
                termsSelect.setChecked(false);
                citizenSelect.setChecked(false);
                agreementText.setTextColor(getResources().getColor(R.color.colorText));
                termsText.setTextColor(getResources().getColor(R.color.colorText));
                citizenText.setTextColor(getResources().getColor(R.color.colorText));

                firstName.requestFocus();


                ((LoginSignupActivity) getActivity()).loginLinkClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);

            }

        };
        makeLinks(agreementText,
                new String[]{"US MRKT Sales Associate Agreement"},
                new ClickableSpan[]{agreementsLinkClickSpan}
        );

        makeLinks(termsText,
                new String[]{"Terms and Conditions"},
                new ClickableSpan[]{termsLinkClickSpan}
        );

        makeLinks(loginClickText,
                new String[]{"LOG IN"},
                new ClickableSpan[]{loginLinkClickSpan}
        );


        //Setting onclick
        registerClick.setOnClickListener(this);


        return signupView;

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_signup_registerClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());


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


                boolean validFlag = validateTextFields(firstNameText, lastNameText, emailText, phoneNumberText, passwordText, confirmPasswordText,
                                                       addressOneText, addressTwoText, aptUnitText, cityText, stateText, postalCodeText);

                if (validFlag)
                {
                    boolean agreementSelectFlag = checkAgreementSelection();

                    if (agreementSelectFlag)
                    {
                        //Calling Email Checking
                        emailCheck(firstNameText, lastNameText, emailText, phoneNumberText, passwordText, confirmPasswordText,
                                   addressOneText, addressTwoText, aptUnitText, cityText, stateText, postalCodeText);
                    }

                }

                break;

        }
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
    private boolean validateTextFields(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText,
                                       String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        phoneCodePicker.registerPhoneNumberTextView(phoneNumber);

        Log.e("Is US User",isCitizenSelected+"");

        boolean validFlag = true;

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

        if (addressOneText.isEmpty()) {

            addressOneError.setText(getString(R.string.error_address_empty));
            addressOneError.setVisibility(View.VISIBLE);
            addressOne.requestFocus();
            validFlag = false;

        }

        if (passwordText.isEmpty())
        {
            passwordError.setText(getString(R.string.error_password_empty));
            passwordError.setVisibility(View.VISIBLE);
            password.requestFocus();
            validFlag = false;
        }
        else
        {
            if (passwordText.length() < 6 || passwordText.length() >= 11 ) {

                passwordError.setText(getString(R.string.error_password_length));
                passwordError.setVisibility(View.VISIBLE);
                password.requestFocus();
                validFlag = false;
            }
            else
            {
                if (confirmPasswordText.isEmpty()) {

                    confirmPasswordError.setText(getString(R.string.error_confirm_password_empty));
                    confirmPasswordError.setVisibility(View.VISIBLE);
                    confirmPassword.requestFocus();
                    validFlag = false;

                }
                else
                {
                    if (!confirmPasswordText.equals(passwordText)) {
                        confirmPasswordError.setText(getString(R.string.error_confirm_password_mismatch));
                        confirmPasswordError.setVisibility(View.VISIBLE);
                        confirmPassword.requestFocus();
                        validFlag = false;
                    }
                }
            }
        }



        if (!isEnglishSelected && !isSpanishSelected) {

            prefLanguangeError.setText(getString(R.string.error_pref_language_empty));
            prefLanguangeError.setVisibility(View.VISIBLE);
            validFlag = false;

        }

        if (phoneNumberText.isEmpty()) {

            phoneNumberError.setText(getString(R.string.error_phone_number_empty));
            phoneNumberError.setVisibility(View.VISIBLE);
            phoneNumber.requestFocus();

            validFlag = false;

        } else {

            if (!phoneCodePicker.isValid()) {
                phoneNumberError.setText(getString(R.string.error_phone_number_invalid));
                phoneNumberError.setVisibility(View.VISIBLE);
                phoneNumber.requestFocus();

                validFlag = false;
            }
        }

        if (emailText.isEmpty()) {

            emailError.setText(getString(R.string.error_email_empty));
            emailError.setVisibility(View.VISIBLE);
            email.requestFocus();

            validFlag = false;

        }
        else {

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

    //Func - Checking agreement selection
    private boolean checkAgreementSelection() {

        boolean validFlag = true;

        if (!isTermsSelected) {
            termsText.setTextColor(getResources().getColor(R.color.colorErrorRed));
            validFlag = false;
        }

        if (!isAgreementSelected) {
            agreementText.setTextColor(getResources().getColor(R.color.colorErrorRed));
            validFlag = false;
        }


        return validFlag;

    }

    //Func - Email Check
    private void emailCheck(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText,
                            String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callEmailCheckService(firstNameText, lastNameText, emailText, phoneNumberText, passwordText, confirmPasswordText,
                                  addressOneText, addressTwoText, aptUnitText, cityText, stateText, postalCodeText);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(signupFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }



    //Func - Signup
    private void signup(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText,
                        String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {


        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            callSignupService(firstNameText, lastNameText, emailText, phoneNumberText, passwordText, confirmPasswordText,
                              addressOneText, addressTwoText, aptUnitText, cityText, stateText, postalCodeText);

        } else {
            Snackbar snackbar = Snackbar
                    .make(signupFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Service - Email Check
    private void callEmailCheckService(final String firstNameText, final String lastNameText, final String emailText, final String phoneNumberText, final String passwordText, final String confirmPasswordText,
                                       final String addressOneText, final String addressTwoText, final String aptUnitText, final String cityText, final String stateText, final String postalCodeText) {



        //Showing loading
        progressDialog.show();


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.emailCheck(emailText);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(signupFragmentContainer, R.string.error_email_exists, Snackbar.LENGTH_LONG);

                    snackbar.show();

                    emailError.setText(getString(R.string.error_email_exists));
                    emailError.setVisibility(View.VISIBLE);
                    email.requestFocus();

                }
                //If status code is not 200
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    if (response.code() == 404)
                    {

                        //Calling Service
                        signup(firstNameText, lastNameText, emailText, phoneNumberText, passwordText, confirmPasswordText,
                                addressOneText, addressTwoText, aptUnitText, cityText, stateText, postalCodeText);
                    }
                    else
                    {


                        Snackbar snackbar = Snackbar
                                .make(signupFragmentContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(signupFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

                snackbar.show();

            }

        });


    }

    //Service - Signup
    private void callSignupService(String firstNameText, String lastNameText, String emailText, String phoneNumberText, String passwordText, String confirmPasswordText,
                                   String addressOneText, String addressTwoText, String aptUnitText, String cityText, String stateText, String postalCodeText) {

        //Showing loading
        progressDialog.show();

        SignupInput signupInput = new SignupInput();

        signupInput.setName(firstNameText);
        signupInput.setLastName(lastNameText);
        signupInput.setEmail(emailText);
        signupInput.setFlagCode(phoneCodePicker.getSelectedCountryNameCode());
        Log.e("FlagCodeName",phoneCodePicker.getSelectedCountryNameCode());
        signupInput.setCode(phoneCodePicker.getSelectedCountryCodeWithPlus());
        signupInput.setPhoneNo(phoneNumberText);
        signupInput.setPassword(passwordText);

        if (isEnglishSelected)
        {
            signupInput.setPrefLanguage("1");
            selectedLangId="1";
        }

        if (isSpanishSelected)
        {
            signupInput.setPrefLanguage("2");
            selectedLangId="2";
        }

        Log.e("SelectedLanguageID",selectedLangId+"");

        signupInput.setAddress1(addressOneText);
        signupInput.setAddress2(addressTwoText);
        signupInput.setUnit(aptUnitText);
        signupInput.setCity(cityText);
        signupInput.setState(stateText);
        signupInput.setPostalCode(postalCodeText);
        signupInput.setCountry(countryRegionPicker.getSelectedCountryNameCode());

        if (isAgreementSelected) {

            signupInput.setSaAg("YES");

        }
        else {

            signupInput.setSaAg("NO");

        }

        if (isTermsSelected) {

            signupInput.setTerms("YES");
        }
        else {

            signupInput.setTerms("NO");

        }



        if (isCitizenSelected) {

            signupInput.setUsResident("YES");


        }
        else {

            signupInput.setUsResident("NO");
        }

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.registerUser(signupInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    startActivity(new Intent(getActivity(), WelcomeScreen.class));
                    getActivity().finish();
                }
                //If status code is not 201
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(signupFragmentContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(signupFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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




}