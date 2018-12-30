package wingsoloar.com.buding_oa.setting;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/9/13.
 */

public class Personal_setting_main extends Activity {

    private RelativeLayout avator;
    private RelativeLayout tel;
    private TextView title;
    private ImageView return_button;
    private TextView tel_textview;
    private EditText tel_edittext;
    private Button save;
    private String tel_string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_settings_main);
        avator = findViewById(R.id.avator);
        tel = findViewById(R.id.tel);
        title = findViewById(R.id.top_message_title);
        return_button = findViewById(R.id.top_message_return_button);
        tel_textview = findViewById(R.id.tel_textview);
        tel_edittext = findViewById(R.id.tel_edittext);
        save=findViewById(R.id.contact_list_choose);
        title.setText("个人信息");
        return_button.setVisibility(View.VISIBLE);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_setting_main.this,Personal_setting_avator.class);
                startActivity(intent);
            }
        });

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tel_edittext.setVisibility(View.VISIBLE);
                tel_textview.setVisibility(View.GONE);
                tel_edittext.requestFocus();
                save.setText("保存");
                save.setVisibility(View.VISIBLE);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tel_string=tel_edittext.getText().toString();
                        tel_edittext.setVisibility(View.GONE);
                        tel_textview.setText(tel_string);
                        tel_textview.setVisibility(View.VISIBLE);
                        save.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}