package com.example.fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Baslangic extends AppCompatActivity {
    Button giris, kaydol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);

        giris = findViewById(R.id.giris);
        kaydol = findViewById(R.id.kaydol);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Baslangic.this, Login.class);
                startActivity(intent);

            }
        });

        kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Baslangic.this, Kaydol.class);
                startActivity(intent);

            }
        });


    }
}

