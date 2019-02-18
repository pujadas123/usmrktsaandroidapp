package in.exuber.usmarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.sharecampaignlog.sharecampaignlogoutput.ShareCampaignLogOutput;


public class CampaignShareLogListAdapter extends RecyclerView.Adapter<CampaignShareLogListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<ShareCampaignLogOutput> shareCampaignLogOutputList;


    public CampaignShareLogListAdapter(Context context, List<ShareCampaignLogOutput> shareCampaignLogOutputList) {

        this.context = context;
        this.shareCampaignLogOutputList = shareCampaignLogOutputList;
    }

    @Override
    public CampaignShareLogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campaignsharelog_listadapter, parent, false);
        return new CampaignShareLogListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CampaignShareLogListAdapter.ViewHolder holder, int position) {


        //Setting values
        holder.campaignShareSocialMedia.setText(shareCampaignLogOutputList.get(position).getType().getName());

        long shareTimeMillis = shareCampaignLogOutputList.get(position).getCreatedOn();
        DateFormat formatter = new SimpleDateFormat("E, dd MMM - hh:mm a", Locale.US);
        String pringTimeString = formatter.format(shareTimeMillis);

        holder.campaignShareDate.setText(pringTimeString);


    }

    @Override
    public int getItemCount() {

        return shareCampaignLogOutputList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView campaignShareDate, campaignShareSocialMedia;



        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            campaignShareDate = view.findViewById(R.id.tv_campaignShareLogListAdapter_shareDate);
            campaignShareSocialMedia = view.findViewById(R.id.tv_campaignShareLogListAdapter_socialMedia);


        }


        @Override
        public void onClick(View view) {




        }

    }

}