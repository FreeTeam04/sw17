package at.sw2017.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity implements OnClickListener {

    Button buttonAdd = (Button) findViewById(R.id.buttonAdd);
    Button buttonSubtract = (Button) findViewById(R.id.buttonSubtract);
    Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
    Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
    Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
    Button buttonClear = (Button) findViewById(R.id.buttonClear);

    ArrayList<Button> numberButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        buttonAdd.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        buttonClear.setOnClickListener(this);

        setUpNumberButtonListener();
    }

    public void setUpNumberButtonListener() {
        for (int i = 0; i <= 9; i++) {
            String buttonName = "button" + i;

            int id = getResources().getIdentifier(buttonName, "id", R.class.getPackage().getName());

            Button button = (Button) findViewById(id);
            button.setOnClickListener(this);

            numberButtons.add(button);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
