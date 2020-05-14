package com.example.dotsandboxes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectDifficulty extends AppCompatActivity {
    Button Easy,Difficult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdifficulty);

        Easy = (Button) findViewById(R.id.easy);
        Difficult = (Button) findViewById(R.id.Difficult);


        Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grid = new Intent(getBaseContext(),SelectGrid.class);
                grid.putExtra("players",5);
                startActivity(grid);
            }
        });

        Difficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grid = new Intent(getBaseContext(),SelectGrid.class);
                grid.putExtra("players",6);
                startActivity(grid);
            }
        });


    }

}
