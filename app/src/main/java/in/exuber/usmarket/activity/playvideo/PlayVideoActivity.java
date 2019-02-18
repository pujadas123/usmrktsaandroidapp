package in.exuber.usmarket.activity.playvideo;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

import in.exuber.usmarket.R;
import in.exuber.usmarket.utils.Constants;

public class PlayVideoActivity extends AppCompatActivity {

    //Declaring views
    private LinearLayout playVideoActivityContainer;
    private FullscreenVideoLayout videoLayout;
    private ImageView backClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.video_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Geting data from intent
        Bundle passedBundle = getIntent().getExtras();
        String videoUrl = passedBundle.getString(Constants.INTENT_KEY_SELECTED_VIDEO_URL);

        //Initialising views
        videoLayout = findViewById(R.id.fvl_playVideo_video);
        videoLayout.setActivity(this);

        Uri videoUri = Uri.parse(videoUrl.replace(" ","%20"));
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Video error",e.toString());
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
