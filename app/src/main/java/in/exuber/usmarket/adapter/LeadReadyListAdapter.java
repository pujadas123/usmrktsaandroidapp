package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import in.exuber.usmarket.activity.convertedleaddetails.ConvertedLeadsDetailActivity;
import in.exuber.usmarket.activity.readyleaddetails.ReadyLeadsDetailActivity;
import in.exuber.usmarket.apimodels.readyleads.readyleadsoutput.ReadyLeadsOutput;
import in.exuber.usmarket.models.LeadsActivieCategoryOutput;
import in.exuber.usmarket.models.leads.LeadsOutput;
import in.exuber.usmarket.utils.Constants;

import static android.content.Context.MODE_PRIVATE;


public class LeadReadyListAdapter extends RecyclerView.Adapter<LeadReadyListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<ReadyLeadsOutput> readyLeadsOutputList;





    public LeadReadyListAdapter(Context context, List<ReadyLeadsOutput> readyLeadsOutputList) {

        this.context = context;
        this.readyLeadsOutputList = readyLeadsOutputList;

    }

    @Override
    public LeadReadyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leadsready_listadapter, parent, false);
        return new LeadReadyListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LeadReadyListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.leadName.setText(readyLeadsOutputList.get(position).getName()+" "+readyLeadsOutputList.get(position).getLastName());

        if (readyLeadsOutputList.get(position).getPhoneNo() == null)
        {
            holder.leadSource.setVisibility(View.GONE);
        }
        else
        {
            holder.leadSource.setText(readyLeadsOutputList.get(position).getCountryCode() + " " +readyLeadsOutputList.get(position).getPhoneNo());

            holder.leadSource.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return readyLeadsOutputList.size();
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
            leadParentLayout = view.findViewById(R.id.rl_leadsReadyListAdapter_parentLayout);
            leadName = view.findViewById(R.id.tv_leadsReadyListAdapter_leadName);

            leadSourceLayout = view.findViewById(R.id.ll_leadsReadyListAdapter_leadSourceLayout);
            leadSource = view.findViewById(R.id.tv_leadsReadyListAdapter_leadSource);
            leadSourceIcon = view.findViewById(R.id.iv_leadsReadyListAdapter_leadSourceIcon);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();

            ReadyLeadsOutput readyLeadsOutput = readyLeadsOutputList.get(clickPosition);

            //Converting to string
            String leadItemString = gson.toJson(readyLeadsOutput);

            //Preparing Intent
            Intent leadDetailsIntent = new Intent(view.getContext(), ReadyLeadsDetailActivity.class);

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