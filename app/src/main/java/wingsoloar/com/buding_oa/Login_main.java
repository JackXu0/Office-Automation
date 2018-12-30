package wingsoloar.com.buding_oa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by Xerox on 2017/7/19.
 */

public class Login_main extends Activity {

    private EditText username_eb;
    private EditText password_eb;
    private TextView ip_textview;
    private Button login;
    private String username;
    private String password;
    private String ip_name;
    private String ip;
    private String id;
    private wingsoloar.com.buding_oa.views.ExpandView_login ip_list;
    private ImageView button;
    private TextView element1;
    private TextView element2;
    private TextView element3;
    private TextView element4;
    private boolean setIp=false;
    private SharedPreferences preferences;
    private boolean changed;
    private String ipString1;
    private String ipString2;
    private String ipString3;
    private String ipString4;

    OkHttpClient okHttpClient=new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);
        JMessageClient.init(this);

        initView();
        implement_ip_list();
        bindElementOnClickListener();
        initData();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost(v);
            }
        });
    }

    private void initView(){
        username_eb = (EditText) findViewById(R.id.username_eb);
        password_eb = (EditText) findViewById(R.id.password_eb);
        ip_textview=(TextView) findViewById(R.id.login_ip_textview);
        login = (Button) findViewById(R.id.login_button);
        ip_list=findViewById(R.id.login_expandView);
        button=(ImageView) findViewById(R.id.login_setting);
        element1=ip_list.findViewById(R.id.ip_list_element1);
        element2=ip_list.findViewById(R.id.ip_list_element2);
        element3=ip_list.findViewById(R.id.ip_list_element3);
        element4=ip_list.findViewById(R.id.ip_list_element4);
        ip_list.initExpandView();

        ip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ip_list.isExpand()){
                    ip_list.setVisibility(View.GONE);
                    ip_list.setIsExpand(false);
                    Animation mCollapseAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand);
                    login.clearAnimation();
                    login.startAnimation(mCollapseAnimation);
                }else{
                    ip_list.setVisibility(View.VISIBLE);
                    ip_list.expand();

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_main.this,ip_list.class);
                startActivity(intent);
            }
        });
    }

    private void implement_ip_list(){

        preferences=getSharedPreferences("ip_informaiton",MODE_PRIVATE);
        String nameString1=preferences.getString("name1","");
        String nameString2=preferences.getString("name2","");
        String nameString3=preferences.getString("name3","");
        String nameString4=preferences.getString("name4","");
        changed=preferences.getBoolean("changed",false);

        element1.setText(nameString1);
        element2.setText(nameString2);
        element3.setText(nameString3);
        element4.setText(nameString4);

        if(!nameString1.equals("")){
            element1.setVisibility(View.VISIBLE);
        }else{
            element1.setVisibility(View.GONE);
        }

        if(!nameString2.equals("")){
            element2.setVisibility(View.VISIBLE);
        }else{
            element2.setVisibility(View.GONE);
        }

        if(!nameString3.equals("")){
            element3.setVisibility(View.VISIBLE);
        }else{
            element3.setVisibility(View.GONE);
        }

        if(!nameString4.equals("")){
            element4.setVisibility(View.VISIBLE);
        }else{
            element4.setVisibility(View.GONE);
        }

        if(!changed){
            element1.setText("oa");
            element1.setVisibility(View.VISIBLE);
        }

//        boolean temp=element4.getVisibility()==View.GONE&&element3.getVisibility()==View.GONE&&element2.getVisibility()==View.GONE&&
//                element1.getVisibility()==View.GONE;
//
//        if(temp){
//            element1.setText("请前往设置");
//            element1.setVisibility(View.VISIBLE);
//        }
    }

    private void bindElementOnClickListener(){

        preferences=getSharedPreferences("ip_informaiton",MODE_PRIVATE);
        ipString1=preferences.getString("ip1","");
        ipString2=preferences.getString("ip2","");
        ipString3=preferences.getString("ip3","");
        ipString4=preferences.getString("ip4","");
        changed=preferences.getBoolean("changed",false);
        preferences=getSharedPreferences("login",MODE_PRIVATE);;
        final SharedPreferences.Editor editor = preferences.edit();

        element1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changed){
                    ip_textview.setText(element1.getText());
                    ip=ipString1.replace("\"","");
                }else{
                    ip_textview.setText("oa");
                    ip="wx.sjff119.com.cn:8887/zc_jsonfacade";
                }
                ip_list.setVisibility(View.GONE);
                ip_list.setIsExpand(false);
                Animation mCollapseAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand);
                login.clearAnimation();
                login.startAnimation(mCollapseAnimation);
                setIp=true;
                editor.putString("server_ip",ip);
                editor.putString("ip_name",element1.getText().toString());
                editor.commit();
            }
        });

        element2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip_textview.setText(element2.getText());
                ip=ipString2.replace("\"","");
                ip_list.setVisibility(View.GONE);
                ip_list.setIsExpand(false);
                Animation mCollapseAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand);
                login.clearAnimation();
                login.startAnimation(mCollapseAnimation);
                setIp=true;
                editor.putString("server_ip",ip);
                editor.putString("ip_name",element2.getText().toString());
                editor.commit();
            }
        });

        element3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip_textview.setText(element3.getText());
                ip=ipString3.replace("\"","");
                ip_list.setVisibility(View.GONE);
                ip_list.setIsExpand(false);
                Animation mCollapseAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand);
                login.clearAnimation();
                login.startAnimation(mCollapseAnimation);
                setIp=true;
                editor.putString("server_ip",ip);
                editor.putString("ip_name",element3.getText().toString());
                editor.commit();
            }
        });

        element4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip_textview.setText(element4.getText());
                ip=ipString4.replace("\"","");
                ip_list.setVisibility(View.GONE);
                ip_list.setIsExpand(false);
                Animation mCollapseAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.expand);
                login.clearAnimation();
                login.startAnimation(mCollapseAnimation);
                setIp=true;
                editor.putString("server_ip",ip);
                editor.putString("ip_name",element4.getText().toString());
                editor.commit();
            }
        });
    }

    private void initData(){
        try {
            preferences=getSharedPreferences("login",MODE_PRIVATE);
            username = preferences.getString("username", "");
            password = preferences.getString("password", "");
            ip_name=preferences.getString("ip_name","");
            ip=preferences.getString("server_ip","");
            changed=preferences.getBoolean("changed",false);
            if(!changed){
                ip="wx.sjff119.com.cn:8887/zc_jsonfacade";
                ip_name="oa";
            }
            preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);
            id=preferences.getString("id","");
            username_eb.setText(username);
            password_eb.setText(password);
            if(!ip_name.equals("")){
                ip_textview.setText(ip_name);
                setIp=true;
            }
        }catch (Exception e){

        }
    }

    private void doPost(View view){

//        if(!setIp){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(Login_main.this,
//                            "请选择服务器", Toast.LENGTH_LONG).show();
//                }
//            });
//            return;
//        }

        username=username_eb.getText().toString();
        password=password_eb.getText().toString();

        login.setBackgroundResource(R.drawable.login_on);

        preferences=getSharedPreferences("login",MODE_PRIVATE);;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("server_ip",ip);
        editor.commit();

        FormBody requestBodyBuilder1=new FormBody.Builder()
                .add("systemName","ERP")
                .add("accountName","创发拉链ERP")
                .add("userName",username)
                .add("password",password)
                .add("ip",ip).build();

        Log.e(ip,"process");

        Request.Builder builder = new Request.Builder();
        Request request1 = builder.url("http://"+ip+"/Sys/LoginFacade.asmx/LoginAccount").post(requestBodyBuilder1).build();
        executeRequest1(request1);
        Log.e("3","login1");

    }

    private void executeRequest1(Request request){
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("1","login1");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login.setBackgroundResource(R.drawable.login_off);
                        Toast.makeText(Login_main.this,
                                "连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("2","login1");
                    id=response.body().string();
                    SharedPreferences preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id",id);
                    editor.commit();
                    Log.e(username,"chat_main");
                    try{
                        JMessageClient.login(username,"123456", new BasicCallback() {

                            @Override
                            public void gotResult(int i, String s) {
                                //if(i==0)
                                    //Toast.makeText(getBaseContext(),"极光登陆成功",Toast.LENGTH_LONG).show();
                            }
                        });
                    }catch(Exception e){
                        JMessageClient.register(username, "123456", new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {

                            }
                        });
                        JMessageClient.login(username,"123456", new BasicCallback() {

                            @Override
                            public void gotResult(int i, String s) {
                                //if(i==0)
                                //Toast.makeText(getBaseContext(),"极光登陆成功",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    FormBody requestBodyBuilder2=new FormBody.Builder()
                            .add("ticker",id.replace("\"",""))
                            .add("fclass","base")
                            .add("fkey","companyname").build();


                    Request.Builder builder = new Request.Builder();
                    Request request2 = builder.url("http://" + ip + "/Sys/T_SYSTEMFacade.asmx/getSysParameter").post(requestBodyBuilder2).build();
                    executeRequest2(request2);
                    Intent intent= new Intent(Login_main.this,MainActivity.class);
                    startActivity(intent);

                    Log.e("5","login1");

                }else{
                    Log.e("3","login1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login_main.this,
                                    "登陆失败", Toast.LENGTH_SHORT).show();
                            login.setBackgroundResource(R.drawable.login_off);
                        }
                    });
                }
            }
        });
    }

    private void executeRequest2(Request request){
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login.setBackgroundResource(R.drawable.login_off);
                        Toast.makeText(Login_main.this,
                                "连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    SharedPreferences preferences=getSharedPreferences("company",MODE_PRIVATE);;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name",response.body().string());
                    editor.commit();

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login_main.this,
                                    "登陆失败", Toast.LENGTH_SHORT).show();
                            login.setBackgroundResource(R.drawable.login_off);
                        }
                    });

                }
            }
        });
    }

    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
        Log.e("ssd",String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
    }


    /**
     如果在JMessageClient.init时启用了消息漫游功能，则每当一个会话的漫游消息同步完成时
     sdk会发送此事件通知上层。
     **/
    public void onEvent(ConversationRefreshEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        //获取事件发生的原因，对于漫游完成触发的事件，此处的reason应该是
        //MSG_ROAMING_COMPLETE
        ConversationRefreshEvent.Reason reason = event.getReason();
        Log.e("ssd",String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
        Log.e("ssd","事件发生的原因 : " + reason);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        implement_ip_list();
        bindElementOnClickListener();
        Log.e("4","login1");
    }


}
