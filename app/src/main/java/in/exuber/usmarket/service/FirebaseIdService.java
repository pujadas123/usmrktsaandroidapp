package in.exuber.usmarket.service;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.updatemobileid.updatemobileidinput.UpdateMobileIdInput;
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

public class FirebaseIdService extends FirebaseInstanceIdService {


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Declaring Rectrofit log
    private static OkHttpClient.Builder builder;


    @Override
    public void onTokenRefresh() {

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Log the token
        Log.e("Refreshed Token", refreshedToken);

        boolean isLoggedIn = marketPreference.getBoolean(Constants.IS_LOGGED_IN, false);

        if (isLoggedIn)
        {
            updateMobileId(refreshedToken);
        }



    }

    //Func - Update Mobile Id
    private void updateMobileId(String refreshedToken) {


        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            //Calling Service
            callUpdateMobileIdService(refreshedToken);
        }

    }

    private void callUpdateMobileIdService(String refreshedToken) {

        String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.updateMobileId(new UpdateMobileIdInput(userId,refreshedToken));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                //Checking for response code
                if (response.code() == 201 ) {

                    Log.e("Success","Token Updated");

                }
                else
                {
                    Log.e("Response Error",response.code()+"");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("Failure",t.toString());


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
