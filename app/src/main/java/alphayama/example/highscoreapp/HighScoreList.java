package <package_name>.highscoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HighScoreList extends AppCompatActivity {

    RecyclerView highScoreList;
    private HighScoreAdapter highScoreAdapter;
    private ArrayList<HighScore> highScoreArrayList;
    private SimpleDateFormat dateCompare;
    private File storagePath;
    private FileManagement fileIO;

    // this is called when the main activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing all data members
        highScoreList=findViewById(R.id.highScoreListView);
        highScoreList.setNestedScrollingEnabled(false);

        highScoreList.setLayoutManager(new LinearLayoutManager(this));
        highScoreArrayList=new ArrayList<>();
        highScoreAdapter=new HighScoreAdapter(this,highScoreArrayList);
        highScoreList.setAdapter(highScoreAdapter);
        highScoreList.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        dateCompare = new SimpleDateFormat("MM/dd/yyyy");
        storagePath=getApplicationContext().getFilesDir();
        fileIO = new FileManagement();
        loadScoreListData();
        Toast.makeText(getApplicationContext(),"Data Loaded",Toast.LENGTH_LONG);
//        highScoreAdapter.notifyDataSetChanged();
    }

    // Adds actions to the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds actions to the menu
        getMenuInflater().inflate(R.menu.action_buttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Defines what function needs to be performed when an action bar item is tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // This method is called when EnterHighScore activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Gets HighScore object from Intent
                String highScoreInfoStr = data.getStringExtra("highScoreStr");

                // Parse the string and create object
                HighScore highScore = new HighScore(
                        highScoreInfoStr.split("\t")[0],
                        Integer.parseInt(highScoreInfoStr.split("\t")[1]),
                        highScoreInfoStr.split("\t")[2]);
                try {
                    addNewHighScore(highScore);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // add New High Score to High Score list
    private void addNewHighScore(HighScore highScore) throws IOException {
        boolean insertedInBetween=false;
        for (int i=0;i<highScoreArrayList.size();i+=1) {
            if (highScore.getScore()>highScoreArrayList.get(i).getScore()){
                highScoreArrayList.add(i,highScore);
                insertedInBetween=true;
                break;
            }
            else if (highScore.getScore()==highScoreArrayList.get(i).getScore()){
                try{
                    if (dateCompare.parse(highScore.getDate()).compareTo(
                            dateCompare.parse(highScoreArrayList.get(i).getDate()))>=0){
                        highScoreArrayList.add(i,highScore);
                        insertedInBetween=true;
                        break;
                    }
                }catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        if(!insertedInBetween)
            highScoreArrayList.add(highScore);
        if(highScoreArrayList.size()>20)
            highScoreArrayList.remove(highScoreArrayList.size()-1);
        //send toast message
        Toast.makeText(getApplicationContext(),"Score List Updated!",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"Data Loaded",Toast.LENGTH_SHORT).show();
        // write data to file
        fileIO.writeFile(storagePath,highScoreArrayList);
        // update the recyclerview
        highScoreAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Data Saved!",Toast.LENGTH_SHORT).show();
    }

    // load list from tab separated file
    private void loadScoreListData(){
        //on load, if data file exists, it is stored in a temp array list
        ArrayList<HighScore> tempHighScoreList = new ArrayList<>();
        try {
            tempHighScoreList =fileIO.readFile(storagePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //if the temp array list is not empty, update the main array list
        if (!tempHighScoreList.isEmpty())
            for(HighScore highScore:tempHighScoreList){
                highScoreArrayList.add(highScore);
            }
    }

}