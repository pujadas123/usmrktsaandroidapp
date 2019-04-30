package in.exuber.usmarket.fragment.appintro;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroThreeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout appIntroThreeFragmentContainer;
    private TextView skipClick;

    //Declaring variables
    private Context context;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    ProgressDialog pd;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    public AppIntroThreeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroThreeFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroThreeView =  inflater.inflate(R.layout.fragment_app_intro_three, container, false);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising views
        appIntroThreeFragmentContainer = appIntroThreeView.findViewById(R.id.fragment_app_intro_two);
        skipClick = appIntroThreeView.findViewById(R.id.tv_appIntroThreeFragment_skipClick);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(context);

        //Setting onclick
        skipClick.setOnClickListener(this);

        return appIntroThreeView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.tv_appIntroThreeFragment_skipClick:

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
                    .make(appIntroThreeFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }


    //Service - Update App Intro
    private void callUpdateAppIntroService() {

        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("userId", userId);
            jObject.put("appIntro", "true");

        } catch (JSONException e) {
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

        asyncHttpClient.put(null, Constants.APP_APPINTRO_SEEN, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                pd=new ProgressDialog(getActivity());
                pd.setMessage("Please Wait...");
                pd.setCancelable(true);
                pd.setIndeterminate(false);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final String Response = new String(responseBody);
                Log.v("AppOne_Response", Response);

                Log.v("Status code", statusCode+"");

                ((AppIntroActivity)getActivity()).appIntroFinished();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Error", String.valueOf(error));
                Log.e("ErrorStatus", String.valueOf(statusCode));
                pd.dismiss();

            }
        });
    }
}
