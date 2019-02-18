package in.exuber.usmarket.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.adapter.dialog.ExistingCampaignCategoryFilterListAdapter;
import in.exuber.usmarket.fragment.campaign.CampaignExistingFragment;


@SuppressLint("ValidFragment")
public class ExistingCampaignCategoryFilterDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private RecyclerView filterList;
	private ImageView closeClick;

	//Decalring variables
	private int selectedPosition;
	private ArrayList<String> filterDataList;
	private CampaignExistingFragment campaignExistingFragment;

	//Adapter
	private ExistingCampaignCategoryFilterListAdapter existingCampaignCategoryFilterListAdapter;

	public ExistingCampaignCategoryFilterDialog(int selectedPosition, CampaignExistingFragment campaignExistingFragment) {

		this.selectedPosition = selectedPosition;
		this.campaignExistingFragment = campaignExistingFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View filterDialog = inflater.inflate(R.layout.dialog_existingcampaign_categoryfilter, null);

		//Initialising variables
		filterDataList = new ArrayList<>();

		//Initialising views
		closeClick = filterDialog.findViewById(R.id.iv_existingCampaign_categoryFilterDialog_closeClick);

		//Recyclerview
		filterList = filterDialog.findViewById(R.id.rv_existingCampaign_categoryFilterDialog_filterList);
		filterList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(getActivity());
		linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
		filterList.setLayoutManager(linearLayoutManagerNotification);

		//Setting values
		setAdapterData();

		//Setting adapter
		existingCampaignCategoryFilterListAdapter = new ExistingCampaignCategoryFilterListAdapter(getActivity(),filterDataList,selectedPosition,ExistingCampaignCategoryFilterDialog.this);
		filterList.setAdapter(existingCampaignCategoryFilterListAdapter);
		existingCampaignCategoryFilterListAdapter.notifyDataSetChanged();


		//Setting onclick
		closeClick.setOnClickListener(this);


		builder.setView(filterDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.iv_existingCampaign_categoryFilterDialog_closeClick:

				//Dismissing dialog
				dismissDialog();

				break;
		}

	}

	//Func - Dismiss dialog
	public void dismissDialog() {
		dismiss();
	}

	//Func - Set Adapterdata
	public void setAdapterData()
	{
		filterDataList.add("All Categories");
		filterDataList.add("Real Estate");
		filterDataList.add("Investment");

	}

    //Func - Set Adapterdata
    public void setSelectedData(int clickPosition) {

		campaignExistingFragment.setCategory(clickPosition,filterDataList.get(clickPosition));

        dismissDialog();

    }
}
