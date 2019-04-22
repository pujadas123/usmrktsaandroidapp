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
import android.widget.RelativeLayout;
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
public class AppIntroFourFragment extends Fragment implements View.OnClickListener {

    //Declaring variables
    private Context context;

    //Declaring views
    private LinearLayout getStartedClick;
    RelativeLayout ll_AppintroFourParent;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    ProgressDialog pd;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    public AppIntroFourFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroFourFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroFourView =  inflater.inflate(R.layout.fragment_app_intro_four, container, false);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(context);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising views
        ll_AppintroFourParent=appIntroFourView.findViewById(R.id.ll_AppintroFourParent);
        getStartedClick = appIntroFourView.findViewById(R.id.ll_appIntroFourFragment_getStartedClick);

        getStartedClick.setOnClickListener(this);



        return appIntroFourView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_appIntroFourFragment_getStartedClick:



                boolean isInternetPresent = connectionDetector.isConnectingToInternet();

                if (isInternetPresent) {


                    callUpdateAppintroService();
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(ll_AppintroFourParent, R.string.error_internet, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

                break;
        }
    }


    private void callUpdateAppintroService() {

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
