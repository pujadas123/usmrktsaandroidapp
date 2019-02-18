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
import in.exuber.usmarket.dialog.ExistingCampaignLanguageFilterDialog;
import in.exuber.usmarket.dialog.NewCampaignLanguageFilterDialog;


public class ExistingCampaignLanguageFilterListAdapter extends RecyclerView.Adapter<ExistingCampaignLanguageFilterListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private int selectedPosition;
    private ArrayList<String> filiterList;
    private ExistingCampaignLanguageFilterDialog existingCampaignLanguageFilterDialog;


    public ExistingCampaignLanguageFilterListAdapter(Context context, ArrayList<String> filiterList, int selectedPosition, ExistingCampaignLanguageFilterDialog existingCampaignLanguageFilterDialog) {

        this.context = context;
        this.filiterList = filiterList;
        this.selectedPosition = selectedPosition;
        this.existingCampaignLanguageFilterDialog = existingCampaignLanguageFilterDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_existingcampaign_languagefilter_listadapter, parent, false);
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

            filterParentLayout = view.findViewById(R.id.ll_existingCampaign_languageFilterListAdapter_parentLayout);
            filterName = view.findViewById(R.id.tv_existingCampaign_languageFilterListAdapter_filterName);

        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            existingCampaignLanguageFilterDialog.setSelectedData(clickPosition);



        }

    }

}