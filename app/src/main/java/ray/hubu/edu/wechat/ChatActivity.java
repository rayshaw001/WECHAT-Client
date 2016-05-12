package ray.hubu.edu.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Queue;

import ray.hubu.edu.app.MyApplication;
import ray.hubu.edu.tools.InformThread;
import ray.hubu.edu.tools.MessageTool;
import ray.hubu.edu.tools.SendThread;

/**
 * Created by Ray on 2015/12/17.
 */
public class ChatActivity extends Activity {
    private TextView friendView;
    private Button btnSend;
    private EditText txtSend;
    static private TextView txtArea;
    static String content = "";
    private String url ;
    private MyHandler myHandler;
    //final MyApplication app = (MyApplication)getApplication();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        Intent intent = getIntent();

        myHandler = new MyHandler();
        //important info
        final MyApplication app = (MyApplication)getApplication();
        //MyApplication app = (MyApplication)getApplication();
        url = getString(R.string.basic_url);
        final String myId = app.getFriends().get(0).getId().toString();
        final String myNickName = ((MyApplication)getApplication()).getFriends().get(0).getNickname();
        final String friendNickName = intent.getExtras().getString("NickName");
        final String friendId =  String.valueOf(intent.getExtras().getLong("fId"));
        //head info
        friendView =(TextView) findViewById(R.id.friendLabel);
        friendView.setText(friendNickName);

        txtSend = (EditText)findViewById(R.id.txtSend);
        txtArea = (TextView)findViewById(R.id.textArea);
        //btnSend msg
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtTobeSent;

                txtTobeSent = txtSend.getText().toString();

                content = txtArea.getText().toString();
                //start to add myself msg
                content += myNickName + ":\n" + txtTobeSent +"\t" + "time" + "\n";
                txtSend.setText("");
                txtArea.setText(content);
                SendThread sThread = new SendThread(myId,friendId,txtTobeSent,getString(R.string.basic_url)+ "loged/saveMessage.do");
                sThread.start();
            }
        });

        ReadThread rThread = new ReadThread(app,friendId);
        rThread.start();

    }


    // get message from queue according to friend's id
    class ReadThread extends Thread
    {
        MyApplication app;
        boolean wait = false;
        Long id;
        String fID;
        public ReadThread(MyApplication app,String fID){
            this.app = app;
            this.fID = fID;
            id = app.getFriends().get(0).getId();
        };
        @Override
        public void run()
        {

            String msg;
            Long max = 0L;
            Long temp = 0L;
            Message message;
            Bundle b;
            while (!wait) {
                Queue<String> queue =app.getMsgs().get(id);
                synchronized (queue) {
                    if (queue ==null || queue.size() == 0) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    } else {
                        //queue = app.getMsgs().get(id);
                        while (queue.size() != 0) {
                            message = new Message();
                            b = new Bundle();
                            msg = queue.poll();
                            b.putString("msg", msg);
                            temp = Long.parseLong(MessageTool.getMsgId(msg));
                            max = max>temp?max:temp;
                            message.setData(b);
                            ChatActivity.this.myHandler.sendMessage(message);
                        }
                        new InformThread(getString(R.string.basic_url),
                                id,Long.parseLong(fID),max).start();
                    }
                }
            }
        }
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }
        public MyHandler(Looper L) {
            super(L);
        }

        // must override method handleMessage
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // update UI
            Bundle b = msg.getData();
            Long fId ;

            String Content = b.getString("msg");
            fId = Long.valueOf(MessageTool.getID(Content));
            String preTxt =txtArea.getText()==null?"":txtArea.getText().toString();
            txtArea.setText(preTxt + "\n" + fId + ":" + "\n" + MessageTool.getMsg(Content) + "\n");
        }
    }

}
