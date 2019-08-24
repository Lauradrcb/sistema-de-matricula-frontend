package com.tcc.laura.sistemamatricula.frontend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle extras = getIntent().getExtras();
        String value = extras.getString("username");
        TextView TV = (TextView)findViewById(R.id.UserName);
        System.out.println(TV);
        if (extras != null) {
            System.out.println(value);
        }
        TV.setText(value);
    }
}
