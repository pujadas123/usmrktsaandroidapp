package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.campaigntraining.CampaignTrainingActivity;
import in.exuber.usmarket.activity.playvideo.PlayVideoActivity;
import in.exuber.usmarket.apimodels.campaigntraining.campaigntrainingoutput.CampaignTrainingOutput;
import in.exuber.usmarket.utils.Constants;


public class CampaignTrainingListAdapter extends RecyclerView.Adapter {

    //Declaring variables
    private Context context;
    private List<CampaignTrainingOutput> campaignTrainingOutputList;


    public CampaignTrainingListAdapter(Context context, List<CampaignTrainingOutput> campaignTrainingOutputList) {

        this.context = context;
        this.campaignTrainingOutputList = campaignTrainingOutputList;
    }

    @Override
    public int getItemViewType(int position) {

        if (campaignTrainingOutputList.get(position).getType().getId().equals(Constants.TRAINING_PDF_ID))
        {
            return 0;
        }
        else if (campaignTrainingOutputList.get(position).getType().getId().equals(Constants.TRAINING_IMAGE_ID))
        {

            return 1;
        }
        else if (campaignTrainingOutputList.get(position).getType().getId().equals(Constants.TRAINING_VIDEO_ID))
        {

            return 2;
        }
        else if (campaignTrainingOutputList.get(position).getType().getId().equals(Constants.TRAINING_LINK_ID))
        {

            return 3;
        }
        else
        {
            return 4;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new ViewHolderPdf(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_pdf_listadapter, parent, false));
            case 1:
                return new ViewHolderImage(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_image_listadapter, parent, false));
            case 2:
                return new ViewHolderVideo(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_video_listadapter, parent, false));
            case 3:
                return new ViewHolderLink(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_link_listadapter, parent, false));
            case 4:
                return new ViewHolderArticle(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_training_article_listadapter, parent, false));

        }
        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {

            //Setting values

            case 0:

                ViewHolderPdf viewHolderPdf = (ViewHolderPdf) holder;
                viewHolderPdf.trainingText.setText(campaignTrainingOutputList.get(position).getName());

                break;

            case 1:

                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;

                if (campaignTrainingOutputList.get(position).getUrl() == null)
                {

                    viewHolderImage.trainingImage.setImageResource(R.drawable.ic_no_image);

                }
                else
                {
                    String trainingImageUrl = campaignTrainingOutputList.get(position).getUrl();

                    if (trainingImageUrl.isEmpty())
                    {
                        viewHolderImage.trainingImage.setImageResource(R.drawable.ic_no_image);

                    }
                    else
                    {
                        Picasso.get()
                                .load(trainingImageUrl.replace(" ","%20"))
                                .placeholder(R.drawable.ic_no_image)
                                .error(R.drawable.ic_no_image)
                                .into(viewHolderImage.trainingImage);



                    }
                }



                break;

            case 2:



                break;


            case 3:

                ViewHolderLink viewHolderLink = (ViewHolderLink) holder;
                viewHolderLink.linkName.setText(campaignTrainingOutputList.get(position).getName());


                break;

            case 4:

                ViewHolderArticle viewHolderArticle = (ViewHolderArticle) holder;
                viewHolderArticle.articleText.setText(campaignTrainingOutputList.get(position).getArticle());

                break;
        }
    }


    @Override
    public int getItemCount() {

        return campaignTrainingOutputList.size();
    }


    public class ViewHolderPdf extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private RelativeLayout parentLayout;
        private TextView  trainingText;
        private ImageView downloadClick;




        public ViewHolderPdf(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            parentLayout = view.findViewById(R.id.rl_trainingListAdapter_pdf_parentLayout);
            trainingText = view.findViewById(R.id.tv_trainingListAdapter_pdf_docName);
            downloadClick = view.findViewById(R.id.iv_trainingListAdapter_pdf_downloadIcon);

            //setting on click
            parentLayout.setOnClickListener(this);
            downloadClick.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            CampaignTrainingOutput campaignTrainingOutput = campaignTrainingOutputList.get(clickPosition);


            switch (view.getId())
            {
                case R.id.rl_trainingListAdapter_pdf_parentLayout:

                    ((CampaignTrainingActivity)context).openDocumentUrl(campaignTrainingOutput);

                    break;

                case R.id.iv_trainingListAdapter_pdf_downloadIcon:

                    ((CampaignTrainingActivity)context).downloadDocumentUrl(campaignTrainingOutput);

                    break;
            }



        }

    }


    public class ViewHolderImage extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout parentLayout;
        private ImageView trainingImage;




        public ViewHolderImage(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            parentLayout = view.findViewById(R.id.ll_trainingListAdapter_image_parentLayout);
            trainingImage = view.findViewById(R.id.iv_trainingListAdapter_image_trainingImage);



        }

        @Override
        public void onClick(View view) {




        }




    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout parentLayout, playVideoClick;

        public ViewHolderVideo(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            parentLayout = view.findViewById(R.id.ll_trainingListAdapter_video_parentLayout);
            playVideoClick = view.findViewById(R.id.ll_trainingListAdapter_video_playVideoClick);

            //setting on click
            parentLayout.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            CampaignTrainingOutput campaignTrainingOutput = campaignTrainingOutputList.get(clickPosition);

            switch (view.getId())
            {
                case R.id.ll_trainingListAdapter_video_parentLayout:

                    String videoUrl = campaignTrainingOutput.getUrl();

                    //Preparing Intent
                    Intent videoIntent = new Intent(view.getContext(), PlayVideoActivity.class);

                    //Create the bundle
                    Bundle videoBundle = new Bundle();

                    //Add your data to bundle
                    videoBundle.putString(Constants.INTENT_KEY_SELECTED_VIDEO_URL,videoUrl);

                    //Add the bundle to the intent
                    videoIntent.putExtras(videoBundle);

                    view.getContext().startActivity(videoIntent);

                    break;
            }

        }
    }

    public class ViewHolderLink extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private RelativeLayout parentLayout;
        private TextView linkName;

        public ViewHolderLink(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            parentLayout = view.findViewById(R.id.rl_trainingListAdapter_link_parentLayout);
            linkName = view.findViewById(R.id.tv_campaignTrainingListAdapter_link_linkName);


            //setting on click
            parentLayout.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            CampaignTrainingOutput campaignTrainingOutput = campaignTrainingOutputList.get(clickPosition);


            switch (view.getId())
            {
                case R.id.rl_trainingListAdapter_link_parentLayout:

                    ((CampaignTrainingActivity)context).openLinkUrl(campaignTrainingOutput);

                    break;
            }
        }
    }

    public class ViewHolderArticle extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout parentLayout;
        private TextView articleText;


        public ViewHolderArticle(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            parentLayout = view.findViewById(R.id.ll_trainingListAdapter_article_parentLayout);

            articleText = view.findViewById(R.id.tv_campaignTrainingListAdapter_article_articleText);


        }


        @Override
        public void onClick(View view) {


        }
    }


}