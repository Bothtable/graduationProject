package com.example.eluoscode.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.eluoscode.Config;

public class MapModel {

    //地图画笔
    Paint mapPaint;
    //辅助线画笔
    Paint linePaint;
    //状态画笔
    Paint statePaint;

    //地图
    public boolean[][] maps;
    //方块大小
    int boxSize;
    //地图宽度
    int xWidth;
    //地图高度
    int yHeight;

    public MapModel(int xWidth,int yHeight, int boxSize){
        this.boxSize = boxSize;
        this.xWidth = xWidth;
        this.yHeight = yHeight;
        //初始画笔
        mapPaint = new Paint();
        mapPaint.setColor(0x50000000);
        mapPaint.setAntiAlias(true);//抗锯齿

        linePaint = new Paint();
        linePaint.setColor(0xff666666);
        linePaint.setAntiAlias(true);//抗锯齿

        statePaint = new Paint();
        statePaint.setColor(0xffff0000);
        statePaint.setAntiAlias(true);//抗锯齿
        statePaint.setTextSize(100);

        //初始化地图
        maps = new boolean[Config.MAPX][Config.MAPY];
    }

    /*绘制地图*/
    public void drawMaps(Canvas canvas){
        //绘制地图
        for(int x = 0;x<maps.length;x++){
            for (int y = 0;y<maps[x].length;y++){
                if(maps[x][y] == true) {
                    canvas.drawRect(x * boxSize, y * boxSize,
                            x * boxSize + boxSize, y * boxSize + boxSize, mapPaint);
                }
            }
        }
    }

    /*绘制辅助线*/
    public void drawLines(Canvas canvas){
        //地图辅助线
        for (int x = 0; x < maps.length; x++) {
            canvas.drawLine(x * boxSize, 0, x * boxSize,
                    yHeight, linePaint);
        }
        for (int y = 0; y < maps[0].length; y++) {
            canvas.drawLine(0, y * boxSize,
                    xWidth, y * boxSize, linePaint);
        }
    }

    /*绘制状态*/
    public void drawState(Canvas canvas,boolean isOver,boolean isPause){
        //游戏结束
        if(isOver){
            canvas.drawText("游戏结束",xWidth/2-statePaint.measureText("游戏结束")/2,yHeight/2,statePaint);

        }
        //暂停状态
        if(isPause&&!isOver){
            canvas.drawText("暂停",xWidth/2-statePaint.measureText("暂停")/2,yHeight/2,statePaint);
        }
    }

    /*清除地图*/
    public void cleanMap(){
        //清除地图
        for(int x = 0;x<maps.length;x++){
            for(int y = 0;y<maps[0].length;y++){
                maps[x][y] = false;
            }
        }
    }

    /*消行*/
    public void cleanLine(){
        for(int y = maps[0].length-1;y>0;y--){
            //消行判断
            if(checkLine(y)){
                deleteline(y);
                //从消掉的哪一行开始重新遍历
                y++;
            }
        }
    }

    /*判断消行*/
    public boolean checkLine(int y){
        for (int x = 0; x<maps.length;x++) {
            //如果有一个不为true，则该行不能消除
            if (!maps[x][y])
                return false;
        }
            return true;
        }


    public void deleteline(int dy){
        for(int y = maps[0].length-1;y>0;y--){
            for (int x = 0; x<maps.length;x++){
                maps[x][y] = maps[x][y-1];
            }
        }
    }

}
