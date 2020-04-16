package com.example.eluoscode;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import com.example.eluoscode.control.GameControl;


import static com.example.eluoscode.Config.XWHITE;
import static com.example.eluoscode.Config.YHEIGHT;

public class MainActivity extends Activity implements View.OnClickListener {

    //view里面没有变量，没有数据
    //游戏区域控件
    public View gamePanel;

    //游戏控制器
    GameControl gameControl;

    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            String type = (String)msg.obj;

            if(type == null){
                return;
            }
            if(type.equals("invalidate")) {
                //刷新重绘view
                gamePanel.invalidate();
            }else if(type.equals("pause")){

            }else if(type.equals("continue")){

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        gameControl = new GameControl(handler, this);
        initView();
        intiListener();
    }

    /**
     * 初始化视图
     **/
    public void initView() {

        //1.得到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.layoutGame);
        //实例化游戏区域
        gamePanel = new View(this) {
            //重写游戏区域绘制
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //绘制
                gameControl.draw(canvas);
            }
        };
        //3.设置游戏区域大小
        gamePanel.setLayoutParams(new ViewGroup.LayoutParams(XWHITE, YHEIGHT));

        //设置背景颜色
        gamePanel.setBackgroundColor(0x10000000);
        //4.添加进父容器
        layoutGame.addView(gamePanel);
    }

    /**
     * 初始化监听
     **/
    public void intiListener() {
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnTop).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnDown).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
    }



    /**
     * 捕捉点击事件
     **/
    @Override
    public void onClick(View v) {
        gameControl.onClick(v.getId());

        //调用重绘view
        gamePanel.invalidate();
    }

}