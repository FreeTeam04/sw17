package at.sw2017.financesolution;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.sw2017.financesolution.helper.FinanceDataConnector;
import at.sw2017.financesolution.helper.FinanceDataConnectorImpl;
import at.sw2017.financesolution.models.Category;
import at.sw2017.financesolution.models.Transaction;

public class AddCategoryActivity extends AppCompatActivity {

    private final static String LOG_ADD_CATEGORY = "CreateCategory";

    private TextView textHeader;
    private EditText textName;

    private FinanceDataConnector dataConnector = null;

    private Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        textHeader = (TextView) findViewById(R.id.add_category_header);
        textName = (EditText) findViewById(R.id.editName);

        dataConnector = FinanceDataConnectorImpl.getInstance(getApplicationContext());

        // checking if we are going to edit transaction
        currentCategory = null;
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long category_id = b.getLong("EDIT");
            currentCategory = dataConnector.getCategory(category_id);

            if(currentCategory != null) {
                textHeader.setText("Edit Category");

                String name = currentCategory.getName();

                // Initializing activity elements
                textName.setText(name);

                Log.i(LOG_ADD_CATEGORY, "Editing Category (id = " + category_id + ")");
            }

        }


        // add button event for save button
        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCategory();
            }
        });

    }

    private void makeCategory() {
        Category category = new Category();
        Boolean updateCategory = false;

        // Checking if we are editing transaction
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long category_id = b.getLong("EDIT");
            Category currentCategory = dataConnector.getCategory(category_id);

            if(currentCategory != null) {
                category = currentCategory;
                updateCategory = true;
            }
        }

        // check if fields are filled

        if(textName.getText().toString().isEmpty()){
            Toast.makeText(AddCategoryActivity.this, "No name", Toast.LENGTH_LONG).show();
            return;
        }

        category.setName(textName.getText().toString());

        if(updateCategory) {
            long id = dataConnector.updateCategory(category);
            Log.i(LOG_ADD_CATEGORY, "Edited Category (id = "+ category.getDBID() +")");
        } else {
            long id = dataConnector.createCategory(category);
            Log.i(LOG_ADD_CATEGORY, "Added new Category (id = " + id + ").");
        }

        this.finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}
