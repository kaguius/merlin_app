package ke.merlin.modules.loans;

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
import ke.merlin.dao.loans.StLoansArrearsStatusDao;
import ke.merlin.models.loans.Loans;
import ke.merlin.models.loans.StLoansArrearsStatus;
import ke.merlin.models.sms.SmsOutgoing;
import ke.merlin.modules.sms.SmsDataAdaptor;

/**
 * Created by mecmurimi on 13/12/2017.
 */

public class LoansdataAdaptor extends RecyclerView.Adapter<LoansdataAdaptor.ViewHolder> implements Filterable {
    private ArrayList<Loans> mArrayList;
    private ArrayList<Loans> mFilteredList;

    public LoansdataAdaptor(ArrayList<Loans> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public LoansdataAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_loans, viewGroup, false);
        return new LoansdataAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LoansdataAdaptor.ViewHolder viewHolder, int i) {

        StLoansArrearsStatusDao stLoansArrearsStatusDao = new StLoansArrearsStatusDao();
        StLoansArrearsStatus stLoansArrearsStatus = stLoansArrearsStatusDao.getById(mFilteredList.get(i).getArrearsStatusId());

        double total = (mFilteredList.get(i).getAmount() + mFilteredList.get(i).getInterest() + mFilteredList.get(i).getFees()) - mFilteredList.get(i).getEarlySettlement();
        String total_s = String.valueOf(total);
        String amount_s = String.valueOf(mFilteredList.get(i).getAmount());
        String interest_s = String.valueOf(mFilteredList.get(i).getInterest());

        viewHolder.loan_code.setText(mFilteredList.get(i).getId());
        viewHolder.tr_code.setText(mFilteredList.get(i).getTransactionCode());
        viewHolder.amount.setText(amount_s);
        viewHolder.interest.setText(interest_s);
        viewHolder.total.setText(total_s);
        viewHolder.date.setText(mFilteredList.get(i).getLoanDate());
        viewHolder.due.setText(mFilteredList.get(i).getLoanDueDate());
        viewHolder.status.setText(stLoansArrearsStatus.getName());
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

                    ArrayList<Loans> filteredList = new ArrayList<>();

                    for (Loans leadsOutcomes : mArrayList) {

                        if (leadsOutcomes.getId().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<Loans>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView loan_code, tr_code, amount, interest, total, date, due, status;

        public ViewHolder(View view) {
            super(view);

            loan_code = (TextView) view.findViewById(R.id.loan_code);
            tr_code = (TextView) view.findViewById(R.id.tr_code);
            amount = (TextView) view.findViewById(R.id.amount);
            interest = (TextView) view.findViewById(R.id.interest);
            total = (TextView) view.findViewById(R.id.total);
            date = (TextView) view.findViewById(R.id.date);
            due = (TextView) view.findViewById(R.id.due);
            status = (TextView) view.findViewById(R.id.status);

        }
    }

}