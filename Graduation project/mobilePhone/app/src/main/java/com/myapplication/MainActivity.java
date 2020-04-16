package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.intent.MyPackage;
import com.myapplication.intent.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class MainActivity extends Activity {
    EditText name;  //用户名
    EditText pass;  //密码
    Socket socket;
    InputStream is ;
    Network network;
    Thread thread;
    Thread readThread;
    boolean readFlag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText) findViewById(R.id.name);  //获取用户名
        pass=(EditText) findViewById(R.id.pass);  //获取密码

        //初始化连接
        thread = new Thread(Network.getNetworkPtr());
        thread.start();
        network = Network.getNetworkPtr();
        socket = network.getSocket();
        //开始获取服务器数据
        readData();

    }

    //将账号密码保存起来并进行登录
    public void  Check(View v) {
        if(!socket.isConnected()){
            thread.start();
            socket = network.getSocket();
        }
        readThread.start();
        Network.getNetworkPtr().account = name.getText().toString();
        Network.getNetworkPtr().password = pass.getText().toString();
        String account = name.getText().toString();
        String password = pass.getText().toString();
        if(account.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this,"数据不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject json =  MyPackage.jsonPack("type",0,"account",account,"password",password);
        Network.getNetworkPtr().sendData(json);


    }

    //读方法
    public void readData(){
        readThread = new Thread(){
            public void run(){
                //接受服务器的验证
                try {
                    while (readFlag) {
                        is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuffer data = new StringBuffer();
                        while (data.lastIndexOf("}") == -1) {
                            String temp = br.readLine();
                            data.append(temp);
                        }
                        if(data.toString().equals("")){
                            continue;
                        }
                        JSONObject jsonObject = new JSONObject(data.toString());
                        handleRecieveData(jsonObject);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
    }

    public void handleRecieveData(JSONObject json){
        System.out.println(json);
        try{
            int type = (int)json.get("type");
            switch (type){
                //处理登录返回数据
                case  0:
                    handlLoginData(json);
                    break;
                case  1:
                    break;
                case  2:
                    break;
                default:
                    break;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    public void handlLoginData(JSONObject json){
        try{
            String result = json.getString("result");
            if(result.equals("yes")){
                //调用下一个界面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }else if(result.equals("no")) {
                readFlag = false;
                Looper.prepare();
                Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
