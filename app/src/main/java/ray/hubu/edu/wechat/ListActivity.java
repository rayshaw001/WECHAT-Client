package ray.hubu.edu.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ray.hubu.edu.app.MyApplication;
import ray.hubu.edu.entity.User;

/**
 * Created by Ray on 2015/12/17.
 */
public class ListActivity extends Activity {
    private ListView listview;
    private TextView tView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyApplication app = (MyApplication)getApplication();
        setContentView(R.layout.fragment_list);
        listview = (ListView)findViewById(R.id.list_view);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.item, getData(app)));
        //setContentView(listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ListActivity.this, ChatActivity.class);
                intent.putExtra("fId", app.getFriends().get(position).getId());
                android.util.Log.w("friends id is", app.getFriends().get(position).getId().toString());
                intent.putExtra("NickName", app.getFriends().get(position).getNickname());
                ListActivity.this.startActivity(intent);
            }
        });

        //android.util.Log.w("msg of is Json success" ,app.getFriends().toString());

    }

    /*
    class tClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ListActivity.this,ChatActivity.class);
            ListActivity.this.startActivity(intent);
        }
    }
    */
    private List<String> getData(MyApplication app)
    {

        List<String> data = new ArrayList<String>();
        int num = 0;
        List<User> friends = app.getFriends();
        for(int i=0;i<friends.size();i++)
        {
            num = getNum(app,friends.get(i).getId());
            data.add(friends.get(i).getNickname() + "---" + friends.get(i).getStatus() +  (num==0?"":("---" + num)));
        }
        //android.util.Log.w("data is empty",data==null?"true":"false");
        return data;
    }

    private int getNum(MyApplication app,Long id)
    {
        Log.w("getNum app:",app.toString());
        Log.w("getNum Msgs:",app.getMsgs().toString());
//        Log.w("getNum Msgs.getid:", app.getMsgs().get(id).toString());
        return app.getMsgs().containsKey(id)?app.getMsgs().get(id).size():0;
    }


}
