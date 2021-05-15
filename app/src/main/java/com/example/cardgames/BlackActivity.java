package com.example.cardgames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.ImageViewCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.math.*;
import java.util.Arrays;

public class BlackActivity extends AppCompatActivity{

    // Let's just get some buttons out of the way first...

    private Button hitButton;
    private Button standButton;
    private Button doubleButton;
    private Button splitButton;


    // Now, this is where the cards will be displayed. For now, I'm just showing them with text, but
    // later, I'll be using actual pictures of cards... hopefully it works out.

    private ConstraintLayout cardLayout;
    private TextView cardText;
    private CharSequence cardWords;

    private int[] cardFaces = {R.drawable.acec,R.drawable.twoc,R.drawable.threec,R.drawable.fourc,R.drawable.fivec,R.drawable.sixc,R.drawable.sevenc,R.drawable.eightc,R.drawable.ninec,R.drawable.tenc,R.drawable.jackc,R.drawable.queenc,R.drawable.kingc,
            R.drawable.aced,R.drawable.twod,R.drawable.threed,R.drawable.fourd,R.drawable.fived,R.drawable.sixd,R.drawable.sevend,R.drawable.eightd,R.drawable.nined,R.drawable.tend,R.drawable.jackd,R.drawable.queend,R.drawable.kingd,
            R.drawable.aceh,R.drawable.twoh,R.drawable.threeh,R.drawable.fourh,R.drawable.fiveh,R.drawable.sixh,R.drawable.sevenh,R.drawable.eighth,R.drawable.nineh,R.drawable.tenh,R.drawable.jackh,R.drawable.queenh,R.drawable.kingh,
            R.drawable.aces,R.drawable.twos,R.drawable.threes,R.drawable.fours,R.drawable.fives,R.drawable.sixs,R.drawable.sevens,R.drawable.eights,R.drawable.nines,R.drawable.tens,R.drawable.jacks,R.drawable.queens,R.drawable.kings};


    // First - make an array of all the cards. I'm going to save both the value and suit as a
    // number. That should make it easier to tell when a card's been picked for later on.
    // The order for suits is: Clubs, Diamonds, Hearts, Spades. Also, 1 will mean an Ace, 11 will
    // refer to a Jack, 12 a Queen, and 13 a King. Thankfully, there are no Jokers in Blackjack.

    private String[] cards = new String[52];

    private String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };
    private String[] numbers = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };


    // This array just saves the value for each card because I'm lazy.

    private int[] cardValues = new int[52];


    // Now that all the cards exist, we're going to need to give them to the players. Currently,
    // we've playing a five-player game every time. We're going to need a way to detect if a
    // card has been dealt or not - booleans!

    private boolean[] hasDealt = new boolean[52];


    // This two-dimensional array will hold the integer value for each card everyone has. For
    // example, the Seven of Clubs would be written as 7, and the Three of Diamonds as 16.
    // Also, I picked 11 as that's the max number of cards one could have, and that'd be the worst
    // case scenario (all Aces, all Twos, and three Threes)... highly unlikely.

    // And on the off chance I put this online, I'd prefer to leave remembering who has what card
    // to the server - all the client would have to do is say when it wants a new card.
    private int[][] playerCards = new int[5][11];


    // This is the part where my coding kind of sucks. I'm going to be displaying all the user's
    // non-programmatically, meaning I'll need to store them in an array... woo-hoo.
    private ImageView[] cardViews = new ImageView[12];
    private int[] cardNums = {2,2,2,2,2};
    private ImageView[] enemyCards = new ImageView[4];


    // This variable is for figuring out if a card has been taken yet... to be used in a while loop.
    private boolean freeCard = true;
    private int randomCard = 0;


    // On the off chance the player decides to split...
    private boolean split = false;


    // Let's make some names for the opponents... totally not from Brawl Stars.
    private String[] possibleNames = {"Shelly","Colt","Jessie","Brock","Bo","Rosa","Penny","Darryl","Carl","Rico","Piper","Pam","Bea","Tara","Max","Byron","Leon","Sandy","Amber","Colette","Belle"};
    private TextView[] enemyNames = new TextView[4];
    private boolean nameUsed = false;
    private int nameNumber = 0;

    Point size = new Point();
    ImageView greenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_blackjack);


        // I'm going to preemptively name everyone, because why not?
        enemyNames[0] = findViewById(R.id.enemy1);
        enemyNames[1] = findViewById(R.id.enemy2);
        enemyNames[2] = findViewById(R.id.enemy3);
        enemyNames[3] = findViewById(R.id.enemy4);

        for(int i = 0; i < enemyNames.length; i ++){
            nameUsed = true;
            while(nameUsed) {
                nameNumber = (int) Math.floor(Math.random() * possibleNames.length);
                nameUsed = false;
                for (int j = 0; j < enemyNames.length; j++) {
                    if (enemyNames[j].getText().equals(possibleNames[nameNumber])) {
                        System.out.println("Ban " + possibleNames[nameNumber]);
                        nameUsed = true;
                    }
                }
                enemyNames[i].setText(possibleNames[nameNumber]);
            }
        }


        // First I've gotta set the cards up...

