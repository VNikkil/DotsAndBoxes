package com.example.dotsandboxes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout.LayoutParams;
import com.example.dotsandboxes.R;

public class SelectGrid extends AppCompatActivity {

    Button S3,S5,S7;
    int players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectgrid);

        Intent player = getIntent();
        players = (Integer) player.getSerializableExtra("players");

        S3 = (Button)findViewById(R.id.size3);
        S5 = (Button)findViewById(R.id.size5);
        S7 = (Button)findViewById(R.id.size7);

        S3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game();
                View g = findViewById(R.id.grid);
                g.setTag(4 + players*10);

            }
        });

        S5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game();
                View g = findViewById(R.id.grid);
                g.setTag(6 + players*10);
            }
        });

        S7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game();
                View g = findViewById(R.id.grid);
                g.setTag(8 + players*10);
            }
        });
    }

    public void Game() {
        Log.i("TAG", " " + players);
        setContentView(R.layout.game);
    }
}
