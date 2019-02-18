package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
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
import in.exuber.usmarket.activity.productcampaigndetails.ProductCampaignDetailActivity;
import in.exuber.usmarket.apimodels.campaign.campaignoutput.CampaignOutput;
import in.exuber.usmarket.fragment.campaign.CampaignNewFragment;
import in.exuber.usmarket.utils.Constants;


public class CampaignProductListAdapter extends RecyclerView.Adapter<CampaignProductListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<CampaignOutput> campaignOutputList;


    public CampaignProductListAdapter(Context context, List<CampaignOutput> campaignOutputList) {

        this.context = context;
        this.campaignOutputList = campaignOutputList;

    }

    @Override
    public CampaignProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campaignproduct_listadapter, parent, false);
        return new CampaignProductListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CampaignProductListAdapter.ViewHolder holder, final int position) {

        //Setting values
        holder.campaignName.setText(campaignOutputList.get(position).getCompaignName());
        holder.campaignProduct.setText(campaignOutputList.get(position).getProduct().getProductName());
        holder.campaignCategory.setText(campaignOutputList.get(position).getCategory().getName());
        holder.campaignLanguage.setText(campaignOutputList.get(position).getLanguage().getName());

        if (campaignOutputList.get(position).getCompaignImage() == null)
        {

            holder.campaignThumbnail.setVisibility(View.GONE);

        }
        else
        {
            String campaignImageUrl = campaignOutputList.get(position).getCompaignImage();

            if (campaignImageUrl.isEmpty())
            {
                holder.campaignThumbnail.setVisibility(View.GONE);

            }
            else
            {
                Picasso.get()
                        .load(campaignImageUrl.replace(" ","%20"))
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
            parentLayout = view.findViewById(R.id.ll_campaignProduct_parentLayout);
            campaignName = view.findViewById(R.id.tv_campaignProduct_campaignName);
            campaignProduct = view.findViewById(R.id.tv_campaignProduct_campaignProduct);
            campaignCategory = view.findViewById(R.id.tv_campaignProduct_campaignCategory);
            campaignLanguage = view.findViewById(R.id.tv_campaignProduct_campaignLanguage);

            campaignThumbnail = view.findViewById(R.id.iv_campaignProduct_campaignThumbnail);

            facebookClick = view.findViewById(R.id.iv_campaignProduct_facebookShareClick);
            instagramClick = view.findViewById(R.id.iv_campaignProduct_instagramShareClick);
            twitterClick = view.findViewById(R.id.iv_campaignProduct_twitterShareClick);
            whatsappClick = view.findViewById(R.id.iv_campaignProduct_whatsappShareClick);

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
            CampaignOutput campaignOutput = campaignOutputList.get(clickPosition);

            switch (view.getId())
            {
                case R.id.ll_campaignProduct_parentLayout:

                    Gson gson = new Gson();


                    //Converting to string
                    String campaignItemString = gson.toJson(campaignOutput);

                    //Preparing Intent
                    Intent campaignDetailsIntent = new Intent(view.getContext(), CampaignDetailActivity.class);

                    //Create the bundle
                    Bundle campaignDetailBundle = new Bundle();

                    //Add your data to bundle
                    campaignDetailBundle.putString(Constants.INTENT_KEY_SELECTED_CAMPAIGN,campaignItemString);

                    //Add the bundle to the intent
                    campaignDetailsIntent.putExtras(campaignDetailBundle);

                    view.getContext().startActivity(campaignDetailsIntent);

                    break;

                case R.id.iv_campaignProduct_facebookShareClick:


                    ((ProductCampaignDetailActivity)context).shareCampaign(Constants.SHARE_FACEBOOK_ID,campaignOutput);

                    break;

                case R.id.iv_campaignProduct_instagramShareClick:

                    ((ProductCampaignDetailActivity)context).shareCampaign(Constants.SHARE_INSTAGRAM_ID,campaignOutput);


                    break;

                case R.id.iv_campaignProduct_twitterShareClick:

                    ((ProductCampaignDetailActivity)context).shareCampaign(Constants.SHARE_TWITTER_ID,campaignOutput);


                    break;

                case R.id.iv_campaignProduct_whatsappShareClick:

                    ((ProductCampaignDetailActivity)context).shareCampaign(Constants.SHARE_WHATSAPP_ID,campaignOutput);


                    break;


            }

        }

    }


}