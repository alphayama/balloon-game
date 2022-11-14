package alphayama.example.highscoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Playground extends AppCompatActivity{

    private TextView scoreTextView;
    private TextView timerTextView;
    private PlayArea playArea;

    private int selectedShapeID;
    private int selectedColorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        //Get selected color and shape of the balloon
        Intent intent = getIntent();
        this.selectedColorID=0;//intent.getIntExtra("colorID",0);
        this.selectedShapeID=0;//intent.getIntExtra("shapeID",0);

        //Get views
        this.scoreTextView=findViewById(R.id.scoreTextView);
        timerTextView=findViewById(R.id.timerTextView);
        playArea=findViewById(R.id.playArea);


        //set selected balloon, textviews
        playArea.setSelectedColorID(intent.getIntExtra("colorID",0));
        playArea.setSelectedShapeID(intent.getIntExtra("shapeID",0));
        playArea.setScoreTextView(scoreTextView);
        playArea.setTimerTextView(timerTextView);


    }

    //Closes the activity i.e. cancels the game
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playArea.getTimer().cancel();
        this.finish();
    }


}