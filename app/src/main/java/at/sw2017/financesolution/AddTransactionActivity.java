package at.sw2017.financesolution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

public class AddTransactionActivity extends AppCompatActivity {

    private final static String LOG_ADD_TRANSACTION = "CreateTransaction";

    private TextView textHeader;
    private EditText textDescription;
    private EditText textAmount;
    private Spinner categorySpinner;
    private DatePicker datePicker;
    private ToggleButton btnToggle;

    private FinanceDataConnector dataConnector = null;

    private Transaction currentTransaction;

    private final static boolean SIGN_DEFAULT_STATE = false; // false = minus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        textHeader = (TextView) findViewById(R.id.add_transaction_header);
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
        btnToggle = (ToggleButton) findViewById(R.id.sign);
        btnToggle.setChecked(SIGN_DEFAULT_STATE);

        // checking if we are going to edit transaction
        currentTransaction = null;
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long transaction_id = b.getLong("EDIT");
            Transaction currentTransaction = dataConnector.getTransaction(transaction_id);

            if(currentTransaction != null) {
                textHeader.setText("Edit Transaction");

                Category category = new Category();
                category = dataConnector.getCategory(currentTransaction.getCategoryID());
                Log.i(LOG_ADD_TRANSACTION, "CATEGORY (id = " + category.getDBID() + ")");

                String description = currentTransaction.getDescription();
                Double amount = currentTransaction.getAmount();
                Date date = currentTransaction.getDate();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);

                // Initializing activity elements
                textDescription.setText(description);
                textAmount.setText(amount.toString().replace("-",""));
                btnToggle.setChecked(!(amount < 0));
                datePicker.updateDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                int spinnerPos = adapterCategory.getPosition(category);
                categorySpinner.setSelection(spinnerPos);

                Log.i(LOG_ADD_TRANSACTION, "Editing Transaction (id = " + transaction_id + ")");
            }
        }

        // add button event for save button
        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTransaction();
            }
        });
    }

    private void makeTransaction() {
        Transaction transaction = new Transaction();
        Boolean updateTransaction = false;

        // Checking if we are editing transaction
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long transaction_id = b.getLong("EDIT");
            Transaction currentTransaction = dataConnector.getTransaction(transaction_id);

            if(currentTransaction != null) {
                transaction = currentTransaction;
                updateTransaction = true;
            }
        }

        double amount = Double.parseDouble(textAmount.getText().toString());

        if (!btnToggle.isChecked())
            amount = amount * -1;

        transaction.setDescription(textDescription.getText().toString());
        transaction.setAmount(amount);
        transaction.setCategory((Category)categorySpinner.getSelectedItem());
        transaction.setCategoryID(((Category)categorySpinner.getSelectedItem()).getDBID());

        int day     = datePicker.getDayOfMonth();
        int month   = datePicker.getMonth();
        int year    = datePicker.getYear();

        GregorianCalendar calendarBeg = new GregorianCalendar(year, month, day);
        Date transactionDate=calendarBeg.getTime();
        transaction.setDate(transactionDate);

        if(updateTransaction) {
            long id = dataConnector.updateTransaction(transaction);
            Log.i(LOG_ADD_TRANSACTION, "Edited Transaction (id = "+ transaction.getId() +")");
        } else {
            long id = dataConnector.createTransaction(transaction);
            Log.i(LOG_ADD_TRANSACTION, "Added new Transaction (id = " + id + ").");
        }

        this.finish();
    }
}
