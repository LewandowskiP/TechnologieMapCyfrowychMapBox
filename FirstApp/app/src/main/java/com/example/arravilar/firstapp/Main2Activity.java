package com.example.arravilar.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EditText txtSaveRoute = (EditText)findViewById(R.id.txtSaveRoute);
        txtSaveRoute.setText("TmpRoute");
    }

    public void saveClick(View v){
       EditText txtSaveRoute = (EditText)findViewById(R.id.txtSaveRoute);

        if (GlobalValues.getInstance().getRouteList()==null){
            RouteList newList = new RouteList(this);
            GlobalValues.getInstance().setRouteList(newList);
            newList.addRoute(txtSaveRoute.getText().toString());
        }
        else{
            RouteList currentList = GlobalValues.getInstance().getRouteList();
            currentList.addRoute(txtSaveRoute.getText().toString());
        }
<<<<<<< HEAD

        GlobalValues.getInstance().setRecordRoute(true);
        onBackPressed();
=======
        super.onBackPressed();
>>>>>>> 40bcee1d8ada3e43e47f7b4d243fb1ad8cc8f09d

    }
}
