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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;
import in.exuber.usmarket.activity.home.HomeActivity;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroOneFragment extends Fragment implements View.OnClickListener {

    //Declaring variables
    private Context context;

    //Declaring views
    private TextView skipClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    LinearLayout ll_AppintroOneParent;

    ProgressDialog pd;

    //Connection detector class
    private ConnectionDetector connectionDetector;

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

        //Initialising views
        ll_AppintroOneParent=appIntroOneView.findViewById(R.id.ll_AppintroOneParent);
        skipClick = appIntroOneView.findViewById(R.id.tv_appIntroOneFragment_skipClick);

        //Setting onclick
        skipClick.setOnClickListener(this);

        if (marketPreference.getString("appIntroDone",null).equals("false")) {
            startActivity(new Intent(getActivity(), AppIntroActivity.class));
            getActivity().finish();
        }
        else {

            boolean isProductAdded = marketPreference.getBoolean(Constants.IS_PRODUCT_ADDED, false);

            if (isProductAdded)
            {
                //Calling Home activity
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }
            else
            {
                //Calling Home Add product activity
                startActivity(new Intent(getActivity(), HomeAddProductsActivity.class));
                getActivity().finish();
            }
        }

        return appIntroOneView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_appIntroOneFragment_skipClick:

                ((AppIntroActivity)getActivity()).appIntroFinished();

                boolean isInternetPresent = connectionDetector.isConnectingToInternet();

                if (isInternetPresent) {


                    callUpdateAppintroService();
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(ll_AppintroOneParent, R.string.error_internet, Snackbar.LENGTH_LONG);

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
