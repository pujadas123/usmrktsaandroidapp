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
public class AppIntroThreeFragment extends Fragment implements View.OnClickListener {

    //Declaring views
    private TextView skipClick;

    //Declaring variables
    private Context context;

    public AppIntroThreeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AppIntroThreeFragment(Context context) {

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appIntroThreeView =  inflater.inflate(R.layout.fragment_app_intro_three, container, false);

        //Initialising views
        skipClick = appIntroThreeView.findViewById(R.id.tv_appIntroThreeFragment_skipClick);

        //Setting onclick
        skipClick.setOnClickListener(this);

        return appIntroThreeView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.tv_appIntroThreeFragment_skipClick:

                ((AppIntroActivity)getActivity()).appIntroFinished();

                break;
        }
    }
}
