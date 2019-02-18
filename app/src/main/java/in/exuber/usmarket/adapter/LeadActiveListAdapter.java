package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.activeleadsdetails.LeadsDetailActivity;
import in.exuber.usmarket.apimodels.allleads.allleadsoutput.AllLeadsOutput;
import in.exuber.usmarket.models.LeadsActivieCategoryOutput;
import in.exuber.usmarket.models.leads.LeadsOutput;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;


public class LeadActiveListAdapter extends RecyclerView.Adapter<LeadActiveListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<AllLeadsOutput> allLeadsOutputList;

    public LeadActiveListAdapter(Context context,  List<AllLeadsOutput> allLeadsOutputList) {
        this.context = context;
        this.allLeadsOutputList = allLeadsOutputList;

    }

    @Override
    public LeadActiveListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leadsactive_listadapter, parent, false);
        return new LeadActiveListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LeadActiveListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.leadName.setText(allLeadsOutputList.get(position).getName()+" "+allLeadsOutputList.get(position).getLastName());

        if (allLeadsOutputList.get(position).getLeadSource() == null)
        {
            holder.leadSourceLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.leadSource.setText(allLeadsOutputList.get(position).getLeadSource().getName());

            holder.leadSourceLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return allLeadsOutputList.size();
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
            leadParentLayout = view.findViewById(R.id.rl_leadsActiveListAdapter_parentLayout);
            leadName = view.findViewById(R.id.tv_leadsActiveListAdapter_leadName);

            leadSourceLayout = view.findViewById(R.id.ll_leadsActiveListAdapter_leadSourceLayout);
            leadSource = view.findViewById(R.id.tv_leadsActiveListAdapter_leadSource);
            leadSourceIcon = view.findViewById(R.id.iv_leadsActiveListAdapter_leadSourceIcon);

        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            Gson gson = new Gson();
            AllLeadsOutput allLeadsOutput = allLeadsOutputList.get(clickPosition);

            //Converting to string
            String leadItemString = gson.toJson(allLeadsOutput);

            //Preparing Intent
            Intent leadDetailsIntent = new Intent(view.getContext(), LeadsDetailActivity.class);

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