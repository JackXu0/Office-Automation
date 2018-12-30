package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/8/28.
 */

public class Homepage_setting_main extends Activity {

    private TextView title;
    private LinearLayout notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_setting_main);

        title=findViewById(R.id.top_message_title);
        notification=findViewById(R.id.notification);

        title.setText("设置");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage_setting_main.this,Homepage_setting_notification.class);
                startActivity(intent);
            }
        });
    }

}
