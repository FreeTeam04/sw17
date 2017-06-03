package at.sw2017.financesolution;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText eDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        eDate = (EditText) findViewById(R.id.edittext_add_reminder_date);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        eDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
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


}
