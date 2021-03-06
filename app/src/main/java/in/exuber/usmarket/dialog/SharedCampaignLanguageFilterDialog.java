package in.exuber.usmarket.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaignshared.CampaignSharedActivity;
import in.exuber.usmarket.adapter.dialog.SharedCampaignLanguageFilterListAdapter;


@SuppressLint("ValidFragment")
public class SharedCampaignLanguageFilterDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private RecyclerView filterList;
	private ImageView closeClick;

	//Decalring variables
	private int selectedPosition;
	private ArrayList<String> filterDataList;

	private Context context;

	//Adapter
	private SharedCampaignLanguageFilterListAdapter sharedCampaignLanguageFilterListAdapter;

	public SharedCampaignLanguageFilterDialog(Context context, int selectedPosition) {

		this.selectedPosition = selectedPosition;
		this.context = context;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View filterDialog = inflater.inflate(R.layout.dialog_sharedcampaign_languagefilter, null);

		//Initialising variables
		filterDataList = new ArrayList<>();

		//Initialising views
		closeClick = filterDialog.findViewById(R.id.iv_sharedCampaign_languageFilterDialog_closeClick);

		//Recyclerview
		filterList = filterDialog.findViewById(R.id.rv_sharedCampaign_languageFilterDialog_filterList);
		filterList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(getActivity());
		linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
		filterList.setLayoutManager(linearLayoutManagerNotification);

		//Setting values
		setAdapterData();

		//Setting adapter
		sharedCampaignLanguageFilterListAdapter = new SharedCampaignLanguageFilterListAdapter(getActivity(),filterDataList,selectedPosition,SharedCampaignLanguageFilterDialog.this);
		filterList.setAdapter(sharedCampaignLanguageFilterListAdapter);
		sharedCampaignLanguageFilterListAdapter.notifyDataSetChanged();


		//Setting onclick
		closeClick.setOnClickListener(this);


		builder.setView(filterDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.iv_sharedCampaign_languageFilterDialog_closeClick:

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
		filterDataList.add("All");
		filterDataList.add("English");
		filterDataList.add("Spanish");
		filterDataList.add("French");

	}

    //Func - Set Adapterdata
    public void setSelectedData(int clickPosition) {

		((CampaignSharedActivity)context).setLanguage(clickPosition);

        dismissDialog();

    }
}
