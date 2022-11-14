package <package_name>.highscoreapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class PlayArea extends View {

    private Handler h;
    private int frameRate;

    private boolean gameStarted = false;
    private int selectedColorID;
    private int selectedShapeID;
    private List<Balloon> balloonList;
    private int numOfBalloonRows;

    private int score;
    private int numOfMissedBalloons;
    private int numOfBalloonsPopped;
    CountDownTimer timer;
    long timeRemaining;

    private int screenHeight;
    private int screenWidth;

    TextView scoreTextView;
    TextView timerTextView;

    // constructors
    public PlayArea(Context context) {
        super(context);
        h=new Handler();
        frameRate=10;
        balloonList=new ArrayList<Balloon>();
        gameStarted=false;
    }

    public PlayArea(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        h=new Handler();
        frameRate=10;
        balloonList=new ArrayList<Balloon>();
        gameStarted=false;
    }

    // controls the 60second timer
    public void timerControl(){
        //Set 60 second timer with a 1 second interval
        timer=new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining=millisUntilFinished;
                timerTextView.setText(Long.toString(millisUntilFinished/1000)+" s");
            }

            // on finish it will launch the enter high score activity
            @Override
            public void onFinish() {
                Intent intent = new Intent(getContext(),EnterHighScore.class);
                intent.putExtra("highScore",score);
                intent.putExtra("missedBalloons",numOfMissedBalloons);
                getContext().startActivity(intent);
                Activity activity = (Activity)getContext();
                activity.finish();
            }
        }.start();
    }

    // changes timer time
    public void timerControl(long customTime){
        //Set 60 second timer with a 1 second interval
        timer=new CountDownTimer(customTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining=millisUntilFinished;
                timerTextView.setText(Long.toString(millisUntilFinished/1000)+" s");
            }

            // on finish it will launch the enter high score activity
            @Override
            public void onFinish() {
                Intent intent = new Intent(getContext(),EnterHighScore.class);
                intent.putExtra("highScore",score);
                intent.putExtra("missedBalloons",numOfMissedBalloons);
                getContext().startActivity(intent);
                Activity activity = (Activity)getContext();
                activity.finish();
            }
        }.start();
    }

    // Draws everything on screen. Drawn every 1ms
    @Override
    public void onDraw(Canvas canvas){
        this.screenHeight=canvas.getHeight();
        this.screenWidth= canvas.getWidth();
        Random random=new Random();
        Boolean [] shapeSelect = {true,false};
        //System.out.println(screenWidth/64);

        // generate no of balloon rows
        this.numOfBalloonRows=random.nextInt(3)+6;
        // generate random numbers
        HashSet<Integer> set = new HashSet<>();
        while (set.size() < numOfBalloonRows) {
            int row = random.nextInt(numOfBalloonRows);
            if (!set.contains(row)) {
                set.add(row);
            }
        }
        int [] rows=set.stream().mapToInt(Number::intValue).toArray();


        // if game hasn't started, initialize no of balloons and balloons
        if(!gameStarted){
//            this.numOfBalloonRows=random.nextInt(3)+6;
            this.gameStarted=true;
            //int[] rows= random.ints(this.numOfBalloonRows,1,11).toArray();

            int rowID=0;

            for(int i=0;i<numOfBalloonRows;i+=1){
                if(shapeSelect[random.nextInt(2)])
                    balloonList.add(new BalloonSquare(getContext(),screenHeight,screenWidth,rows[rowID]));
                else
                    balloonList.add(new BalloonCircle(getContext(),screenHeight,screenWidth,rows[rowID]));
                rowID+=1;
            }
            gameStarted=true;
            // calls timer control to start timer
            timerControl();
        }

        // Find which balloons have exited the canvas area and remove them
        // Update the number of correct balloons that were not popped
        List<Balloon> balloonsToBeRemoved=new ArrayList<>();
        //int ctr=0;
        for(Balloon balloon:balloonList){
            if(balloon.getY_coordinate()<=0){
                balloonsToBeRemoved.add(balloon);
                //ctr+=1;
                if (balloon.getShapeID()==selectedShapeID && balloon.getColor()==selectedColorID)
                    numOfMissedBalloons+=1;
            }
        }

        //add new balloons in rows
        for(Balloon balloon:balloonsToBeRemoved) {
            balloonList.remove(balloon);
            if(shapeSelect[random.nextInt(2)])
                balloonList.add(new BalloonSquare(getContext(),screenHeight,screenWidth,balloon.getRow()));
            else
                balloonList.add(new BalloonCircle(getContext(),screenHeight,screenWidth,balloon.getRow()));
        }

        // checks if balloons overlap and changes their speed to prevent overlap
        for(Balloon balloon1:balloonList){
            for(Balloon balloon2:balloonList){
                if(balloon1.rect_coordinates.intersect(balloon2.rect_coordinates)){
                    if(balloon1.getSpeed()>balloon2.getSpeed()){
                        balloon2.setSpeed(balloon2.getSpeed()-1);
                        balloon1.setSpeed(balloon1.getSpeed()+1);
                    }
                    else{
                        balloon1.setSpeed(balloon1.getSpeed()-1);
                        balloon2.setSpeed(balloon2.getSpeed()+1);
                    }
//                    if (balloon1.getY_coordinate()<balloon2.getY_coordinate())
//                        balloon1.setY_coordinate(balloon1.getY_coordinate()-balloon1.getSize());
//                    else
//                        balloon2.setY_coordinate(balloon2.getY_coordinate()-balloon2.getSize());
                }
            }
        }

        // Draw Balloons
        for(Balloon balloon:balloonList)
            balloon.drawBalloonOnPlayArea(canvas);

        // runs the timer on separate thread
        // code adapted from Prof Cole's webpage
        Runnable r = new Runnable() {
            @Override
            public void run()
            {
                invalidate();
            }
        };
        h.postDelayed(r, frameRate);

    }

    // invoked on touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!gameStarted)
            return super.onTouchEvent(event);

        Balloon poppedBalloon = null;

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            //Search for popped balloon and verify if it is correct balloon popped or not
            for(Balloon balloon:balloonList){
                if(
                        balloon.getX_coordinate()<event.getX() &&
                                balloon.getY_coordinate()>event.getY() &&
                                (balloon.getX_coordinate()+balloon.getSize() >event.getX()) &&
                                (balloon.getY_coordinate()-balloon.getSize()<event.getY())
                ){
                    poppedBalloon=balloon;
                    if(poppedBalloon.getColor()==selectedColorID && poppedBalloon.getShapeID()==selectedShapeID){
                        this.score+=1;
                    }
                    else{
                        this.score-=1;
                        if(this.score<0)
                            this.score=0;
                    }

                    break;
                }
            }
        }
        // if a balloon was popped, remove that balloon and update the time
        if (poppedBalloon!=null){
            this.numOfBalloonsPopped+=1;
            this.balloonList.remove(poppedBalloon);
            this.scoreTextView.setText(Integer.toString(this.score));
            if (timer!=null && numOfBalloonsPopped>0 && numOfBalloonsPopped%10==0){
                timer.cancel();
                timerControl(timeRemaining+10000);
            }
        }

        return super.onTouchEvent(event);
    }


    //Getter and Setter methods for the data members

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getSelectedColorID() {
        return selectedColorID;
    }

    public void setSelectedColorID(int selectedColorID) {
        this.selectedColorID = selectedColorID;
    }

    public int getSelectedShapeID() {
        return selectedShapeID;
    }

    public void setSelectedShapeID(int selectedShapeID) {
        this.selectedShapeID = selectedShapeID;
    }

    public List<Balloon> getBalloonList() {
        return balloonList;
    }

    public void setBalloonList(List<Balloon> balloonList) {
        this.balloonList = balloonList;
    }

    public int getNumOfBalloonRows() {
        return numOfBalloonRows;
    }

    public void setNumOfBalloonRows(int numOfBalloonRows) {
        this.numOfBalloonRows = numOfBalloonRows;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumOfMissedBalloons() {
        return numOfMissedBalloons;
    }

    public void setNumOfMissedBalloons(int numOfMissedBalloons) {
        this.numOfMissedBalloons = numOfMissedBalloons;
    }

    public int getNumOfBalloonsPopped() {
        return numOfBalloonsPopped;
    }

    public void setNumOfBalloonsPopped(int numOfBalloonsPopped) {
        this.numOfBalloonsPopped = numOfBalloonsPopped;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public TextView getScoreTextView() {
        return scoreTextView;
    }

    public void setScoreTextView(TextView scoreTextView) {
        this.scoreTextView = scoreTextView;
    }

    public TextView getTimerTextView() {
        return timerTextView;
    }

    public void setTimerTextView(TextView timerTextView) {
        this.timerTextView = timerTextView;
    }
}
