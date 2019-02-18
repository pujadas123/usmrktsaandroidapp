package in.exuber.usmarket.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.sharedcampaign.sharedcampaignoutput.SharedCampaignOutput;
import in.exuber.usmarket.models.faq.FaqOutput;
import in.exuber.usmarket.models.paidcommision.PaidCommissionOutput;
import in.exuber.usmarket.models.products.ProductsOutput;

public class PaidCommissionListAdapter extends RecyclerView.Adapter<PaidCommissionListAdapter.ViewHolder>implements Filterable {

    //Declaring variables
    private Context context;
    private ArrayList<PaidCommissionOutput> paidCommissionOutputArrayList;
    private ArrayList<PaidCommissionOutput> filteredpaidCommissionOutputArrayList;

    public PaidCommissionListAdapter(Context context, ArrayList<PaidCommissionOutput> paidCommissionOutputArrayList) {
        this.context = context;
        this.paidCommissionOutputArrayList = paidCommissionOutputArrayList;
        this.filteredpaidCommissionOutputArrayList=paidCommissionOutputArrayList;
    }

    @Override
    public PaidCommissionListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_row_paidcommission, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaidCommissionListAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.txt_IdSl.setText((i+1)+"");
        viewHolder.txt_IdSl.setText("PD" + filteredpaidCommissionOutputArrayList.get(i).getProductId());
        viewHolder.txt_ProductName.setText(filteredpaidCommissionOutputArrayList.get(i).getProductName());
        viewHolder.txt_Commission.setText("$" + filteredpaidCommissionOutputArrayList.get(i).getPaid()+".00");
    }

    @Override
    public int getItemCount() {
        return filteredpaidCommissionOutputArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredpaidCommissionOutputArrayList = paidCommissionOutputArrayList;
                } else {
                    ArrayList<PaidCommissionOutput> filteredList = new ArrayList<>();
                    for (PaidCommissionOutput row : paidCommissionOutputArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String commision= String.valueOf(row.getPaid());
                        Log.e("Adapter_COMMISSION",commision);
                        if (row.getProductId().toLowerCase().contains(charString.toLowerCase()) || row.getProductName().toLowerCase().contains(charString.toLowerCase()) || commision.contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    filteredpaidCommissionOutputArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredpaidCommissionOutputArrayList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredpaidCommissionOutputArrayList = (ArrayList<PaidCommissionOutput>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_IdSl, txt_ProductName,txt_Commission;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_IdSl=itemView.findViewById(R.id.txt_IdSl);
            txt_ProductName=itemView.findViewById(R.id.txt_ProductName);
            txt_Commission=itemView.findViewById(R.id.txt_Commission);
        }
    }
}
