package com.philip.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean p1Turn = true; //is it player 1's turn
    boolean gameHasEnded = false;

    //null means empty, true means p1, false means p2
    Boolean[] p1OnSquare = {null, null, null, null, null, null, null, null, null};
    //the coordinates for a win game state
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8}, {0,4,8}, {2,4,6}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    OnClick method for the counter images
     */
    public void dropIn(View view){
        ImageView counter = (ImageView) view; //get the image that was clicked
        //get the tag of the counter so it can be located in the p1OnSquare array
        int tagCounter = Integer.parseInt(counter.getTag().toString());
        System.out.println("Counter " + tagCounter + " clicked");

        //check if the game has ended and if that position is available
        if(!gameHasEnded && p1OnSquare[tagCounter] == null){
            //if its the active players turn p1OnSquare is true
            p1OnSquare[tagCounter] = p1Turn ? true : false;

            counter.setTranslationY(-1000f); //put the counter off screen

            //get the relevant image's id
            int counterImage = p1Turn ? R.drawable.yellow : R.drawable.red;
            counter.setImageResource(counterImage); //set the image of the counter
            p1Turn = !p1Turn; //toggle player's turn

            //animate the counter onto the screen
            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            //check if any positions were empty
            boolean wasEmpty = false;
            for (int[] winningPosition : winningPositions) {
                //check for null to avoid an exception
                if(p1OnSquare[winningPosition[0]] != null && p1OnSquare[winningPosition[1]] != null &&
                        p1OnSquare[winningPosition[2]] != null) {
                    //check if a player's counters coincide with a win state
                    if (p1OnSquare[winningPosition[0]] == p1OnSquare[winningPosition[1]] &&
                            p1OnSquare[winningPosition[1]] == p1OnSquare[winningPosition[2]]) {
                        String endGameMessage = (p1OnSquare[winningPosition[0]] ? "Yellow" : "Red")
                                + " has won!";
                        endGame(endGameMessage);
                        return;
                    }
                }
                else{
                    wasEmpty = true;
                }
            }
            //if no positions were empty it is a tie game
            if(!wasEmpty) {
                endGame("Tie Game!");
            }
        }
    }

    /*
    Handles ending the game.
    Displays the game results and a button to play again.
     */
    private void endGame(String endGameMessage){
        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtMessage.setText(endGameMessage);

        LinearLayout layoutEndGame = (LinearLayout) findViewById(R.id.layoutEndGame);
        layoutEndGame.setVisibility(View.VISIBLE);
        gameHasEnded = true;
    }

    /*
    Reset the game so it can be played again.
     */
    public void playAgain(View view){
        gameHasEnded = false;

        LinearLayout layoutEndGame = (LinearLayout) findViewById(R.id.layoutEndGame);
        //the layout should be hidden now
        layoutEndGame.setVisibility(View.INVISIBLE);

        p1Turn = true;
        //loop through the positions and set them to null
        for (int i=0; i < p1OnSquare.length; i++) {
            p1OnSquare[i] = null;
        }

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        //remove the image from each ImageVIew
        for (int i=0; i < gridLayout.getChildCount(); i++){
            //handle if the child is not an ImageView
            try {
                ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
            }
            catch(Exception ex){}
        }
    }
}
