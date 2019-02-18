package in.exuber.usmarket.fragment.campaign;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.CampaignExistingListAdapter;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CampaignId;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.CreatedBy;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.ShareCampaignInput;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.Type;
import in.exuber.usmarket.apimodels.sharecampaign.sharecampaigninput.UserId;
import in.exuber.usmarket.dialog.ExistingCampaignCategoryFilterDialog;
import in.exuber.usmarket.dialog.ExistingCampaignLanguageFilterDialog;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CampaignExistingFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private NestedScrollView campaignExistingFragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout_existingCampaignList;
    private RecyclerView existingCampaignList;

    private TextView categoryClick;
    private LinearLayout languageClick;
    private TextView languageText;
    private LinearLayout loadMoreClick;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Progress dialog
    private ProgressDialog progressDialogSecond;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Declaring variables
    private List<CampaignOutput> existingCampaignOutputList;
    private List<CampaignOutput> filteredExistingCampaignOutputList;

    private int selectedCategoryPosition = 0;
    private int selectedLanguagePosition = 0;



    //Adapter
    private CampaignExistingListAdapter campaignExistingListAdapter;


    public CampaignExistingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View existingCampaignsView =  inflater.inflate(R.layout.fragment_campaign_existing, container, false);

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Initialising progress dialog
        progressDialogSecond = new ProgressDialog(getActivity());
        progressDialogSecond.setMessage(getString(R.string.loader_caption));
        progressDialogSecond.setCancelable(true);
        progressDialogSecond.setIndeterminate(false);
        progressDialogSecond.setCancelable(false);


        //Initialising variables
        existingCampaignOutputList = new ArrayList<>();
        filteredExistingCampaignOutputList = new ArrayList<>();

        //Initialising views
        campaignExistingFragmentContainer = existingCampaignsView.findViewById(R.id.fragment_campaign_existing);

        //Recyclerview
        existingCampaignList =  existingCampaignsView.findViewById(R.id.rv_campaignExistingFragment_campaignsList);
        existingCampaignList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerLeads = new LinearLayoutManager(getActivity());
        linearLayoutManagerLeads.setOrientation(LinearLayoutManager.VERTICAL);
        existingCampaignList.setLayoutManager(linearLayoutManagerLeads);

        categoryClick =  existingCampaignsView.findViewById(R.id.tv_campaignExistingFragment_categoryClick);
        languageClick =  existingCampaignsView.findViewById(R.id.ll_campaignExistingFragment_languageClick);
        languageText =  existingCampaignsView.findViewById(R.id.tv_campaignExistingFragment_languageText);

        loadMoreClick = existingCampaignsView.findViewById(R.id.ll_campaignExistingFragment_loadMoreClick);

        swipeRefreshLayout_existingCampaignList = existingCampaignsView.findViewById(R.id.srl_campaignExistingFragment_pullToRefresh);
        swipeRefreshLayout_existingCampaignList.setColorSchemeResources(
                R.color.colorPrimary);

        progressDialog =  existingCampaignsView.findViewById(R.id.ll_custom_dialog);
        errorDisplay =  existingCampaignsView.findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = existingCampaignsView.findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  existingCampaignsView.findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  existingCampaignsView.findViewById(R.id.tv_errorMain_errorTryAgain);


        categoryClick.setText("All Categories");
        languageText.setText("ALL");
        loadMoreClick.setVisibility(View.GONE);


        //Get Existing Campaigns
        getExistingCampaigns();

        swipeRefreshLayout_existingCampaignList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Existing Campaigns
                getExistingCampaigns();

                swipeRefreshLayout_existingCampaignList.setRefreshing(false);
            }
        });

        //setting onclick
        categoryClick.setOnClickListener(this);
        languageClick.setOnClickListener(this);
        loadMoreClick.setOnClickListener(this);
        errorDisplayTryClick.setOnClickListener(this);


        return existingCampaignsView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_campaignExistingFragment_categoryClick:


                //Calling dialog
                FragmentManager filterCategoryManager = getFragmentManager();
                ExistingCampaignCategoryFilterDialog filterCategoryDialog = new ExistingCampaignCategoryFilterDialog(selectedCategoryPosition,CampaignExistingFragment.this);
                filterCategoryDialog.show(filterCategoryManager, "FILTER_CATEGORY_DIALOG");

                break;

            case R.id.ll_campaignExistingFragment_languageClick:


                //Calling dialog
                FragmentManager infoLanguageManager = getFragmentManager();
                ExistingCampaignLanguageFilterDialog filterLanguageDialog = new ExistingCampaignLanguageFilterDialog(selectedLanguagePosition,CampaignExistingFragment.this);
                filterLanguageDialog.show(infoLanguageManager, "FILTER_LANGUAGE_DIALOG");

                break;

            case R.id.ll_campaignExistingFragment_loadMoreClick:

                //Loading full data
                loadFullData();

                break;

            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Get Existing Campaigns
                getExistingCampaigns();

                break;


        }

    }

    //Func - Set Category
    public void setCategory(int clickPosition, String selectedCategory) {

        selectedCategoryPosition = clickPosition;
        categoryClick.setText(selectedCategory);

        fillterAndSetData();

    }

    //Func - Set Language
    public void setLanguage(int clickPosition) {

        selectedLanguagePosition = clickPosition;

        if (clickPosition == 0)
        {
            languageText.setText("ALL");
        }
        else if (clickPosition == 1)
        {
            languageText.setText("EN");
        }
        else if (clickPosition == 2)
        {
            languageText.setText("SP");
        }
        else if (clickPosition == 3)
        {
            languageText.setText("FR");
        }

        fillterAndSetData();



    }

    //Func - Filter and Set Data
    private void fillterAndSetData() {


        progressDialog.setVisibility(View.GONE);
        errorDisplay.setVisibility(View.GONE);
        campaignExistingFragmentContainer.setVisibility(View.VISIBLE);


        boolean isAllCategorySelected = false;
        boolean isAllLanguageSelected = false;

        filteredExistingCampaignOutputList = new ArrayList<>();


        if (selectedCategoryPosition == 0)
        {
            isAllCategorySelected = true;
        }
        if (selectedLanguagePosition == 0)
        {
            isAllLanguageSelected = true;
        }

        if (isAllCategorySelected && isAllLanguageSelected)
        {
            filteredExistingCampaignOutputList = existingCampaignOutputList;
        }
        else if (isAllCategorySelected)
        {
            String selectedLanguageId = null;

            switch (selectedLanguagePosition)
            {

                case 1:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_ENGLISH_ID;

                    break;

                case 2:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_SPANISH_ID;

                    break;

                case 3:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_FRENCH_ID;

                    break;


            }


            for (int index = 0; index < existingCampaignOutputList.size();index++)
            {

                CampaignOutput campaignOutput = existingCampaignOutputList.get(index);

                if (campaignOutput.getLanguage().getId().equals(selectedLanguageId))
                {
                    filteredExistingCampaignOutputList.add(campaignOutput);
                }

            }
        }
        else if (isAllLanguageSelected)
        {
            String selectedCategoryId = null;

            switch (selectedCategoryPosition)
            {

                case 1:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_REALESTATE_ID;

                    break;

                case 2:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_INVESTMENT_ID;

                    break;

            }

            for (int index = 0; index < existingCampaignOutputList.size();index++)
            {

                CampaignOutput campaignOutput = existingCampaignOutputList.get(index);

                if (campaignOutput.getCategory().getId().equals(selectedCategoryId))
                {
                    filteredExistingCampaignOutputList.add(campaignOutput);
                }

            }

        }
        else
        {
            String selectedCategoryId = null;
            String selectedLanguageId = null;

            switch (selectedCategoryPosition)
            {

                case 1:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_REALESTATE_ID;

                    break;

                case 2:

                    selectedCategoryId = Constants.PRODUCT_CATEGORY_INVESTMENT_ID;

                    break;

            }

            switch (selectedLanguagePosition)
            {

                case 1:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_ENGLISH_ID;

                    break;

                case 2:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_SPANISH_ID;

                    break;

                case 3:

                    selectedLanguageId = Constants.CAMPAIGN_LANGUAGE_FRENCH_ID;

                    break;


            }

            for (int index = 0; index < existingCampaignOutputList.size();index++)
            {

                CampaignOutput campaignOutput = existingCampaignOutputList.get(index);

                if (campaignOutput.getCategory().getId().equals(selectedCategoryId) && campaignOutput.getLanguage().getId().equals(selectedLanguageId))
                {
                    filteredExistingCampaignOutputList.add(campaignOutput);
                }

            }

        }

        Log.e("Filtered Size",filteredExistingCampaignOutputList.size()+"");

        if (filteredExistingCampaignOutputList.size() < 4)
        {
            //Setting adapter
            campaignExistingListAdapter = new CampaignExistingListAdapter(getActivity(),filteredExistingCampaignOutputList,CampaignExistingFragment.this);
            existingCampaignList.setAdapter(campaignExistingListAdapter);
            campaignExistingListAdapter.notifyDataSetChanged();

            loadMoreClick.setVisibility(View.GONE);
        }
        else
        {

            List<CampaignOutput> filteredExistingCampaignOutputListTemp = new ArrayList<>();
            filteredExistingCampaignOutputListTemp.add(filteredExistingCampaignOutputList.get(0));
            filteredExistingCampaignOutputListTemp.add(filteredExistingCampaignOutputList.get(1));
            filteredExistingCampaignOutputListTemp.add(filteredExistingCampaignOutputList.get(2));


            //Setting adapter
            campaignExistingListAdapter = new CampaignExistingListAdapter(getActivity(),filteredExistingCampaignOutputListTemp,CampaignExistingFragment.this);
            existingCampaignList.setAdapter(campaignExistingListAdapter);
            campaignExistingListAdapter.notifyDataSetChanged();

            loadMoreClick.setVisibility(View.VISIBLE);


        }





    }

    //Func - Load Full Data
    private void loadFullData() {

        filteredExistingCampaignOutputList = existingCampaignOutputList;

        //Setting adapter
        campaignExistingListAdapter = new CampaignExistingListAdapter(getActivity(),filteredExistingCampaignOutputList,CampaignExistingFragment.this);
        existingCampaignList.setAdapter(campaignExistingListAdapter);
        campaignExistingListAdapter.notifyDataSetChanged();

        loadMoreClick.setVisibility(View.GONE);

    }

    private void shareToMedia(String shareId, CampaignOutput shareCampaignOutput) {

        if (shareId.equals(Constants.SHARE_FACEBOOK_ID))
        {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.orca");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }


            try {

                startActivity(facebookIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignExistingFragmentContainer, "Facebook have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }
        }

        if (shareId.equals(Constants.SHARE_INSTAGRAM_ID))
        {
            Intent instagramIntent = new Intent(Intent.ACTION_SEND);
            instagramIntent.setType("text/plain");
            instagramIntent.setPackage("com.instagram.android");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }



            try {

                startActivity(instagramIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignExistingFragmentContainer, "Instagram have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_TWITTER_ID))
        {
            Intent twitterIntent = new Intent(Intent.ACTION_SEND);
            twitterIntent.setType("text/plain");
            twitterIntent.setPackage("com.twiter.android");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }



            try {

                startActivity(twitterIntent);

            } catch (Exception e) {

                Intent twitterWebIntent = new Intent();

                if (shareCampaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                    }
                    else
                    {
                        twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                    }
                }

                twitterWebIntent.setAction(Intent.ACTION_VIEW);

                if (shareCampaignOutput.getCompaignImage() == null)
                {

                    twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+shareCampaignOutput.getCompaignName())));

                }
                else
                {
                    String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                    if (campaignImageUrl.isEmpty())
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Campaign : "+shareCampaignOutput.getCompaignName())));

                    }
                    else
                    {
                        twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Link : "+campaignImageUrl)));

                    }
                }


                startActivity(twitterWebIntent);

                Snackbar snackbar = Snackbar
                        .make(campaignExistingFragmentContainer, "Twitter have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_WHATSAPP_ID))
        {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");

            if (shareCampaignOutput.getCompaignImage() == null)
            {

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

            }
            else
            {
                String campaignImageUrl = shareCampaignOutput.getCompaignImage();

                if (campaignImageUrl.isEmpty())
                {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Campaign : "+shareCampaignOutput.getCompaignName());

                }
                else
                {
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+campaignImageUrl);

                }
            }

            try {

                startActivity(whatsappIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(campaignExistingFragmentContainer, "Whatsapp have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();
            }
        }
    }

    //Func - Encode Url
    private String urlEncode(String s) {

        try {

            return URLEncoder.encode(s, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return "";
        }
    }


    //Func - Getting Existing Campaigns
    private void getExistingCampaigns() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            errorDisplay.setVisibility(View.GONE);
            campaignExistingFragmentContainer.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetExistingCampaignsService();

        }
        else
        {
            progressDialog.setVisibility(View.GONE);
            campaignExistingFragmentContainer.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Func - Share Campaign
    public void shareCampaign(String shareId, CampaignOutput shareCampaignOutput) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callShareCampaignService(shareId,shareCampaignOutput);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(campaignExistingFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Func - Getting Existing Campaigns
    private void callGetExistingCampaignsService() {


        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<CampaignOutput>> call = (Call<List<CampaignOutput>>) api.getExistingCampaigns(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_EXISTING_CAMPAIGNS);
        call.enqueue(new Callback<List<CampaignOutput>>() {
            @Override
            public void onResponse(Call<List<CampaignOutput>> call, Response<List<CampaignOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {



                    existingCampaignOutputList = response.body();


                    if (existingCampaignOutputList.size() == 0)
                    {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        campaignExistingFragmentContainer.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                        errorDisplayText.setText(getString( R.string.error_no_data_existing_campaigns));
                        errorDisplayTryClick.setVisibility(View.GONE);


                    }
                    else {


                        fillterAndSetData();

                    }

                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    campaignExistingFragmentContainer.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CampaignOutput>> call, Throwable t) {


                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    campaignExistingFragmentContainer.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_campaign);
                    errorDisplayText.setText(getString( R.string.error_no_data_existing_campaigns));
                    errorDisplayTryClick.setVisibility(View.GONE);
                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    campaignExistingFragmentContainer.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_server);
                    errorDisplayText.setText(getString(R.string.error_server));
                }



            }

        });


    }

    //Service - Share Campaign
    private void callShareCampaignService(final String shareId, final CampaignOutput shareCampaignOutput) {

        //Showing loading
        progressDialogSecond.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        ShareCampaignInput shareCampaignInput = new ShareCampaignInput();

        UserId userIdObject = new UserId();
        userIdObject.setUserId(userId);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);

        CampaignId campaignIdObject = new CampaignId();
        campaignIdObject.setCompaignId(shareCampaignOutput.getProduct().getProductId());

        Type typeObject = new Type();
        typeObject.setId(shareId);

        shareCampaignInput.setUserId(userIdObject);
        shareCampaignInput.setCreatedBy(createdByObject);
        shareCampaignInput.setCampaignId(campaignIdObject);
        shareCampaignInput.setType(typeObject);



        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.shareCampaigns(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_SHARE_CAMPAIGNS,
                shareCampaignInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    //Share to Medis
                    shareToMedia(shareId,shareCampaignOutput);

                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialogSecond.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(campaignExistingFragmentContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialogSecond.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(campaignExistingFragmentContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
            client.writeTimeout(60000, TimeUnit.MILLISECONDS);
            client.readTimeout(60000, TimeUnit.MILLISECONDS);
            client.connectTimeout(60000, TimeUnit.MILLISECONDS);
            return client;
        }
        return builder;
    }








}
