package in.exuber.usmarket.activity.forgotpassword;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;
import in.exuber.usmarket.dialog.ForgotPasswordDialog;
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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout forgotPasswordActivityContainer;

    private EditText email;
    private TextView emailError;

    private LinearLayout submitClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Progress dialog
    private ProgressDialog progressDialog;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Initialising views
        forgotPasswordActivityContainer = findViewById(R.id.activity_forgot_password);

        email = findViewById(R.id.et_forgotPass_email);
        emailError = findViewById(R.id.tv_forgotPass_emailError);

        submitClick = findViewById(R.id.ll_forgotPass_submitClick);

        //Hiding views
        emailError.setVisibility(View.GONE);

        //Setting onclick
        submitClick.setOnClickListener(this);
    }




    @Override
    public void onBackPressed() {

        finish();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.ll_forgotPass_submitClick:

                //Hiding Keyboard
                hideKeyBoard(ForgotPasswordActivity.this);

                //Hiding views
                emailError.setVisibility(View.GONE);


                String emailText = email.getText().toString().trim();

                boolean validFlag = validateTextFields(emailText);

                if (validFlag)
                {
                    //Calling Email Checking
                    emailCheck(emailText);

                }


                break;
        }
    }

    //Func - Close Click
    public void closeClick() {

        finish();
    }

    //Func - Validating text fields
    private boolean validateTextFields(String emailText) {

        boolean validFlag = true;

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

    //Func - Email Check
    private void emailCheck(String emailText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callEmailCheckService(emailText);
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(forgotPasswordActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }



    //Func - Forgot password
    private void forgotPass(String emailText) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            //Calling Service
            callForgotPassService(emailText);
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(forgotPasswordActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }



    //Service - Email Check
    private void callEmailCheckService(final String emailText) {

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

                    //Calling Email Checking
                    forgotPass(emailText);
                }
                //If status code is not 200
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    if (response.code() == 404)
                    {

                        //Dismiss loading
                        progressDialog.dismiss();

                        Snackbar snackbar = Snackbar
                                .make(forgotPasswordActivityContainer, R.string.error_email_not_exists, Snackbar.LENGTH_LONG);

                        snackbar.show();

                        emailError.setText(getString(R.string.error_email_not_exists));
                        emailError.setVisibility(View.VISIBLE);
                        email.requestFocus();
                    }
                    else
                    {


                        Snackbar snackbar = Snackbar
                                .make(forgotPasswordActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

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
                        .make(forgotPasswordActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

                snackbar.show();

            }

        });
    }

    //Service - Forgot password
    private void callForgotPassService(String emailText) {

        //Showing loading
        progressDialog.show();


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.forgotPassword(emailText);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    //Calling dialog
                    FragmentManager errorManager = getFragmentManager();
                    ForgotPasswordDialog errorDialog = new ForgotPasswordDialog(ForgotPasswordActivity.this,getString(R.string.message_forgotPass));
                    errorDialog.show(errorManager, "FORGOT_PASSWORD_DIALOG");

                }
                //If status code is not 200
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(forgotPasswordActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(forgotPasswordActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
