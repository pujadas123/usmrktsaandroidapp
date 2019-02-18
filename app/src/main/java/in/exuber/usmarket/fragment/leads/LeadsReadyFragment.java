package in.exuber.usmarket.fragment.leads;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.LeadReadyListAdapter;
import in.exuber.usmarket.apimodels.readyleads.readyleadsoutput.ReadyLeadsOutput;
import in.exuber.usmarket.models.LeadsActivieCategoryOutput;
import in.exuber.usmarket.models.leads.LeadsOutput;
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

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class LeadsReadyFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsReadyFragmentFragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout_readyLeadsList;
    private RecyclerView readyLeadsList;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;


    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private List<ReadyLeadsOutput> readyLeadsOutputList;
    private LeadsHomeFragment leadsHomeFragment;

    //Adapter
    private LeadReadyListAdapter leadReadyListAdapter;


    @SuppressLint("ValidFragment")
    public LeadsReadyFragment(LeadsHomeFragment leadsHomeFragment) {

        this.leadsHomeFragment = leadsHomeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View activeLeadsView = inflater.inflate(R.layout.fragment_leads_ready, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising variables
        readyLeadsOutputList = new ArrayList<>();

        //Initialising views
        leadsReadyFragmentFragmentContainer = activeLeadsView.findViewById(R.id.fragment_leads_ready);

        //Recyclerview
        readyLeadsList =  activeLeadsView.findViewById(R.id.rv_leadsReadyFragment_leadsList);
        readyLeadsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerLeads = new LinearLayoutManager(getActivity());
        linearLayoutManagerLeads.setOrientation(LinearLayoutManager.VERTICAL);
        readyLeadsList.setLayoutManager(linearLayoutManagerLeads);

        ///Swipe Refresh Layout
        swipeRefreshLayout_readyLeadsList = activeLeadsView.findViewById(R.id.srl_leadsReadyFragment_pullToRefresh);
        swipeRefreshLayout_readyLeadsList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  activeLeadsView.findViewById(R.id.ll_custom_dialog);
        errorDisplay =  activeLeadsView.findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = activeLeadsView.findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  activeLeadsView.findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  activeLeadsView.findViewById(R.id.tv_errorMain_errorTryAgain);

        //Get Ready Leads
        getReadyLeads();

        //Swipe Referesh listner
        swipeRefreshLayout_readyLeadsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Ready Leads
                getReadyLeads();

                swipeRefreshLayout_readyLeadsList.setRefreshing(false);
            }
        });

        //Setting onclick
        errorDisplayTryClick.setOnClickListener(this);

        return  activeLeadsView;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isLeadDataChanged = marketPreference.getBoolean(Constants.IS_READY_LEAD_DATA_CHANGED, false);

        if (isLeadDataChanged)
        {

            SharedPreferences.Editor preferenceEditor = marketPreference.edit();

            //Preference Editor
            preferenceEditor.putBoolean(Constants.IS_READY_LEAD_DATA_CHANGED, false);

            preferenceEditor.commit();

            //Get Ready Leads
            getReadyLeads();

        }


    }


    public void onBackPressed() {

        getActivity().finish();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Get Ready Leads
                getReadyLeads();



                break;
        }

    }


    //Func - Getting Ready Leads
    private void getReadyLeads() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);
        leadsHomeFragment.setSecondTabTitleText();

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Hiding views
            errorDisplay.setVisibility(View.GONE);
            readyLeadsList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetReadyLeadsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            readyLeadsList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));

        }
    }

    //Func - Getting Ready Leads
    private void callGetReadyLeadsService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<ReadyLeadsOutput>> call = (Call<List<ReadyLeadsOutput>>) api.getReadyLeads(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_READY_LEADS);
        call.enqueue(new Callback<List<ReadyLeadsOutput>>() {
            @Override
            public void onResponse(Call<List<ReadyLeadsOutput>> call, Response<List<ReadyLeadsOutput>> response) {



                //Checking for response code
                if (response.code() == 200 ) {


                    readyLeadsOutputList = response.body();

                    if (readyLeadsOutputList.size() == 0)
                    {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        readyLeadsList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_leads);
                        errorDisplayText.setText(getString( R.string.error_no_data_ready_leads));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        readyLeadsList.setVisibility(View.VISIBLE);

                        //Setting adapter
                        leadReadyListAdapter = new LeadReadyListAdapter(getActivity(),readyLeadsOutputList);
                        readyLeadsList.setAdapter(leadReadyListAdapter);
                        leadReadyListAdapter.notifyDataSetChanged();

                        leadsHomeFragment.setSecondTabTitle(readyLeadsOutputList.size());

                    }




                }
                //If status code is not 200
                else
                {
                    progressDialog.setVisibility(View.GONE);
                    readyLeadsList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ReadyLeadsOutput>> call, Throwable t) {


                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    readyLeadsList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_leads);
                    errorDisplayText.setText(getString( R.string.error_no_data_ready_leads));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    readyLeadsList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }



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
