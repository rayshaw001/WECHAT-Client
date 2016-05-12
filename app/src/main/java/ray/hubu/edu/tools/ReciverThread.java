package ray.hubu.edu.tools;

import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import ray.hubu.edu.app.MyApplication;

/**
 * Created by Ray on 2016/3/16.
 */
//消息接收线程    登陆后开始   收到消息之后放入MyApplication对象里面的消息队列
public class ReciverThread extends Thread
{
    MyApplication app = null;
    Map<Long,Queue<String>> msgs;
    //msgs addict with msgID info
    //msg format "fromID:msg:msgID"
    Socket socket;
    InputStream is;
    boolean flag = false;
    public ReciverThread(MyApplication app,Map<Long,Queue<String>> msgs)
    {
        this.app = app;
        this.msgs =msgs;
    }

    @Override
    public void run()
    {
        Queue<String> queue;
        String Content = null;
        InetAddress iAddress = null;
        DatagramPacket dp = null;
        DatagramSocket ds = null;
        byte []data = null;
        Long fId ;
        Message msg = null;
        byte[] b = new byte[1024];
        //Bundle b = null;// �������
        socket = app.getSocket();
        try {
            is = socket.getInputStream();
            while (!flag)
            {
                is.read(b);
                Content = new String(b,0,b.length,"gbk");
                if(Content.contains("true")&&Content.length()==4)
                {
                    flag = true;
                }
                else if(Content!=null&&Content.length()>1)
                {
                    android.util.Log.w("receiver msg =", Content);
                    fId = Long.valueOf(MessageTool.getID(Content));
                    queue = app.getMsgs().get(fId);
                    synchronized (queue)
                    {
                        queue.add(Content);
                        queue.notifyAll();
                    }
                }


            //b = new Bundle();
            //iAddress = InetAddress.getByName(app.getFriends().get(0).getIp());
            //ds = new DatagramSocket(2222);
//            data = new byte[1024];
//            dp = new DatagramPacket(data, 1024);
//            while(true) {
//                msg = new Message();
//                ds.receive(dp);
//                Content = new String(dp.getData(),0,dp.getLength(),"gbk");
//
//                if(Content!=null&&Content.length()>1) {
//                    android.util.Log.w("receiver msg =", Content);
//                    fId = Long.valueOf(MessageTool.getID(Content));
//                    queue = app.getMsgs().get(fId);
//                    synchronized (queue)
//                    {
//                        queue.add(Content);
//                        queue.notifyAll();
//                    }
                    /*
                    else
                    {
                        Queue<String> q = new ArrayDeque<String>();
                        q.add(Content);
                        app.getMsgs().put(fId,q);
                    }
                    */
                    //other information
            //    }

                //Thread.currentThread().notifyAll();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        finally
        {
            //ds.close();
        }
    }


}