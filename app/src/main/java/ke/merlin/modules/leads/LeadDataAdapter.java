package ke.merlin.modules.leads;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ke.merlin.R;
import ke.merlin.dao.stations.StationsDao;
import ke.merlin.dao.users.UsersDao;
import ke.merlin.models.customers.Customers;
import ke.merlin.models.stations.Stations;
import ke.merlin.models.users.Users;
import ke.merlin.modules.leads.outcomes.LeadOutcomesActivity;

/**
 * Created by mecmurimi on 01/10/2017.
 */

public class LeadDataAdapter extends RecyclerView.Adapter<LeadDataAdapter.ViewHolder> implements Filterable {
    private ArrayList<Customers> mArrayList;
    private ArrayList<Customers> mFilteredList;

    UsersDao usersDao;
    StationsDao stationsDao;

    private Activity activity;

    public LeadDataAdapter(Activity activity, ArrayList<Customers> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.activity = activity;

        usersDao = new UsersDao();
        stationsDao = new StationsDao();
    }

    @Override
    public LeadDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_leads, viewGroup, false);
        return new LeadDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadDataAdapter.ViewHolder viewHolder, int i) {

        final String id = mFilteredList.get(i).getId();
        final String firstname = mFilteredList.get(i).getFirstName();
        final String lastname = mFilteredList.get(i).getLastName();
        String lo = mFilteredList.get(i).getLoanOfficerId();
        String co = mFilteredList.get(i).getCollectionsOfficerId();
        String station = mFilteredList.get(i).getStationsId();

        Users loOff = usersDao.getUserById(mFilteredList.get(i).getLoanOfficerId());
        Users coOff = usersDao.getUserById(mFilteredList.get(i).getCollectionsOfficerId());
        Stations stations = stationsDao.getStationById(mFilteredList.get(i).getStationsId());

        viewHolder.tv_name.setText(mFilteredList.get(i).getFirstName() + " " + mFilteredList.get(i).getLastName());
        viewHolder.tv_lo.setText("LO: " +  loOff.getFirstName() + " " + loOff.getLastName() );
        viewHolder.tv_co.setText("CO: " + coOff.getFirstName() + " " + coOff.getLastName());
        viewHolder.tv_mobile.setText("Mobile: " + mFilteredList.get(i).getPrimaryPhone());
        viewHolder.tv_station.setText("Station: " + stations.getName());

        viewHolder.txt_l_outcomes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, LeadOutcomesActivity.class);
                i.putExtra("id", id);
                i.putExtra("first_name", firstname);
                i.putExtra("last_name", lastname);
                activity.startActivity(i);
            }
        });

        viewHolder.txt_l_convert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ConvertStepperActivity.class);
                i.putExtra("id", id);
                activity.startActivity(i);
            }
        });
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

                    ArrayList<Customers> filteredList = new ArrayList<>();

                    for (Customers customers : mArrayList) {

                        if (customers.getId().toLowerCase().contains(charString) || customers.getFirstName().toLowerCase().contains(charString)
                                || customers.getLastName().toLowerCase().contains(charString) || String.valueOf(customers.getPrimaryPhone()).toLowerCase().contains(charString)) {

                            filteredList.add(customers);
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
                mFilteredList = (ArrayList<Customers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_lo, tv_co, tv_mobile, tv_station, txt_l_outcomes, txt_l_convert;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_lo = (TextView)view.findViewById(R.id.tv_lo);
            tv_co = (TextView)view.findViewById(R.id.tv_co);
            tv_mobile = (TextView)view.findViewById(R.id.tv_mobile);
            tv_station = (TextView)view.findViewById(R.id.tv_station);
            txt_l_outcomes = (TextView) view.findViewById(R.id.txt_l_outcomes);
            txt_l_convert = (TextView) view.findViewById(R.id.txt_l_convert);

        }
    }
}