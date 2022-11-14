package <package_name>.highscoreapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class BalloonSquare extends Balloon {
    public BalloonSquare(Context context, int screenHeight, int screenWidth) {
        super(context, screenHeight, screenWidth);
        this.shapeID=0;
    }

    public BalloonSquare(Context context, int screenHeight, int screenWidth, int x) {
        super(context, screenHeight, screenWidth, x);
        this.shapeID=0;
    }

    //Draw balloon on Play Area
    @Override
    public void drawBalloonOnPlayArea(Canvas canvas){
        this.rect_coordinates=new RectF(this.x_coordinate ,
                this.y_coordinate-this.size ,
                this.x_coordinate+this.size,
                this.y_coordinate);
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawRect(rect_coordinates, paint);
        //Update speed on each timer tick
        this.y_coordinate-=this.speed;
    }
}
