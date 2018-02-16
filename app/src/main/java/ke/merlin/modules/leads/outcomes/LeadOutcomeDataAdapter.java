package ke.merlin.modules.leads.outcomes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.merlin.R;
import ke.merlin.dao.customers.StLeadOutcomesDao;
import ke.merlin.models.customers.LeadsOutcomes;

public class LeadOutcomeDataAdapter extends RecyclerView.Adapter<LeadOutcomeDataAdapter.ViewHolder> implements Filterable {
    private ArrayList<LeadsOutcomes> mArrayList;
    private ArrayList<LeadsOutcomes> mFilteredList;
    private StLeadOutcomesDao stLeadOutcomesDao;

    public LeadOutcomeDataAdapter(ArrayList<LeadsOutcomes> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        stLeadOutcomesDao = new StLeadOutcomesDao();
    }

    @Override
    public LeadOutcomeDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_lead_outcomes, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadOutcomeDataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.txt_next_visit.setText("Next Visit Date: " + mFilteredList.get(i).getNextVisitDate());
        viewHolder.txt_outcome.setText("Outcome: " + stLeadOutcomesDao.getById(mFilteredList.get(i).getOutcomesId()).getName());
        viewHolder.txt_explanation.setText("Outcome Explanation " + mFilteredList.get(i).getOutcomeExplanation());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<LeadsOutcomes> filteredList = new ArrayList<>();

                    for (LeadsOutcomes leadsOutcomes : mArrayList) {

                        if (leadsOutcomes.getNextVisitDate().toLowerCase().contains(charString)) {

                            filteredList.add(leadsOutcomes);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<LeadsOutcomes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_next_visit,txt_outcome,txt_explanation;
        public ViewHolder(View view) {
            super(view);

            txt_next_visit = (TextView)view.findViewById(R.id.txt_next_visit);
            txt_outcome = (TextView)view.findViewById(R.id.txt_outcome);
            txt_explanation = (TextView)view.findViewById(R.id.txt_explanation);

        }
    }

}