/*        {R.drawable.acec,R.drawable.twoc,R.drawable.threec,R.drawable.fourc,R.drawable.fivec,R.drawable.sixc,R.drawable.sevenc,R.drawable.eightc,R.drawable.ninec,R.drawable.tenc,R.drawable.jackc,R.drawable.queenc,R.drawable.kingc,
                R.drawable.aced,R.drawable.twod,R.drawable.threed,R.drawable.fourd,R.drawable.fived,R.drawable.sixd,R.drawable.sevend,R.drawable.eightd,R.drawable.nined,R.drawable.tend,R.drawable.jackd,R.drawable.queend,R.drawable.kingd,
                R.drawable.aceh,R.drawable.twoh,R.drawable.threeh,R.drawable.fourh,R.drawable.fiveh,R.drawable.sixh,R.drawable.sevenh,R.drawable.eighth,R.drawable.nineh,R.drawable.tenh,R.drawable.jackh,R.drawable.queenh,R.drawable.kingh,
                R.drawable.aces,R.drawable.twos,R.drawable.threes,R.drawable.fours,R.drawable.fives,R.drawable.sixs,R.drawable.sevens,R.drawable.eights,R.drawable.nines,R.drawable.tens,R.drawable.jacks,R.drawable.queens,R.drawable.kings};
*/
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 13; j ++){
                cards[j + (i*13)] = numbers[j] + " of " + suits[i];
                cardValues[j + (i*13)] = (j%13 > 8 ? 10: j + 1);

            }
        }



        //System.out.println("Don't worry, kids! It's " + Arrays.toString(cardValues));

        /*for(int i = 0; i < 52; i ++){
            System.out.println(cards[i]);
        }*/

        // Since there's no "house" here, as we're not in a casino, there is no dealer, thus
        // eliminating house advantage. As such, everyone plays by the same rules. Now, let's give
        // everyone their cards!

        // Player 1 will draw first, then Player 2, then Player 3, and so on.

        for(int i = 0; i < 5; i ++){
            for(int j = 0; j < 2; j ++) {
                freeCard = false;
                while (!freeCard) {
                    randomCard = (int) (Math.floor(Math.random() * 52));
                    if (!hasDealt[randomCard]) {
                        hasDealt[randomCard] = true;
                        freeCard = true;
                        playerCards[i][j] = randomCard;
                        //If you got an ace, we're going to auto-turn it into something higher.
                        if(cardValues[randomCard] == 1 && checkCardValues(i) <= 11){
                            System.out.println("It happened!");
                            cardValues[randomCard] = 11;
                        }
                    }else {
                        System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
                    }

                }
            }

        }



        // This will just read out who has what card.
        for(int i = 0; i < 5; i ++) {
            System.out.println("Player " + i + " has:");
            System.out.println(cards[playerCards[i][0]]);
            System.out.println(cards[playerCards[i][1]]);
            System.out.println(" ");
        }

        enemyCards[0] = findViewById(R.id.ecard00);
        enemyCards[1] = findViewById(R.id.ecard10);
        enemyCards[2] = findViewById(R.id.ecard20);
        enemyCards[3] = findViewById(R.id.ecard30);

        for(int i = 0; i < 4; i ++){
            enemyCards[i].setImageResource(cardFaces[playerCards[i+1][0]]);
        }

        System.out.println("Your score is " + checkCardValues(0));

        // Let's begin by displaying the user's cards on the screen.
        // For now, I'm just going to display all of them as Aces of Spades...

        hitButton = findViewById(R.id.hitButton);
        standButton = findViewById(R.id.standButton);
        doubleButton = findViewById(R.id.doubleButton);
        splitButton = findViewById(R.id.splitButton);

        cardViews[0] = findViewById(R.id.card1);
        cardViews[1] = findViewById(R.id.card2);
        cardViews[2] = findViewById(R.id.card3);
        cardViews[3] = findViewById(R.id.card4);
        cardViews[4] = findViewById(R.id.card5);
        cardViews[5] = findViewById(R.id.card6);
        cardViews[6] = findViewById(R.id.card7);
        cardViews[7] = findViewById(R.id.card8);
        cardViews[8] = findViewById(R.id.card9);
        cardViews[9] = findViewById(R.id.card10);
        cardViews[10] = findViewById(R.id.card11);
        cardViews[11] = findViewById(R.id.card12);

        //cardViews[1].setImageResource(aces);

        getWindowManager().getDefaultDisplay().getSize(size);


        greenView = findViewById(R.id.greenView);
        greenView.getLayoutParams().height = size.y/3;

        for(int i = 0; i < cardNums[0]; i ++){

                cardViews[i].setVisibility(View.VISIBLE);
                //System.out.println("This should be at " + (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2)));
                //System.out.println("It's a good thing size is still at " + size.y);
                //System.out.println("Which means our final X value is " + (size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                cardViews[i].setX((size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                //System.out.println(cardNums[0]);
                //System.out.println(cardViews[i].getX());
                cardViews[i].setImageResource(cardFaces[playerCards[0][i]]);

        }

        // Now everyone has their initial two cards... we've got to play the game!
        // I'm going to be focusing majorly on Player One, and replicating it if this ever gets online.
        // There are four things you can do in Blackjack:
        // Hit - ask for another card from the dealer
        // Stand - take no more cards - you're in!
        // Double Down - double your bet, take exactly one more card, and stand
        // Split - you can only do this when your two starting cards are the same value. You
        // essentially split them into two separate bets, gaining one more card for each.

        // We're going to have buttons for all of these... starting with the hit button.

        {hitButton.setOnClickListener(new View.OnClickListener() {

            //@Override
            public void onClick(View v) {

                // First, we've got to give a player a new card.

                cardNums[0] ++;
                for(int i = 0; i < cardNums[0]; i ++){
                    cardViews[i].setVisibility(View.VISIBLE);
                    cardViews[i].setTranslationX((size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                }

                // Now we've got to assign it a value...

                freeCard = false;
                while (!freeCard) {
                    randomCard = (int) (Math.floor(Math.random() * 52));
                    if (!hasDealt[randomCard]) {
                        hasDealt[randomCard] = true;
                        System.out.println("You got the " + cards[randomCard] + "!");

                        //If you got an ace, we're going to auto-turn it into something higher.
                        if(cardValues[randomCard] == 1 && checkCardValues(0) < 11){
                            cardValues[randomCard] = 11;
                        }

                        freeCard = true;
                        playerCards[0][cardNums[0]-1] = randomCard;
                    }else{
                        System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
                    }

                }

                // Check if the player has a soft 22+, and if so, auto-convert the ace from 11 to 1.

                for(int i = 0; i < cardNums[0]; i ++){
                    //System.out.println(cardValues[playerCards[0][i]]);
                    if(cardValues[playerCards[0][i]] == 11 && checkCardValues(0) > 21){
                        System.out.println("We should take care of that ace...");
                        cardValues[playerCards[0][i]] = 1;
                    }
                }

                cardViews[cardNums[0]-1].setImageResource(cardFaces[playerCards[0][cardNums[0]-1]]);

                System.out.println(checkCardValues(0));

                doubleButton.setVisibility(View.GONE);
                splitButton.setVisibility(View.GONE);

                if(checkCardValues(0) > 21){
                    doTurnOver();
                }

            }
        });
        }


        // Next up, standing!

        {standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurnOver();
            }
        });}

        // Now, onto doubling down.

        {doubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Here we go!");
                cardNums[0] ++;
                for(int i = 0; i < cardNums[0]; i ++){
                    cardViews[i].setVisibility(View.VISIBLE);
                    cardViews[i].setTranslationX((size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                }

                // Now we've got to assign it a value...

                freeCard = false;
                while (!freeCard) {
                    randomCard = (int) (Math.floor(Math.random() * 52));
                    if (!hasDealt[randomCard]) {
                        hasDealt[randomCard] = true;
                        System.out.println("You got the " + cards[randomCard] + "!");
                        freeCard = true;
                        playerCards[0][cardNums[0]-1] = randomCard;
                    }else{
                        System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
                    }
                    cardViews[cardNums[0]-1].setImageResource(cardFaces[playerCards[0][cardNums[0]-1]]);

                }

                // Check for a soft 22+ again.

                for(int i = 0; i < cardNums[0]; i ++){
                    //System.out.println(cardValues[playerCards[0][i]]);
                    if(cardValues[playerCards[0][i]] == 11 && checkCardValues(0) > 21){
                        System.out.println("We should take care of that ace...");
                        cardValues[playerCards[0][i]] = 1;
                    }
                }

                System.out.println(checkCardValues(0));

                //doubleButton.setVisibility(View.GONE);

                // Do some sort of "turn over" command
                doTurnOver();

            }
        });
        }

        // The last thing we need to look for is a split. This is an interesting one, as it can only
        // happen if the player's first two cards are of equal value.

        if(cardValues[playerCards[0][0]] != cardValues[playerCards[0][1]]){
            splitButton.setVisibility(View.GONE);
        }

        // If, by some magical chance, the player's got two of the same cards, we should give 'em
        // the chance to split. Set up the button!

        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                cardNums[0]++;
                for (int i = 0; i < cardNums[0]; i++) {
                    cardViews[i].setVisibility(View.VISIBLE);
                    cardViews[i].setTranslationX((size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                }

                // Now we've got to assign it a value...

                freeCard = false;
                while (!freeCard) {
                    randomCard = (int) (Math.floor(Math.random() * 52));
                    if (!hasDealt[randomCard]) {
                        hasDealt[randomCard] = true;
                        System.out.println("You got the " + cards[randomCard] + "!");

                        //If you got an ace, we're going to auto-turn it into something higher.
                        if (cardValues[randomCard] == 1) {
                            cardValues[randomCard] = 11;
                        }

                        freeCard = true;
                        playerCards[0][cardNums[0] - 1] = randomCard;
                    } else {
                        System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
                    }

                }
                    cardViews[cardNums[0]-1].setImageResource(cardFaces[playerCards[0][cardNums[0]-1]]);


                }

                {
                    cardNums[0]++;
                    for (int i = 0; i < cardNums[0]; i++) {
                        cardViews[i].setVisibility(View.VISIBLE);
                        cardViews[i].setTranslationX((size.y * (float) (i + (cardNums[0] % 2 == 0 ? 0.5 : 0) - (cardNums[0] / 2))) / (10 + cardNums[0]));
                    }

                    // Now we've got to assign it a value...

                    freeCard = false;
                    while (!freeCard) {
                        randomCard = (int) (Math.floor(Math.random() * 52));
                        if (!hasDealt[randomCard]) {
                            hasDealt[randomCard] = true;
                            System.out.println("You got the " + cards[randomCard] + "!");

                            //If you got an ace, we're going to auto-turn it into something higher.
                            if (cardValues[randomCard] == 1) {
                                cardValues[randomCard] = 11;
                            }

                            freeCard = true;
                            playerCards[0][cardNums[0] - 1] = randomCard;
                        } else {
                            System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
                        }

                    }

                    cardViews[cardNums[0]-1].setImageResource(cardFaces[playerCards[0][cardNums[0]-1]]);


                }

                doTurnOver();

            }
        });


    }

    protected void giveEnemyCard(int user){
        cardNums[user] ++;
        //for(int i = 0; i < cardNums[user]; i ++){
        //    cardViews[i].setVisibility(View.VISIBLE);
        //    cardViews[i].setTranslationX((size.y * (float) (i + (cardNums[user] % 2 == 0 ? 0.5 : 0) - (cardNums[user] / 2))) / (10 + cardNums[user]));
        //}

        // Now we've got to assign it a value...

        freeCard = false;
        while (!freeCard) {
            randomCard = (int) (Math.floor(Math.random() * 52));
            if (!hasDealt[randomCard]) {
                hasDealt[randomCard] = true;
                System.out.println("They got the " + cards[randomCard] + "!");

                //If you got an ace, we're going to auto-turn it into something higher.
                if(cardValues[randomCard] == 1 && checkCardValues(user) < 11){
                    cardValues[randomCard] = 11;
                }

                freeCard = true;
                playerCards[user][cardNums[user]-1] = randomCard;
            }else{
                System.out.println("Wait... someone else already has the " + cards[randomCard] + "!");
            }

        }

        // Check if the player has a soft 22+, and if so, auto-convert the ace from 11 to 1.

        for(int i = 0; i < cardNums[user]; i ++){
            //System.out.println(cardValues[playerCards[0][i]]);
            if(cardValues[playerCards[user][i]] == 11 && checkCardValues(user) > 21){
                System.out.println("We should take care of that ace...");
                cardValues[playerCards[user][i]] = 1;
            }
        }
    };

    //@Override
    protected int checkCardValues(int player){

        int totalValue = 0;

        for(int i = 0; i < cardNums[player]; i ++){
            System.out.println("We've got the " + cards[playerCards[player][i]]);
            totalValue += cardValues[playerCards[player][i]];
        }
        return totalValue;
    };

    protected void doTurnOver(){
        hitButton.setVisibility(View.GONE);
        standButton.setVisibility(View.GONE);
        doubleButton.setVisibility(View.GONE);
        splitButton.setVisibility(View.GONE);
        enemyTurn(1);
        enemyTurn(2);
        enemyTurn(3);
        enemyTurn(4);

        for(int i = 0; i < 5; i ++) {
            System.out.println("Player " + i + " has:");
            System.out.println(cards[playerCards[i][0]]);
            System.out.println(cards[playerCards[i][1]]);
            System.out.println("For a total of " + checkCardValues(i) + "!");
            System.out.println(" ");
        }

    };

    protected int enemyTurn(int user){
        if(checkCardValues(user) >= 19) {
            return checkCardValues(user);
        }else{
            if(checkCardValues(user) >= 9 && checkCardValues(user) <= 11){
                giveEnemyCard(user);

                // This is them potentially doubling down... add some sort of bet?

                return checkCardValues(user);

            }else{
                do{
                    giveEnemyCard(user);
                }while(checkCardValues(user) < 17);
                return checkCardValues(user);
            }
        }

    };

}
