package in.exuber.usmarket.activity.welcomescreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.loginsignup.LoginSignupActivity;

public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener {

    //Declariing views
    private LinearLayout letsGoClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //Initialising views
        letsGoClick = findViewById(R.id.ll_welcomeScreen_letsGoClick);

        //Setting on click
        letsGoClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_welcomeScreen_letsGoClick:

                Log.e("Clicked","Clicked");

                startActivity(new Intent(WelcomeScreen.this, LoginSignupActivity.class));
                finish();

                break;
        }

    }
}
