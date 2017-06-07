package at.sw2017.financesolution;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Reminder;
import at.sw2017.financesolution.models.Transaction;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText eTitle;
    private EditText eAmout;
    private EditText eDate;
    private EditText eTime;
    private Button bSave;
    private Button bDelete;

    private Calendar calendar;

    private FinanceDataConnector dataConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataConnector = FinanceDataConnectorImpl.getInstance(getApplicationContext());
        calendar = Calendar.getInstance();

        eTitle = (EditText) findViewById(R.id.edittext_add_reminder_title);
        eAmout = (EditText) findViewById(R.id.edittext_add_reminder_amount);

        eDate = (EditText) findViewById(R.id.edittext_add_reminder_date);
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dateFragment.show(ft, "dialog");
            }
        });
        eDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(Calendar.getInstance().getTime()));

        eTime = (EditText) findViewById(R.id.edittext_add_reminder_time);
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timeFragment = new TimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                timeFragment.show(ft, "dialog");
            }
        });
        eTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()));

        bDelete = (Button) findViewById(R.id.button_delete_reminder);
        bDelete.getBackground().setColorFilter(0xDFDF0044, PorterDuff.Mode.MULTIPLY);

        final Bundle b = getIntent().getExtras();
        if(b == null) {
            bDelete.setVisibility(View.INVISIBLE);
        }
        else {
            final long reminderId = b.getLong("EDIT");
            final Reminder reminder = dataConnector.getReminder(reminderId);

            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteReminder(reminder);
                }
            });

            eTitle.setText(reminder.getTitle());
            Double amount = reminder.getAmount();
            eAmout.setText(amount.toString());
            eDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(reminder.getDate()));
            eTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(reminder.getDate()));
            calendar.setTime(reminder.getDate());
        }

        bSave = (Button) findViewById(R.id.button_save_reminder);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminder();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        eDate = (EditText) findViewById(R.id.edittext_add_reminder_date);
        calendar.set(year, month, day);
        eDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        eDate = (EditText) findViewById(R.id.edittext_add_reminder_time);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        eDate.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
    }

    private void createReminder() {
        Reminder reminder = new Reminder();
        Boolean updateReminder = false;

        // Checking if we are editing transaction
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long reminder_id = b.getLong("EDIT");
            Reminder currentReminder = dataConnector.getReminder(reminder_id);

            if(currentReminder != null) {
                reminder = currentReminder;
                updateReminder = true;
            }
        }

        // check if fields are filled
        if(eTitle.getText().toString().isEmpty()) {
            Toast.makeText(AddReminderActivity.this, "No title", Toast.LENGTH_LONG).show();
            return;
        }

        String amountString = eAmout.getText().toString();
        if (amountString.isEmpty() || amountString.equals(".")) {
            Toast.makeText(AddReminderActivity.this, "No amount", Toast.LENGTH_LONG).show();
            return;
        }

        double amount = Double.parseDouble(amountString);

        reminder.setTitle(eTitle.getText().toString());
        reminder.setAmount(amount);

        Date reminderDate = calendar.getTime();

        reminder.setDate(reminderDate);

        long id;

        if(updateReminder) {
            id = dataConnector.updateReminder(reminder);
        }
        else {
            id = dataConnector.createReminder(reminder);
        }

        reminder.setId(id);

        manageAlarm(reminder, updateReminder, false);

        this.finish();
    }

    private void deleteReminder(Reminder reminder) {
        dataConnector.removeReminder(reminder);
        manageAlarm(reminder, false, true);
        this.finish();
    }

    private void manageAlarm(Reminder reminder, boolean updateReminder, boolean deleteReminder) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("REMINDER_TITLE", reminder.getTitle());
        intentAlarm.putExtra("REMINDER_ID", reminder.getId());

        int intentId = (int) reminder.getId();

        PendingIntent pendingIntent;

        if(updateReminder || deleteReminder) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intentId, intentAlarm, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), intentId, intentAlarm, PendingIntent.FLAG_ONE_SHOT);

        if(!deleteReminder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.getDate().getTime(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getDate().getTime(), pendingIntent);
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (AddReminderActivity)getActivity(), year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), (AddReminderActivity)getActivity(), hour, min, true);
        }
    }
}
