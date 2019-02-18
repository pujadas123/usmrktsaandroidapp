package in.exuber.usmarket.fragment.appintro;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroFourFragment extends Fragment implements View.OnClickListener {

    //Declaring variables
    private Context context;

    //Declaring views
    private LinearLayout getStartedClick;

    public AppIntroFourFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroFourFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroFourView =  inflater.inflate(R.layout.fragment_app_intro_four, container, false);

        //Initialising views
        getStartedClick = appIntroFourView.findViewById(R.id.ll_appIntroFourFragment_getStartedClick);

        getStartedClick.setOnClickListener(this);



        return appIntroFourView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_appIntroFourFragment_getStartedClick:

                ((AppIntroActivity)getActivity()).appIntroFinished();

                break;
        }
    }
}
