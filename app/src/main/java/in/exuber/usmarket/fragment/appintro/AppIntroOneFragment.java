package in.exuber.usmarket.fragment.appintro;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.apimodels.upadateappintro.updateappintroinput.UpdateAppIntroInput;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroOneFragment extends Fragment implements View.OnClickListener {

    //Declaring variables
    private Context context;

    //Declaring views
    private LinearLayout appIntroOneFragmentContainer;
    private TextView skipClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;


    //Progress dialog
    private ProgressDialog progressDialog;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    public AppIntroOneFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroOneFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroOneView =  inflater.inflate(R.layout.fragment_app_intro_one, container, false);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(context);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        //Initialising views
        appIntroOneFragmentContainer = appIntroOneView.findViewById(R.id.fragment_app_intro_one);
        skipClick = appIntroOneView.findViewById(R.id.tv_appIntroOneFragment_skipClick);

        //Setting onclick
        skipClick.setOnClickListener(this);


        return appIntroOneView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_appIntroOneFragment_skipClick:


                //Update App Intro
                updateAppIntro();

                break;
        }
    }

    //Func - Update App Intro
    private void updateAppIntro() {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            callUpdateAppIntroService();
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(appIntroOneFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }

    //Service - Update App Intro
    private void callUpdateAppIntroService() {

        //Showing loading
        progressDialog.show();

        String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);

        UpdateAppIntroInput updateAppIntroInput = new UpdateAppIntroInput(userId,"true");

        //Showing loading
        progressDialog.show();


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.updateAppIntro(updateAppIntroInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Checking for response code
                if (response.code() == 200 ) {


                    //Dismiss loading
                    progressDialog.dismiss();

                    ((AppIntroActivity)getActivity()).appIntroFinished();


                }
                //If status code is not 200
                else
                {
                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(appIntroOneFragmentContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(appIntroOneFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
