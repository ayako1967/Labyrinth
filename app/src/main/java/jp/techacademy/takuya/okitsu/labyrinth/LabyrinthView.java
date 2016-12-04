package jp.techacademy.takuya.okitsu.labyrinth;

/**
 * Created by takuy on 2016/12/04.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

public class LabyrinthView extends SurfaceView implements SurfaceHolder.Callback{

    private static final  int DRAW_INTERVAL = 1000/60;

    private final Paint paint = new Paint();

    private final Bitmap ballBitmap;
    private final Bitmap bmpRsz;
    private float ballX;
    private float ballY;


    private Map map;

    public LabyrinthView(Context context) {
        super(context);

        Matrix matrix = new Matrix();

        //縮小比率
        float rsz_ratio_w = 0.4f; //横縮小率
        float rsz_ratio_h = 0.4f; //縦縮小率

        //比率をMatrixに設定
        matrix.postScale(rsz_ratio_w,rsz_ratio_h);

        //元画像
        ballBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.droid);
        //リサイズ画像
        bmpRsz = Bitmap.createBitmap(ballBitmap,0,0,ballBitmap.getWidth(),ballBitmap.getHeight(),matrix,true);

        getHolder().addCallback(this);
    }

    private DrawThread drawThread;

    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean();

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();
            while (! isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }
                drawLabyrinth(canvas);

                holder.unlockCanvasAndPost(canvas);
                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }
    }
    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }
    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }

        drawThread.finish();
        drawThread = null;
        return true;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }
    public void drawLabyrinth(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int blockSize = ballBitmap.getHeight();
        if (map == null) {
            map = new Map(canvas.getWidth(),canvas.getHeight(),blockSize);
        }
        map.draw(canvas);
        canvas.drawBitmap(bmpRsz,50,50,paint);
    }
}
