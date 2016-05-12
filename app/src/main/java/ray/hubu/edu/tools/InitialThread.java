package ray.hubu.edu.tools;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ray.hubu.edu.app.MyApplication;
import ray.hubu.edu.wechat.R;

/**
 * Created by Ray on 2016/3/16.
 */
//not finished
//初始化消息数据线程   登陆后线程开始
//将所有未读取得消息放入消息队列
public class InitialThread extends Thread
{
    MyApplication app;
    Map<Long,Queue<String>> msgs;
    public InitialThread(MyApplication app,Map<Long,Queue<String>> msgs)
    {
        this.app = app;
        this.msgs= msgs;
    }

    @Override
    public void run()
    {
        String url = app.getString(R.string.basic_url);
        url += "loged/getAllMsg.do";
        HttpPost post = null;
        List<NameValuePair> params = null;
        String data = null;
        HttpEntity entity = null;
        HttpClient client = null;
        HttpResponse response = null;
        try {
            post =  new HttpPost(url);
            client = new DefaultHttpClient();
            params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("myID", app.getFriends().get(0).getId().toString()));
//                params.add(new BasicNameValuePair("fromID", fromID));
//                params.add(new BasicNameValuePair("msg",msg));
            entity = new UrlEncodedFormEntity(params, "utf-8");
            post.setEntity(entity);
            response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                data = EntityUtils.toString(response.getEntity());

                //Json transport
                //and put all msg into msg queue

                if(data==null||data.equals("fail"))
                {
                    android.util.Log.w(" result of send msg ",data);
                }
            }
        }
        catch (UnsupportedEncodingException ue)
        {
            ue.printStackTrace();
        }
        catch(ClientProtocolException cpe)
        {
            cpe.printStackTrace();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

}