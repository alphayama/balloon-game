package alphayama.example.highscoreapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.TypedValue;

import java.util.Random;

public class Balloon {
    protected int size;
    protected int color;
    protected int speed;
    protected int shapeID;
    protected int row;

    protected float x_coordinate;
    protected float y_coordinate;

    //Coordinates for Square
    protected RectF rect_coordinates;

    private static int [] colorIDList = {Color.RED,
            Color.rgb(255, 165, 0),
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.rgb(128,0,128),
            Color.WHITE};

    public Balloon(Context context, int screenHeight, int screenWidth){
        Random rand = new Random();
        this.size = (int) ((rand.nextInt((64 - 32) + 1) + 32)*context.getResources().getDisplayMetrics().density+0.5f);
        this.color = colorIDList[rand.nextInt(colorIDList.length)];
        //Randomize speed between 1 and 15
        this.speed= rand.nextInt(10)+1;

        //Set coordinates
        this.x_coordinate=rand.nextInt(screenWidth - this.size);
        this.y_coordinate=screenHeight+this.size;
        this.rect_coordinates=new RectF(this.x_coordinate ,
                                    this.y_coordinate-this.size ,
                                    this.x_coordinate+this.size,
                                            this.y_coordinate);

    }

    public Balloon(Context context, int screenHeight, int screenWidth, int x){
        Random rand = new Random();
        if (x>6)
            this.size = (int) (32*context.getResources().getDisplayMetrics().density+0.5f);
        else
            this.size = (int) ((rand.nextInt((50 - 32) + 1) + 32)*context.getResources().getDisplayMetrics().density+0.5f);
        this.color = colorIDList[rand.nextInt(colorIDList.length)];
        //Randomize speed between 1 and 15
        this.speed= rand.nextInt(10)+1;
        this.row=x;

        //Set coordinates
        this.x_coordinate=50*x*context.getResources().getDisplayMetrics().density+0.5f;
        this.y_coordinate=screenHeight+this.size;
        this.rect_coordinates=new RectF(this.x_coordinate ,
                this.y_coordinate-this.size ,
                this.x_coordinate+this.size,
                this.y_coordinate);

    }

    public void drawBalloonOnPlayArea(Canvas canvas){}

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {

        this.speed = speed;
        if(this.speed>10){
            this.speed=10;
        }
        if (this.speed<1){
            this.speed=1;
        }
    }

    public float getX_coordinate() {
        return x_coordinate;
    }

    public void setX_coordinate(float x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public float getY_coordinate() {
        return y_coordinate;
    }

    public void setY_coordinate(float y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public RectF getRect_coordinates() {
        return rect_coordinates;
    }

    public void setRect_coordinates(RectF rect_coordinates) {
        this.rect_coordinates = rect_coordinates;
    }

    public static int[] getColorIDList() {
        return colorIDList;
    }

    public static void setColorIDList(int[] colorIDList) {
        Balloon.colorIDList = colorIDList;
    }

    public int getShapeID() {
        return shapeID;
    }

    public void setShapeID(int shapeID) {
        this.shapeID = shapeID;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
