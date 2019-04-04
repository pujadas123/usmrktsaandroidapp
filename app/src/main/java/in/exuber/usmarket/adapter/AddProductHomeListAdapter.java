package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.activity.productdetail.ProductDetailActivity;
import in.exuber.usmarket.apimodels.product.productoutput.ProductOutput;
import in.exuber.usmarket.utils.Constants;


public class AddProductHomeListAdapter extends RecyclerView.Adapter<AddProductHomeListAdapter.ViewHolder> implements Filterable {

    //Declaring variables
    private Context context;
    private List<ProductOutput> productOutputList;
    private List<ProductOutput> filteredProductOutputList;


    public AddProductHomeListAdapter(Context context, List<ProductOutput> productOutputList) {

        this.context = context;
        this.productOutputList = productOutputList;
        this.filteredProductOutputList = productOutputList;
    }

    @Override
    public AddProductHomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_addproducthome_listadapter, parent, false);
        return new AddProductHomeListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AddProductHomeListAdapter.ViewHolder holder, final int position) {


        //Setting values
        holder.productName.setText(filteredProductOutputList.get(position).getProductName());
        if (filteredProductOutputList.get(position).getProductCategory() == null)
        {
            holder.productCategory.setVisibility(View.GONE);
        }
        else
        {
            holder.productCategory.setText(filteredProductOutputList.get(position).getProductCategory().getName());
            holder.productCategory.setVisibility(View.VISIBLE);
        }

        holder.productPrice.setText(context.getString(R.string.dollar_sign)+filteredProductOutputList.get(position).getPrice()+".00");
        holder.productCommission.setText(filteredProductOutputList.get(position).getCommission()+context.getString(R.string.commission_tail));


        if (filteredProductOutputList.get(position).getImage1() == null)
        {

            holder.productThumbnail.setImageResource(R.drawable.ic_no_image);

        }
        else
        {
            String productImageUrl = filteredProductOutputList.get(position).getImage1();

            if (productImageUrl.isEmpty())
            {
                holder.productThumbnail.setImageResource(R.drawable.ic_no_image);

            }
            else
            {
                Picasso.get()
                        .load(productImageUrl.replace(" ","%20"))
                        .placeholder(R.drawable.ic_no_image)
                        .error(R.drawable.ic_no_image)
                        .into(holder.productThumbnail);

            }
        }

        if (filteredProductOutputList.get(position).isSelected()) {

            filteredProductOutputList.get(position).setSelected(true);
        }
        else
        {
            filteredProductOutputList.get(position).setSelected(false);
        }


        //Switch change listner
        holder.addProductSelect.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        if (isChecked) {

                            filteredProductOutputList.get(position).setSelected(true);

                        } else {

                            filteredProductOutputList.get(position).setSelected(false);


                        }
                    }
                });


    }

    @Override
    public int getItemCount() {

        return filteredProductOutputList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredProductOutputList = productOutputList;
                }
                else {



                    ArrayList<ProductOutput> filteredList = new ArrayList<>();

                    for (ProductOutput productOutput : productOutputList) {

                        if (productOutput.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(productOutput);
                            continue;
                        }

                        if (productOutput.getProductCategory() != null)
                        {
                            if (productOutput.getProductCategory().getName().toLowerCase().contains(charString.toLowerCase()))
                            {
                                filteredList.add(productOutput);
                                continue;
                            }
                        }
                        if (productOutput.getPrice().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(productOutput);
                            continue;
                        }
                    }

                    filteredProductOutputList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProductOutputList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredProductOutputList = new ArrayList<>();
                filteredProductOutputList = (List<ProductOutput>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private TextView productName, productCategory, productPrice, productCommission;
        private ImageView productThumbnail;
        private SwitchCompat addProductSelect;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            productName = view.findViewById(R.id.tv_addProductHomeListAdapter_productName);
            productCategory = view.findViewById(R.id.tv_addProductHomeListAdapter_productCategory);
            productPrice = view.findViewById(R.id.tv_addProductHomeListAdapter_productPrice);
            productCommission = view.findViewById(R.id.tv_addProductHomeListAdapter_productCommision);

            productThumbnail = view.findViewById(R.id.iv_addProductHomeListAdapter_productThumbnail);

            addProductSelect = view.findViewById(R.id.switch_addProductHomeListAdapter_addSwitch);


        }


        @Override
        public void onClick(View view) {


            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();
            ProductOutput productOutput = filteredProductOutputList.get(clickPosition);

            //Converting to string
            String productItemString = gson.toJson(productOutput);

            //Preparing Intent
            Intent productDetailsIntent = new Intent(view.getContext(), ProductDetailActivity.class);

            //Create the bundle
            Bundle productDetailBundle = new Bundle();

            //Add your data to bundle
            productDetailBundle.putString(Constants.INTENT_KEY_SELECTED_PRODUCT,productItemString);
            productDetailBundle.putBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT,false);

            //Add the bundle to the intent
            productDetailsIntent.putExtras(productDetailBundle);

            view.getContext().startActivity(productDetailsIntent);


        }

    }

    public List<ProductOutput> getFilteredProductOutputList() {
        return filteredProductOutputList;
    }
}