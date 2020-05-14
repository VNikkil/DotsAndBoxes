package com.example.dotsandboxes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectPlayers extends AppCompatActivity {

    Button P2,P3,P4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectplayers);

        P2 = (Button) findViewById(R.id.players2);
        P3 = (Button) findViewById(R.id.players3);
        P4 = (Button) findViewById(R.id.players4);

        P2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grid = new Intent(getBaseContext(),SelectGrid.class);
                grid.putExtra("players",2);
                startActivity(grid);
            }
        });

        P3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grid = new Intent(getBaseContext(),SelectGrid.class);
                grid.putExtra("players",3);
                startActivity(grid);
            }
        });

        P4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent grid = new Intent(getBaseContext(),SelectGrid.class);
                grid.putExtra("players",4);
                startActivity(grid);
            }
        });

    }

}
