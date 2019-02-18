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
import in.exuber.usmarket.dialog.AddLeadsLeadSourceFilterDialog;
import in.exuber.usmarket.dialog.NewCampaignCategoryFilterDialog;


public class NewCampaignCategoryFilterListAdapter extends RecyclerView.Adapter<NewCampaignCategoryFilterListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private int selectedPosition;
    private ArrayList<String> filiterList;
    private NewCampaignCategoryFilterDialog newCampaignCategoryFilterDialog;


    public NewCampaignCategoryFilterListAdapter(Context context, ArrayList<String> filiterList, int selectedPosition, NewCampaignCategoryFilterDialog newCampaignCategoryFilterDialog) {

        this.context = context;
        this.filiterList = filiterList;
        this.selectedPosition = selectedPosition;
        this.newCampaignCategoryFilterDialog = newCampaignCategoryFilterDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_newcampaign_categoryfilter_listadapter, parent, false);
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

            filterParentLayout = view.findViewById(R.id.ll_newCampaign_categoryFilterListAdapter_parentLayout);
            filterName = view.findViewById(R.id.tv_newCampaign_categoryFilterListAdapter_filterName);

        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();

            newCampaignCategoryFilterDialog.setSelectedData(clickPosition);

        }

    }

}