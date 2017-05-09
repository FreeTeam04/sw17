package at.sw2017.financesolution;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

public class AddTransactionActivity extends AppCompatActivity {

    private final static String LOG_ADD_TRANSACTION = "CreateTransaction";

    private EditText textDescription;
    private EditText textAmount;
    private Spinner categorySpinner;
    private DatePicker datePicker;

    private FinanceDataConnector dataConnector = null;

    private final static boolean SIGN_DEFAULT_STATE = false; // false = minus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        textDescription = (EditText) findViewById(R.id.editDescription);
        textAmount = (EditText) findViewById(R.id.editAmount);
        datePicker = (DatePicker) findViewById(R.id.datepickerDate);

        // add categories to spinner
        dataConnector = FinanceDataConnectorImpl.getInstance(getApplicationContext());
        ArrayList<Category> categories = dataConnector.getAllCategories();

        categorySpinner = (Spinner) findViewById(R.id.spinnerGategory);
        ArrayAdapter<Category> adapterCategory = new ArrayAdapter<Category>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapterCategory);

        // set default of sign button to off
        ToggleButton btnToggle = (ToggleButton) findViewById(R.id.sign);
        btnToggle.setChecked(SIGN_DEFAULT_STATE);

        // add button event for save button
        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });
    }

    private void addTransaction() {
        Transaction newTransaction = new Transaction();
        newTransaction.setDescription(textDescription.getText().toString());
        newTransaction.setAmount(Double.parseDouble(textAmount.getText().toString()));
        newTransaction.setCategory((Category)categorySpinner.getSelectedItem());
        newTransaction.setCategoryID(((Category)categorySpinner.getSelectedItem()).getDBID());

        int day     = datePicker.getDayOfMonth();
        int month   = datePicker.getMonth()+1;
        int year    = datePicker.getYear();

        GregorianCalendar calendarBeg = new GregorianCalendar(year, month, day);
        Date transactionDate=calendarBeg.getTime();
        newTransaction.setDate(transactionDate);

        long id = dataConnector.createTransaction(newTransaction);
        Log.i(LOG_ADD_TRANSACTION, "Added new Transaction to db (id = " + id + ").");
    }

}
