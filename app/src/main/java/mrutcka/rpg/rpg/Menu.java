package mrutcka.rpg.rpg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    int countWin;
    Intent i;
    TextView score;
    Button restart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);


        score = (TextView) findViewById(R.id.score);

        score.setText("Score: " + getIntent().getIntExtra("wins", countWin));

        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.restart:

                i = new Intent(this, MainActivity.class);
                startActivity(i);

                break;
        }
    }
}
