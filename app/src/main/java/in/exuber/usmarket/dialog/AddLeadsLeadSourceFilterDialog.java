package in.exuber.usmarket.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsadd.LeadsAddActivity;
import in.exuber.usmarket.adapter.dialog.AddLeadsLeadSourceFilterListAdapter;


@SuppressLint("ValidFragment")
public class AddLeadsLeadSourceFilterDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private RecyclerView filterList;
	private ImageView closeClick;

	//Decalring variables
	private int selectedPosition;
	private ArrayList<String> filterDataList;

	//Adapter
	private AddLeadsLeadSourceFilterListAdapter addLeadsLeadSourceFilterListAdapter;

	public AddLeadsLeadSourceFilterDialog(int selectedPosition) {

		this.selectedPosition = selectedPosition;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View filterDialog = inflater.inflate(R.layout.dialog_addleads_leadsourcefilter, null);

		//Initialising variables
		filterDataList = new ArrayList<>();

		//Initialising views
		closeClick = filterDialog.findViewById(R.id.iv_addLeads_leadSourceFilterDialog_closeClick);

		//Recyclerview
		filterList = filterDialog.findViewById(R.id.rva_addLeads_leadSourceFilterDialog_filterList);
		filterList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(getActivity());
		linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
		filterList.setLayoutManager(linearLayoutManagerNotification);

		//Setting values
		setAdapterData();

		//Setting adapter
		addLeadsLeadSourceFilterListAdapter = new AddLeadsLeadSourceFilterListAdapter(getActivity(),filterDataList,selectedPosition,AddLeadsLeadSourceFilterDialog.this);
		filterList.setAdapter(addLeadsLeadSourceFilterListAdapter);
		addLeadsLeadSourceFilterListAdapter.notifyDataSetChanged();


		//Setting onclick
		closeClick.setOnClickListener(this);


		builder.setView(filterDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.iv_addLeads_leadSourceFilterDialog_closeClick:

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
		filterDataList.add("Social Network");
		filterDataList.add("Website");
		filterDataList.add("Email");
		filterDataList.add("Phone");
        filterDataList.add("Referral");
        filterDataList.add("Other");
	}

    //Func - Set Adapterdata
    public void setSelectedData(int clickPosition) {

        ((LeadsAddActivity)getActivity()).setLeadSource(clickPosition,filterDataList.get(clickPosition));

        dismissDialog();

    }
}
