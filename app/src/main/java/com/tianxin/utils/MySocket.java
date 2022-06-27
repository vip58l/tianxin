package com.tianxin.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tianxin.R;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Socket.body;
import com.tencent.opensource.model.Socket.data;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * SOCKET通信
 */
public class MySocket extends Thread {
    private static final String TAG = MySocket.class.getSimpleName();
    private static MySocket mySocket;
    private static boolean istrue = true;
    public Thread myThread, readObject;
    public Socket socket = null;
    private int UserId;
    private Paymnets paymnets;
    private Gson gson = new Gson();

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public static MySocket getMySocket() {
        if (mySocket == null) {
            istrue = true;
            mySocket = new MySocket();
        }
        return mySocket;
    }

    public static MySocket getMySocket(Paymnets paymnets) {
        if (mySocket == null) {
            istrue = true;
            mySocket = new MySocket();

            if (mySocket.socket == null) {
                mySocket.start();
            }
        }
        mySocket.setPaymnets(paymnets);
        return mySocket;
    }

    @Override
    public void run() {
        Inittsocket();
    }

    /**
     * 初始化initsocket
     */
    private void Inittsocket() {
        try {
            String url = BuildConfig.HTTP_WEB.split("//")[1];
            UserId = Integer.parseInt(UserInfo.getInstance().getUserId());
            socket = new Socket(TextUtils.isEmpty(url) ? "150.158.53.237" : url, 12345);
            socket.setKeepAlive(true);//开启保持活动状态的套接字

            System.out.println("连接服务socket正常");

            //SOCKET上线了 首次进入给服务器发送消息
            //sendselrver(socket);

            //启动线程发送心跳Alive
            myThread = new writeObject();
            myThread.start();

            //接收服务端发来的消息
            readObject = new readObject();
            readObject.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接服务失败，请稍后再试...");
            connection();
        }
    }

    /**
     * 给服务器发送聊天消息
     */
    public void sendselrver() {
        try {
            if (socket.isConnected()) {
                UserInfo userInfo = UserInfo.getInstance();
                data info = new data();
                info.setCode(3000);
                info.setMessage(DemoApplication.instance().getString(R.string.Tcahtsment));
                info.setUserid(Integer.parseInt(userInfo.getUserId()));
                info.setSex(Integer.parseInt(userInfo.getSex()));
                info.setName(userInfo.getName());
                info.setAvatar(userInfo.getAvatar());
                info.setToken(userInfo.getToken());

                body<data> databody = new body<>();
                databody.setMsg(DemoApplication.instance().getString(R.string.Tcahtsment));
                databody.setCode(3000);
                databody.setData(info);

                String toJson = gson.toJson(databody);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(toJson);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给服务器发送聊天消息
     */
    public void sendselrver(String msg) {
        try {
            if (socket.isConnected()) {
                UserInfo userInfo = UserInfo.getInstance();
                data info = new data();
                info.setCode(2000);
                info.setMessage(msg);
                info.setUserid(Integer.parseInt(userInfo.getUserId()));
                info.setSex(Integer.parseInt(userInfo.getSex()));
                info.setName(userInfo.getName());
                info.setAvatar(userInfo.getAvatar());
                info.setToken(userInfo.getToken());

                body<data> databody = new body<data>();
                databody.setMsg("聊天");
                databody.setCode(2000);
                databody.setData(info);

                String toJson = gson.toJson(databody);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(toJson);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给服务器发送聊天消息
     */
    public void sendselrver(String msg, int type) {
        try {
            if (socket.isConnected()) {
                UserInfo userInfo = UserInfo.getInstance();
                data info = new data();
                info.setCode(type);
                info.setMessage(msg);
                info.setUserid(Integer.parseInt(userInfo.getUserId()));
                info.setSex(Integer.parseInt(userInfo.getSex()));
                info.setName(userInfo.getName());
                info.setAvatar(userInfo.getAvatar());
                info.setToken(userInfo.getToken());

                body<data> databody = new body<>();
                databody.setMsg("chat");
                databody.setCode(type);
                databody.setData(info);

                String toJson = gson.toJson(databody);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(toJson);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给服务器发送聊天消息
     */
    public void sendselrver(String img, String video, int type) {
        try {
            if (socket.isConnected()) {
                UserInfo userInfo = UserInfo.getInstance();
                data info = new data();
                info.setCode(type);
                info.setMessage("chat");
                info.setVideo(video);
                info.setImges(img);
                info.setUserid(Integer.parseInt(userInfo.getUserId()));
                info.setSex(Integer.parseInt(userInfo.getSex()));
                info.setName(userInfo.getName());
                info.setAvatar(userInfo.getAvatar());
                info.setToken(userInfo.getToken());

                body<data> databody = new body<>();
                databody.setMsg("chat");
                databody.setCode(type);
                databody.setData(info);

                String toJson = gson.toJson(databody);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(toJson);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送心跳包
     */
    private class writeObject extends Thread {
        @Override
        public void run() {
            try {
                if (socket.isConnected()) {
                    UserInfo userInfo = UserInfo.getInstance();
                    data info = new data();
                    info.setCode(1);
                    info.setMessage("Alive");
                    info.setUserid(Integer.parseInt(userInfo.getUserId()));
                    info.setSex(Integer.parseInt(userInfo.getSex()));
                    info.setName(userInfo.getName());
                    info.setAvatar(userInfo.getAvatar());
                    info.setToken(userInfo.getToken());
                    body<data> databody = new body<>();
                    databody.setMsg("Alive");
                    databody.setCode(1000);
                    databody.setData(info);

                    while (istrue) {
                        if (socket.getOutputStream() == null) {
                            break;
                        }
                        String toJson = gson.toJson(databody);
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(toJson);
                        out.flush();
                        Thread.sleep(5000);
                    }
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println("网络异常继开...");
                e.printStackTrace();
            }

        }
    }

    /**
     * 读取服务端发来的消息
     */
    private class readObject extends Thread {
        @Override
        public void run() {
            try {
                if (socket.isConnected()) {
                    while (istrue) {
                        ObjectInput in = new ObjectInputStream(socket.getInputStream());
                        String entity = (String) in.readObject();
                        //System.out.println(entity);
                        if (paymnets != null) {
                            paymnets.onSuccess(entity);
                        }
                    }
                    socket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                socket = null;
                System.out.println("连接服务失败，请稍后再试...");
                connection();
            }


        }
    }

    /**
     * 连接请求失败，请稍后重试...
     */
    private void connection() {
        try {
            int count = 20;
            int millis = 1000;
            for (int i = 0; i < count; i++) {
                System.out.println(String.format("%s秒后重新连接服务...", count - i));
                Thread.sleep(millis);
            }

            if (istrue) {
                run();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 退出发送心跳包Alive...
     */
    public static void unInit() {
        istrue = false;
        mySocket = null;
    }
}
