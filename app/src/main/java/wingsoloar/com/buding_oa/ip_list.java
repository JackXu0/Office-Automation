package wingsoloar.com.buding_oa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by wingsolarxu on 2017/8/19.
 */

public class ip_list extends Activity {

    private Button submit;
    private EditText name1;
    private EditText ip1;
    private EditText name2;
    private EditText ip2;
    private EditText name3;
    private EditText ip3;
    private EditText name4;
    private EditText ip4;
    private String nameString1="";
    private String nameString2="";
    private String nameString3="";
    private String nameString4="";
    private String ipString1="";
    private String ipString2="";
    private String ipString3="";
    private String ipString4="";
    private boolean changed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_list);

        SharedPreferences preferences=getSharedPreferences("ip_informaiton",MODE_PRIVATE);
        nameString1=preferences.getString("name1","");
        nameString2=preferences.getString("name2","");
        nameString3=preferences.getString("name3","");
        nameString4=preferences.getString("name4","");
        ipString1=preferences.getString("ip1","");
        ipString2=preferences.getString("ip2","");
        ipString3=preferences.getString("ip3","");
        ipString4=preferences.getString("ip4","");
        changed=preferences.getBoolean("changed",false);

        //initialize
        submit = (Button) findViewById(R.id.ip_list_submit);
        name1 = (EditText) findViewById(R.id.ip_list_name1);
        ip1 = (EditText)findViewById(R.id.ip_list_ip1);
        name2 =(EditText) findViewById(R.id.ip_list_name2);
        ip2 = (EditText)findViewById(R.id.ip_list_ip2);
        name3 = (EditText)findViewById(R.id.ip_list_name3);
        ip3 = (EditText)findViewById(R.id.ip_list_ip3);
        name4 = (EditText)findViewById(R.id.ip_list_name4);
        ip4 =(EditText) findViewById(R.id.ip_list_ip4);

        name1.setText(nameString1);
        name2.setText(nameString2);
        name3.setText(nameString3);
        name4.setText(nameString4);
        ip1.setText(ipString1);
        ip2.setText(ipString2);
        ip3.setText(ipString3);
        ip4.setText(ipString4);
        if(!changed){
            name1.setText("oa");
            ip1.setText("wx.sjff119.com.cn:8887/zc_jsonfacade");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                finish();
            }
        });
    }

    private void getData() {
        nameString1=name1.getText().toString();
        nameString2=name2.getText().toString();
        nameString3=name3.getText().toString();
        nameString4=name4.getText().toString();
        ipString1=ip1.getText().toString();
        ipString2=ip2.getText().toString();
        ipString3=ip3.getText().toString();
        ipString4=ip4.getText().toString();
        SharedPreferences preference=getSharedPreferences("ip_informaiton",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        if(!nameString1.equals("oa")||!ipString1.equals("wx.sjff119.com.cn:8887/zc_jsonfacade")){
            editor.putBoolean("changed",true);
        }
        editor.putString("name1",nameString1);
        editor.putString("name2",nameString2);
        editor.putString("name3",nameString3);
        editor.putString("name4",nameString4);
        editor.putString("ip1",ipString1);
        editor.putString("ip2",ipString2);
        editor.putString("ip3",ipString3);
        editor.putString("ip4",ipString4);
        editor.commit();

    }
}
