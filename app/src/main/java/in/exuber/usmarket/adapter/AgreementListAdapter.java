package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.profileagreementsdetail.ProfileAgreementsDetailActivity;
import in.exuber.usmarket.models.agreement.AgreementOutput;


public class AgreementListAdapter extends RecyclerView.Adapter<AgreementListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private ArrayList<AgreementOutput> agreementOutputList;


    public AgreementListAdapter(Context context, ArrayList<AgreementOutput> agreementOutputList) {

        this.context = context;
        this.agreementOutputList = agreementOutputList;
    }

    @Override
    public AgreementListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profileagreements_listadapter, parent, false);
        return new AgreementListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AgreementListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.agreementHeader.setText(agreementOutputList.get(position).getAgreementHeader());
        holder.agreementDescription.setText(agreementOutputList.get(position).getAgreementDescription());


    }

    @Override
    public int getItemCount() {

        return agreementOutputList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView agreementHeader, agreementDescription;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            agreementHeader = view.findViewById(R.id.tv_profileAgreementList_header);
            agreementDescription = view.findViewById(R.id.tv_profileAgreementList_description);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            Gson gson = new Gson();
            AgreementOutput agreementOutput = agreementOutputList.get(clickPosition);

            //Converting to string
            String agreementItemString = gson.toJson(agreementOutput);

            //Preparing Intent
            Intent agreementDetailsIntent = new Intent(view.getContext(), ProfileAgreementsDetailActivity.class);

            //Create the bundle
            Bundle agreementDetailBundle = new Bundle();

            //Add your data to bundle
            agreementDetailBundle.putString("selectedAgreement",agreementItemString);

            //Add the bundle to the intent
            agreementDetailsIntent.putExtras(agreementDetailBundle);

            view.getContext().startActivity(agreementDetailsIntent);

        }

    }

}