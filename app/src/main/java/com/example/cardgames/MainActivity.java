// Remember, whenever you're creating a new activity, you've got to manually add it to the manifest!

package com.example.cardgames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Button blackJack;
    private ImageView introCards;
    Point size = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        getWindowManager().getDefaultDisplay().getSize(size);

        introCards = findViewById(R.id.introCards);
        introCards.setAdjustViewBounds(true);
        introCards.getLayoutParams().height = size.y/3;

        blackJack = findViewById(R.id.blackjackButton);

        blackJack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Feeding your snail is something not to do at a stoplight!");
                changeToBlackjack(v);
            }
        });


    }

    protected void changeToBlackjack(View view) {
        Intent changeIntent = new Intent(this, BlackActivity.class);
        startActivity(changeIntent);
    };

}