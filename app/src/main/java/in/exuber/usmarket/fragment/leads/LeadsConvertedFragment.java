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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.LeadConvertedListAdapter;
import in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput.ConvertedLeadsOutput;
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
public class LeadsConvertedFragment extends Fragment implements View.OnClickListener {


    //Declaring views
    private LinearLayout leadsConvertedFragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout_convertedLeadsList;
    private RecyclerView convertedLeadsList;

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
    private List<ConvertedLeadsOutput> convertedLeadsOutputList;
    private LeadsHomeFragment leadsHomeFragment;


    //Adapter
    private LeadConvertedListAdapter leadConvertedListAdapter;

    @SuppressLint("ValidFragment")
    public LeadsConvertedFragment(LeadsHomeFragment leadsHomeFragment) {

        this.leadsHomeFragment = leadsHomeFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertedLeadsView = inflater.inflate(R.layout.fragment_leads_converted, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising variables
        convertedLeadsOutputList = new ArrayList<>();

        //Initialising views
        leadsConvertedFragmentContainer = convertedLeadsView.findViewById(R.id.fragment_leads_converted);

        //Recyclerview
        convertedLeadsList =  convertedLeadsView.findViewById(R.id.rv_leadsConvertedFragment_leadsList);
        convertedLeadsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerLeads = new LinearLayoutManager(getActivity());
        linearLayoutManagerLeads.setOrientation(LinearLayoutManager.VERTICAL);
        convertedLeadsList.setLayoutManager(linearLayoutManagerLeads);

        ///Swipe Refresh Layout
        swipeRefreshLayout_convertedLeadsList = convertedLeadsView.findViewById(R.id.srl_leadsConvertedFragment_pullToRefresh);
        swipeRefreshLayout_convertedLeadsList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  convertedLeadsView.findViewById(R.id.ll_custom_dialog);
        errorDisplay =  convertedLeadsView.findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = convertedLeadsView.findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  convertedLeadsView.findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  convertedLeadsView.findViewById(R.id.tv_errorMain_errorTryAgain);

        //Get Converted Leads
        getConvertedLeads();

        //Swipe Referesh listner
        swipeRefreshLayout_convertedLeadsList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Converted Leads
                getConvertedLeads();

                swipeRefreshLayout_convertedLeadsList.setRefreshing(false);
            }
        });


        //Setting onclick
        errorDisplayTryClick.setOnClickListener(this);

        return convertedLeadsView;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isLeadDataChanged = marketPreference.getBoolean(Constants.IS_CONVERTED_LEAD_DATA_CHANGED, false);

        if (isLeadDataChanged)
        {

            SharedPreferences.Editor preferenceEditor = marketPreference.edit();

            //Preference Editor
            preferenceEditor.putBoolean(Constants.IS_CONVERTED_LEAD_DATA_CHANGED, false);

            preferenceEditor.commit();

            //Get Converted Leads
            getConvertedLeads();
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

                //Get Converted Leads
                getConvertedLeads();



                break;
        }



    }

    //Func - Getting Converted Leads
    private void getConvertedLeads() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);
        leadsHomeFragment.setThirdTabTitleText();

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Hiding views
            errorDisplay.setVisibility(View.GONE);
            convertedLeadsList.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetConvertedLeadsService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            convertedLeadsList.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Service - Getting Converted Leads
    private void callGetConvertedLeadsService() {



        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<ConvertedLeadsOutput>> call = (Call<List<ConvertedLeadsOutput>>) api.getConvertedLeads(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_CONVERTED_LEADS);
        call.enqueue(new Callback<List<ConvertedLeadsOutput>>() {
            @Override
            public void onResponse(Call<List<ConvertedLeadsOutput>> call, Response<List<ConvertedLeadsOutput>> response) {



                //Checking for response code
                if (response.code() == 200 ) {


                    convertedLeadsOutputList = response.body();

                    if (convertedLeadsOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        convertedLeadsList.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_leads);
                        errorDisplayText.setText(getString( R.string.error_no_data_converted_leads));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        convertedLeadsList.setVisibility(View.VISIBLE);


                        //Setting adapter
                        leadConvertedListAdapter = new LeadConvertedListAdapter(getActivity(),convertedLeadsOutputList);
                        convertedLeadsList.setAdapter(leadConvertedListAdapter);
                        leadConvertedListAdapter.notifyDataSetChanged();

                        leadsHomeFragment.setThirdTabTitle(convertedLeadsOutputList.size());
                    }






                }
                //If status code is not 200
                else
                {

                    progressDialog.setVisibility(View.GONE);
                    convertedLeadsList.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<ConvertedLeadsOutput>> call, Throwable t) {



                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    convertedLeadsList.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_leads);
                    errorDisplayText.setText(getString( R.string.error_no_data_converted_leads));
                    errorDisplayTryClick.setVisibility(View.GONE);
                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    convertedLeadsList.setVisibility(View.GONE);


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
