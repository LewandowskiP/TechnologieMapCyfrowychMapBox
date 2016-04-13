package com.example.arravilar.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        String routeName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        EditText txtSaveRoute = (EditText)findViewById(R.id.txtSaveRoute);
        txtSaveRoute.setText(routeName);
    }
}
