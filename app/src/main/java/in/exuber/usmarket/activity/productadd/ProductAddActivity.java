package in.exuber.usmarket.activity.productadd;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.ProductAddListAdapter;
import in.exuber.usmarket.models.products.ProductsOutput;
import in.exuber.usmarket.utils.Constants;

import static in.exuber.usmarket.utils.UtilMethods.hideKeyBoard;

public class ProductAddActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private LinearLayout productAddActivityContainer;
    private TextView toolbarHeader;

    SearchView et_productAdd_search;

    private EditText productSearch;

    private LinearLayout realEstateClick, investmentClick;

    private RecyclerView productList;


    //Sharedpreferences
    private SharedPreferences marketPreference;

    //Declaring variables
    private ArrayList<ProductsOutput> productsOutputList;
    private ArrayList<ProductsOutput> selectedProductsList;

    //Adapter
    private ProductAddListAdapter productAddListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialising shared preferences
        marketPreference = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        //Setting Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_primary);

        //Initialising variables
        productsOutputList = new ArrayList<>();
        selectedProductsList = new ArrayList<>();

        //Initialising views
        productAddActivityContainer = findViewById(R.id.activity_product_add);
        toolbarHeader = findViewById(R.id.tv_main_toolBar_headerText);

        realEstateClick = findViewById(R.id.ll_productAdd_realEstateClick);
        investmentClick = findViewById(R.id.ll_productAdd_investmentClick);

        et_productAdd_search=findViewById(R.id.et_productAdd_search);

        /*et_productAdd_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                *//*if(list.contains(s)){
                    adapter.getFilter().filter(s);
                }else{
                    Toast.makeText(ProductAddActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*//*
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/


        //Recyclerview
        productList = findViewById(R.id.rv_productAdd_productList);
        productList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerProducts = new LinearLayoutManager(this);
        linearLayoutManagerProducts.setOrientation(LinearLayoutManager.VERTICAL);
        productList.setLayoutManager(linearLayoutManagerProducts);


        //Setting toolbar header
        toolbarHeader.setText(getResources().getString(R.string.add_products_caps));

        //Setting values
        setAdapterData();

        //Setting adapter
        productAddListAdapter = new ProductAddListAdapter(ProductAddActivity.this,productsOutputList);
        productList.setAdapter(productAddListAdapter);
        productAddListAdapter.notifyDataSetChanged();

        //Setting onclick
        realEstateClick.setOnClickListener(this);
        investmentClick.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_done_menu_done:

                //Hiding Keyboard
                hideKeyBoard(ProductAddActivity.this);

                if (selectedProductsList.size() == 0)
                {
                    Snackbar snackbar = Snackbar
                            .make(productAddActivityContainer, R.string.error_add_product_empty, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else
                {
                    finish();
                }


                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }


    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId())
        {
            case R.id.ll_productAdd_realEstateClick:

                //Hiding Keyboard
                hideKeyBoard(ProductAddActivity.this);



                realEstateClick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                investmentClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));

                break;

            case R.id.ll_productAdd_investmentClick:

                //Hiding Keyboard
                hideKeyBoard(ProductAddActivity.this);


                investmentClick.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                realEstateClick.setBackgroundColor(getResources().getColor(R.color.colorLightGrey));


                break;


        }

    }

    //Func - Set Adapter Data
    private void setAdapterData() {


        /*//ONE
        ProductsOutput productsOutputOne = new ProductsOutput();
        productsOutputOne.setProductName("Product #1");
        productsOutputOne.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputOne.setProductCategory("Real Estate");
        productsOutputOne.setProductPrice("$100");
        productsOutputOne.setProductDate("Mon 12 Jun, 2018");
        productsOutputOne.setProductImage(R.drawable.demo_product_one);
        productsOutputOne.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputOne);


        //TWO
        ProductsOutput productsOutputTwo = new ProductsOutput();
        productsOutputTwo.setProductName("Product #2");
        productsOutputTwo.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputTwo.setProductCategory("Real Estate");
        productsOutputTwo.setProductPrice("$200");
        productsOutputTwo.setProductDate("Mon 12 Jun, 2018");
        productsOutputTwo.setProductImage(R.drawable.demo_product_two);
        productsOutputTwo.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputTwo);


        //THREE
        ProductsOutput productsOutputThree = new ProductsOutput();
        productsOutputThree.setProductName("Product #3");
        productsOutputThree.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputThree.setProductCategory("Real Estate");
        productsOutputThree.setProductPrice("$300");
        productsOutputThree.setProductDate("Mon 12 Jun, 2018");
        productsOutputThree.setProductImage(R.drawable.demo_product_three);
        productsOutputThree.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputThree);



        //FOUR
        ProductsOutput productsOutputFour = new ProductsOutput();
        productsOutputFour.setProductName("Product #4");
        productsOutputFour.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputFour.setProductCategory("Real Estate");
        productsOutputFour.setProductPrice("$400");
        productsOutputFour.setProductDate("Mon 12 Jun, 2018");
        productsOutputFour.setProductImage(R.drawable.demo_product_four);
        productsOutputFour.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputFour);


        //FIVE
        ProductsOutput productsOutputFive = new ProductsOutput();
        productsOutputFive.setProductName("Product #5");
        productsOutputFive.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputFive.setProductCategory("Real Estate");
        productsOutputFive.setProductPrice("$500");
        productsOutputFive.setProductDate("Mon 12 Jun, 2018");
        productsOutputFive.setProductImage(R.drawable.demo_product_five);
        productsOutputFive.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputFive);


        //Six
        ProductsOutput productsOutputSix = new ProductsOutput();
        productsOutputSix.setProductName("Product #6");
        productsOutputFive.setProductDescription("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut ero labore et dolore magna aliqua.");
        productsOutputSix.setProductCategory("Real Estate");
        productsOutputSix.setProductPrice("$500");
        productsOutputSix.setProductDate("Mon 12 Jun, 2018");
        productsOutputSix.setProductImage(R.drawable.demo_product_six);
        productsOutputSix.setProductCommission("10% of Sale Price");
        productsOutputList.add(productsOutputSix);*/


    }

    public void addProduct(ProductsOutput productsOutput) {

        selectedProductsList.add(productsOutput);
    }

    public void removeProduct(ProductsOutput productsOutput) {

        selectedProductsList.remove(productsOutput);
    }
}
