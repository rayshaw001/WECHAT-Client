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

/**
 * Created by Ray on 2016/3/16.
 */
public class SendThread extends Thread
{
    String fromID;
    String toID;
    String msg;
    String url;
    public SendThread(String fromID, String toID, String msg, String url)
    {
        this.fromID = fromID;
        this.toID = toID;
        this.msg = msg;
        this.url = url;
    }
    @Override
    public void run()
    {
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
            params.add(new BasicNameValuePair("toID", toID));
            params.add(new BasicNameValuePair("fromID", fromID));
            params.add(new BasicNameValuePair("msg",msg));
            entity = new UrlEncodedFormEntity(params, "utf-8");
            post.setEntity(entity);
            response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                data = EntityUtils.toString(response.getEntity());
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