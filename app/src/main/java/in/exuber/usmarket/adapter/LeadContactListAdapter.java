package in.exuber.usmarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.models.leads.LeadsContactOutput;


public class LeadContactListAdapter extends RecyclerView.Adapter<LeadContactListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private ArrayList<LeadsContactOutput> leadsContactOutputList;


    public LeadContactListAdapter(Context context, ArrayList<LeadsContactOutput> leadsContactOutputList) {

        this.context = context;
        this.leadsContactOutputList = leadsContactOutputList;
    }

    @Override
    public LeadContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leadscontact_listadapter, parent, false);
        return new LeadContactListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(LeadContactListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.leadContact.setText(leadsContactOutputList.get(position).getContact());
        if (leadsContactOutputList.get(position).getContactSource().equals("Email"))
        {
            holder.leadContactImage.setImageResource(R.drawable.ic_email_grey);
        }
        else if (leadsContactOutputList.get(position).getContactSource().equals("Phone"))
        {

            holder.leadContactImage.setImageResource(R.drawable.ic_phone_grey);
        }
        else
        {
            holder.leadContactImage.setImageResource(R.drawable.ic_facebook_grey);
        }


    }

    @Override
    public int getItemCount() {

        return leadsContactOutputList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView leadContact;
        private ImageView leadContactImage;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            leadContact = view.findViewById(R.id.tv_leadContactListAdapter_leadContact);
            leadContactImage = view.findViewById(R.id.iv_leadContactListAdapter_leadContactImage);

        }


        @Override
        public void onClick(View view) {

        }

    }

}