package in.exuber.usmarket.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.exuber.usmarket.R;
import in.exuber.usmarket.activity.glossary.GlossaryActivity;
import in.exuber.usmarket.apimodels.glossary.glossaryoutput.GlossaryOutput;


public class GlossaryListAdapter extends BaseAdapter implements Filterable {

    //Declaring views
    public Context context;
    public List<GlossaryOutput> glossaryOutputList;
    public List<GlossaryOutput> filteredGlossaryOutputList;

    public GlossaryListAdapter(Context context, List<GlossaryOutput> glossaryOutputList) {

        this.context = context;
        this.glossaryOutputList = glossaryOutputList;
        this.filteredGlossaryOutputList = glossaryOutputList;
    }

    public class GlossaryHolder
    {
        private TextView glossaryText;
    }

    @Override
    public int getCount() {
        return filteredGlossaryOutputList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredGlossaryOutputList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GlossaryHolder holder;

        if(convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_glossary_listadapter, parent, false);
            holder = new GlossaryHolder();
            holder.glossaryText = convertView.findViewById(R.id.tv_glossaryListAdapter_terms);
            convertView.setTag(holder);
        }
        else
        {
            holder=(GlossaryHolder) convertView.getTag();
        }

        holder.glossaryText.setText(filteredGlossaryOutputList.get(position).getTerm());

        holder.glossaryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((GlossaryActivity)context).showGlossaryDetails(filteredGlossaryOutputList.get(position));

            }
        });


        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                final FilterResults oReturn = new FilterResults();
                final List<GlossaryOutput> results = new ArrayList<>();

                String charString = charSequence.toString();

                if (!charString.isEmpty())
                {
                    for (final GlossaryOutput glossaryOutput : glossaryOutputList) {

                        if (glossaryOutput.getTerm().toLowerCase().startsWith(charString))
                        {
                            results.add(glossaryOutput);
                        }

                    }
                    oReturn.values = results;
                }
                else
                {
                    oReturn.values = glossaryOutputList;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredGlossaryOutputList = (ArrayList<GlossaryOutput>) results.values;
                notifyDataSetChanged();

            }
        };
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
