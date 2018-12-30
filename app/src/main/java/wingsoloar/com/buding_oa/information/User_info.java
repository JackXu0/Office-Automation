package wingsoloar.com.buding_oa.information;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import wingsoloar.com.buding_oa.Login_main;
import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/8/4.
 */

public class User_info extends Activity {

    private RelativeLayout back;
    private TextView number;
    private Button dail;
    private TextView name_tx;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        back = findViewById(R.id.phone_detail_back);
        number = findViewById(R.id.phone_detail_number);
        dail = findViewById(R.id.phone_detail_dail);
        name_tx = findViewById(R.id.info_name);
        String name = getIntent().getStringExtra("info_name");

        name_tx.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number.getText().toString()));
                try {
                    User_info.this.startActivity(intent);

                }catch(Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(User_info.this,
                                    "请确认\"拨打电话\"权限已开启", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }
}
