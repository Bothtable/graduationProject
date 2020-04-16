package com.example.eluoscode.control;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.eluoscode.Config;
import com.example.eluoscode.R;
import com.example.eluoscode.model.BoxsModel;
import com.example.eluoscode.model.MapModel;

public class GameControl {

    Handler handler;

    MapModel mapsModel;
    //方块模型
    BoxsModel boxsModel;

    //自动下落线程
    public Thread downThread;

    //游戏暂停状态
    public  boolean isPause;
    //游戏接收状态
    public boolean isOver;

    public GameControl(Handler handler,Context context) {
        this.handler = handler;
        initData(context);
    }

    /**
     * 初始化数据
     **/
    public void initData(Context context) {
        //获得屏幕宽度
        int width = getScreenWidth(context);
        //设置游戏区域宽度 = 屏幕宽度2/3
        Config.XWHITE = width * 2 / 3;
        Config.YHEIGHT = Config.XWHITE * 2;

        //初始花放块大小 = 游戏区域宽度/10
        int temp= Config.XWHITE / Config.MAPX;
        //实例化方块
        boxsModel = new BoxsModel(temp);
        //实例化地图模型
        mapsModel = new MapModel(Config.XWHITE,Config.YHEIGHT,boxsModel.boxSize);

    }

    /**
     * 获得屏幕宽度的方法
     **/
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 开始游戏
     **/
    public void startGame() {
        if (downThread == null) {
            downThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            //休眠500ms
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断是否处于结束状态
                        if (isOver || isPause)
                            continue;
                        //执行一次下落
                        moveBottom();
                        //通知主线程刷新view
                        Message msg = new Message();
                        msg.obj = "invalidate";
                        handler.sendMessage(msg);
                    }
                }
            };
            downThread.start();
        }
        //清除地图
        mapsModel.cleanMap();
        //游戏结束状态设为false
        isOver = false;
        //暂停状态设为false
        isPause = false;
        //生成新的方块
        boxsModel.newBoxs();
    }


    /**
     * 下落
     **/
    public boolean moveBottom() {
        //移动成功 不做处理
        if (boxsModel.move(0, 1, mapsModel))
            return true;
        //移动失败 堆积处理
        for (int i = 0; i < boxsModel.boxs.length; i++)
            mapsModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y] = true;
        //消行处理
        mapsModel.cleanLine();
        //生成新的方块
        boxsModel.newBoxs();
        //判断游戏结束
        isOver = checkOver();
        return false;
    }

    /**
     * 结束判断
     **/
    public boolean checkOver() {
        for (int i = 0; i < boxsModel.boxs.length; i++) {
            if (mapsModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y])
                return true;
        }
        return false;
    }

    //绘制控制
    public void draw(Canvas canvas) {
        mapsModel.drawMaps(canvas);
        //绘制方块
        boxsModel.drawBoxs(canvas);
        //地图辅助线
        mapsModel.drawLines(canvas);
        //绘制状态
        mapsModel.drawState(canvas,isOver,isPause);
    }

    /**暂停**/
    public void setPause(){
        if(isPause) {
            isPause = false;
            Message msg = new Message();
            msg.obj = "pause";
            handler.sendMessage(msg);
        }

        else {
            isPause = true;
            Message msg = new Message();
            msg.obj = "continue";
            handler.sendMessage(msg);
        }
    }

    public void onClick(int id) {
        switch (id) {
            //左
            case R.id.btnLeft:
                if(isPause || isOver)
                    return;
                boxsModel.move(-1, 0,mapsModel);
                break;
            //上
            case R.id.btnTop:
                if(isPause || isOver)
                    return;
                boxsModel.rotate(mapsModel);
                break;
            //右
            case R.id.btnRight:
                if(isPause || isOver)
                    return;
                boxsModel.move(1, 0,mapsModel);
                break;
            //下
            case R.id.btnDown:
                if(isPause||isOver)
                    return;
                //快输下落
                while (true) {
                    if (!moveBottom()||boxsModel.boxs.length == 0)
                        break;
                }
                break;
            //开始
            case R.id.btnStart:
                startGame();
                break;
            //暂停
            case R.id.btnPause:
                setPause();
                break;
        }
    }
}
