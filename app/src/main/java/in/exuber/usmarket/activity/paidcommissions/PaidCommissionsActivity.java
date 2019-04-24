package in.exuber.usmarket.activity.paidcommissions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.roughike.bottombar.BottomBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaignshared.CampaignSharedActivity;
import in.exuber.usmarket.activity.faq.FaqActivity;
import in.exuber.usmarket.activity.filters.FiltersActivity;
import in.exuber.usmarket.activity.paymentinfo.Payment_Info_Activity;
import in.exuber.usmarket.adapter.FaqListAdapter;
import in.exuber.usmarket.adapter.PaidCommissionListAdapter;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.models.faq.FaqOutput;
import in.exuber.usmarket.models.paidcommision.PaidCommissionOutput;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;

public class PaidCommissionsActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_paidCommission_Parent;
    FrameLayout fl_paidcommission_frameLayout;

    private TextView toolbarHeader;
    private SearchView et_paidCommission_search;

    //Sharedpreferences
    private SharedPreferences marketPreference;
    SharedPreferences.Editor preferenceEditor;

    //Connection detector class
    private ConnectionDetector connectionDetector;

    RecyclerView recycList_PaidCommision;

    TextView txt_date,txt_TotalPrice,txt_Filter;
    LinearLayout txt_total,ll_Previous,ll_Next;

    ProgressDialog pd;

    int currentleadindex=0;

    int currentpage;

    //Adapter
    private PaidCommissionListAdapter paidCommissionListAdapter;

    private ArrayList<PaidCommissionOutput> paidCommissionOutputs;



    private FrameLayout homeFrameLayout;
    private BottomBar homeBotoomBar;


    public static int TOTAL_NUM_ITEMS;
    public static int ITEM_PER_PAGE;
    public static int ITEM_REMAINING;
    public static int LAST_PAGE;

    public static int currentPage=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_commissions);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  PaidCommissionsActivity.this.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        preferenceEditor=marketPreference.edit();
        preferenceEditor.apply();

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        //Initialising variables
        paidCommissionOutputs = new ArrayList<>();

        ll_paidCommission_Parent=findViewById(R.id.ll_paidCommission_Parent);
        fl_paidcommission_frameLayout=findViewById(R.id.fl_paidcommission_frameLayout);


        TOTAL_NUM_ITEMS=paidCommissionOutputs.size();
        ITEM_PER_PAGE=2;
        ITEM_REMAINING=TOTAL_NUM_ITEMS % ITEM_PER_PAGE;
        LAST_PAGE=TOTAL_NUM_ITEMS / ITEM_PER_PAGE;

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.paid_commissions_caps));


        recycList_PaidCommision=findViewById(R.id.recycList_PaidCommision);
        recycList_PaidCommision.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProducts = new LinearLayoutManager(this);
        linearLayoutManagerProducts.setOrientation(LinearLayoutManager.VERTICAL);
        recycList_PaidCommision.setLayoutManager(linearLayoutManagerProducts);

        txt_total=findViewById(R.id.txt_total);

        txt_date=findViewById(R.id.txt_date);
        txt_TotalPrice=findViewById(R.id.txt_TotalPrice);
        txt_Filter=findViewById(R.id.txt_Filter);
        txt_Filter.setOnClickListener(this);

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = sdf.format(date);
        txt_date.setText(dateString);

        /*ll_Previous=findViewById(R.id.ll_Previous);
        ll_Previous.setOnClickListener(this);
        ll_Next=findViewById(R.id.ll_Next);
        ll_Next.setOnClickListener(this);*/

        et_paidCommission_search=findViewById(R.id.et_paidCommission_search);
        et_paidCommission_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String product) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (paidCommissionListAdapter !=  null) {
                    paidCommissionListAdapter.getFilter().filter(query);
                }

                return false;
            }
        });

        setAdapterData();

    }

    /*private void generatePage(){

        ArrayList<PaidCommissionOutput> paidCommissionOutputs2 = new ArrayList<>();
        ll_Next.setVisibility(View.VISIBLE);
        int startIndex = currentpage*4;
        int endIndex = startIndex+4;

        if (endIndex >= paidCommissionOutputs.size()-1 )
        {
            endIndex = paidCommissionOutputs.size() -1;
            ll_Next.setVisibility(View.GONE);
        }

        Log.e("Start", String.valueOf(startIndex));
        Log.e("End", String.valueOf(endIndex));
        Log.e("ArrayLength", String.valueOf(paidCommissionOutputs.size()));

        for (int i=startIndex; i<=endIndex; i++){
            PaidCommissionOutput pageData=paidCommissionOutputs.get(i);
            paidCommissionOutputs2.add(pageData);
        }


        paidCommissionListAdapter = new PaidCommissionListAdapter(PaidCommissionsActivity.this,paidCommissionOutputs2);
        recycList_PaidCommision.setAdapter(paidCommissionListAdapter);
        paidCommissionListAdapter.notifyDataSetChanged();

    }*/


    //Func - Share Campaign
    public void setAdapterData() {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callPaidCommissionService();

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(ll_paidCommission_Parent, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }



    //Func - Set Adapter Data
    private void callPaidCommissionService() {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(60*1000);
        StringEntity entity = null;
        try {
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (Exception cart) {
            cart.printStackTrace();
        }

        asyncHttpClient.addHeader("accept", "application/json;charset=UTF-8");

        asyncHttpClient.addHeader("auth-token", marketPreference.getString("tokenid", ""));

        asyncHttpClient.addHeader("user-id", marketPreference.getString("userid", ""));

        asyncHttpClient.addHeader("role-id", marketPreference.getString("roleid", ""));

        asyncHttpClient.addHeader("service", Constants.APP_PAID_COMISSION_SERVICE_NAME);
        Log.e("comission_service_name", Constants.APP_PAID_COMISSION_SERVICE_NAME);

        asyncHttpClient.get(null, Constants.APP_PAID_COMISSION_LIST_API + marketPreference.getString("userid", "") + "/", null, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                pd=new ProgressDialog(PaidCommissionsActivity.this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(true);
                pd.setIndeterminate(false);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final String Response = new String(responseBody);
                Log.v("comission_Response", Response);
                pd.dismiss();

                Log.v("comission_Status_code", statusCode+"");

                if (statusCode==200) {

                    try {

                        JSONArray faqjsonArray=new JSONArray(Response);
                        for (int i = 0; i < faqjsonArray.length(); i++) {
                            JSONObject jsonobject = faqjsonArray.getJSONObject(i);

                            JSONObject productObject=jsonobject.getJSONObject("productId");

                            String productId=productObject.getString("id");
                            String productName=productObject.getString("productName");
                            int paid=jsonobject.getInt("paid");
                            int totalAmount=jsonobject.getInt("totalAmount");

                            if (totalAmount == 0)
                            {
                                txt_total.setVisibility(View.GONE);
                            }
                            else
                            {
                                double amount = Double.parseDouble(String.valueOf(totalAmount));
                                DecimalFormat formatter = new DecimalFormat("$#,###,###.00");
                                txt_TotalPrice.setText(formatter.format(amount));

                                txt_total.setVisibility(View.VISIBLE);
                            }

                            //txt_TotalPrice.setText("$" + totalAmount + ".00");



                            paidCommissionOutputs.add(new PaidCommissionOutput(productId,productName,paid,totalAmount));

                            Log.d("TotalCommission", String.valueOf(totalAmount));
                            Log.d("Commission", String.valueOf(paid));

                        }

                        if (paidCommissionOutputs.size() != 0)
                        {
                            //Setting adapter
                            paidCommissionListAdapter = new PaidCommissionListAdapter(PaidCommissionsActivity.this,paidCommissionOutputs);
                            recycList_PaidCommision.setAdapter(paidCommissionListAdapter);
                            paidCommissionListAdapter.notifyDataSetChanged();
                            pd.dismiss();
                        }
                        else
                        {
                            Log.e("Paidcommission","CommissionMSG");
                            pd.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(ll_paidCommission_Parent, R.string.paid_commission, Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //If status code is not 200
                else
                {
                    pd.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(ll_paidCommission_Parent, R.string.error_response_code + statusCode, Snackbar.LENGTH_LONG);

                    snackbar.show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                pd.dismiss();
                Log.e("comission_Error",error.toString());
                Log.e("comission_Errorstatus", String.valueOf(statusCode));

            }

        });

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
        switch (view.getId()){
            case R.id.txt_Filter:
                Intent intent=new Intent(PaidCommissionsActivity.this, FiltersActivity.class);
                startActivity(intent);
                fileList();
                break;

            /*case R.id.ll_Previous:
                if (currentpage >0)
                {
                    currentpage = currentpage -1;
                    generatePage();
                }

                break;
            case R.id.ll_Next:
                currentpage = currentpage+1;
                generatePage();
                break;*/
        }
    }
}
