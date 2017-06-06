package at.sw2017.financesolution;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

import at.sw2017.financesolution.models.Reminder;
import at.sw2017.financesolution.models.Transaction;

/**
 * Created by joe on 06.06.17.
 */

class ReminderListViewAdapter extends BaseAdapter {
    private ArrayList<Reminder> reminderList;
    private ArrayList<Reminder> filteredReminderList;

    Activity activity;
    TextView txtReminderDate;
    TextView txtReminderTitle;
    TextView txtReminderAmount;

    public ReminderListViewAdapter(ReminderActivity activity, ArrayList<Reminder> reminderList) {
        super();
        this.activity = activity;
        this.reminderList = new ArrayList<Reminder>(reminderList);
        this.filteredReminderList = new ArrayList<Reminder>(reminderList);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.filteredReminderList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.filteredReminderList.get(position);
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
            convertView = inflater.inflate(R.layout.reminder_list_row, null);

            txtReminderDate = (TextView) convertView.findViewById(R.id.row_reminder_date_text_view);
            txtReminderTitle = (TextView) convertView.findViewById(R.id.row_reminder_title_text_view);
            txtReminderAmount = (TextView) convertView.findViewById(R.id.row_reminder_amount_text_view);
        }

        Reminder currentReminder = this.filteredReminderList.get(position);
        txtReminderDate.setText(DateFormat.getDateInstance().format(currentReminder.getDate()));
        txtReminderTitle.setText(currentReminder.getTitle());
        txtReminderAmount.setText(String.valueOf(currentReminder.getAmount()));

        return convertView;
    }

    public void filter(String filterText) {
        this.filteredReminderList.clear();
        if(filterText.isEmpty()) {
            this.filteredReminderList.addAll(this.reminderList);
        }
        else {
            for(Reminder currentReminder : this.reminderList) {
                String currentTitle = currentReminder.getTitle();
                if(currentTitle.toLowerCase().contains(filterText.toLowerCase())) {
                    this.filteredReminderList.add(currentReminder);
                }
            }
        }
    }
}
