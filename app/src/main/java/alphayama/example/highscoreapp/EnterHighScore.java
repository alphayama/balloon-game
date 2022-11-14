package alphayama.example.highscoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// this activity implements the enter high score form functionality
public class EnterHighScore extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button datePickButton;
    private Button newHighScoreSubmitButton;
    private EditText playerNameEditText;
    private TextView scoreEditText;
    private String dateString;
    private FileManagement fileIO;

    private int score;
    private int missedBalloons;


    // invoked when Enter High  Score activity is launched
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_high_score);

        Intent intent = getIntent();
        score=intent.getIntExtra("highScore",0);
        missedBalloons=intent.getIntExtra("missedBalloons",0);

        initDatePicker();
        datePickButton=findViewById(R.id.date_pick_button);

        scoreEditText=findViewById(R.id.editTextScore);
        playerNameEditText=findViewById(R.id.editTextPersonName);
        newHighScoreSubmitButton=findViewById(R.id.add_new_score_button);
        newHighScoreSubmitButton.setEnabled(false);

        //populate date picker with and set default date to current date
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        dateString=makeDateString(dayOfMonth,month+1,year);
        datePickButton.setText("Date: "+dateString);

        scoreEditText.setText("Score: "+Integer.toString(score)+"\nMissed Balloons: "+Integer.toString(missedBalloons));


        // on text change in Player Name field, this function is called
        playerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        }

    //validates the player name and score fields
    private void validateFields() {
        if (playerNameEditText.getText().toString().isEmpty())
            newHighScoreSubmitButton.setEnabled(false);
        else
            newHighScoreSubmitButton.setEnabled(true);
    }

    // submits the playerName, score and date to MainActivity using HighScore object
    public void submitNewHighScore(View view) throws IOException {
//        String highScoreInfoStr = +
//                "\t"+scoreEditText.getText().toString()+"\t"+dateString;
//        Intent intent = new Intent();
//        intent.putExtra("highScoreStr", highScoreInfoStr);
//        setResult(RESULT_OK, intent);
        HighScore highScore = new HighScore(playerNameEditText.getText().toString(),
                                            score,dateString);
        fileIO=new FileManagement();
        fileIO.addNewHighScore(getApplicationContext().getFilesDir(),highScore);
        finish();
    }

    // initializes the date picker
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                String date=makeDateString(dayOfMonth,month,year);
                dateString=date;
                datePickButton.setText("Date: "+date);
            }
        };

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        // set current date as default date
        datePickerDialog=new DatePickerDialog(this,dateSetListener,year,month,dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    // creates date string
    private String makeDateString(int dayOfMonth, int month, int year){
        return month+"/"+dayOfMonth+"/"+year;
    }

    //opens date picker when the button is clicked
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}