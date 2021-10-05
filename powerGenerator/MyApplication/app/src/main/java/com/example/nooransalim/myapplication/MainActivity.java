package com.example.nooransalim.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {


    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;

    double p2, p3, p4, p5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText2 = (EditText) findViewById(R.id.editText2);
        Button b2 = (Button) findViewById(R.id.button);
        final TextView textView5 = (TextView) findViewById(R.id.textView5);

        b2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try {
                    String s2 = editText2.getText()+ "";
                    double a = Math.pow(2.00,Double.parseDouble(s2));
                    // Round to two decimal places
                    String test2 = String.format("%.2f", a);
                    textView5.setText(test2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Only numerical inputs", e.getMessage());
                }

            }
        });

        editText3 = (EditText) findViewById(R.id.editText3);
        Button b3 = (Button) findViewById(R.id.button2);
        final TextView textView6 = (TextView) findViewById(R.id.textView6);

        b3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try{
                    String s3 = editText3.getText()+ "";
                    double b = Math.pow(3.00,Double.parseDouble(s3));
                    // Round to two decimal places
                    String test3 = String.format("%.2f", b);
                    textView6.setText(test3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Only numerical inputs", e.getMessage());
                }
            }
        });

        editText4 = (EditText) findViewById(R.id.editText4);
        Button b4 = (Button) findViewById(R.id.button3);
        final TextView textView7 = (TextView) findViewById(R.id.textView7);

        b4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try{
                    String s4 = editText4.getText()+ "";
                    double c = Math.pow(4.00,Double.parseDouble(s4));
                    // Round to two decimal places
                    String test4 = String.format("%.2f", c);
                    textView7.setText(test4);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Only numerical inputs", e.getMessage());
                }
            }
        });

        editText5 = (EditText) findViewById(R.id.editText5);
        Button b5 = (Button) findViewById(R.id.button4);
        final TextView textView8 = (TextView) findViewById(R.id.textView8);

        b5.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                try{
                    String s5 = editText5.getText()+ "";
                    double dd = Math.pow(5.00,Double.parseDouble(s5));
                    // Round to two decimal places
                    String test5 = String.format("%.2f", dd);
                textView8.setText(test5);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Only numerical inputs", e.getMessage());
                }
            }
        });

    }


    }

