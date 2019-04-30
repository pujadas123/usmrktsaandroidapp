package in.exuber.usmarket.fragment.leads;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsadd.LeadsAddActivity;
import in.exuber.usmarket.adapter.LeadActiveListAdapter;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
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
public class LeadsActiveFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout leadsActiveFragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout_activeLeadsList;
    private RecyclerView activeLeadsList;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;
    private ScrollView errorDisplayActiveLeads;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    private ImageView errorDisplayIconActiveLeads;
    private TextView errorDisplayHeaderActiveLeads;
    private TextView errorDisplayTextActiveLeads;
    private TextView errorDisplayAddLeadClickActiveLeads;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Connection detector class
    private ConnectionDetector connectionDetector;



    //Declaring variables
    private LeadsHomeFragment leadsHomeFragment;
    private List<AllLeadsOutput> allLeadsOutputList;

    //Adapter
    private LeadActiveListAdapter leadActiveListAdapter;


    @SuppressLint("ValidFragment")
    public LeadsActiveFragment(LeadsHomeFragment leadsHomeFragment) {

        this.leadsHomeFragment = leadsHomeFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View activeLeadsView = inflater.inflate(R.layout.fragment_leads_active, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);


        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising variables
        allLeadsOutputList = new ArrayList<>();

        //Initialising views
        leadsActiveFragmentContainer = activeLeadsView.findViewById(R.id.fragment_leads_active);

        //Recyclerview
        activeLeadsList =  activeLeadsView.findViewById(R.id.rv_leadsActiveFragment_leadsList);
        activeLeadsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerLeads = new LinearLayoutManager(getActivity());
        linearLayoutManagerLeads.setOrientation(LinearLayoutManager.VERTICAL);
        activeLeadsList.setLayoutManager(linearLayoutManagerLeads);

        ///Swipe Refresh Layout
        swipeRefreshLayout_activeLeadsList = activeLeadsView.findViewById(R.id.srl_leadsActiveFragment_pullToRefresh);
        swipeRefreshLayout_activeLeadsList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  activeLeadsView.findViewById(R.id.ll_custom_dialog);
        errorDisplay =  activeLeadsView.findViewById(R.id.ll_errorMain_layout);
        errorDisplayActiveLeads =  activeLeadsView.findViewById(R.id.sv_errorActiveLeads_layout);


        errorDisplayIcon = activeLeadsView.findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  activeLeadsView.findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  activeLeadsView.findViewById(R.id.tv_errorMain_errorTryAgain);

        errorDisplayIconActiveLeads = activeLeadsView.findViewById(R.id.iv_errorActiveLeads_errorIcon);
        errorDisplayHeaderActiveLeads =  activeLeadsView.findViewById(R.id.tv_errorActiveLeads_errorHeader);
        errorDisplayTextActiveLeads =  activeLeadsView.findViewById(R.id.tv_errorActiveLeads_errorText);
        errorDisplayAddLeadClickActiveLeads =  activeLeadsView.findViewById(R.id.tv_errorActiveLeads_addLeadClick);


        //Get All Leads
        getAllLeads();

        //Swipe Referesh listner
        swipeRefreshLayout_activeLeadsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get All Leads
                getAllLeads();

                swipeRefreshLayout_activeLeadsList.setRefreshing(false);
            }
        });

        //Setting onclick
        errorDisplayTryClick.setOnClickListener(this);
        errorDisplayAddLeadClickActiveLeads.setOnClickListener(this);

        return  activeLeadsView;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isLeadDataChanged = marketPreference.getBoolean(Constants.IS_ACTIVE_LEAD_DATA_CHANGED, false);

        if (isLeadDataChanged)
        {

            SharedPreferences.Editor preferenceEditor = marketPreference.edit();

            //Preference Editor
            preferenceEditor.putBoolean(Constants.IS_ACTIVE_LEAD_DATA_CHANGED, false);

            preferenceEditor.commit();

            //Get All Leads
            getAllLeads();
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

                //Calling Service
                getAllLeads();



                break;

            case R.id.tv_errorActiveLeads_addLeadClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Add leads Activity
                startActivity(new Intent(getActivity(), LeadsAddActivity.class));

                break;
        }



    }

    //Func - Getting All Leads
    private void getAllLeads() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);
        leadsHomeFragment.setFirstTabTitleText();

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Hiding views
            errorDisplay.setVisibility(View.GONE);
            activeLeadsList.setVisibility(View.GONE);
            errorDisplayActiveLeads.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);


            //Calling Service
            callGetAllLeadsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            activeLeadsList.setVisibility(View.GONE);
            errorDisplayActiveLeads.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));


        }
    }

    //Service - Getting All Leads
    private void callGetAllLeadsService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<AllLeadsOutput>> call = (Call<List<AllLeadsOutput>>) api.getActiveLeads(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_ACTIVE_LEADS);
        call.enqueue(new Callback<List<AllLeadsOutput>>() {
            @Override
            public void onResponse(Call<List<AllLeadsOutput>> call, Response<List<AllLeadsOutput>> response) {



                //Checking for response code
                if (response.code() == 200 ) {


                    allLeadsOutputList = response.body();


                    if (allLeadsOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);
                        activeLeadsList.setVisibility(View.GONE);

                        errorDisplayActiveLeads.setVisibility(View.VISIBLE);

                        errorDisplayIconActiveLeads.setImageResource(R.drawable.ic_error_leads);
                        errorDisplayHeaderActiveLeads.setText(getString( R.string.error_no_data_active_leads));
                        errorDisplayTextActiveLeads.setText(getString( R.string.error_description_no_data_active_leads));



                    }
                    else {


                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);
                        errorDisplayActiveLeads.setVisibility(View.GONE);

                        activeLeadsList.setVisibility(View.VISIBLE);

                        //Setting adapter
                        leadActiveListAdapter = new LeadActiveListAdapter(getActivity(),allLeadsOutputList);
                        activeLeadsList.setAdapter(leadActiveListAdapter);
                        leadActiveListAdapter.notifyDataSetChanged();

                        leadsHomeFragment.setFirstTabTitle(allLeadsOutputList.size());

                    }



                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    activeLeadsList.setVisibility(View.GONE);
                    errorDisplayActiveLeads.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<AllLeadsOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());


                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    errorDisplay.setVisibility(View.GONE);
                    activeLeadsList.setVisibility(View.GONE);

                    errorDisplayActiveLeads.setVisibility(View.VISIBLE);

                    errorDisplayIconActiveLeads.setImageResource(R.drawable.ic_error_leads);
                    errorDisplayHeaderActiveLeads.setText(getString( R.string.error_no_data_active_leads));
                    errorDisplayTextActiveLeads.setText(getString( R.string.error_description_no_data_active_leads));



                }
                else
                {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    activeLeadsList.setVisibility(View.GONE);
                    errorDisplayActiveLeads.setVisibility(View.GONE);


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
