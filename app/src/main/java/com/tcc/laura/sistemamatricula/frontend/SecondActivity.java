package com.tcc.laura.sistemamatricula.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private Button button_home;
    private Button button_matricula;
    private Button button_provas;
    private Button button_notas;
    private Button button_sair;

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

        button_home = (Button) findViewById(R.id.ButtonHome);
        button_matricula = (Button) findViewById(R.id.ButtonMatricula);
        button_provas = (Button) findViewById(R.id.ButtonProvas);
        button_notas = (Button) findViewById(R.id.ButtonNotas);
        button_sair = (Button) findViewById(R.id.ButtonSair);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "Você já está em Home", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {}
            }
        });

        button_matricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "Matricula está em construção", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {}
            }
        });

        button_provas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "Provas está em construção", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {}
            }
        });

        button_notas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "Notas está em construção", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {}
            }
        });

        button_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToMainActivity();
                } catch (Exception ex) {}
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


