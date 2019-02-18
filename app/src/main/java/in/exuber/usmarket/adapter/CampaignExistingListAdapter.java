package in.exuber.usmarket.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaigndetail.CampaignDetailActivity;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.fragment.campaign.CampaignExistingFragment;
import in.exuber.usmarket.utils.ConnectionDetector;
import in.exuber.usmarket.utils.Constants;


public class CampaignExistingListAdapter extends RecyclerView.Adapter<CampaignExistingListAdapter.ViewHolder> {

    //Declaring variables
    //Declaring variables
    private Context context;
    private List<CampaignOutput> campaignOutputList;
    private CampaignExistingFragment campaignExistingFragment;

    public CampaignExistingListAdapter(Context context, List<CampaignOutput> campaignOutputList, CampaignExistingFragment campaignExistingFragment) {

        this.context = context;
        this.campaignOutputList = campaignOutputList;
        this.campaignExistingFragment = campaignExistingFragment;
    }

    @Override
    public CampaignExistingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_existingcampaign_listadapter, parent, false);
        return new CampaignExistingListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CampaignExistingListAdapter.ViewHolder holder, final int position) {


        //Setting values
        //Setting values
        holder.campaignName.setText(campaignOutputList.get(position).getCompaignName());
        holder.campaignProduct.setText(campaignOutputList.get(position).getProduct().getProductName());
        holder.campaignCategory.setText(campaignOutputList.get(position).getCategory().getName());
        holder.campaignLanguage.setText(campaignOutputList.get(position).getLanguage().getName());

        if (campaignOutputList.get(position).getCompaignImage() == null) {

            holder.campaignThumbnail.setVisibility(View.GONE);

        } else {
            String campaignImageUrl = campaignOutputList.get(position).getCompaignImage();

            if (campaignImageUrl.isEmpty()) {
                holder.campaignThumbnail.setVisibility(View.GONE);

            } else {
                Picasso.get()
                        .load(campaignImageUrl.replace(" ", "%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(holder.campaignThumbnail);

                holder.campaignThumbnail.setVisibility(View.VISIBLE);

            }
        }




    }

    @Override
    public int getItemCount() {

        return campaignOutputList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView campaignName, campaignProduct, campaignCategory, campaignLanguage;
        private ImageView campaignThumbnail;
        private LinearLayout parentLayout;
        private ImageView facebookClick, instagramClick, twitterClick, whatsappClick;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);


            //Initialising views
            parentLayout = view.findViewById(R.id.ll_existingCampaignList_parentLayout);
            campaignName = view.findViewById(R.id.tv_existingCampaignList_campaignName);
            campaignProduct = view.findViewById(R.id.tv_existingCampaignList_campaignProduct);
            campaignCategory = view.findViewById(R.id.tv_existingCampaignList_campaignCategory);
            campaignLanguage = view.findViewById(R.id.tv_existingCampaignList_campaignLanguage);

            campaignThumbnail = view.findViewById(R.id.iv_existingCampaignList_campaignThumbnail);

            facebookClick = view.findViewById(R.id.iv_existingCampaignList_facebookShareClick);
            instagramClick = view.findViewById(R.id.iv_existingCampaignList_instagramShareClick);
            twitterClick = view.findViewById(R.id.iv_existingCampaignList_twitterShareClick);
            whatsappClick = view.findViewById(R.id.iv_existingCampaignList_whatsappShareClick);

            //Setting OnClick
            parentLayout.setOnClickListener(this);
            facebookClick.setOnClickListener(this);
            instagramClick.setOnClickListener(this);
            twitterClick.setOnClickListener(this);
            whatsappClick.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            Gson gson = new Gson();
            CampaignOutput campaignOutput = campaignOutputList.get(clickPosition);


            switch (view.getId())
            {

                case R.id.ll_existingCampaignList_parentLayout:

                    //Converting to string
                    String campaignItemString = gson.toJson(campaignOutput);

                    //Preparing Intent
                    Intent campaignDetailsIntent = new Intent(view.getContext(), CampaignDetailActivity.class);

                    //Create the bundle
                    Bundle campaignDetailBundle = new Bundle();

                    //Add your data to bundle
                    campaignDetailBundle.putString(Constants.INTENT_KEY_SELECTED_CAMPAIGN, campaignItemString);

                    //Add the bundle to the intent
                    campaignDetailsIntent.putExtras(campaignDetailBundle);

                    view.getContext().startActivity(campaignDetailsIntent);

                    break;

                case R.id.iv_existingCampaignList_facebookShareClick:

                    campaignExistingFragment.shareCampaign(Constants.SHARE_FACEBOOK_ID,campaignOutput);



                    break;

                case R.id.iv_existingCampaignList_instagramShareClick:

                    campaignExistingFragment.shareCampaign(Constants.SHARE_INSTAGRAM_ID,campaignOutput);



                    break;

                case R.id.iv_existingCampaignList_twitterShareClick:

                    campaignExistingFragment.shareCampaign(Constants.SHARE_TWITTER_ID,campaignOutput);


                    break;

                case R.id.iv_existingCampaignList_whatsappShareClick:

                    campaignExistingFragment.shareCampaign(Constants.SHARE_WHATSAPP_ID,campaignOutput);


                    break;
            }




        }

    }


}