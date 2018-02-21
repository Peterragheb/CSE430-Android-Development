package com.example.peter.lab1_currency_converter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Spinner sp1, sp2;
    TextView results;
    EditText input;
    Button convert;
    double value;
    final String ERROR_EMPTY_INPUT = "Please enter a value";
    final String ERROR_TYPE_INPUT = "Please enter a valid value";
    final String ERROR_SPINNERS_MATCH = "Conversion cannot be from and to the same Currency";
    final String REGEX_NUMBERS = "[0-9]+";
    final String RESULTS_DEFAULT_VALUE = "Results";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp1 = (Spinner) findViewById(R.id.sp_options1);
        sp2 = (Spinner) findViewById(R.id.sp_options2);
        convert = (Button) findViewById(R.id.btn_convert);
        input = (EditText) findViewById(R.id.et_input);
        results = (TextView) findViewById(R.id.tv_results);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);
        sp2.setAdapter(arrayAdapter);
        sp1.setSelection(1);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp1.getSelectedItemPosition()!=sp2.getSelectedItemPosition()) {
                    if (!input.getText().equals("")) {
                        if (input.getText().toString().matches(REGEX_NUMBERS)) {
                            value = Double.parseDouble(input.getText().toString());
                            if (sp1.getSelectedItemPosition()==0){
                                //convert from egp to usd
                                value/=17.6;
                            }
                            else if (sp1.getSelectedItemPosition()==1){
                                //convert from usd to egp
                                value*=17.6;
                            }

                            // to make the decimal precision 2 digits only
                            DecimalFormat numberFormat = new DecimalFormat("0.00");
                            results.setText(String.valueOf(numberFormat.format(value)));
                           // results.setText(String.valueOf(value));
                            //to force hide virtual keyboard
                            View view1 = MainActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, ERROR_TYPE_INPUT, Toast.LENGTH_SHORT).show();
                            results.setText(RESULTS_DEFAULT_VALUE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, ERROR_EMPTY_INPUT, Toast.LENGTH_SHORT).show();
                        results.setText(RESULTS_DEFAULT_VALUE);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, ERROR_SPINNERS_MATCH, Toast.LENGTH_SHORT).show();
                    results.setText(RESULTS_DEFAULT_VALUE);
                }
            }
        });
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {//to reload saved state after rotation
        super.onRestoreInstanceState(savedInstanceState);
        results.setText(savedInstanceState.getString("Results"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {//to save state before rotation
        super.onSaveInstanceState(outState);
        outState.putString("Results",results.getText().toString());
    }

}
