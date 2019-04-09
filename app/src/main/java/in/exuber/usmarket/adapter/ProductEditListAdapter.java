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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.activity.productadd.ProductAddActivity;
import in.exuber.usmarket.activity.productdetail.ProductDetailActivity;
import in.exuber.usmarket.activity.productedit.ProductEditActivity;
import in.exuber.usmarket.apimodels.productuser.productuseroutput.ProductUserOutput;
import in.exuber.usmarket.models.products.ProductsOutput;
import in.exuber.usmarket.utils.Constants;


public class ProductEditListAdapter extends RecyclerView.Adapter<ProductEditListAdapter.ViewHolder> implements Filterable {

    //Declaring variables
    private Context context;
    private List<ProductUserOutput> productOutputList;
    private List<ProductUserOutput> filteredProductOutputList;


    public ProductEditListAdapter(Context context, List<ProductUserOutput> productOutputList) {

        this.context = context;
        this.productOutputList = productOutputList;
        this.filteredProductOutputList = productOutputList;
    }

    @Override
    public ProductEditListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_productedit_listadapter, parent, false);
        return new ProductEditListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductEditListAdapter.ViewHolder holder, final int position) {


        //Setting values
        holder.productName.setText(filteredProductOutputList.get(position).getProductId().getProductName());
        if (filteredProductOutputList.get(position).getProductId().getProductCategory() == null)
        {
            holder.productCategory.setVisibility(View.GONE);
        }
        else
        {
            holder.productCategory.setText(filteredProductOutputList.get(position).getProductId().getProductCategory().getName());
            holder.productCategory.setVisibility(View.VISIBLE);
        }

        double amount = Double.parseDouble(filteredProductOutputList.get(position).getProductId().getPrice());
        DecimalFormat formatter = new DecimalFormat("$#,###,###.00");
        holder.productPrice.setText(formatter.format(amount));
        holder.productCommission.setText(filteredProductOutputList.get(position).getProductId().getCommission()+context.getString(R.string.commission_tail));


        if (filteredProductOutputList.get(position).getProductId().getImage1() == null)
        {

            holder.productThumbnail.setImageResource(R.drawable.ic_no_image);

        }
        else
        {
            String productImageUrl = filteredProductOutputList.get(position).getProductId().getImage1();

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


            holder.addProductSelect.setChecked(true);
            Log.e("Selected","YES");
        }
        else
        {

            holder.addProductSelect.setChecked(false);
            Log.e("Selected","NO");
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



                    ArrayList<ProductUserOutput> filteredList = new ArrayList<>();

                    for (ProductUserOutput productOutput : productOutputList) {

                        if (productOutput.getProductId().getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(productOutput);
                            continue;
                        }

                        if (productOutput.getProductId().getProductCategory() != null)
                        {
                            if (productOutput.getProductId().getProductCategory().getName().toLowerCase().contains(charString.toLowerCase()))
                            {
                                filteredList.add(productOutput);
                                continue;
                            }
                        }
                        if (productOutput.getProductId().getPrice().toLowerCase().contains(charString.toLowerCase()))
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
                filteredProductOutputList = (List<ProductUserOutput>) filterResults.values;
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
            productName = view.findViewById(R.id.tv_productEditListAdapter_productName);
            productCategory = view.findViewById(R.id.tv_productEditListAdapter_productCategory);
            productPrice = view.findViewById(R.id.tv_productEditListAdapter_productPrice);
            productCommission = view.findViewById(R.id.tv_productEditListAdapter_productCommision);

            productThumbnail = view.findViewById(R.id.iv_productEditListAdapter_productThumbnail);

            addProductSelect = view.findViewById(R.id.switch_productEditListAdapter_addSwitch);




        }


        @Override
        public void onClick(View view) {


            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();
            ProductUserOutput productUserOutput = filteredProductOutputList.get(clickPosition);

            //Converting to string
            String productItemString = gson.toJson(productUserOutput);

            //Preparing Intent
            Intent productDetailsIntent = new Intent(view.getContext(), ProductDetailActivity.class);

            //Create the bundle
            Bundle productDetailBundle = new Bundle();

            //Add your data to bundle
            productDetailBundle.putString(Constants.INTENT_KEY_SELECTED_PRODUCT,productItemString);
            productDetailBundle.putBoolean(Constants.INTENT_KEY_IS_USER_PRODUCT,true);

            //Add the bundle to the intent
            productDetailsIntent.putExtras(productDetailBundle);

            view.getContext().startActivity(productDetailsIntent);


        }

    }

    public List<ProductUserOutput> getFilteredProductOutputList() {
        return filteredProductOutputList;
    }
}