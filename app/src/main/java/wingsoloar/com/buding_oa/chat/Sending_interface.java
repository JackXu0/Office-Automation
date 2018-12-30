package wingsoloar.com.buding_oa.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wingsoloar.com.buding_oa.Login_main;
import wingsoloar.com.buding_oa.R;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.nex3z.flowlayout.FlowLayout;

/**
 * Created by wingsolarxu on 2017/8/22.
 */

public class Sending_interface extends Activity {

    private ArrayList<String> receivers;
    private ArrayList<String> receivers_ids;
    private com.nex3z.flowlayout.FlowLayout flowLayout;
    private TextView receiver;
    private TextView top_message_title;
    private EditText text;
    private Button send;
    private ImageView add_receivers;
    private ImageView add_files;
    private List<String> fileList;
    private List<String> filepaths_server;
    private static final int FILE_LOCATE_CODE=1;
    private boolean isReply;
    private ImageView return_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending_interface_main);
        receivers=getIntent().getStringArrayListExtra("receivers");
        isReply=getIntent().getBooleanExtra("isReply",false);

        top_message_title=findViewById(R.id.top_message_title);
        top_message_title.setText("发文");
        flowLayout= findViewById(R.id.sending_interface_flowlayout);
        text=findViewById(R.id.text);
        send=findViewById(R.id.contact_list_choose);
        add_receivers=findViewById(R.id.add_receivers_button);
        add_files=findViewById(R.id.add_receivers_file_button);
        return_button=findViewById(R.id.top_message_return_button);
        return_button.setVisibility(View.VISIBLE);
        receivers_ids=new ArrayList<>();

        for(int i =0;i<receivers.size();i++){
            LayoutInflater inflater= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            receiver= (TextView) inflater.inflate(R.layout.sending_interface_item,null);
            receiver.setText(receivers.get(i).split("@")[0]+"、");
            receivers_ids.add(receivers.get(i).split("@")[1]);
            if(i==receivers.size()-1)
                receiver.setText(receivers.get(i).split("@")[0]);
            flowLayout.addView(receiver);
            Log.e("gg","pp");
        }

        if(isReply)
            add_receivers.setVisibility(View.GONE);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!text.getText().equals("")&&!(text.getText()==null&&text.getText().length()!=0)){
                    send.setText("发送");
                    send.setVisibility(View.VISIBLE);
                }else{
                    send.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!text.getText().equals("")&&!(text.getText()==null&&text.getText().length()!=0)){
                    send.setText("发送");
                    send.setVisibility(View.VISIBLE);
                }else{
                    send.setVisibility(View.GONE);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=text.getText().toString();
                String receivers_string="<record>";
                for(int i=0;i<receivers_ids.size();i++){
                    receivers_string+="<reg>"+receivers_ids.get(i)+"</reg>";
                }
                receivers_string+="</record>";

                SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);;
                String ip=preferences.getString("server_ip","");
                String username=preferences.getString("username","");
                preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);
                String id=preferences.getString("id","");

                FormBody formBody=new FormBody.Builder()
                        .add("ticker",id.replace("\"",""))
                        .add("ReceiveUserList",receivers_string)
                        .add("Message",content)
                        .add("SendUser",username.replace("\"","")).build();

                Request.Builder builder1=new Request.Builder();
                Request request1=builder1.url("http://"+ip+"/sys/sysfacade.asmx/SendOAMessage").post(formBody).build();
                filepaths_server=new ArrayList<String>();
                for (int i=0;i<fileList.size();i++) {
                    File file=new File(fileList.get(i));
                    MultipartBody.Builder builder2 = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    RequestBody requestBody = builder2.build();
                    Request.Builder requestBuilder=new Request.Builder();
                    Request request2 = requestBuilder.url("http://"+ip+"/SJFF/File_upload.ashx")
                            .post(requestBody).build();
                    executeRequest2(request2);
                }

                executeRequest1(request1);
                finish();
            }
        });

        add_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LFilePicker()
                        .withActivity(Sending_interface.this)
                        .withRequestCode(FILE_LOCATE_CODE)
                        .withIconStyle(Constant.ICON_STYLE_GREEN)
                        .withTitle("个人文件柜")
                        .start();

            }
        });

        add_receivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void executeRequest1(Request request){
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("1","pp1");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    text.setText("");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"发送成功",Toast.LENGTH_SHORT);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    Log.e("2","pp1");
                }else{
                    Log.e("3","pp1");

                }
            }
        });
    }

    private void executeRequest2(Request request){
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("1","pp1");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    filepaths_server.add(response.body().string());

                    Log.e("2","pp1");
                }else{
                    Log.e("3","pp1");

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_LOCATE_CODE) {
                fileList = data.getStringArrayListExtra("paths");
                flowLayout=findViewById(R.id.sending_interface_file_flowlayout);
                for(int i =0;i<fileList.size();i++){
                    LayoutInflater inflater= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    receiver= (TextView) inflater.inflate(R.layout.sending_interface_item,null);
                    receiver.setText((new File(fileList.get(i))).getName()+"、");
                    receiver.setTextColor(Color.parseColor("#1E90FF"));
                    if(i==fileList.size()-1)
                        receiver.setText((new File(fileList.get(i))).getName());
                    flowLayout.addView(receiver);
                    Log.e("gg","pp");
                }
            }
        }
    }


}
