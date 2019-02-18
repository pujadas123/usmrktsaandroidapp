package in.exuber.usmarket.fragment.appintro;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.appintro.AppIntroActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroTwoFragment extends Fragment implements View.OnClickListener {


    //Declaring variables
    private Context context;

    //Declaring views
    private TextView skipClick;

    public AppIntroTwoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroTwoFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroTwoView =  inflater.inflate(R.layout.fragment_app_intro_two, container, false);

        //Initialising views
        skipClick = appIntroTwoView.findViewById(R.id.tv_appIntroTwoFragment_skipClick);

        //Setting onclick
        skipClick.setOnClickListener(this);


        return appIntroTwoView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_appIntroTwoFragment_skipClick:

                ((AppIntroActivity)getActivity()).appIntroFinished();

                break;
        }

    }
}
