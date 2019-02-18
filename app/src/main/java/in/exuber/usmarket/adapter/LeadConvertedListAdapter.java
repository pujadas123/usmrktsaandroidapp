package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.convertedleaddetails.ConvertedLeadsDetailActivity;
import in.exuber.usmarket.activity.readyleaddetails.ReadyLeadsDetailActivity;
import in.exuber.usmarket.apimodels.convertedleads.convertedleadsoutput.ConvertedLeadsOutput;
import in.exuber.usmarket.utils.Constants;


public class LeadConvertedListAdapter extends RecyclerView.Adapter<LeadConvertedListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<ConvertedLeadsOutput> convertedLeadsOutputList;


    public LeadConvertedListAdapter(Context context, List<ConvertedLeadsOutput> convertedLeadsOutputList) {

        this.context = context;
        this.convertedLeadsOutputList = convertedLeadsOutputList;

    }

    @Override
    public LeadConvertedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leadsconverted_listadapter, parent, false);
        return new LeadConvertedListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LeadConvertedListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.leadName.setText(convertedLeadsOutputList.get(position).getName()+" "+convertedLeadsOutputList.get(position).getLastName());

        if(convertedLeadsOutputList.get(position).getLeadSource() == null)
        {
            holder.leadSourceLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.leadSource.setText(convertedLeadsOutputList.get(position).getLeadSource().getName());

            holder.leadSourceLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return convertedLeadsOutputList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private RelativeLayout leadParentLayout;
        private TextView leadName, leadSource, leadSourceIcon;
        private LinearLayout leadSourceLayout;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);


            //Initialising views
            leadParentLayout = view.findViewById(R.id.rl_leadsConvertedListAdapter_parentLayout);
            leadName = view.findViewById(R.id.tv_leadsConvertedListAdapter_leadName);

            leadSourceLayout = view.findViewById(R.id.ll_leadsConvertedListAdapter_leadSourceLayout);
            leadSource = view.findViewById(R.id.tv_leadsConvertedListAdapter_leadSource);
            leadSourceIcon = view.findViewById(R.id.iv_leadsConvertedListAdapter_leadSourceIcon);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();
            ConvertedLeadsOutput convertedLeadsOutput = convertedLeadsOutputList.get(clickPosition);

            //Converting to string
            String leadItemString = gson.toJson(convertedLeadsOutput);

            //Preparing Intent
            Intent leadDetailsIntent = new Intent(view.getContext(), ConvertedLeadsDetailActivity.class);

            //Create the bundle
            Bundle leadDetailBundle = new Bundle();

            //Add your data to bundle
            leadDetailBundle.putString(Constants.INTENT_KEY_SELECTED_LEAD,leadItemString);

            //Add the bundle to the intent
            leadDetailsIntent.putExtras(leadDetailBundle);

            view.getContext().startActivity(leadDetailsIntent);



        }

    }

}