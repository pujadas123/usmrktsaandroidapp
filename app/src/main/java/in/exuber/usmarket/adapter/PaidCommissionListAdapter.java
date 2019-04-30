package in.exuber.usmarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.paidcommision.PaidCommissionOutput;

public class PaidCommissionListAdapter extends RecyclerView.Adapter<PaidCommissionListAdapter.ViewHolder>implements Filterable {

    //Declaring variables
    private Context context;
    private List<PaidCommissionOutput> paidCommissionOutputList;
    private List<PaidCommissionOutput> filteredPaidCommissionOutputList;

    public PaidCommissionListAdapter(Context context, List<PaidCommissionOutput> paidCommissionOutputList) {

        this.context = context;
        this.paidCommissionOutputList = paidCommissionOutputList;
        this.filteredPaidCommissionOutputList = paidCommissionOutputList;
    }

    @Override
    public PaidCommissionListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_paidcommission_listadapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaidCommissionListAdapter.ViewHolder viewHolder, int position) {

        viewHolder.productId.setText("PD" + filteredPaidCommissionOutputList.get(position).getProductId().getId());
        viewHolder.productName.setText(filteredPaidCommissionOutputList.get(position).getProductId().getProductName());

        double amount = Double.parseDouble(String.valueOf(filteredPaidCommissionOutputList.get(position).getPaid()));
        DecimalFormat formatter = new DecimalFormat("$#,###,###.00");
        viewHolder.productCommission.setText(formatter.format(amount));
    }

    @Override
    public int getItemCount() {
        return filteredPaidCommissionOutputList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredPaidCommissionOutputList = paidCommissionOutputList;

                } else {

                    ArrayList<PaidCommissionOutput> filteredList = new ArrayList<>();

                    for (PaidCommissionOutput row : paidCommissionOutputList) {

                        String commisionSearch = String.valueOf(row.getPaid());
                        String productIdSearch = String.valueOf(row.getProductId().getId());


                        if (productIdSearch.toLowerCase().contains(charString.toLowerCase()) || row.getProductId().getProductName().toLowerCase().contains(charString.toLowerCase()) || commisionSearch.contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    filteredPaidCommissionOutputList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPaidCommissionOutputList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPaidCommissionOutputList = (ArrayList<PaidCommissionOutput>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Declaring views
        TextView productId, productName, productCommission;

        public ViewHolder(View itemView) {
            super(itemView);

            //Initailising views
            productId = itemView.findViewById(R.id.tv_paidCommissionListAdapter_productId);
            productName = itemView.findViewById(R.id.tv_paidCommissionListAdapter_productName);
            productCommission = itemView.findViewById(R.id.tv_paidCommissionListAdapter_commission);
        }
    }
}
