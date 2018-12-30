package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.im.android.api.JMessageClient;
import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/8/28.
 */

public class Homepage_setting_notification extends Activity implements View.OnClickListener{

    private TextView title;
    private ImageView receive_button;
    private ImageView sound_button;
    private ImageView vibrate_button;
    private boolean is_to_receive;
    private boolean is_to_make_a_sound;
    private boolean is_to_vibrate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_setting_notification);

        title=findViewById(R.id.top_message_title);
        receive_button=findViewById(R.id.receive_button);
        sound_button=findViewById(R.id.sound_button);
        vibrate_button=findViewById(R.id.vibrate_button);

        SharedPreferences preferences=getSharedPreferences("homepage_setting_notificaiton",MODE_PRIVATE);
        is_to_receive=preferences.getBoolean("is_to_receive",true);
        is_to_make_a_sound=preferences.getBoolean("is_to_make_a_sound",true);
        is_to_vibrate=preferences.getBoolean("is_to_vibrate",true);


        title.setText("新消息通知");
        receive_button.setOnClickListener(this);
        sound_button.setOnClickListener(this);
        vibrate_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences preferences=getSharedPreferences("homepage_setting_notificaiton",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        is_to_receive=preferences.getBoolean("is_to_receive",true);
        is_to_make_a_sound=preferences.getBoolean("is_to_make_a_sound",true);
        is_to_vibrate=preferences.getBoolean("is_to_vibrate",true);
        switch (view.getId()){
            case R.id.receive_button:
                if (is_to_receive==true){
                    JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                    receive_button.setBackgroundResource(R.drawable.login_off);
                    editor.putBoolean("is_to_receive",false);
                    editor.commit();
                }else{
                    if(is_to_make_a_sound&&is_to_vibrate){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);
                    }else if(is_to_make_a_sound){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND);
                    }else if(is_to_vibrate){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
                    }else{
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
                    }

                    receive_button.setBackgroundResource(R.drawable.login_on);
                    editor.putBoolean("is_to_receive",true);
                    editor.commit();
                }
                break;

            case R.id.sound_button:
                if (is_to_make_a_sound==true){
                    if(!is_to_receive){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                    }else if(is_to_vibrate){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
                    }else{
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
                    }
                    sound_button.setBackgroundResource(R.drawable.login_off);
                    editor.putBoolean("is_to_make_a_sound",false);
                    editor.commit();
                }else{
                    if(!is_to_receive){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                    }else if(is_to_vibrate){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);
                    }else{
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND);
                    }
                    sound_button.setBackgroundResource(R.drawable.login_on);
                    editor.putBoolean("is_to_make_a_sound",true);
                    editor.commit();
                }
                break;

            case R.id.vibrate_button:
                if (is_to_vibrate==true){
                    if(!is_to_receive){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                    }else if(is_to_make_a_sound){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND);
                    }else{
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
                    }
                    vibrate_button.setBackgroundResource(R.drawable.login_off);
                    editor.putBoolean("is_to_vibrate",false);
                    editor.commit();
                }else{
                    if (!is_to_receive){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                    }else if(is_to_make_a_sound){
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);
                    }else{
                        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
                    }
                    vibrate_button.setBackgroundResource(R.drawable.login_on);
                    editor.putBoolean("is_to_vibrate",true);
                    editor.commit();
                }
                break;

        }
    }



}
