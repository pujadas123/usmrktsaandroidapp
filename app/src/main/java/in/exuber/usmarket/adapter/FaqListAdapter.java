package in.exuber.usmarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.apimodels.faq.faqoutput.FAQOutput;


public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.ViewHolder> {

    //Declaring variables
    private Context context;
    private List<FAQOutput> faqOutputList;

    public FaqListAdapter(Context context, List<FAQOutput> faqOutputList) {
        this.context = context;
        this.faqOutputList = faqOutputList;
    }

    @Override
    public FaqListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_faq_listadapter, viewGroup, false);
        return new FaqListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FaqListAdapter.ViewHolder viewHolder, int position) {

        viewHolder.faqCount.setText((position+1)+".");
        viewHolder.faqQuestion.setText(faqOutputList.get(position).getQuestion());
        viewHolder.faqAnswer.setText(faqOutputList.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqOutputList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Declaring views
        private TextView faqCount, faqQuestion, faqAnswer;

        public ViewHolder(View itemView) {
            super(itemView);

            //Initialising views
            faqCount = itemView.findViewById(R.id.tv_faqListAdapter_faqCount);
            faqQuestion = itemView.findViewById(R.id.tv_faqListAdapter_faqQuestion);
            faqAnswer = itemView.findViewById(R.id.tv_faqListAdapter_faqAnswer);}
    }
}
