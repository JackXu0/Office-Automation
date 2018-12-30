package wingsoloar.com.buding_oa.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.information.User_info;

/**
 * Created by wingsolarxu on 2017/7/28.
 */

public class chat_main extends Activity {

    private Button send_button;
    private LinearLayout return_button;
    private EditText send_content;

    private ListView messagelist;
    private ArrayAdapter adapter;
    private List<Message> messages;
    Conversation conversation;
    private Context context;
    private int type=0;
    private Message message;
    private String username;
    private String password;
    private ImageView right_info_button;
    private String name_the_other;
    private TextView username_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        messages=new ArrayList<>();
        context=this;

        final int type=getIntent().getIntExtra("isNotification",0);
        init();
        register(type);
    }


    private void init() {

        send_button= (Button) findViewById(R.id.typingbar_btn_send);
        send_content= (EditText) findViewById(R.id.typingbar_editText);
        messagelist= (ListView) findViewById(R.id.message_listview);
        return_button=findViewById(R.id.chat_main_return_btn);
        right_info_button=findViewById(R.id.chat_right_info_btn);
        username_tv=findViewById(R.id.chat_main_username_tv);
    }

    private void register(int isNotification){
        Log.e("1","chat_main");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(chat_main.this,
                        "success", Toast.LENGTH_SHORT).show();
            }
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if(isNotification==0){
            Log.e("2","chat_main");
            conversation=JMessageClient.getSingleConversation(getIntent().getStringExtra("name_id"));
            name_the_other=getIntent().getStringExtra("name1");
            if(conversation==null){
                conversation = Conversation.createSingleConversation(getIntent().getStringExtra("name_id"));
                //conversation=Conversation.createSingleConversation("29203021");
            }
        }else{
            Log.e("3","chat_main");
            conversation=JMessageClient.getSingleConversation(getIntent().getStringExtra("username"));
            //conversation=JMessageClient.getSingleConversation("29203021");
            name_the_other=getIntent().getStringExtra("username");
            if(conversation==null){
                conversation = Conversation.createSingleConversation(getIntent().getStringExtra("username"));
                //conversation=Conversation.createSingleConversation("29203021");
            }
        }

        username_tv.setText(name_the_other);
        Log.e("111","process");
        right_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(chat_main.this,User_info.class);
                intent.putExtra("info_name",name_the_other);
                chat_main.this.startActivity(intent);
                Log.e(name_the_other,"process");
                Log.e("111","process");
            }
        });

       // Log.e("5","chat_main");
        messages.addAll(conversation.getAllMessage());
       // Log.e("6","chat_main");

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=send_content.getText().toString();
                send_content.setText("");
          //      JMessageClient.createSingleTextMessage(String username, String appKey, String text)
                message=conversation.createSendMessage(new TextContent(content));
                messages.add(message);
                adapter.notifyDataSetChanged();
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseDesc) {
                        if (responseCode == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("5","chat_main");
                                    Toast.makeText(chat_main.this,
                                            "success", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("6","chat_main");
                                    Toast.makeText(chat_main.this,
                                            "fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                JMessageClient.sendMessage(message);
            }
        });



        adapter=new MessageAdapter(context,messages);
        messagelist.setAdapter(adapter);
    }
}
