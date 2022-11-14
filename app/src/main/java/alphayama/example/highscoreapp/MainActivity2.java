package alphayama.example.highscoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    private Button playGameButton;
    private Button viewHighScoresButton;
    private TextView instructionTextView;
    private int selectedColorID;
    private String selectedColorName;
    private int selectedShapeID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // checks if app has storage permission or not
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        playGameButton=findViewById(R.id.playGameButton);
        viewHighScoresButton=findViewById(R.id.viewHighScoreButton);
        instructionTextView=findViewById(R.id.instructionText);

        int [] colorIDList = {Color.RED,
                                Color.rgb(255, 165, 0),
                                Color.YELLOW,
                                Color.GREEN,
                                Color.BLUE,
                                Color.rgb(128,0,128),
                                Color.WHITE};
        String [] colorNameList = {"Red","Orange","Yellow","Green","Blue","Purple","White"};
        String [] shapeNameList = {"Square","Circle"};

        Random random = new Random();
        int selectedColorIndex=random.nextInt(colorIDList.length);
        selectedColorID=colorIDList[selectedColorIndex];
        selectedColorName=colorNameList[selectedColorIndex];
        //Square=0, Circle=1
        selectedShapeID=random.nextInt(2);

        instructionTextView.setText("Only Pop the "+selectedColorName+" "+shapeNameList[selectedShapeID]+"s!!");


        playGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playGame = new Intent(getApplicationContext(),Playground.class);
                playGame.putExtra("colorID",selectedColorID);
                playGame.putExtra("shapeID",selectedShapeID);
                System.out.println(selectedColorID);
                System.out.println(selectedShapeID);
                startActivity(playGame);
            }
        });

        viewHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HighScoreList.class);
                startActivity(intent);
            }
        });

    }
}