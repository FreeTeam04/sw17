package at.sw2017.financesolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Reminder;
import at.sw2017.financesolution.models.Transaction;

public class ReminderActivity extends AppCompatActivity {

    private static final int EDIT_REMINDER_ACTIVIY = 0;

    private FinanceDataConnector financeDataConnector;

    public EditText reminderSearchField;
    public ListView reminderListView;

    private ReminderListViewAdapter reminderListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        financeDataConnector = FinanceDataConnectorImpl.getInstance(getApplicationContext());

        reminderListView = (ListView) findViewById(R.id.reminder_list_view);
        final ArrayList<Reminder> reminderList = financeDataConnector.getAllReminders();

        reminderListViewAdapter = new ReminderListViewAdapter(this, reminderList);
        reminderListView.setAdapter(reminderListViewAdapter);
        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Reminder reminder = (Reminder) reminderListView.getItemAtPosition(position);
                Intent editReminderIntent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                editReminderIntent.putExtra("EDIT", reminder.getId());
                startActivityForResult(editReminderIntent, EDIT_REMINDER_ACTIVIY);
            }
        });

        reminderSearchField = (EditText) findViewById(R.id.reminder_search_field);

        reminderSearchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String filterText = reminderSearchField.getText().toString();
                reminderListViewAdapter.filter(filterText);
                reminderListView.setAdapter(reminderListViewAdapter);
                //transactionListView.invalidate();
                //transactionListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_REMINDER_ACTIVIY) {
            refreshAdapter();
        }
    }

    public void refreshAdapter() {
        ArrayList<Reminder> reminderList = financeDataConnector.getAllReminders();
        reminderListViewAdapter = new ReminderListViewAdapter(this, reminderList);
        reminderListView.setAdapter(reminderListViewAdapter);
    }
}
