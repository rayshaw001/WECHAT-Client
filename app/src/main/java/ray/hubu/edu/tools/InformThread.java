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

import ray.hubu.edu.wechat.R;

/**
 * Created by Ray on 2016/3/16.
 */
//to tell server the latest msgs that i read
public class InformThread extends Thread{
    String url;
    Long usrID;
    Long msgID;
    Long fID;
    public InformThread(String url,Long usrID,Long fID,Long msgID)
    {
        this.url = url;
        this.usrID = usrID;
        this.fID =fID;
        this.msgID =msgID;
    }
    @Override
    public void run()
    {
       // String url = app.getString(R.string.basic_url);
        url += "loged/inform.do";
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
            params.add(new BasicNameValuePair("id", usrID.toString()));     //add usrId
            params.add(new BasicNameValuePair("friendID",fID.toString()));  //add friendId
            params.add(new BasicNameValuePair("msgID",msgID.toString()));   //add msgId
            entity = new UrlEncodedFormEntity(params, "utf-8");

            post.setEntity(entity);
            client.execute(post);
            //inform server to record latest msgId
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
