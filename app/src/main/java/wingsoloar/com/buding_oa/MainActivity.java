package wingsoloar.com.buding_oa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import wingsoloar.com.buding_oa.Contact.ContactList_main_fragment;
import wingsoloar.com.buding_oa.homepage.Homepage_main;
import wingsoloar.com.buding_oa.manager.UpdateManager;

import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_SOUND;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_VIBRATE;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Fragment fragment_homepage;
    private Fragment fragment_contact;

    private ImageView imageContact;
    private ImageView imageHomepage;

    private LinearLayout contact_layout;
    private LinearLayout homepage_layout;

    private TextView extra;
    private String company_name = "";
    private boolean isExit = false;
    private TextView homepage_page;
    private LinearLayout bottom_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        bottom_bar = findViewById(R.id.bottom_bar);
        Log.e("onCreate()", "process");

        init();
        intiEvents();
        if (savedInstanceState == null)
            setSelected(1);

    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.registerEventReceiver(this);
    }

    public void intiEvents() {
        contact_layout.setOnClickListener(this);
        homepage_layout.setOnClickListener(this);
        Log.e("intiEvent()", "process");
    }

    public void init() {
        Log.e("init()", "process");
        imageContact = (ImageView) findViewById(R.id.bottom_bar_contact_image);
        imageHomepage = (ImageView) findViewById(R.id.bottom_bat_homepage_image);

        contact_layout = (LinearLayout) findViewById(R.id.bottom_bar_content_layout);
        homepage_layout = (LinearLayout) findViewById(R.id.bottom_bar_homepage_layout);

        extra = (TextView) findViewById(R.id.top_message_title);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view1 = inflater.inflate(R.layout.homepage_main_original, null);

        //JMessageClient.setNotificationFlag();
        JMessageClient.registerEventReceiver(this);
    }


    public void setSelected(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //FragmentTransaction transChild=getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);

        switch (i) {

            case 1:

                imageHomepage.setImageResource(R.drawable.bottom_bar_homepage_pressed);


                if (fragment_homepage == null) {
                    fragment_homepage = new Homepage_main();


                    if (!fragment_homepage.isAdded()) {

                        transaction.add(R.id.main_fragment, fragment_homepage);
                    }
                } else {

                    transaction.show(fragment_homepage);
                }

                Log.e("homepage selected", "process");
                break;

            case 2:

                imageContact.setImageResource(R.drawable.bottom_bar_contact_pressed);

                if (fragment_contact == null) {

                    fragment_contact = new ContactList_main_fragment();
                    if (!fragment_contact.isAdded())
                        transaction.add(R.id.main_fragment, fragment_contact);

                } else {
                    transaction.show(fragment_contact);
                }


                Log.e("contact_list selected", "process");
                break;


        }

        transaction.commit();
        //transChild.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (fragment_contact != null) {
            transaction.hide(fragment_contact);
            Log.e("contact_list hiden", "process");
        }
        if (fragment_homepage != null) {
            transaction.hide(fragment_homepage);
            Log.e("homepage hiden", "process");
        }
    }

    @Override
    public void onClick(View view) {
        resetImages();
        switch (view.getId()) {
            case R.id.bottom_bar_content_layout:
                setSelected(2);
                break;

            case R.id.bottom_bar_homepage_layout:
                setSelected(1);
                break;
        }
    }

    private void resetImages() {
        imageContact.setImageResource(R.drawable.bottom_bar_contact);
        imageHomepage.setImageResource(R.drawable.bottom_bar_homepage);
        Log.e("homepage images reseted", "process");
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };

    public void onEventMainThread(MessageEvent event) {
        cn.jpush.im.android.api.model.Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
//                messages.add(msg);
//                adapter.notifyDataSetChanged();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;


            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(mEventBus event) {
        if(event.getPage()==0){
            bottom_bar.setVisibility(View.VISIBLE);
        }else{
            bottom_bar.setVisibility(View.GONE);
        }
    }
}
