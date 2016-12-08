package jp.techacademy.takuya.okitsu.labyrinth;

/**
 * Created by takuy on 2016/12/05.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;

public class Ball {
    private final Paint paint = new Paint();

    private final Bitmap ballBitmap;

    private final RectF rect;
    private final Rect srcRect;

    public interface OnMoveListener {
        int getCanMoveHorizontalDistance(RectF ballRect,int xOffset);

        int getCanMoveVerticalDistance(RectF ballRect,int yOffset);
    }

    private final OnMoveListener listener;

    public Ball(Bitmap bmp,int left,int top,float scale,OnMoveListener listener) {
        ballBitmap = bmp;

      /*  int right = left + bmp.getWidth();
        int bottom = top + bmp.getHeight();
        */
        float right =  left + bmp.getWidth() * scale;
        float bottom =  top + bmp.getHeight() * scale;

        rect = new RectF(left,top,right,bottom);

        srcRect = new Rect(0,0,bmp.getWidth(),bmp.getHeight());

        this.listener = listener;
    }
    void draw(Canvas canvas) {
        //canvas.drawBitmap(ballBitmap,rect.left,rect.top,paint);
        canvas.drawBitmap(ballBitmap,srcRect,rect,paint);
    }

    void move(float xOffset,float yOffset) {
        xOffset = listener.getCanMoveHorizontalDistance(rect,Math.round(xOffset));
        rect.offset(xOffset,0);

        yOffset = listener.getCanMoveVerticalDistance(rect,Math.round(yOffset));
        rect.offset(0,yOffset);
    }
}
