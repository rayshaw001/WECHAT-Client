package ray.hubu.edu.app;

import android.app.Application;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ray.hubu.edu.entity.User;
import ray.hubu.edu.tools.InitialThread;
import ray.hubu.edu.tools.ReciverThread;
import ray.hubu.edu.wechat.R;

/**
 * Created by Ray on 2016/3/3.
 */
public class MyApplication extends Application {
    private List<User> friends = null;
    private Map<Long,Queue<String>> msgs =null;

    private InitialThread it;
    private ReciverThread rt;
    private Socket socket;
    private SocketAddress socketaddress;

    //final private String url = getString(R.string.basic_url);

    public MyApplication() {
        super();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            msgs = new HashMap<Long, Queue<String>>();
            it = new InitialThread(this, msgs);
            rt = new ReciverThread(this, msgs);
            socket = new Socket();
            //Log.w("ip",getString(R.string.basic_ip));
            socketaddress = new InetSocketAddress(
                    getString(R.string.basic_ip),
                    Integer.parseInt(getString(R.string.basic_port)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        friends = new ArrayList<User>();
        Log.w("this is onCreate()","successed");
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        try
        {
            if(socket!=null)
            {
                socket.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<User> getFriends()
    {
        return friends;
    }

    public void setFriends(List<User> friends)
    {
        this.friends = friends;
    }

    public Map<Long, Queue<String>> getMsgs()
    {
        return msgs;
    }

    public void setMsgs(Map<Long, Queue<String>> msgs)
    {
        this.msgs = msgs;
    }

    public ReciverThread getRt() {
        return rt;
    }

    public void setRt(ReciverThread rt) {
        this.rt = rt;
    }

    public InitialThread getIt() {
        return it;
    }

    public void setIt(InitialThread it) {
        this.it = it;
    }

    public  Socket getSocket()
    {
        return this.socket;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public SocketAddress getSocketaddress(){
        return this.socketaddress;
    }

}
