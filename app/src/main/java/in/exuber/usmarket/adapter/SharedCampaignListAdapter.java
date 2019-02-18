package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.sharedcampaigndetail.SharedCampaignDetailActivity;
import in.exuber.usmarket.apimodels.sharedcampaign.sharedcampaignoutput.SharedCampaignOutput;
import in.exuber.usmarket.utils.Constants;


public class SharedCampaignListAdapter extends RecyclerView.Adapter<SharedCampaignListAdapter.ViewHolder> implements Filterable {

    //Declaring variables
    private Context context;
    private List<SharedCampaignOutput> campaignOutputList;
    private List<SharedCampaignOutput> filteredCampaignOutputList;


    public SharedCampaignListAdapter(Context context, List<SharedCampaignOutput> campaignOutputList) {

        this.context = context;
        this.campaignOutputList = campaignOutputList;
        this.filteredCampaignOutputList = campaignOutputList;
    }

    @Override
    public SharedCampaignListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sharedcampaign_listadapter, parent, false);
        return new SharedCampaignListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SharedCampaignListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.campaignName.setText(filteredCampaignOutputList.get(position).getCampaignId().getCompaignName());


        holder.campaignProduct.setText(filteredCampaignOutputList.get(position).getCampaignId().getProduct().getProductName());

        if (filteredCampaignOutputList.get(position).getCampaignId().getCategory() != null)
        {
            holder.campaignCategory.setText(filteredCampaignOutputList.get(position).getCampaignId().getCategory().getName());

            holder.campaignCategoryLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.campaignCategoryLayout.setVisibility(View.GONE);
        }


        if (filteredCampaignOutputList.get(position).getCampaignId().getLanguage() != null)
        {
            holder.campaignLanguage.setText(filteredCampaignOutputList.get(position).getCampaignId().getLanguage().getName());

            holder.campaignLanguageLayout.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.campaignLanguageLayout.setVisibility(View.GONE);
        }


        if (filteredCampaignOutputList.get(position).getCampaignId().getCompaignImage() == null)
        {

            holder.campaignThumbnail.setImageResource(R.drawable.ic_no_image);


        }
        else
        {
            String campaignImageUrl = filteredCampaignOutputList.get(position).getCampaignId().getCompaignImage();

            if (campaignImageUrl.isEmpty())
            {
                holder.campaignThumbnail.setImageResource(R.drawable.ic_no_image);

            }
            else
            {
                Picasso.get()
                        .load(campaignImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(holder.campaignThumbnail);



            }
        }



    }

    @Override
    public int getItemCount() {

        return filteredCampaignOutputList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredCampaignOutputList = campaignOutputList;
                }
                else {



                    List<SharedCampaignOutput> filteredList = new ArrayList<>();

                    for (SharedCampaignOutput sharedCampaignOutput : campaignOutputList) {

                        if (sharedCampaignOutput.getCampaignId().getCompaignName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(sharedCampaignOutput);
                            continue;
                        }

                        if (sharedCampaignOutput.getCampaignId().getCategory() != null)
                        {
                            if (sharedCampaignOutput.getCampaignId().getCategory().getName().toLowerCase().contains(charString.toLowerCase()))
                            {
                                filteredList.add(sharedCampaignOutput);
                                continue;
                            }
                        }

                        if (sharedCampaignOutput.getCampaignId().getLanguage() != null)
                        {
                            if (sharedCampaignOutput.getCampaignId().getLanguage().getName().toLowerCase().contains(charString.toLowerCase()))
                            {
                                filteredList.add(sharedCampaignOutput);
                                continue;
                            }
                        }

                    }

                    filteredCampaignOutputList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCampaignOutputList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCampaignOutputList = new ArrayList<>();
                filteredCampaignOutputList = (List<SharedCampaignOutput>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView campaignName, campaignProduct, campaignCategory, campaignLanguage;
        private ImageView campaignThumbnail;
        private LinearLayout campaignCategoryLayout, campaignLanguageLayout;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            campaignName = view.findViewById(R.id.tv_sharedCampaignList_campaignName);
            campaignProduct = view.findViewById(R.id.tv_sharedCampaignList_campaignProduct);
            campaignCategory = view.findViewById(R.id.tv_sharedCampaignList_campaignCategory);
            campaignLanguage = view.findViewById(R.id.tv_sharedCampaignList_campaignLanguage);

            campaignThumbnail = view.findViewById(R.id.iv_sharedCampaignList_campaignThumbnail);

            campaignCategoryLayout = view.findViewById(R.id.ll_sharedCampaignList_campaignCategoryLayout);
            campaignLanguageLayout = view.findViewById(R.id.ll_sharedCampaignList_campaignLanguageLayout);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();
            SharedCampaignOutput campaignOutput = filteredCampaignOutputList.get(clickPosition);

            //Converting to string
            String campaignItemString = gson.toJson(campaignOutput);

            //Preparing Intent
            Intent campaignDetailsIntent = new Intent(view.getContext(), SharedCampaignDetailActivity.class);

            //Create the bundle
            Bundle campaignDetailBundle = new Bundle();

            //Add your data to bundle
            campaignDetailBundle.putString(Constants.INTENT_KEY_SELECTED_CAMPAIGN,campaignItemString);

            //Add the bundle to the intent
            campaignDetailsIntent.putExtras(campaignDetailBundle);

            view.getContext().startActivity(campaignDetailsIntent);



        }

    }

}