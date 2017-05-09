package at.sw2017.financesolution;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import at.sw2017.financesolution.models.Transaction;

/**
 * Created by jk on 09.05.17.
 */

public class TransactionListViewAdapter extends BaseAdapter {
    private ArrayList<Transaction> transactionList;
    private ArrayList<Transaction> filteredTransactionList;

    Activity activity;
    TextView txtTransactionDate;
    TextView txtTransactionCategory;
    TextView txtTransactionDescription;
    TextView txtTransactionAmount;

    public TransactionListViewAdapter(Activity activity, ArrayList<Transaction> transactionList) {
        super();
        this.activity = activity;
        this.transactionList = new ArrayList<Transaction>(transactionList);
        this.filteredTransactionList = new ArrayList<Transaction>(transactionList);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.filteredTransactionList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.filteredTransactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.transaction_list_row, null);

            txtTransactionDate = (TextView) convertView.findViewById(R.id.row_date_text_view);
            txtTransactionCategory = (TextView) convertView.findViewById(R.id.row_category_text_view);
            txtTransactionDescription = (TextView) convertView.findViewById(R.id.row_description_text_view);
            txtTransactionAmount = (TextView) convertView.findViewById(R.id.row_amount_text_view);
        }

        Transaction currentTransaction = this.filteredTransactionList.get(position);
        txtTransactionDate.setText(DateFormat.getDateInstance().format(currentTransaction.getDate()));
        txtTransactionCategory.setText(currentTransaction.getCategory().getName());
        txtTransactionDescription.setText(currentTransaction.getDescription());
        txtTransactionAmount.setText(String.valueOf(currentTransaction.getAmount()));

        return convertView;
    }

    public void filter(String filterText) {
        this.filteredTransactionList.clear();
        if(filterText.isEmpty()) {
            this.filteredTransactionList.addAll(this.transactionList);
        }
        else {
            for(Transaction currentTransaction : this.transactionList) {
                String currentDescription = currentTransaction.getDescription();
                String currentCategoryName = currentTransaction.getCategory().getName();
                if(currentDescription.contains(filterText) || currentCategoryName.contains(filterText)) {
                    this.filteredTransactionList.add(currentTransaction);
                }
            }
        }

        notifyDataSetChanged();
    }
}
