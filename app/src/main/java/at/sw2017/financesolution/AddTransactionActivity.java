package at.sw2017.financesolution;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Transaction;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        descriptionText = (EditText) findViewById(R.id.editDescription);

    }

    private void addTransaction() {
        FinanceDataConnector dataConnector =  FinanceDataConnectorImpl.getInstance(this.getApplicationContext());

        String description = descriptionText.getText().toString();
        descriptionText.setText(description+" WOH!");

        // TODO: Get other stuff from view, create transaction and save it through dataConnector to Database

    }

}
