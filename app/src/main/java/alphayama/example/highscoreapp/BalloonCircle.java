package alphayama.example.highscoreapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BalloonCircle extends Balloon {

    public BalloonCircle(Context context, int screenHeight, int screenWidth) {
        super(context, screenHeight, screenWidth);
        this.shapeID=1;
    }

    public BalloonCircle(Context context, int screenHeight, int screenWidth, int x) {
        super(context, screenHeight, screenWidth, x);
        this.shapeID=1;
    }

    //Draw balloon on Play Area
    @Override
    public void drawBalloonOnPlayArea(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawCircle(this.x_coordinate + this.size /2,
                            this.y_coordinate - this.size/2,
                            this.size / 2, paint);
        //Update speed on each timer tick
        this.y_coordinate-=this.speed;
    }
}
