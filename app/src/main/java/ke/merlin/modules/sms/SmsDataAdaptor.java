package ke.merlin.modules.sms;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.merlin.R;
import ke.merlin.models.sms.SmsOutgoing;

/**
 * Created by mecmurimi on 13/12/2017.
 */

public class SmsDataAdaptor extends RecyclerView.Adapter<SmsDataAdaptor.ViewHolder> implements Filterable {
    private ArrayList<SmsOutgoing> mArrayList;
    private ArrayList<SmsOutgoing> mFilteredList;

    public SmsDataAdaptor(ArrayList<SmsOutgoing> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public SmsDataAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_sms, viewGroup, false);
        return new SmsDataAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsDataAdaptor.ViewHolder viewHolder, int i) {

        Timestamp ts = mFilteredList.get(i).getCreationDate();
        Date date = new Date();
        date.setTime(ts.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        viewHolder.txt_msg_date.setText( (formattedDate != null) ? formattedDate : "");
        viewHolder.txt_msg.setText(mFilteredList.get(i).getMessage());
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

                    ArrayList<SmsOutgoing> filteredList = new ArrayList<>();

                    for (SmsOutgoing leadsOutcomes : mArrayList) {

                        if (leadsOutcomes.getMessage().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<SmsOutgoing>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_msg_date,txt_msg;
        public ViewHolder(View view) {
            super(view);

            txt_msg_date = (TextView)view.findViewById(R.id.txt_msg_date);
            txt_msg = (TextView)view.findViewById(R.id.txt_msg);

        }
    }

}
