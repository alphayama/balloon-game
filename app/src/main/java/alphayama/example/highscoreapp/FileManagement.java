package alphayama.example.highscoreapp;

import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManagement {
    private String fileName= "highScoreData.txt";

    // provides read file functionality
    public ArrayList<HighScore> readFile(File storagePath) throws FileNotFoundException {
        ArrayList<HighScore> highScoreList = new ArrayList<HighScore>();
        HighScore highScore;
        if(true){
            File file = new File(storagePath,"new"+fileName);
            Scanner inputFile = new Scanner(file);
            String line;
            // Read until the end of the file.
            while (inputFile.hasNext())
            {
                line = inputFile.nextLine();
                if(line.split("\t").length<3)
                    break;
                highScore = new HighScore(
                            line.split("\t")[0],
                            Integer.parseInt(line.split("\t")[1]),
                            line.split("\t")[2]);
                    highScoreList.add(highScore);
            }
            inputFile.close();// close the file when done
        }

        return (highScoreList);

    }

    // provides write functionality
    public void writeFile(File storagePath, ArrayList<HighScore> highScoreList) throws IOException {
        File file = new File(storagePath,"new"+fileName);
        FileWriter outputFile = new FileWriter(file);
        // iterate over the high score array list and save each line
        for(HighScore highScore:highScoreList){
            outputFile.write(highScore.getPlayerName()+"\t"+
                        highScore.getScore()+"\t"+
                        highScore.getDate()+"\n");

        }
        outputFile.flush();//write lines to file
        outputFile.close(); //close file

    }

    // add New High Score to High Score list and saves to file
    public void addNewHighScore(File storagePath,HighScore highScore) throws IOException {
        ArrayList<HighScore> highScoreArrayList = new ArrayList<>();
        SimpleDateFormat dateCompare = new SimpleDateFormat("MM/dd/yyyy");
        try {
            highScoreArrayList =readFile(storagePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        // write data to file
        writeFile(storagePath,highScoreArrayList);

    }

}
