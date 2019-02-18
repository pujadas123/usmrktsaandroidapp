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
import in.exuber.usmarket.apimodels.glossary.glossaryoutput.GlossaryOutput;


@SuppressLint("ValidFragment")
public class GlossaryDialog extends DialogFragment implements View.OnClickListener {

	//Declaring views
	private LinearLayout closeClick;
	private TextView termsText, descriptionText;

	//Declaring variables
	private GlossaryOutput glossaryOutput;

	public GlossaryDialog(GlossaryOutput glossaryOutput) {

		this.glossaryOutput = glossaryOutput;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//Creating Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//Inflating view
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View glossaryDialog = inflater.inflate(R.layout.dialog_glossary, null);

		//Initialising views
		termsText = glossaryDialog.findViewById(R.id.tv_glossaryDialog_termsText);
		descriptionText = glossaryDialog.findViewById(R.id.tv_glossaryDialog_descriptionText);
		closeClick = glossaryDialog.findViewById(R.id.ll_glossaryDialog_closeClick);


		//Setting values
		termsText.setText(glossaryOutput.getTerm());
		descriptionText.setText(glossaryOutput.getDefination());

		//Setting onclick listner
		closeClick.setOnClickListener(this);

		builder.setView(glossaryDialog);
		return builder.create();
	}



	@Override
	public void onClick(View v) {

		switch (v.getId())
		{
			case R.id.ll_glossaryDialog_closeClick:

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
