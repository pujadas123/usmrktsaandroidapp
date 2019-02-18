package in.exuber.usmarket.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.forgotpassword.ForgotPasswordActivity;


@SuppressLint("ValidFragment")
public class ForgotPasswordDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private Context context;
	private LinearLayout closeClick;
	private TextView successDialogText;

	//Decalring variables
	private String successText;

	public ForgotPasswordDialog(Context context,String successText) {

		this.successText = successText;
		this.context = context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View infoDialog = inflater.inflate(R.layout.dialog_forgot_password, null);

		//Initialising views
		successDialogText = infoDialog.findViewById(R.id.tv_forgotPasswordDialog_successText);
		closeClick = infoDialog.findViewById(R.id.ll_forgotPasswordDialog_closeClick);


		//Setting values
		successDialogText.setText(successText);

		//Setting properties
		setCancelable(false);

		//Setting onclick listner
		closeClick.setOnClickListener(this);

		builder.setView(infoDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.ll_forgotPasswordDialog_closeClick:

				((ForgotPasswordActivity)context).closeClick();

				//Dismissing dialog
				dismissDialog();

				break;
		}

	}

	//Func - Dismiss dialog
	public void dismissDialog() {
		dismiss();
	}
}
