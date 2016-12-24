package com.trimph.dialdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("density::", getResources().getDisplayMetrics().density + "");
        Log.e("density::", getResources().getDisplayMetrics().densityDpi + "");


        final DialView dialView = (DialView) findViewById(R.id.dialView);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialView.setCurrentAngle(80f);
            }
        });


    }
}
