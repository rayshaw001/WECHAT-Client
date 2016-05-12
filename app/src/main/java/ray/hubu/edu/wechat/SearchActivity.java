package ray.hubu.edu.wechat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//import android.support.v7.app.*

public class SearchActivity extends Activity {
    private Button btnSearch;
    private EditText txtSearch;
    private MyHandler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        myHandler = new MyHandler();
        btnSearch = (Button)findViewById(R.id.btnSearch);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.basic_url) + "loged/Search.do";
                String keyword = txtSearch.getText().toString();
                SearchThread sThread = new SearchThread(url,keyword);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SearchThread implements Runnable
    {
        private String url;
        private String keyword;
        public SearchThread()
        {

        }
        public SearchThread(String url,String keyword)
        {
            this.url = url;
            this.keyword = keyword;
        }
        @Override
        public void run()
        {
            HttpClient client = null;
            HttpPost post = null;
            HttpResponse response = null;
            List<NameValuePair> params = null;
            HttpEntity entity = null;
            Message msg = null;
            Bundle bundle;
            String key;
            String data;
            try
            {
                client = new DefaultHttpClient();
                post = new HttpPost(url);
                params = new ArrayList<NameValuePair>();
                entity = new UrlEncodedFormEntity(params,"utf-8");
                key = txtSearch.getText().toString();
                params.add(new BasicNameValuePair("keyword", key));
                post.setEntity(entity);
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    data = EntityUtils.toString(response.getEntity());
                    msg = new Message();
                    bundle = new Bundle();
                    bundle.putString("result",data);
                    msg.setData(bundle);
                    SearchActivity.this.myHandler.sendMessage(msg);
                }
            }
            catch(UnsupportedEncodingException uee)
            {
                uee.printStackTrace();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    protected class MyHandler extends Handler{
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle b = msg.getData();
            String Json = b.getString("result");
            //Json解析并加入到ListView里面
            //txtArea.setText(txtArea.getText().toString() + "\n" + fId + ":" + "\n" + Content.substring(0, Content.lastIndexOf(":")) + "\n");
            //listView or recylerView
        }
    }
}
