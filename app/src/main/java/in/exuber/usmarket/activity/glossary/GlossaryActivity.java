package in.exuber.usmarket.activity.glossary;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.alphabetik.Alphabetik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.GlossaryListAdapter;
import in.exuber.usmarket.apimodels.glossary.glossaryoutput.GlossaryOutput;
import in.exuber.usmarket.dialog.GlossaryDialog;
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

import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

public class GlossaryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    //Declaring views
    private LinearLayout glossaryActivityContainer;
    private TextView toolbarHeader;

    private LinearLayout glossaryLayout;
    private ListView glossaryList;

    private SearchView glossarySearch;

    private Alphabetik alphabetik;

    private LinearLayout progressDialog;
    private LinearLayout errorDisplay;

    private ImageView errorDisplayIcon;
    private TextView errorDisplayText;
    private TextView errorDisplayTryClick;


    //Connection detector class
    private ConnectionDetector connectionDetector;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    private List<GlossaryOutput> glossaryOutputList;
    private ArrayList<String> glossaryTermsList;

    private String[] customAlphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};


    //Adapter
    private GlossaryListAdapter glossaryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);


        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);


        //Initialising variables
        glossaryOutputList = new ArrayList<>();
        glossaryTermsList = new ArrayList<>();

        //Initialising views
        glossaryActivityContainer = findViewById(R.id.activity_glossary);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        alphabetik = findViewById(R.id.ai_glossary_albhabetIndex);

        glossaryLayout = findViewById(R.id.ll_glossary_glossaryLayout);
        glossaryList = findViewById(R.id.lv_glossary_glossaryList);
        glossarySearch = findViewById(R.id.et_glossary_search);


        progressDialog =  findViewById(R.id.ll_custom_dialog);
        errorDisplay =  findViewById(R.id.ll_errorMain_layout);


        errorDisplayIcon = findViewById(R.id.iv_errorMain_errorIcon);
        errorDisplayText =  findViewById(R.id.tv_errorMain_errorText);
        errorDisplayTryClick =  findViewById(R.id.tv_errorMain_errorTryAgain);


        glossaryList.setTextFilterEnabled(true);
        setupSearchView();

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.glossary_caps));
        alphabetik.setAlphabet(customAlphabet);


        //setting onclick
        errorDisplayTryClick.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dummy, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_errorMain_errorTryAgain:

                //Hiding Keyboard
                hideKeyBoard(GlossaryActivity.this);

                //Get Glossary
                getGlossary();

                break;

        }


    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        glossaryListAdapter.getFilter().filter(query.toLowerCase());
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get Glossary
        getGlossary();
    }

    //Func - Set Search view
    private void setupSearchView()
    {

        glossarySearch.setOnQueryTextListener(this);
    }


    //Func - Set clicked position
    private int getPositionFromData(String character) {

        int position = 0;

        for (String termsString : glossaryTermsList) {

            String letter = "" + termsString.charAt(0);
            if (letter.equals("" + character))
            {
                return position;
            }

            position++;
        }
        return 0;
    }

    //Func - Show Details
    public void showGlossaryDetails(GlossaryOutput glossaryOutput) {

        //Calling dialog
        FragmentManager glossaryManager = getFragmentManager();
        GlossaryDialog glossaryDialog = new GlossaryDialog(glossaryOutput);
        glossaryDialog.show(glossaryManager, "GLOSSARY_DIALOG");


    }

    //Func - Get Glossary
    private void getGlossary() {

        errorDisplayTryClick.setVisibility(View.VISIBLE);

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {


            errorDisplay.setVisibility(View.GONE);
            glossaryLayout.setVisibility(View.GONE);

            progressDialog.setVisibility(View.VISIBLE);

            //Calling Service
            callGetGlossaryService();

        }
        else
        {
            //Hiding views
            progressDialog.setVisibility(View.GONE);
            glossaryLayout.setVisibility(View.GONE);

            errorDisplay.setVisibility(View.VISIBLE);

            errorDisplayIcon.setImageResource(R.drawable.ic_error_internet);
            errorDisplayText.setText(getString(R.string.error_internet));
        }
    }

    //Service - Get Glossary
    private void callGetGlossaryService() {

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<List<GlossaryOutput>> call = (Call<List<GlossaryOutput>>) api.getGlossary(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_GET_GLOSSARY);
        call.enqueue(new Callback<List<GlossaryOutput>>() {
            @Override
            public void onResponse(Call<List<GlossaryOutput>> call, Response<List<GlossaryOutput>> response) {

                //Checking for response code
                if (response.code() == 200 ) {

                    glossaryOutputList = response.body();


                    if (glossaryOutputList.size() == 0)
                    {
                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        glossaryLayout.setVisibility(View.GONE);

                        errorDisplay.setVisibility(View.VISIBLE);

                        errorDisplayIcon.setImageResource(R.drawable.ic_glossary_primary);
                        errorDisplayText.setText(getString( R.string.error_no_data_glossary));
                        errorDisplayTryClick.setVisibility(View.GONE);



                    }
                    else {

                        //Hiding views
                        progressDialog.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.GONE);

                        glossaryLayout.setVisibility(View.VISIBLE);

                        glossaryTermsList = new ArrayList<>();

                        for (int i = 0; i < glossaryOutputList.size(); i++) {

                            GlossaryOutput glossaryOutput = glossaryOutputList.get(i);
                            glossaryTermsList.add(glossaryOutput.getTerm());

                        }

                        //Setting adapter
                        glossaryListAdapter = new GlossaryListAdapter(GlossaryActivity.this,glossaryOutputList);
                        glossaryList.setAdapter(glossaryListAdapter);
                        glossaryListAdapter.notifyDataSetChanged();

                        alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                            @Override
                            public void onItemClick(View view, int position, String character) {
                                String info = " Position = " + position + " Char = " + character;
                                Log.e("View: ", view + "," + info);
                                glossaryList.smoothScrollToPosition(getPositionFromData(character));
                            }
                        });
                    }


                }
                //If status code is not 200
                else
                {


                    progressDialog.setVisibility(View.GONE);
                    glossaryLayout.setVisibility(View.GONE);


                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_error_code);
                    errorDisplayText.setText(getString(R.string.error_response_code) + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<GlossaryOutput>> call, Throwable t) {

                Log.e("Failure",t.toString());

                if (t instanceof IOException) {

                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    glossaryLayout.setVisibility(View.GONE);

                    errorDisplay.setVisibility(View.VISIBLE);

                    errorDisplayIcon.setImageResource(R.drawable.ic_glossary_primary);
                    errorDisplayText.setText(getString( R.string.error_no_data_glossary));
                    errorDisplayTryClick.setVisibility(View.GONE);

                }
                else
                {
                    //Hiding views
                    progressDialog.setVisibility(View.GONE);
                    glossaryLayout.setVisibility(View.GONE);


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
