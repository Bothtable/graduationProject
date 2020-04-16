package com.example.eluoscode.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;


public class BoxsModel {
    //方块
    public Point[] boxs;
    //方块的类型
    public int boxType;
    //方块大小
    public int boxSize;
    //方块画笔
    Paint boxPaint;

    public BoxsModel(int boxSize) {
        this.boxSize = boxSize;
        boxPaint = new Paint();
        boxPaint.setColor(0xff000000);
        boxPaint.setAntiAlias(true);//抗锯齿
    }


    public void newBoxs() {
        //随机数生成一个新的方块
        Random random = new Random();
        boxType = random.nextInt(7);
        switch (boxType) {
            //田
            case 0:
                boxs = new Point[]{new Point(5, 1),
                        new Point(5, 0), new Point(4, 1),
                        new Point(4, 0)};
                break;
            //L
            case 1:
                boxs = new Point[]{new Point(4, 1),
                        new Point(3, 0), new Point(3, 1),
                        new Point(5, 1)};
                break;
            //反L
            case 2:
                boxs = new Point[]{new Point(4, 1),
                        new Point(5, 0), new Point(3, 1),
                        new Point(5, 1)};
                break;
            //长条
            case 3:
                boxs = new Point[]{new Point(5, 0),
                        new Point(4, 0), new Point(6, 0),
                        new Point(7, 0)};
                break;
            //土
            case 4:
                boxs = new Point[]{new Point(5, 1),
                        new Point(5, 0), new Point(4, 1),
                        new Point(6, 1)};
                break;
            //z
            case 5:
                boxs = new Point[]{new Point(5, 1),
                        new Point(5, 0), new Point(4, 0),
                        new Point(6, 1)};
                break;
            //反z
            case 6:
                boxs = new Point[]{new Point(5, 0),
                        new Point(5, 1), new Point(4, 1),
                        new Point(6, 0)};
                break;

        }
    }

    /**
     * 移动
     **/
    public boolean move(int x, int y,MapModel mapsModel) {
        //遍历方块数组 每一个都加上偏移的量
        for (int i = 0; i < boxs.length; i++) {
            //把方块预移动后的点 传入边界判断
            if (checkBoundary(boxs[i].x + x, boxs[i].y + y,mapsModel)) {
                return false;
            }
        }
        //遍历方块数组 每一个都加上偏移的量
        for (int i = 0; i < boxs.length; i++) {
            boxs[i].x += x;
            boxs[i].y += y;
        }
        return true;
    }

    /**
     * 旋转
     **/
    public boolean rotate(MapModel mapsModel) {
        //如果当前方块是田字型旋转失败
        if (boxType == 0) {
            return false;
        }
        //遍历方块数组 每一个都绕着中心点顺时针旋转90°
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(笛卡尔公式）
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            //将预旋转后的点 传入边界判断是否出界
            if (checkBoundary(checkX, checkY,mapsModel)) {
                //如果出界旋转失败
                return false;
            }
        }
        //遍历方块数组 每一个都绕着中心点顺时针旋转90°
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(笛卡尔公式）
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            boxs[i].x = checkX;
            boxs[i].y = checkY;
        }
        return true;
    }

    /**
     * 边界判断
     * 边界判断
     * 传入x y去判断是否在边界外
     *
     * @param x 方块x坐标
     * @param y 方块y坐标
     * @return true出界 false未出界
     */
    public boolean checkBoundary(int x, int y,MapModel mapModel) {
        return (x < 0 || y < 0 || x >= mapModel.maps.length
                || y >= mapModel.maps[0].length || mapModel.maps[x][y]);
    }

    public void drawBoxs(Canvas canvas) {
        //方块绘制
        if (boxs != null) {
            for (int i = 0; i < boxs.length; i++) {
                canvas.drawRect(
                        boxs[i].x * boxSize,
                        boxs[i].y * boxSize,
                        boxs[i].x * boxSize + boxSize,
                        boxs[i].y * boxSize + boxSize, boxPaint
                );
            }
        }
    }
}
