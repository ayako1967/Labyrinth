package jp.techacademy.takuya.okitsu.labyrinth;

/**
 * Created by takuy on 2016/12/04.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Map {
    private final int blockSize;
    private int horizontalBlockCount;
    private int verticalBlockCount;

    private final Block[][] blockArray;

    public Map(int width,int height,int blockSize) {
        this.blockSize = blockSize;
        this.horizontalBlockCount = width / blockSize;
        this.verticalBlockCount = height / blockSize;

        blockArray = createMap(0);
    }

    private Block[][] createMap(int seed) {
       Random rand = new Random(seed);
/*        if (horizontalBlockCount % 2 == 0) {
           horizontalBlockCount--;
       }
        if (verticalBlockCount % 2 == 0) {
            verticalBlockCount--;
        }
*/
        Block[][] array = new Block[verticalBlockCount][horizontalBlockCount];
/*
        int[][] map =
                LabyrinthGenerator.getMap(horizontalBlockCount,verticalBlockCount,seed);
*/
        for (int y = 0;y < verticalBlockCount;y++) {
            for (int x = 0;x < horizontalBlockCount;x++) {
                int type = rand.nextInt(2);
                //int type = map[y][x];
                int left = x * blockSize + 1;
                int top = y * blockSize + 1;
                int right = left + blockSize - 2;
                int bottom = top + blockSize - 2;
                array[y][x] = new Block(type,left,top,right,bottom);
            }
        }
        return array;
    }
    void draw(Canvas canvas) {

        int yLength = blockArray.length;
        for (int y = 0; y < yLength;y++) {
            int xLength = blockArray[y].length;
            for (int x = 0;x < xLength;x++) {
                blockArray[y][x].draw(canvas);
            }
        }
    }

    static class Block {
        private static final int TYPE_FLOOR = 0;
        private static final int TYPE_WALL = 1;

        private static final int COLOR_FLOOR = Color.GRAY;
        private static final int COLOR_WALL = Color.BLACK;

        private final int type;
        private final Paint paint;

        final Rect rect;

        private Block(int type,int left,int top,int right,int bottom) {
            this.type = type;
            paint = new Paint();

            switch (type) {
                case TYPE_FLOOR:
                    paint.setColor(COLOR_FLOOR);
                    break;
                case TYPE_WALL:
                    paint.setColor(COLOR_WALL);
                    break;
            }

            rect = new Rect(left,top,right,bottom);
        }
        private void draw(Canvas canvas) {
            canvas.drawRect(rect,paint);
        }
    }
}
