package in.exuber.usmarket.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.leadsadd.LeadsAddActivity;
import in.exuber.usmarket.activity.leadsedit.LeadsEditActivity;
import in.exuber.usmarket.adapter.dialog.AddLeadsLeadSourceFilterListAdapter;
import in.exuber.usmarket.adapter.dialog.EditLeadsLeadSourceFilterListAdapter;


@SuppressLint("ValidFragment")
public class EditLeadsLeadSourceFilterDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private RecyclerView filterList;
	private ImageView closeClick;

	//Decalring variables
	private int selectedPosition;
	private ArrayList<String> filterDataList;

	//Adapter
	private EditLeadsLeadSourceFilterListAdapter editLeadsLeadSourceFilterListAdapter;

	public EditLeadsLeadSourceFilterDialog(int selectedPosition) {

		this.selectedPosition = selectedPosition;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View filterDialog = inflater.inflate(R.layout.dialog_editleads_leadsourcefilter, null);

		//Initialising variables
		filterDataList = new ArrayList<>();

		//Initialising views
		closeClick = filterDialog.findViewById(R.id.iv_editLeads_leadSourceFilterDialog_closeClick);

		//Recyclerview
		filterList = filterDialog.findViewById(R.id.rva_editLeads_leadSourceFilterDialog_filterList);
		filterList.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManagerNotification = new LinearLayoutManager(getActivity());
		linearLayoutManagerNotification.setOrientation(LinearLayoutManager.VERTICAL);
		filterList.setLayoutManager(linearLayoutManagerNotification);

		//Setting values
		setAdapterData();

		//Setting adapter
		editLeadsLeadSourceFilterListAdapter = new EditLeadsLeadSourceFilterListAdapter(getActivity(),filterDataList,selectedPosition,EditLeadsLeadSourceFilterDialog.this);
		filterList.setAdapter(editLeadsLeadSourceFilterListAdapter);
		editLeadsLeadSourceFilterListAdapter.notifyDataSetChanged();


		//Setting onclick
		closeClick.setOnClickListener(this);


		builder.setView(filterDialog);
		return builder.create();
	}


	public void onResume()
	{
		super.onResume();
		Window window = getDialog().getWindow();
		ViewGroup.LayoutParams params = window.getAttributes();
		window.setLayout((int) (getWindowSize(getActivity()).x * 0.80), ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		//TODO:
	}

	public Point getWindowSize(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.iv_editLeads_leadSourceFilterDialog_closeClick:

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

        ((LeadsEditActivity)getActivity()).setLeadSource(clickPosition,filterDataList.get(clickPosition));

        dismissDialog();

    }
}
