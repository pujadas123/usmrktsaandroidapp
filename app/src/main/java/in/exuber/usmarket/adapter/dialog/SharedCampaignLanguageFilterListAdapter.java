package in.exuber.usmarket.adapter.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.dialog.NewCampaignLanguageFilterDialog;
import in.exuber.usmarket.dialog.SharedCampaignLanguageFilterDialog;


public class SharedCampaignLanguageFilterListAdapter extends RecyclerView.Adapter<SharedCampaignLanguageFilterListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private int selectedPosition;
    private ArrayList<String> filiterList;
    private SharedCampaignLanguageFilterDialog sharedCampaignLanguageFilterDialog;


    public SharedCampaignLanguageFilterListAdapter(Context context, ArrayList<String> filiterList, int selectedPosition, SharedCampaignLanguageFilterDialog sharedCampaignLanguageFilterDialog) {

        this.context = context;
        this.filiterList = filiterList;
        this.selectedPosition = selectedPosition;
        this.sharedCampaignLanguageFilterDialog = sharedCampaignLanguageFilterDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sharedcampaign_languagefilter_listadapter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        //Setting values
        holder.filterName.setText(filiterList.get(position));

        if (position == selectedPosition)
        {
            holder.filterParentLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.filterName.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else
        {
            holder.filterParentLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.filterName.setTextColor(context.getResources().getColor(R.color.colorText));
        }



    }

    @Override
    public int getItemCount() {

        return filiterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout filterParentLayout;
        private TextView filterName;




        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            filterParentLayout = view.findViewById(R.id.ll_sharedCampaign_languageFilterListAdapter_parentLayout);
            filterName = view.findViewById(R.id.tv_sharedCampaign_languageFilterListAdapter_filterName);

        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            sharedCampaignLanguageFilterDialog.setSelectedData(clickPosition);



        }

    }

}