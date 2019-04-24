package in.exuber.usmarket.fragment.profile;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.faq.FaqActivity;
import in.exuber.usmarket.activity.glossary.GlossaryActivity;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.activity.paidcommissions.PaidCommissionsActivity;
import in.exuber.usmarket.activity.paymentinfo.Payment_Info_Activity;
import in.exuber.usmarket.activity.profileagreements.ProfileAgreementsActivity;
import in.exuber.usmarket.activity.profileedit.ProfileEditActivity;
import in.exuber.usmarket.libraries.circularimageview.CircleImageView;
import in.exuber.usmarket.models.ProfilePicModel.ProfileImageModel;
import in.exuber.usmarket.models.profileReportdata.ProfileReportOutputList;
import in.exuber.usmarket.models.user.UserOutput;
import in.exuber.usmarket.utils.Api;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;
import in.exuber.usmarket.utils.UtilMethods;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static in.exuber.usmarket.utils.UtilMethods.ShrinkBitmap;
import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHomeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout profileHomeFragmentContainer;

    private RelativeLayout toolbarNotificationClick;
    private LinearLayout toolbarEditClick;

    private CircleImageView userImage;
    private TextView profileName;
    private TextView myLeadsCount, convertedLeadsCount;
    private TextView myProductsCount, campaignsSharedCount;

    private TextView email, phoneNumber, addressOne,addressTwo, cityState, postalCode, countryRegion;

    private LinearLayout helpClick, paidcommissionClick,paymentinfoClick, glossaryClick, faqClick, logoutClick;


    private Bitmap imageForUpload;
    private String captured_image;
    private File file;
    ProgressDialog pd;
    String url;
    private static final int  PERMISSIONS_REQUEST_ACCOUNTS = 1090;
    private static OkHttpClient.Builder builder;

    //Connection detector class
    private ConnectionDetector connectionDetector;


    //Sharedpreferences
    private SharedPreferences marketPreference;
    SharedPreferences.Editor preferenceEditor;

    //Declarinf variables
    private UserOutput userOutput;

    private ArrayList<ProfileReportOutputList> profileReportOutputLists;


    public ProfileHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView =  inflater.inflate(R.layout.fragment_profile_home, container, false);

        //Initialising connection detector
        connectionDetector = new ConnectionDetector(getActivity());

        //Hiding keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference =  getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        preferenceEditor=marketPreference.edit();
        preferenceEditor.apply();

        //Initialising variables
        userOutput = new UserOutput();

        //Initialising variables
        profileReportOutputLists = new ArrayList<>();

        //Initialising views
        profileHomeFragmentContainer = profileView.findViewById(R.id.fragment_profile_home);

        toolbarNotificationClick = profileView.findViewById(R.id.rl_homeProfile_toolBar_notificationLayout);
        toolbarEditClick = profileView.findViewById(R.id.ll_homeProfile_toolBar_editClick);

        userImage = profileView.findViewById(R.id.iv_profileHomeFragment_userImage);
        profileName = profileView.findViewById(R.id.tv_profileHomeFragment_profileName);

        myLeadsCount = profileView.findViewById(R.id.tv_profileHomeFragment_myLeadsCount);
        convertedLeadsCount = profileView.findViewById(R.id.tv_profileHomeFragment_convertedLeadsCount);
        myProductsCount = profileView.findViewById(R.id.tv_profileHomeFragment_myProductsCount);
        campaignsSharedCount = profileView.findViewById(R.id.tv_profileHomeFragment_campaignsSharedCount);

        email = profileView.findViewById(R.id.tv_profileHomeFragment_email);
        phoneNumber = profileView.findViewById(R.id.tv_profileHomeFragment_phoneNumber);
        addressOne = profileView.findViewById(R.id.tv_profileHomeFragment_addressOne);
        addressTwo = profileView.findViewById(R.id.tv_profileHomeFragment_addressTwo);
        cityState = profileView.findViewById(R.id.tv_profileHomeFragment_cityState);
        postalCode = profileView.findViewById(R.id.tv_profileHomeFragment_postalCode);
        countryRegion = profileView.findViewById(R.id.tv_profileHomeFragment_countryRegion);

        paidcommissionClick = profileView.findViewById(R.id.ll_profileHomeFragment_paidCommissionClick);
        paymentinfoClick=profileView.findViewById(R.id.ll_profileHomeFragment_paymentinfoClick);
        logoutClick = profileView.findViewById(R.id.ll_profileHomeFragment_logoutClick);

        Log.e("Country profile edit",marketPreference.getString("Country", ""));

        Locale loc = new Locale("",marketPreference.getString("Country", ""));
        //loc.getDisplayCountry();

        email.setText(marketPreference.getString("email", ""));
        phoneNumber.setText(marketPreference.getString("phoneNoCode", "") + " " + marketPreference.getString("phoneNo", ""));
        addressOne.setText(marketPreference.getString("streetaddress1", ""));

        if (marketPreference.getString("streetaddress2", "").equals("null") || marketPreference.getString("streetaddress2", "").equals("")){
            addressTwo.setVisibility(View.GONE);
        }
        else {
            addressTwo.setVisibility(View.VISIBLE);
            addressTwo.setText(marketPreference.getString("streetaddress2", ""));
        }

        cityState.setText(marketPreference.getString("City", "") + ", " + marketPreference.getString("State", ""));
        postalCode.setText(marketPreference.getString("PostalCode", "") + " - " + loc.getDisplayCountry());



        //Setting values
        profileName.setText(marketPreference.getString("userName", "") + " " + marketPreference.getString("userLastName", ""));



        //Setting onclick listner
        toolbarNotificationClick.setOnClickListener(this);
        toolbarEditClick.setOnClickListener(this);

        paidcommissionClick.setOnClickListener(this);
        paymentinfoClick.setOnClickListener(this);
        logoutClick.setOnClickListener(this);


        return profileView;
    }




    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.rl_homeProfile_toolBar_notificationLayout:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                //Calling Notification Activity
                startActivity(new Intent(getActivity(), NotificationActivity.class));

                break;


            case R.id.ll_homeProfile_toolBar_editClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                Gson gson = new Gson();

                //Converting to string
                String userItemString = gson.toJson(userOutput);

                //Preparing Intent
                Intent profileEditIntent = new Intent(getContext(), ProfileEditActivity.class);

                //Create the bundle
                Bundle profileEditBundle = new Bundle();

                //Add your data to bundle
                profileEditBundle.putString("selectedUser",userItemString);

                //Add the bundle to the intent
                profileEditIntent.putExtras(profileEditBundle);

                startActivity(profileEditIntent);

                break;

            case R.id.ll_profileHomeFragment_paidCommissionClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());
                boolean isInternetPresent = connectionDetector.isConnectingToInternet();

                if (isInternetPresent) {

                    Intent intentcommissions=new Intent(getActivity(), PaidCommissionsActivity.class);
                    startActivity(intentcommissions);

                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(profileHomeFragmentContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }


                break;

            case R.id.ll_profileHomeFragment_paymentinfoClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());
                Intent intentpaymentinfo=new Intent(getActivity(), Payment_Info_Activity.class);
                startActivity(intentpaymentinfo);

                break;

            case R.id.ll_profileHomeFragment_logoutClick:

                //Hiding Keyboard
                hideKeyBoard(getActivity());

                logoutApp();

                break;


        }
    }




    //Func - Log out App
    private void logoutApp() {

        String storedEmail = null;
        String storedPassword = null;

        //Checking Remember me
        boolean rememberMePrevoius = marketPreference.getBoolean(Constants.IS_REMEMBER_ME_SELECTED, false);

        if (rememberMePrevoius)
        {
            storedEmail = marketPreference.getString(Constants.REMEMBER_ME_EMAIL, "");
            storedPassword = marketPreference.getString(Constants.REMEMBER_ME_PASSWORD, "");

        }


        //Clear session
        SharedPreferences.Editor preferenceEditor = marketPreference.edit();
        preferenceEditor.clear();
        preferenceEditor.commit();


        if (rememberMePrevoius)
        {
            preferenceEditor.putBoolean(Constants.IS_REMEMBER_ME_SELECTED, true);
            preferenceEditor.putString(Constants.REMEMBER_ME_EMAIL,storedEmail);
            preferenceEditor.putString(Constants.REMEMBER_ME_PASSWORD,storedPassword);

            preferenceEditor.commit();

        }



        //Calling LoginSignUp activity
        Intent loginSignupIntent = new Intent(getContext(), LoginSignupActivity.class);
        loginSignupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginSignupIntent);

        //Animation..........
        getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);


    }



    @Override
    public void onResume() {
        super.onResume();
        String url=marketPreference.getString("userProfilePic","");

        if (url == null)
        {

            userImage.setImageResource(R.drawable.user_profile);

        }
        else
        {
            if (url.isEmpty())
            {
                userImage.setImageResource(R.drawable.user_profile);

            }
            else
            {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.user_profile)
                        .error(R.drawable.user_profile)
                        .into(userImage);

            }
        }


        setAdapterData();

    }

    public void onBackPressed() {

        getActivity().finishAffinity();


    }




    //Func - Set Adapter Data
    private void setAdapterData() {

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

        asyncHttpClient.addHeader("service", Constants.APP_PROFILE_INFO_DISPLAY_SERVICE_NAME);
        Log.e("FAQ_service_name", Constants.APP_PROFILE_INFO_DISPLAY_SERVICE_NAME);

        asyncHttpClient.get(null, Constants.APP_PROFILE_INFO_DISPLAY_API + marketPreference.getString("userid", "") + "/", null, "application/json", new AsyncHttpResponseHandler() {
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
                Log.v("ProfileData_Response", Response);
                pd.dismiss();

                Log.v("ProfileData_Status_code", statusCode+"");

                if (statusCode==200) {

                    try {

                        JSONObject jsonobject = new JSONObject(Response);

                        String MyLeads=jsonobject.getString("myLeads");
                        myLeadsCount.setText(MyLeads);

                        String MyConvertedLeads=jsonobject.getString("myConLeads");
                        convertedLeadsCount.setText(MyConvertedLeads);

                        String MyProduct=jsonobject.getString("myProduct");
                        myProductsCount.setText(MyProduct);

                        String MySharedCampaign=jsonobject.getString("mySharedCamp");
                        campaignsSharedCount.setText(MySharedCampaign);



                        profileReportOutputLists.add(new ProfileReportOutputList(MyLeads,MyConvertedLeads,MyProduct,MySharedCampaign));

                        Log.d("TotalLeads",MyLeads);
                        Log.d("TotalSharedCampaign",MySharedCampaign);

                        preferenceEditor.commit();


                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {

                    pd.dismiss();
                    Toast.makeText(getActivity(), "Not Successfull", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                pd.dismiss();
                Log.e("ProfileData_Error",error.toString());
                Log.e("ProfileData_Errorstatus", String.valueOf(statusCode));

            }

        });

    }
}
