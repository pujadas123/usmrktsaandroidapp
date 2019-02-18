package in.exuber.usmarket.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.notification.NotificationActivity;
import in.exuber.usmarket.apimodels.notification.notificationoutput.NotificationOutput;
import in.exuber.usmarket.utils.Constants;


public class NotificationListAdapter extends RecyclerView.Adapter {

    //Declaring variables
    private Context context;
    private List<NotificationOutput> notificationOutputList;


    public NotificationListAdapter(Context context, List<NotificationOutput> notificationOutputList) {

        this.context = context;
        this.notificationOutputList = notificationOutputList;

    }

    @Override
    public int getItemViewType(int position) {

        if (notificationOutputList.get(position).getSeen().equalsIgnoreCase(Constants.NOTIFICATION_SEEN_STATUS))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new ViewHolderRead(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notificationread_listadapter, parent, false));
            case 1:
                return new ViewHolderUnread(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notificationunread_listadapter, parent, false));

        }
        return null;
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {

            //Setting values

            case 0:
                ViewHolderRead viewHolder0 = (ViewHolderRead)holder;

                long notificationReadTimeMillis = notificationOutputList.get(position).getCreatedOn();
                DateFormat formatterRead = new SimpleDateFormat("E, dd MMM, yyyy", Locale.US);
                String notificationReadTimeString = formatterRead.format(notificationReadTimeMillis);

                viewHolder0.notificationReadHeader.setText(context.getString(R.string.notification_header)+(position+1));
                viewHolder0.notificationReadDescription.setText(notificationOutputList.get(position).getTypeName());
                viewHolder0.notificationReadDate.setText(notificationReadTimeString);

                break;

            case 1:
                ViewHolderUnread viewHolder1 = (ViewHolderUnread)holder;

                long notificationUnreadTimeMillis = notificationOutputList.get(position).getCreatedOn();
                DateFormat formatterUnread = new SimpleDateFormat("E, dd MMM, yyyy", Locale.US);
                String notificationUnreadTimeString = formatterUnread.format(notificationUnreadTimeMillis);

                viewHolder1.notificationUnreadHeader.setText(context.getString(R.string.notification_header)+(position+1));
                viewHolder1.notificationUnreadDescription.setText(notificationOutputList.get(position).getTypeName());
                viewHolder1.notificationUnreadDate.setText(notificationUnreadTimeString);

                break;
        }
    }

   


    @Override
    public int getItemCount() {

        return notificationOutputList.size();
    }


    public class ViewHolderRead extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout notificationReadParentLayout;
        private TextView notificationReadHeader, notificationReadDescription, notificationReadDate;




        public ViewHolderRead(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            notificationReadParentLayout = view.findViewById(R.id.ll_notificationListReadAdapter_parentLayout);
            notificationReadHeader = view.findViewById(R.id.tv_notificationListReadAdapter_notificationHeader);
            notificationReadDescription = view.findViewById(R.id.tv_notificationListReadAdapter_notificationDescription);
            notificationReadDate = view.findViewById(R.id.tv_notificationListReadAdapter_notificationDate);

            notificationReadParentLayout.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            NotificationOutput notificationOutput =notificationOutputList.get(clickPosition);

            switch (view.getId())
            {
                case R.id.ll_notificationListReadAdapter_parentLayout:

                    ((NotificationActivity)context).moveToPage(notificationOutput);


                    break;
            }

        }

    }


    public class ViewHolderUnread extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Declaring views
        private LinearLayout notificationUnreadParentLayout;
        private TextView notificationUnreadHeader, notificationUnreadDescription, notificationUnreadDate;




        public ViewHolderUnread(View view) {
            super(view);
            view.setOnClickListener(this);

            //Initialising views
            notificationUnreadParentLayout = view.findViewById(R.id.ll_notificationListUnreadAdapter_parentLayout);
            notificationUnreadHeader = view.findViewById(R.id.tv_notificationListUnreadAdapter_notificationHeader);
            notificationUnreadDescription = view.findViewById(R.id.tv_notificationListUnreadAdapter_notificationDescription);
            notificationUnreadDate = view.findViewById(R.id.tv_notificationListUnreadAdapter_notificationDate);

            notificationUnreadParentLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int clickPosition = getLayoutPosition();
            NotificationOutput notificationOutput =notificationOutputList.get(clickPosition);



            switch (view.getId())
            {
                case R.id.ll_notificationListUnreadAdapter_parentLayout:


                    ((NotificationActivity)context).updateSeenAndProceed(notificationOutput);

                    break;

            }
        }

    }

}