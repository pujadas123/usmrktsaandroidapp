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
import in.exuber.usmarket.adapter.dialog.ExistingCampaignLanguageFilterListAdapter;
import in.exuber.usmarket.fragment.campaign.CampaignExistingFragment;


@SuppressLint("ValidFragment")
public class ExistingCampaignLanguageFilterDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private RecyclerView filterList;
	private ImageView closeClick;

	//Decalring variables
	private int selectedPosition;
	private ArrayList<String> filterDataList;
	private CampaignExistingFragment campaignExistingFragment;

	//Adapter
	private ExistingCampaignLanguageFilterListAdapter existingCampaignLanguageFilterListAdapter;

	public ExistingCampaignLanguageFilterDialog(int selectedPosition, CampaignExistingFragment campaignExistingFragment) {

		this.selectedPosition = selectedPosition;
		this.campaignExistingFragment = campaignExistingFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View filterDialog = inflater.inflate(R.layout.dialog_existingcampaign_languagefilter, null);

		//Initialising variables
		filterDataList = new ArrayList<>();

		//Initialising views
		closeClick = filterDialog.findViewById(R.id.iv_existingCampaign_languageFilterDialog_closeClick);

		//Recyclerview
		filterList = filterDialog.findViewById(R.id.rv_existingCampaign_languageFilterDialog_filterList);
		filterList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(getActivity());
		linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
		filterList.setLayoutManager(linearLayoutManagerNotification);

		//Setting values
		setAdapterData();

		//Setting adapter
		existingCampaignLanguageFilterListAdapter = new ExistingCampaignLanguageFilterListAdapter(getActivity(),filterDataList,selectedPosition,ExistingCampaignLanguageFilterDialog.this);
		filterList.setAdapter(existingCampaignLanguageFilterListAdapter);
		existingCampaignLanguageFilterListAdapter.notifyDataSetChanged();


		//Setting onclick
		closeClick.setOnClickListener(this);


		builder.setView(filterDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.iv_existingCampaign_languageFilterDialog_closeClick:

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

		filterDataList.add("ALL");
		filterDataList.add("English");
		filterDataList.add("Spanish");
		filterDataList.add("French");

	}

    //Func - Set Adapterdata
    public void setSelectedData(int clickPosition) {

		campaignExistingFragment.setLanguage(clickPosition);

        dismissDialog();

    }
}
