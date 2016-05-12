package ray.hubu.edu.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import ray.hubu.edu.app.MyApplication;
import ray.hubu.edu.entity.User;

/**
 * Created by Ray on 2015/12/16.
 */
public class LoginActivity extends Activity {
    private Button btnLogin = null;
    private Button btnRegister = null;
    private TextView txtUsername = null;
    private TextView txtPassword = null;
    private TextView error = null;
    private String IP = null;
    private Socket socket = null;
    private SocketAddress socketaddress = null;
   final private MyApplication app = (MyApplication)getApplication();

    //private LoginHandler loginHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new LoginListener());
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new RegisterListener());

        // relationship handler
        //loginHandler = (LoginHandler)new Handler(this.getMainLooper());
    }
    class RegisterListener implements View.OnClickListener{
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,RegisterActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }

    class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                txtUsername = (TextView) findViewById(R.id.iptUsername);
                txtPassword = (TextView) findViewById(R.id.iptPassword);
                error = (TextView)findViewById(R.id.error);
                String username = txtUsername.getText().toString();
                String psw = txtPassword.getText().toString();

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ListActivity.class);
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if(wifiManager.isWifiEnabled())
                {
                    IP=intToIp(wifiManager.getConnectionInfo().getIpAddress());
                }
                else
                {

                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
                    {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                        {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress())
                            {
                               IP = inetAddress.getHostAddress().toString();
                            }
                        }
                    }

                }

                LoginThread login = new LoginThread(username,psw,IP,intent);
                Thread t = new Thread(login);
                t.start();
                error.setText(new String("username or password is wrong"));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }
    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    class LoginThread implements Runnable{
        Intent intent;
        String username;
        String psw;
        String IP ;
        LoginThread(String username,String psw ,String IP,Intent intent)
        {
            this.intent = intent;
            this.username = username;
            this.psw = psw;
            this.IP = IP;
        }
        @Override
        public void run(){
            String id;
            //network connection operation
            String url = getString(R.string.basic_url) + "loging/login.do";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String data = null;
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", psw));
            params.add(new BasicNameValuePair("IP",IP));
            HttpEntity entity = null;

            try {
                entity = new UrlEncodedFormEntity(params, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            post.setEntity(entity);
            HttpClient client = new DefaultHttpClient();

            HttpResponse response = null;
            try {
                //System.out.println(post);
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response.
                    getStatusLine().
                    getStatusCode() == HttpStatus.SC_OK) {

                try {
                    data = EntityUtils.toString(response.getEntity());
                    android.util.Log.w("myself",data);
                    System.out.println("data is " + data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(data==null||data.equals("")) {
                //提示错误信息

            }
            else
            {

                android.util.Log.w("received msg", data);
                MyApplication app = (MyApplication)getApplication() ;
                //ray.hubu.edu.app.MyApplication.ReciverThread rt =app.getRt();
                //data transfer to List<User>
                //means Json to User
                List<User> friends = JsonToUsers(data);
                app.setFriends(friends);
                app.getFriends().get(0).setIp(IP);
                for(User u:friends)
                {
                    app.getMsgs().put(u.getId(),new ArrayDeque<String>());
                }

                app.getRt().start();
                app.getIt().start();
                try
                {
                    //改用socket
                    socket = app.getSocket();
                    socketaddress = app.getSocketaddress();
                    socket.setKeepAlive(true);
                    socket.connect(socketaddress);
                    OutputStream os = socket.getOutputStream();
                    os.write(app.getFriends().get(0).getId().toString().getBytes());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                LoginActivity.this.startActivity(intent);
            }
        }
    }
    private List<User> JsonToUsers(String Json)
    {
        List<User> list = null;
        String temp = null;
        JSONObject jo;
        JSONArray ja ;
        try {
            list = new ArrayList<User>();
            temp ="{\"user\":"+ Json + "}";
            //android.util.Log.w("changed json",temp);
            jo = new JSONObject(temp);
            ja = jo.getJSONArray("user");
            //android.util.Log.w("warn","json size is " + ja.length());
            for(int i = 0;i<ja.length();i++)
            {
                User user = new User();
                jo = ja.getJSONObject(i);
                user.setId(jo.getLong("id"));
                user.setName(jo.getString("name"));
                user.setNickname(jo.getString("nickname"));
                user.setStatus(jo.getString("status"));
                list.add(user);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
