package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.chat.Sending_interface;
import wingsoloar.com.buding_oa.manager.UpdateManager;

/**
 * Created by wingsolarxu on 2017/8/25.
 */

public class Homepage_messages_subpage extends Activity {

    private String sender_name;
    private String send_date;
    private String content;
    private TextView sender_name_tv;
    private TextView send_date_tv;
    private TextView content_tv;
    private TextView title;
    private Button reply;
    private ArrayList<String> filenames;
    private ArrayList<String> filepaths;
    private boolean[] isDownloaded;
    private ImageView return_button;
    private com.nex3z.flowlayout.FlowLayout files;
    private SharedPreferences preferences;
    private String ip;
    private String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_messages_subpage);

        sender_name=getIntent().getStringExtra("sender_name");
        send_date=getIntent().getStringExtra("send_date");
        content=getIntent().getStringExtra("content");
        filenames=getIntent().getStringArrayListExtra("filenames");
        filepaths=getIntent().getStringArrayListExtra("filepaths");
        isDownloaded=new boolean[filenames.size()];

        for(int i=0; i<isDownloaded.length;i++){
            isDownloaded[i]=false;
        }

        sender_name_tv=findViewById(R.id.sender_name);
        send_date_tv=findViewById(R.id.send_time);
        content_tv=findViewById(R.id.homepage_messages_content);
        title=findViewById(R.id.top_message_title);
        reply=findViewById(R.id.contact_list_choose);
        return_button=findViewById(R.id.top_message_return_button);
        files=findViewById(R.id.homepage_messages_subpage_file_flowlayout);

        preferences=getSharedPreferences("login",MODE_PRIVATE);
        ip=preferences.getString("server_ip","");
        preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);
        id=preferences.getString("id","");

        //File file=new UpdateManager(getBaseContext(),null,ip,id).getFileFromServer()


        for(int i =0;i<filenames.size();i++){
            LayoutInflater inflater= (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView receiver= (TextView) inflater.inflate(R.layout.sending_interface_item,null);
            receiver.setText(filenames.get(i)+"、");
            receiver.setTextColor(Color.parseColor("#1E90FF"));
            final int temp=i;
            receiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isDownloaded[temp]){
                        try {
                            new UpdateManager(getBaseContext(),null,ip,id).getFileFromServer(filepaths.get(temp),filenames.get(temp));
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(),"下载文件失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else{
                        //TODO
                        //openfile();
                    }
                }
            });
            if(i==filenames.size()-1)
                receiver.setText(filenames.get(i));
            files.addView(receiver);
            Log.e("gg","pp");
        }

        sender_name_tv.setText(sender_name.split("@")[0]);
        send_date_tv.setText(send_date);
        content_tv.setText(content);
        title.setText("详细信息");
        reply.setText("回复");
        reply.setVisibility(View.VISIBLE);
        return_button.setVisibility(View.VISIBLE);

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage_messages_subpage.this, Sending_interface.class);
                ArrayList<String> receivers=new ArrayList<String>();
                receivers.add(sender_name);
                intent.putStringArrayListExtra("receivers",receivers);
                intent.putExtra("isReply",true);
                startActivity(intent);
            }
        });

    }

    public static Intent openFile(String filePath){

        File file = new File(filePath);
        if(!file.exists()) {

            Log.e("123","createfile");
            return null;}
        /* 取得扩展名 */
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("3gp")||end.equals("mp4")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(filePath);
        }else if(end.equals("apk")){
            return getApkFileIntent(filePath);
        }else if(end.equals("ppt")){
            return getPptFileIntent(filePath);
        }else if(end.equals("xls")){
            return getExcelFileIntent(filePath);
        }else if(end.equals("doc")){
            return getWordFileIntent(filePath);
        }else if(end.equals("pdf")){
            return getPdfFileIntent(filePath);
        }else if(end.equals("chm")){
            return getChmFileIntent(filePath);
        }else if(end.equals("txt")){
            return getTextFileIntent(filePath,false);
        }else{
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent( String param ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"*/*");
        return intent;
    }
    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent( String param ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent( String param ){

        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent( String param, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            Uri uri1 = Uri.parse(param );
            intent.setDataAndType(uri1, "text/plain");
        }else{
            Uri uri2 = Uri.fromFile(new File(param ));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
