package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.homeaddproducts.HomeAddProductsActivity;
import in.exuber.usmarket.activity.productadd.ProductAddActivity;
import in.exuber.usmarket.activity.productdetail.ProductDetailActivity;
import in.exuber.usmarket.models.products.ProductsOutput;


public class ProductAddListAdapter extends RecyclerView.Adapter<ProductAddListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private ArrayList<ProductsOutput> productsOutputList;


    public ProductAddListAdapter(Context context, ArrayList<ProductsOutput> productsOutputList) {

        this.context = context;
        this.productsOutputList = productsOutputList;
    }

    @Override
    public ProductAddListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_productadd_listadapter, parent, false);
        return new ProductAddListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductAddListAdapter.ViewHolder holder, final int position) {


        //Setting values
        /*holder.productName.setText(productsOutputList.get(position).getProductName());
        holder.productCategory.setText(productsOutputList.get(position).getProductCategory());
        holder.productPrice.setText(productsOutputList.get(position).getProductPrice());
        holder.productCommission.setText(productsOutputList.get(position).getProductCommission());

        holder.productThumbnail.setImageResource(productsOutputList.get(position).getProductImage());*/

        //Switch change listner
        holder.addProductSelect.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {

                            ((ProductAddActivity)context).addProduct(productsOutputList.get(position));

                        } else {

                            ((ProductAddActivity)context).removeProduct(productsOutputList.get(position));

                        }
                    }
                });


    }

    @Override
    public int getItemCount() {

        return productsOutputList.size();
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
            productName = view.findViewById(R.id.tv_productAddListAdapter_productName);
            productCategory = view.findViewById(R.id.tv_productAddListAdapter_productCategory);
            productPrice = view.findViewById(R.id.tv_productAddListAdapter_productPrice);
            productCommission = view.findViewById(R.id.tv_productAddListAdapter_productCommision);

            productThumbnail = view.findViewById(R.id.iv_productAddListAdapter_productThumbnail);

            addProductSelect = view.findViewById(R.id.switch_productAddListAdapter_addSwitch);


        }


        @Override
        public void onClick(View view) {


            int clickPosition = getLayoutPosition();

            Gson gson = new Gson();
            ProductsOutput productsOutput = productsOutputList.get(clickPosition);

            //Converting to string
            String productItemString = gson.toJson(productsOutput);

            //Preparing Intent
            Intent productDetailsIntent = new Intent(view.getContext(), ProductDetailActivity.class);

            //Create the bundle
            Bundle productDetailBundle = new Bundle();

            //Add your data to bundle
            productDetailBundle.putString("selectedProduct",productItemString);

            //Add the bundle to the intent
            productDetailsIntent.putExtras(productDetailBundle);

            view.getContext().startActivity(productDetailsIntent);


        }

    }

}