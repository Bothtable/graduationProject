package com.myapplication.intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

//设置为单例模式
public class Network implements Runnable{

    private  static Network network = null;
    //设置唯一的socket
    private Socket socket;
    public   String account;
    public   String password;
    public   String key;

    private Network(){

    }
    public Socket getSocket(){
        while (socket == null);
        return socket;
    }

    public static Network getNetworkPtr(){
        if(network == null){
            network = new Network();
        }
        return  network;
    }

    @Override
    public void run(){
        //接受服务器的验证
        try {
            // 创建一个Socket对象，并指定服务端的IP及端口号
            socket = new Socket("175.24.1.205",10086);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendData(final JSONObject data){
      Thread tr = new Thread(){
            public void run(){
                if(socket != null){
                    if(socket.isConnected()){
                        try{
                            OutputStream os = socket.getOutputStream();
                            os.write(data.toString().getBytes());
                            os.flush();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
      tr.start();

    }
}
