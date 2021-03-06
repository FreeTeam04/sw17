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
import android.support.v7.widget.Toolbar;
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

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final static String LOG_ADD_TRANSACTION = "CreateTransaction";

    private TextView textHeader;
    private EditText textDescription;
    private EditText textAmount;
    private Spinner categorySpinner;
    private TextView textDate;
    private ToggleButton btnToggle;
    private ImageView photoView;

    private Uri photoUri = null;
    private boolean hasPhotoPermissions;
    private Calendar calendar;

    private FinanceDataConnector dataConnector = null;

    private Transaction currentTransaction;

    private final static boolean SIGN_DEFAULT_STATE = false; // false = minus
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CODE_CAMERA_STORAGE = 4545;
    static final int THUMBNAIL_SIZE = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Transaction");
        setSupportActionBar(toolbar);


        textDescription = (EditText) findViewById(R.id.editDescription);
        textAmount = (EditText) findViewById(R.id.editAmount);
        textDate = (TextView) findViewById(R.id.editDate);
        photoView = (ImageView) findViewById(R.id.photoView);

        // add categories to spinner
        dataConnector = FinanceDataConnectorImpl.getInstance(getApplicationContext());
        ArrayList<Category> categories = dataConnector.getAllCategories();

        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<Category> adapterCategory = new ArrayAdapter<Category>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapterCategory);

        // set default of sign button to off
        btnToggle = (ToggleButton) findViewById(R.id.sign);
        btnToggle.setChecked(SIGN_DEFAULT_STATE);

        calendar = Calendar.getInstance();

        // checking if we are going to edit transaction
        currentTransaction = null;
        Bundle b = getIntent().getExtras();
        if(b != null) {
            long transaction_id = b.getLong("EDIT");
            Transaction currentTransaction = dataConnector.getTransaction(transaction_id);

            if(currentTransaction != null) {
                toolbar.setTitle("Edit Transaction");

                Category category = new Category();
                category = dataConnector.getCategory(currentTransaction.getCategoryID());
                Log.i(LOG_ADD_TRANSACTION, "CATEGORY (id = " + category.getDBID() + ")");

                String description = currentTransaction.getDescription();
                Double amount = currentTransaction.getAmount();

                if (currentTransaction.getPhotoPath().isEmpty()) {
                    photoUri = null;
                } else {
                    photoUri = Uri.parse(currentTransaction.getPhotoPath());
                }

                // Initializing activity elements
                textDescription.setText(description);
                textAmount.setText(amount.toString().replace("-",""));
                btnToggle.setChecked(!(amount < 0));

                calendar.setTime(currentTransaction.getDate());

                int spinnerPos = adapterCategory.getPosition(category);
                categorySpinner.setSelection(spinnerPos);

                try {
                    setPhotoThumbnail();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.i(LOG_ADD_TRANSACTION, "Editing Transaction (id = " + transaction_id + ")");
            }
        }

        // set Date label
        textDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));



        // add button event for save button
        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTransaction();
            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (hasPhotoPermissions) {
                if (photoUri == null) {
                    takePhoto();
                } else {
                    openPhoto();
                }
            }

            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dateFragment.show(ft, "dialog");
            }
        });

        // Get permission for storage read/write (photo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_STORAGE);
            }
        } else {
            Log.i("Permission", "Granted");
            hasPhotoPermissions = true;
        }

    }

    // Permissions callback for API > 23
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Granted");
                    hasPhotoPermissions = true;
                } else {
                    Log.e("Permission", "Denied");
                    hasPhotoPermissions = false;
                }
            }
        }
    }

    private void takePhoto(){

        if(hasPhotoPermissions) {


            // check / create image directory
            boolean dirExists = true;
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "sw17");
            if (!dir.exists()) {
                dirExists = dir.mkdirs();
            }
            if (!dirExists) {
                Toast.makeText(AddTransactionActivity.this, "Error creating directory", Toast.LENGTH_LONG).show();
                return;
            }


            // filename
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String fileName = "transaction-" + simpleDateFormat.format(new Date()) + ".jpg";

            // create file
            File file = new File(dir, fileName);

            photoUri = FileProvider.getUriForFile(AddTransactionActivity.this, AddTransactionActivity.this.getApplicationContext().getPackageName() + ".provider", file);

            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            takePhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                Log.d(LOG_ADD_TRANSACTION, "Starting Cam Intent");
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // got image
        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if (resultCode == RESULT_OK) {

                if (photoUri != null) {
                    try {
                        setPhotoThumbnail();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // cancelled or failed: delete file at photoUri and set Uri null
                if (photoUri != null) {

                    File f = new File(photoUri.getPath());
                    if(f.exists()) {
                        this.getContentResolver().delete(photoUri, null, null);
                    }
                    photoUri = null;
                }
            }

        }


    }

    void setPhotoThumbnail() throws IOException {

        if(photoUri != null) {

            // get thumbnail
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri), THUMBNAIL_SIZE, THUMBNAIL_SIZE);

            // rotate 90deg
            Matrix transformationMatrix = new Matrix();
            transformationMatrix.postRotate(90);
            Bitmap rotatedThumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), transformationMatrix, true);
            RoundedBitmapDrawable roundedThumbnail = RoundedBitmapDrawableFactory.create(getResources(), rotatedThumbnail);
            float cornerRadius = (float) rotatedThumbnail.getWidth() * 0.15f;
            roundedThumbnail.setCornerRadius(cornerRadius);

           // photoView.setImageBitmap(rotatedThumbnail);
            photoView.setImageDrawable(roundedThumbnail);
        }
    }

    private void openPhoto() {

        if(photoUri != null) {

            Intent openPhotoIntent = new Intent(Intent.ACTION_VIEW, photoUri);
            openPhotoIntent.setDataAndType(photoUri, "image/*");
            openPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openPhotoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(openPhotoIntent);
        }
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

        // check if fields are filled

        if(textDescription.getText().toString().isEmpty()){
            Toast.makeText(AddTransactionActivity.this, "No description", Toast.LENGTH_LONG).show();
            return;
        }

        String amountString = textAmount.getText().toString();
        if (amountString.isEmpty() || amountString.equals(".")) {
            Toast.makeText(AddTransactionActivity.this, "No amount", Toast.LENGTH_LONG).show();
            return;
        }

        double amount = Double.parseDouble(amountString);

        if (!btnToggle.isChecked())
            amount = amount * -1;

        transaction.setDescription(textDescription.getText().toString());
        transaction.setAmount(amount);
        transaction.setCategory((Category)categorySpinner.getSelectedItem());
        transaction.setCategoryID(((Category)categorySpinner.getSelectedItem()).getDBID());


        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);

        Date transactionDate=calendar.getTime();

        transaction.setDate(transactionDate);

        if (photoUri != null) {
            transaction.setPhotoPath(photoUri.toString());
        } else {
            transaction.setPhotoPath("");
        }

        if(updateTransaction) {
            long id = dataConnector.updateTransaction(transaction);
            Log.i(LOG_ADD_TRANSACTION, "Edited Transaction (id = "+ transaction.getId() +")");
        } else {
            long id = dataConnector.createTransaction(transaction);
            Log.i(LOG_ADD_TRANSACTION, "Added new Transaction (id = " + id + ").");
        }
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day);
        Log.d(LOG_ADD_TRANSACTION, year + " " + month + " " + day);
        textDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (AddTransactionActivity)getActivity(), year, month, day);
        }
    }

    @Override
    public void onBackPressed()
    {
        // override to clean unused photo
        if (photoUri != null) {
            // check if file exists
            File f = new File(photoUri.getPath());
            if(f.exists()){
                this.getContentResolver().delete(photoUri, null, null);
            }
            photoUri = null;
        }
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

}
