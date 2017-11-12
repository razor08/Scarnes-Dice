package com.example.razor.scarnesdice;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView diceImage;
    private TextView turnIndicator, scoreIndicator;
    private Button roll, reset, hold;
    private int turnInd;
    private int[] imageArray = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    private     int userOverall, compOverall, userScore, compScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("lifecycle", "in onCreate");
        diceImage = (ImageView) findViewById(R.id.imageView);
        roll      = (Button) findViewById(R.id.roll);
        roll.setOnClickListener(this);
        hold      = (Button) findViewById(R.id.hold);
        hold.setOnClickListener(this);
        reset     = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        turnIndicator   = (TextView) findViewById(R.id.textView2);
        scoreIndicator  = (TextView) findViewById(R.id.textView3);
    }
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            timerHandler.postDelayed(this, 1000);
            if (turnInd == 1)
            {
                checkScore();
                rollDice();
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        turnIndicator.setText("Your turn. Turn Score: " + userScore);
        scoreIndicator.setText("Your Score: " + userOverall + ". Computer's Score: "+ compOverall);
        Log.v("lifecycle", "in onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("lifecycle", "in onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v("lifecycle", "in onPause");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("lifecycle", "in onDestroy");
    }
    public void updateTurnTextView(int turnInd)
    {
        if (turnInd == 0)
        {
            turnIndicator.setText("Your turn. Turn Score: " + userScore);
        }
        else if (turnInd == 1)
        {
            turnIndicator.setText("Computer's turn. Turn Score: " + compScore);
        }
    }
    public void updateScoreTextView()
    {
        scoreIndicator.setText("Your Score: " + userOverall + ". Computer's Score: "+ compOverall);
    }
    public void updateOverall()
    {
        userOverall = userOverall + userScore;
        userScore = 0;
        compOverall = compOverall + compScore;
        compScore = 0;
    }
    public void checkScore() {
        if (userOverall >= 100) {
            turnIndicator.setText("You Won!");
            scoreIndicator.setText("You reached score: 100");
            disableButtons();
            diceImage.setImageResource(imageArray[0]);
        } else if (compOverall >= 100) {
            turnIndicator.setText("Computer Won!");
            scoreIndicator.setText("Computer reached score: 100");
            disableButtons();
            diceImage.setImageResource(imageArray[0]);
        }
    }
    public void rollDice()
    {
        Random rand = new Random();
        int  n = rand.nextInt(6) + 1;
        if (n!=1) {
            if (turnInd == 0) {
                userScore = userScore + n;
            } else {
                compScore = compScore + n;
            }
            int imageIndex = n;
            diceImage.setImageResource(imageArray[imageIndex-1]);
            updateTurnTextView(turnInd);
        }
        else if (n==1)
        {
            updateOverall();
            if (turnInd == 0)
            {
                turnInd = 1;
                computerTurn();
            } else {
                turnInd = 0;
                enableButtons();
            }
            updateTurnTextView(turnInd);
            updateScoreTextView();
            checkScore();
        }
    }
    public void computerTurn()
    {
        timerHandler.postDelayed(timerRunnable, 0);
        disableButtons();
    }
    public void disableButtons()
    {
        roll.setEnabled(false);
        hold.setEnabled(false);
    }

    public void enableButtons()
    {
        roll.setEnabled(true);
        hold.setEnabled(true);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.roll)
        {
            rollDice();
        }
        else if (id == R.id.hold) {
            updateOverall();
            updateScoreTextView();
            if (turnInd == 0) {
                turnInd = 1;
                computerTurn();
            } else {
                turnInd = 0;
                enableButtons();
            }
            updateTurnTextView(turnInd);
            checkScore();
        }
        else if (id == R.id.reset)
        {
            userOverall = 0;
            compOverall = 0;
            userScore   = 0;
            compScore   = 0;
            updateScoreTextView();
            updateTurnTextView(0);
            enableButtons();
            diceImage.setImageResource(imageArray[0]);
        }
    }
}
