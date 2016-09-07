package com.example.aleksandrabramovski.exchangeapplicationweb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView mtv1, mtv2, mtvDate;
    String mCurrentDate;
    private String url1 = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private String url2;
    private HandleXML obj;
    public Button mButtonShowWeather;
    public Button mButtonCurrency;
    public EditText mEditTextInputValue;
    public TextView mTextViewOutput;
    public String resultCurrency;
    public float resTemp;
    public static String selectedCurrency;
    public TextView mTextViewInCurrency;
    public TextView mTextViewInRoubles;
    public static float nominalInt;
    public static int i = 0, f = 0;
    public static String[] currencyIndexList = {"036", "944", "826"};
    public static String[] currencyArrayList = {};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonCurrency = (Button)findViewById(R.id.buttonChange);
        mEditTextInputValue = (EditText)findViewById(R.id.editTextInputValue);
        mTextViewOutput = (TextView)findViewById(R.id.textViewResultCurrencyExchange);
        mTextViewInCurrency = (TextView)findViewById(R.id.textViewInCurrency);
        mTextViewInRoubles = (TextView)findViewById(R.id.textViewInRoubles);



        //concerning dropdown list:
        final Spinner spinnerCurrency = (Spinner)findViewById(R.id.currencySpinnerList);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.currencyList,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);
        spinnerCurrency.setSelection(7);

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedPosition, long selectedId) {
                String[] choose = getResources().getStringArray(R.array.currencyList);
                selectedCurrency = choose[selectedPosition];
                String finalUrl = url1 + url2;
                mtv2.setText(finalUrl);

                obj = new HandleXML(finalUrl);
                obj.fetchXML();

                while (obj.parsingComplete) ;
//                mtv1.setText(obj.getIndex() + " к RUB");
                mtv1.setText(" к RUB");
                mtv2.setText(obj.getCountry() + " к " + HandleXML.nominalString);
                mTextViewInCurrency.setText(HandleXML.nameCurrency);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //concerning actual date:
        SimpleDateFormat mDate = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        mCurrentDate = mDate.format(calendar.getTime());
        TextView mTextViewCurrentDate = (TextView)findViewById(R.id.textViewDate);
        mTextViewCurrentDate.setText("Today is: " + mCurrentDate);
        url2 = mCurrentDate;

        mtv1 = (TextView)findViewById(R.id.textViewIndex);
        mtv2 = (TextView)findViewById(R.id.textViewCurrency);
    }


    private float convertRubToUSD(float currencyUSD){
        resTemp = Float.parseFloat(obj.getCountry());
        nominalInt = Float.parseFloat(HandleXML.nominalString);
        return (float) ((currencyUSD * nominalInt) / resTemp);
    }

    private float convertUSDToRub(float currencyRub){
        resTemp = Float.parseFloat(obj.getCountry());
        nominalInt = Float.parseFloat(HandleXML.nominalString);
        return (float) ((currencyRub / nominalInt) * resTemp);
    }


    public void onClickChange(View view) {
        RadioButton mRadioButtonChange = (RadioButton) findViewById(R.id.radioButtonRUBtoUSD);
        if (mtv2.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Press Get Rate", Toast.LENGTH_SHORT).show();
            return;
        } else if (mEditTextInputValue.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Insert input value, please", Toast.LENGTH_SHORT).show();
        } else {
            float inputValue = Float.parseFloat(mEditTextInputValue.getText().toString());
            if (inputValue == 0){
                Toast.makeText(getApplicationContext(), "Value cannot be Zero", Toast.LENGTH_SHORT).show();
            } else {
                if (mRadioButtonChange.isChecked()) {
                    mTextViewOutput.setText("В " + obj.getIndex() + ": " + String.valueOf(convertRubToUSD(inputValue)));
                } else {
                    mTextViewOutput.setText("В RUB: " + String.valueOf(convertUSDToRub(inputValue)));
                }
            }
        }
    }
}

