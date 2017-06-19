package com.example.respons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoNet extends AppCompatActivity {

    static AppCompatActivity  _NoNet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_net);

        _NoNet= this;

        Button Retry=(Button) findViewById(R.id.Retry);
        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CallSoap.isConnectionAvailable(NoNet.this)) {
                    if (NewActivity.MainAvtivity != null) NewActivity.MainAvtivity.finish();
                    _NoNet= NoNet.this;
                    Intent i = new Intent(NoNet.this, NewActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }
}
