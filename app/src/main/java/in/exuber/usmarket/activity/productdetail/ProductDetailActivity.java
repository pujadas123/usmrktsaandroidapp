package in.exuber.usmarket.activity.productdetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.playvideo.PlayVideoActivity;
import in.exuber.usmarket.activity.productcampaigndetails.ProductCampaignDetailActivity;
import in.exuber.usmarket.activity.producttraining.ProductTrainingActivity;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.CreatedBy;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.ProductId;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.ShareProductInput;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.Type;
import in.exuber.usmarket.apimodels.shareproduct.shareproductinput.UserId;
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

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout productDetailActivityContainer;
    private TextView toolbarHeader;

    LinearLayout ll_commission;
    View ll_view;
    private TextView productName, productCategory, productPrice, productCommission, productDescription;

    private LinearLayout thumbnailOneLayout, thumbnailTwoLayout, thumbnailThreeLayout, thumbnailFourLayout;
    private ImageView thumbnailOne, thumbnailTwo, thumbnailThree, thumbnailFour;

    private LinearLayout videoLayout, videoPlayClick;

    private LinearLayout campaignClick,productTrainingClick;

    //Connection Detector
    private ConnectionDetector connectionDetector;

    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring Retrofit log
    private static OkHttpClient.Builder builder;

    //Progress dialog
    private ProgressDialog progressDialog;

    //Declaring variables
    private ProductOutput productOutput;
    private ProductUserOutput productUserOutput;

    private boolean isUserProductPassed;
    private String productItemString;


    private ImageView facebookShareClickImageOne, facebookShareClickImageTwo, facebookShareClickImageThree, facebookShareClickImageFour, facebookShareClickVideo;
    private ImageView instagramShareClickImageOne, instagramShareClickImageTwo, instagramShareClickImageThree, instagramShareClickImageFour, instagramShareClickVideo;
    private ImageView twitterShareClickImageOne, twitterShareClickImageTwo, twitterShareClickImageThree, twitterShareClickImageFour, twitterShareClickVideo;
    private ImageView whatsappShareClickImageOne, whatsappShareClickImageTwo, whatsappShareClickImageThree, whatsappShareClickImageFour, whatsappShareClickVideo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Hiding keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //Initialising connection detector
        connectionDetector = new ConnectionDetector(this);

        //Initialising shared preferences
        marketPreference =  getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);

        //Initialising progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loader_caption));
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);


        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Initialising variables
        productOutput = new ProductOutput();
        productUserOutput = new ProductUserOutput();


        //Initialising views
        productDetailActivityContainer = findViewById(R.id.activity_product_detail);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        ll_commission=findViewById(R.id.ll_commission);
        ll_view=findViewById(R.id.ll_view);

        productName = findViewById(R.id.tv_productDetail_productName);
        productCategory = findViewById(R.id.tv_productDetail_productCategory);
        productPrice = findViewById(R.id.tv_productDetail_productPrice);
        productCommission = findViewById(R.id.tv_productDetail_productCommision);
        productDescription = findViewById(R.id.tv_productDetail_productDescription);

        thumbnailOneLayout = findViewById(R.id.ll_product_detailsThumbnail_oneLayout);
        thumbnailTwoLayout = findViewById(R.id.ll_product_detailsThumbnail_twoLayout);
        thumbnailThreeLayout = findViewById(R.id.ll_product_detailsThumbnail_threeLayout);
        thumbnailFourLayout = findViewById(R.id.ll_product_detailsThumbnail_fourLayout);


        thumbnailOne = findViewById(R.id.iv_product_detailsThumbnail_one);
        thumbnailTwo = findViewById(R.id.iv_product_detailsThumbnail_two);
        thumbnailThree = findViewById(R.id.iv_product_detailsThumbnail_three);
        thumbnailFour = findViewById(R.id.iv_product_detailsThumbnail_four);

        videoLayout = findViewById(R.id.ll_product_detailsVideo_videoLayout);
        videoPlayClick = findViewById(R.id.ll_product_detailsVideo_playVideoClick);

        campaignClick = findViewById(R.id.ll_productDetail_campaignsClick);
        productTrainingClick = findViewById(R.id.ll_productDetail_productTrainingClick);


        facebookShareClickImageOne = findViewById(R.id.iv_product_detailsThumbnail_facebookShareClickOne);
        facebookShareClickImageTwo = findViewById(R.id.iv_product_detailsThumbnail_facebookShareClickTwo);
        facebookShareClickImageThree = findViewById(R.id.iv_product_detailsThumbnail_facebookShareClickThree);
        facebookShareClickImageFour = findViewById(R.id.iv_product_detailsThumbnail_facebookShareClickFour);
        facebookShareClickVideo = findViewById(R.id.iv_product_detailsVideo_facebookShareClick);


        instagramShareClickImageOne = findViewById(R.id.iv_product_detailsThumbnail_instagramShareClickOne);
        instagramShareClickImageTwo = findViewById(R.id.iv_product_detailsThumbnail_instagramShareClickTwo);
        instagramShareClickImageThree = findViewById(R.id.iv_product_detailsThumbnail_instagramShareClickThree);
        instagramShareClickImageFour = findViewById(R.id.iv_product_detailsThumbnail_instagramShareClickFour);
        instagramShareClickVideo = findViewById(R.id.iv_product_detailsVideo_instagramShareClick);


        twitterShareClickImageOne = findViewById(R.id.iv_product_detailsThumbnail_twitterShareClickOne);
        twitterShareClickImageTwo = findViewById(R.id.iv_product_detailsThumbnail_twitterShareClickTwo);
        twitterShareClickImageThree = findViewById(R.id.iv_product_detailsThumbnail_twitterShareClickThree);
        twitterShareClickImageFour = findViewById(R.id.iv_product_detailsThumbnail_twitterShareClickFour);
        twitterShareClickVideo = findViewById(R.id.iv_product_detailsVideo_twitterShareClick);

        whatsappShareClickImageOne = findViewById(R.id.iv_product_detailsThumbnail_whatsappShareClickOne);
        whatsappShareClickImageTwo = findViewById(R.id.iv_product_detailsThumbnail_whatsappShareClickTwo);
        whatsappShareClickImageThree = findViewById(R.id.iv_product_detailsThumbnail_whatsappShareClickThree);
        whatsappShareClickImageFour = findViewById(R.id.iv_product_detailsThumbnail_whatsappShareClickFour);
        whatsappShareClickVideo = findViewById(R.id.iv_product_detailsVideo_whatsappShareClick);

        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.product_details_caps));

        //Getting passed data
        Bundle passedBundle = getIntent().getExtras();
        isUserProductPassed = passedBundle.getBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT);
        productItemString = passedBundle.getString(Constants.INTENT_KEY_SELECTED_PRODUCT);

        Log.e("Is User Product",isUserProductPassed+"");

        if (isUserProductPassed)
        {
            setUserProductData();
        }
        else
        {
            setProductData();
        }


        //Setting onclick
        campaignClick.setOnClickListener(this);
        productTrainingClick.setOnClickListener(this);

        facebookShareClickImageOne.setOnClickListener(this);
        facebookShareClickImageTwo.setOnClickListener(this);
        facebookShareClickImageThree.setOnClickListener(this);
        facebookShareClickImageFour.setOnClickListener(this);
        facebookShareClickVideo.setOnClickListener(this);

        instagramShareClickImageOne.setOnClickListener(this);
        instagramShareClickImageTwo.setOnClickListener(this);
        instagramShareClickImageThree.setOnClickListener(this);
        instagramShareClickImageFour.setOnClickListener(this);
        instagramShareClickVideo.setOnClickListener(this);

        twitterShareClickImageOne.setOnClickListener(this);
        twitterShareClickImageTwo.setOnClickListener(this);
        twitterShareClickImageThree.setOnClickListener(this);
        twitterShareClickImageFour.setOnClickListener(this);
        twitterShareClickVideo.setOnClickListener(this);

        whatsappShareClickImageOne.setOnClickListener(this);
        whatsappShareClickImageTwo.setOnClickListener(this);
        whatsappShareClickImageThree.setOnClickListener(this);
        whatsappShareClickImageFour.setOnClickListener(this);
        whatsappShareClickVideo.setOnClickListener(this);

        videoPlayClick.setOnClickListener(this);



    }




    @Override
    protected void onResume() {
        super.onResume();


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
    public void onBackPressed() {

        finish();

    }


    @Override
    public void onClick(View view) {

        Gson gson = new Gson();


        String imageOneUrl, imageTwoUrl, imageThreeUrl, imageFourUrl, videoUrl;

        if (isUserProductPassed)
        {
            imageOneUrl = productUserOutput.getProductId().getImage1();
        }
        else
        {
            imageOneUrl = productOutput.getImage1();
        }

        if (isUserProductPassed)
        {
            imageTwoUrl = productUserOutput.getProductId().getImage2();
        }
        else
        {
            imageTwoUrl = productOutput.getImage2();
        }

        if (isUserProductPassed)
        {
            imageThreeUrl = productUserOutput.getProductId().getImage3();
        }
        else
        {
            imageThreeUrl = productOutput.getImage3();
        }

        if (isUserProductPassed)
        {
            imageFourUrl = productUserOutput.getProductId().getImage4();
        }
        else
        {
            imageFourUrl = productOutput.getImage4();
        }

        if (isUserProductPassed)
        {
            videoUrl = productUserOutput.getProductId().getVideo1();
        }
        else
        {
            videoUrl = productOutput.getVideo1();
        }



        switch (view.getId()){

            //FACEBOOK
            case R.id.iv_product_detailsThumbnail_facebookShareClickOne:

                //Calling Service
                shareProduct(imageOneUrl,Constants.SHARE_FACEBOOK_ID);

                break;

            case R.id.iv_product_detailsThumbnail_facebookShareClickTwo:

                //Calling Service
                shareProduct(imageTwoUrl,Constants.SHARE_FACEBOOK_ID);

                break;

            case R.id.iv_product_detailsThumbnail_facebookShareClickThree:

                //Calling Service
                shareProduct(imageThreeUrl,Constants.SHARE_FACEBOOK_ID);

                break;

            case R.id.iv_product_detailsThumbnail_facebookShareClickFour:

                //Calling Service
                shareProduct(imageFourUrl,Constants.SHARE_FACEBOOK_ID);

                break;

            case R.id.iv_product_detailsVideo_facebookShareClick:

                //Calling Service
                shareProduct(videoUrl,Constants.SHARE_FACEBOOK_ID);

                break;

            //INSTAGRAM
            case R.id.iv_product_detailsThumbnail_instagramShareClickOne:

                //Calling Service
                shareProduct(imageOneUrl,Constants.SHARE_INSTAGRAM_ID);

                break;

            case R.id.iv_product_detailsThumbnail_instagramShareClickTwo:

                //Calling Service
                shareProduct(imageTwoUrl,Constants.SHARE_INSTAGRAM_ID);

                break;

            case R.id.iv_product_detailsThumbnail_instagramShareClickThree:

                //Calling Service
                shareProduct(imageThreeUrl,Constants.SHARE_INSTAGRAM_ID);

                break;

            case R.id.iv_product_detailsThumbnail_instagramShareClickFour:

                //Calling Service
                shareProduct(imageFourUrl,Constants.SHARE_INSTAGRAM_ID);

                break;

            case R.id.iv_product_detailsVideo_instagramShareClick:

                //Calling Service
                shareProduct(videoUrl,Constants.SHARE_INSTAGRAM_ID);

                break;

            //TWITTER
            case R.id.iv_product_detailsThumbnail_twitterShareClickOne:

                //Calling Service
                shareProduct(imageOneUrl,Constants.SHARE_TWITTER_ID);

                break;

            case R.id.iv_product_detailsThumbnail_twitterShareClickTwo:

                //Calling Service
                shareProduct(imageTwoUrl,Constants.SHARE_TWITTER_ID);

                break;

            case R.id.iv_product_detailsThumbnail_twitterShareClickThree:

                //Calling Service
                shareProduct(imageThreeUrl,Constants.SHARE_TWITTER_ID);

                break;

            case R.id.iv_product_detailsThumbnail_twitterShareClickFour:

                //Calling Service
                shareProduct(imageFourUrl,Constants.SHARE_TWITTER_ID);

                break;

            case R.id.iv_product_detailsVideo_twitterShareClick:

                //Calling Service
                shareProduct(videoUrl,Constants.SHARE_TWITTER_ID);

                break;

            //WHATSAPP
            case R.id.iv_product_detailsThumbnail_whatsappShareClickOne:

                //Calling Service
                shareProduct(imageOneUrl,Constants.SHARE_WHATSAPP_ID);

                break;

            case R.id.iv_product_detailsThumbnail_whatsappShareClickTwo:

                //Calling Service
                shareProduct(imageTwoUrl,Constants.SHARE_WHATSAPP_ID);

                break;

            case R.id.iv_product_detailsThumbnail_whatsappShareClickThree:

                //Calling Service
                shareProduct(imageThreeUrl,Constants.SHARE_WHATSAPP_ID);

                break;

            case R.id.iv_product_detailsThumbnail_whatsappShareClickFour:

                //Calling Service
                shareProduct(imageFourUrl,Constants.SHARE_WHATSAPP_ID);

                break;

            case R.id.iv_product_detailsVideo_whatsappShareClick:

                //Calling Service
                shareProduct(videoUrl,Constants.SHARE_WHATSAPP_ID);

                break;


            case R.id.ll_productDetail_campaignsClick:


                //Preparing Intent
                Intent campaignProductIntent = new Intent(ProductDetailActivity.this, ProductCampaignDetailActivity.class);

                //Create the bundle
                Bundle campaignProductBundle = new Bundle();

                //Add your data to bundle

                campaignProductBundle.putString(Constants.INTENT_KEY_SELECTED_PRODUCT,productItemString);
                campaignProductBundle.putBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT,isUserProductPassed);

                //Add the bundle to the intent
                campaignProductIntent.putExtras(campaignProductBundle);

                startActivity(campaignProductIntent);

                break;

            case R.id.ll_productDetail_productTrainingClick:


                //Preparing Intent
                Intent trainingProductIntent = new Intent(ProductDetailActivity.this, ProductTrainingActivity.class);

                //Create the bundle
                Bundle trainingProductBundle = new Bundle();

                //Add your data to bundle
                trainingProductBundle.putString(Constants.INTENT_KEY_SELECTED_PRODUCT,productItemString);
                trainingProductBundle.putBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT,isUserProductPassed);

                //Add the bundle to the intent
                trainingProductIntent.putExtras(trainingProductBundle);

                startActivity(trainingProductIntent);
                break;

            case R.id.ll_product_detailsVideo_playVideoClick:


                //Preparing Intent
                Intent videoIntent = new Intent(ProductDetailActivity.this, PlayVideoActivity.class);

                //Create the bundle
                Bundle videoBundle = new Bundle();

                //Add your data to bundle
                videoBundle.putString(Constants.INTENT_KEY_SELECTED_VIDEO_URL,videoUrl);

                //Add the bundle to the intent
                videoIntent.putExtras(videoBundle);

                startActivity(videoIntent);

                break;
        }
    }


    //Func - Setting User Product Data
    private void setUserProductData() {

        //Converting string to Object
        Gson gson = new Gson();
        productUserOutput = gson.fromJson(productItemString, ProductUserOutput.class);

        //Setting values
        productName.setText(productUserOutput.getProductId().getProductName());

        if (productUserOutput.getProductId().getProductCategory() == null)
        {
            productCategory.setVisibility(View.GONE);
        }
        else
        {
            productCategory.setText(productUserOutput.getProductId().getProductCategory().getName());
            productCategory.setVisibility(View.VISIBLE);
        }

        /*productPrice.setText(getString(R.string.dollar_sign)+productUserOutput.getProductId().getPrice()+".00");
        productCommission.setText(productUserOutput.getProductId().getCommission()+getString(R.string.commission_tail));*/

        if (productUserOutput.getProductId().getPrice() == null){
            productPrice.setVisibility(View.GONE);
        }
        else
        {
            productPrice.setText(getString(R.string.dollar_sign)+productUserOutput.getProductId().getPrice()+".00");
            productPrice.setVisibility(View.VISIBLE);
        }

        if (productUserOutput.getProductId().getCommission() == null){
            ll_commission.setVisibility(View.GONE);
            ll_view.setVisibility(View.GONE);
        }
        else
        {
            productCommission.setText(productUserOutput.getProductId().getCommission()+getString(R.string.commission_tail));
            ll_commission.setVisibility(View.VISIBLE);
            ll_view.setVisibility(View.VISIBLE);
        }


        if (productUserOutput.getProductId().getDesc() == null)
        {
            productDescription.setVisibility(View.GONE);
        }
        else
        {
            productDescription.setText(productUserOutput.getProductId().getDesc());
            productDescription.setVisibility(View.VISIBLE);
        }


        //Image One
        if (productUserOutput.getProductId().getImage1() == null)
        {

            thumbnailOneLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productUserOutput.getProductId().getImage1();

            if (productImageUrl.isEmpty())
            {
                thumbnailOneLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailOne);

                thumbnailOneLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Two
        if (productUserOutput.getProductId().getImage2() == null)
        {

            thumbnailTwoLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productUserOutput.getProductId().getImage2();

            if (productImageUrl.isEmpty())
            {
                thumbnailTwoLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailTwo);

                thumbnailTwoLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Three
        if (productUserOutput.getProductId().getImage3() == null)
        {

            thumbnailThreeLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productUserOutput.getProductId().getImage3();

            if (productImageUrl.isEmpty())
            {
                thumbnailThreeLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailThree);

                thumbnailThreeLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Four
        if (productUserOutput.getProductId().getImage4() == null)
        {

            thumbnailFourLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productUserOutput.getProductId().getImage4();

            if (productImageUrl.isEmpty())
            {
                thumbnailFourLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailFour);

                thumbnailFourLayout.setVisibility(View.VISIBLE);

            }
        }

        //Video 1
        if (productUserOutput.getProductId().getVideo1() == null) {

            videoLayout.setVisibility(View.GONE);

        }
        else
        {
            String videoUrl = productUserOutput.getProductId().getVideo1();

            if (videoUrl.isEmpty())
            {
                videoLayout.setVisibility(View.GONE);
            }
            else
            {
                videoLayout.setVisibility(View.VISIBLE);

                Log.e("videoUrl",videoUrl);

            }
        }

    }

    //Func - Setting Product Data
    private void setProductData() {

        //Converting string to Object
        Gson gson = new Gson();
        productOutput = gson.fromJson(productItemString, ProductOutput.class);

        //Setting values
        productName.setText(productOutput.getProductName());

        if (productOutput.getProductCategory() == null)
        {
            productCategory.setVisibility(View.GONE);
        }
        else
        {
            productCategory.setText(productOutput.getProductCategory().getName());
            productCategory.setVisibility(View.VISIBLE);
        }

        if (productOutput.getPrice() == null){
            productPrice.setVisibility(View.GONE);
        }
        else
        {
            productPrice.setText(getString(R.string.dollar_sign)+productOutput.getPrice()+".00");
            productPrice.setVisibility(View.VISIBLE);
        }

        if (productOutput.getCommission() == null){
            ll_commission.setVisibility(View.GONE);
            ll_view.setVisibility(View.GONE);
        }
        else
        {
            productCommission.setText(productOutput.getCommission()+getString(R.string.commission_tail));
            ll_commission.setVisibility(View.VISIBLE);
            ll_view.setVisibility(View.VISIBLE);
        }

        if (productOutput.getDesc() == null)
        {
            productDescription.setVisibility(View.GONE);
        }
        else
        {
            productDescription.setText(productOutput.getDesc());
            productDescription.setVisibility(View.VISIBLE);
        }


        //Image One
        if (productOutput.getImage1() == null)
        {

            thumbnailOneLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productOutput.getImage1();

            if (productImageUrl.isEmpty())
            {
                thumbnailOneLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailOne);

                thumbnailOneLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Two
        if (productOutput.getImage2() == null)
        {

            thumbnailTwoLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productOutput.getImage2();

            if (productImageUrl.isEmpty())
            {
                thumbnailTwoLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailTwo);

                thumbnailTwoLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Three
        if (productOutput.getImage3() == null)
        {

            thumbnailThreeLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productOutput.getImage3();

            if (productImageUrl.isEmpty())
            {
                thumbnailThreeLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailThree);

                thumbnailThreeLayout.setVisibility(View.VISIBLE);

            }
        }

        //Image Four
        if (productOutput.getImage4() == null)
        {

            thumbnailFourLayout.setVisibility(View.GONE);
        }
        else
        {
            String productImageUrl = productOutput.getImage4();

            if (productImageUrl.isEmpty())
            {
                thumbnailFourLayout.setVisibility(View.GONE);
            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(thumbnailFour);

                thumbnailFourLayout.setVisibility(View.VISIBLE);

            }
        }

        //Video 1
        if (productOutput.getVideo1() == null) {

            videoLayout.setVisibility(View.GONE);

        }
        else
        {
            String videoUrl = productOutput.getVideo1();

            if (videoUrl.isEmpty())
            {
                videoLayout.setVisibility(View.GONE);
            }
            else
            {
                videoLayout.setVisibility(View.VISIBLE);

                Log.e("videoUrl",videoUrl);

            }
        }
    }

    //Func - Share to Media
    private void shareToMedia(String shareUrl, String shareId) {

        if (shareId.equals(Constants.SHARE_FACEBOOK_ID))
        {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.orca");
            facebookIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+shareUrl);

            try {

                startActivity(facebookIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(productDetailActivityContainer, "Facebook have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }
        }

        if (shareId.equals(Constants.SHARE_INSTAGRAM_ID))
        {
            Intent instagramIntent = new Intent(Intent.ACTION_SEND);
            instagramIntent.setType("text/plain");
            instagramIntent.setPackage("com.instagram.android");
            instagramIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+shareUrl);

            try {

                startActivity(instagramIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(productDetailActivityContainer, "Instagram have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_TWITTER_ID))
        {
            Intent twitterIntent = new Intent(Intent.ACTION_SEND);
            twitterIntent.setType("text/plain");
            twitterIntent.setPackage("com.twiter.android");
            twitterIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+shareUrl);

            try {

                startActivity(twitterIntent);

            } catch (Exception e) {

                Intent twitterWebIntent = new Intent();
                twitterWebIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+shareUrl);
                twitterWebIntent.setAction(Intent.ACTION_VIEW);
                twitterWebIntent.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Please Check The Link : "+shareUrl)));
                startActivity(twitterWebIntent);

                Snackbar snackbar = Snackbar
                        .make(productDetailActivityContainer, "Twitter have not been installed", Snackbar.LENGTH_LONG);

                snackbar.show();

                e.printStackTrace();

            }

        }

        if (shareId.equals(Constants.SHARE_WHATSAPP_ID))
        {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please Check The Link : "+shareUrl);

            try {

                startActivity(whatsappIntent);

            } catch (Exception e) {

                Snackbar snackbar = Snackbar
                        .make(productDetailActivityContainer, "Whatsapp have not been installed", Snackbar.LENGTH_LONG);

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



    //Func - Share Product
    private void shareProduct(String shareUrl, String shareId) {

        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            //Calling Service
            callShareProductService(shareUrl,shareId);

        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(productDetailActivityContainer, R.string.error_internet, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    //Service - Share Product
    private void callShareProductService(final String shareUrl, final String shareId) {

        //Showing loading
        progressDialog.show();

        String accessTokenId = marketPreference.getString(Constants.LOGIN_ACCESSTOKEN_ID, null);
        final String userId = marketPreference.getString(Constants.LOGIN_USER_ID, null);
        String roleId = marketPreference.getString(Constants.LOGIN_ROLE_ID, null);

        ShareProductInput shareProductInput = new ShareProductInput();

        UserId userIdObject = new UserId();
        userIdObject.setUserId(userId);

        CreatedBy createdByObject = new CreatedBy();
        createdByObject.setUserId(userId);

        ProductId productIdObject = new ProductId();
        if (isUserProductPassed)
        {
            productIdObject.setProductId(productUserOutput.getProductId().getProductId());
        }
        else
        {
            productIdObject.setProductId(productOutput.getProductId());
        }

        Type typeObject = new Type();
        typeObject.setId(shareId);

        shareProductInput.setUserId(userIdObject);
        shareProductInput.setCreatedBy(createdByObject);
        shareProductInput.setProductId(productIdObject);
        shareProductInput.setType(typeObject);



        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        final Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = (Call<ResponseBody>) api.shareProducts(accessTokenId,
                userId,
                roleId,
                Constants.SERVICE_SHARE_CAMPAIGNS,
                shareProductInput);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //Checking for response code
                if (response.code() == 201 ) {

                    //Dismiss loading
                    progressDialog.dismiss();

                    //Share to Medis
                    shareToMedia(shareUrl,shareId);

                }
                //If status code is not 201
                else
                {

                    //Dismiss loading
                    progressDialog.dismiss();

                    Snackbar snackbar = Snackbar
                            .make(productDetailActivityContainer, getString(R.string.error_response_code) + response.code(), Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                //Dismiss loading
                progressDialog.dismiss();

                Log.e("Failure",t.toString());

                Snackbar snackbar = Snackbar
                        .make(productDetailActivityContainer, R.string.error_server, Snackbar.LENGTH_LONG);

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
