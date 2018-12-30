package wingsoloar.com.buding_oa.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wingsoloar.com.buding_oa.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by wingsolarxu on 2017/8/10.
 */

public class UpdateManager {

    private String mVersion_code="1.1";
    private String mVersion_name="Buding_OA";
    private String mVersion_path="http://";
    private String path;
    private Context context;
    private ProgressBar progressBar;
    private boolean isCancelled = false;
    private Dialog download_dialog;
    private String savepath;
    private int progress;
    private FragmentActivity fragmentActivity;
    private String ip;
    private String id;
    private int permission;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public UpdateManager(final Context context,FragmentActivity fragmentActivity,String ip,String id){
        this.context=context;
        this.ip=ip;
        this.id=id;
        Log.e(ip,"aaa");
        path="http://"+ip+"/Sys/UpdateFacade.asmx/UpdateApk";
        this.fragmentActivity=fragmentActivity;
        permission = ActivityCompat.checkSelfPermission(fragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            try{
                ActivityCompat.requestPermissions(
                        fragmentActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }catch(Exception e){}

            fragmentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,"请打开存储权限",Toast.LENGTH_SHORT).show();
                }
            });

            return;

        }

    }

    public void checkUpdate(){

        if (permission != PackageManager.PERMISSION_GRANTED)
            return;

        FormBody requestBodyBuilder=new FormBody.Builder()
                .add("ticker",id.replace("\"",""))
                .add("appType","android")
                .build();
        Request.Builder builder=new Request.Builder();
        Request request=builder.url(path).post(requestBodyBuilder).build();
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                fragmentActivity.runOnUiThread(new Runnable() {//需传递FragmentActivity

                    @Override
                    public void run() {
                        Toast.makeText(context,"连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String jsonData = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        mVersion_code=jsonObject.getString("version_code");
                        mVersion_name="buding_Oa";
                        mVersion_path="http://"+ip+jsonObject.getString("version_path");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(isUpdated()){
                        fragmentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showNoticeDialog();
                            }
                        });

                    }else{
                        fragmentActivity.runOnUiThread(new Runnable() {//需传递FragmentActivity
                            @Override
                            public void run() {
                                Toast.makeText(context,"已经是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }else{
                    fragmentActivity.runOnUiThread(new Runnable() {//需传递FragmentActivity

                        @Override
                        public void run() {
                            Toast.makeText(context,"服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public boolean isUpdated(){
        Log.e(mVersion_code+"","update1");
        double server_version=0;
        try{
            server_version= Double.parseDouble(mVersion_code);
        }catch (Exception e){
            fragmentActivity.runOnUiThread(new Runnable() {//需传递FragmentActivity

                @Override
                public void run() {
                    Toast.makeText(context,"版本号错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
        double localVersion=1.0;
        try {

            localVersion=fragmentActivity.getPackageManager().getPackageInfo("wingsoloar.com.buding_oa",0).versionCode;
            Log.e(localVersion+"","update1");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("2","update1");
            e.printStackTrace();
        }

        if(server_version>localVersion)
            return true;
        else
            return false;
    }

    public void showNoticeDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("版本更新");
        String message="\t是否进行此次更新?";
        builder.setMessage(message);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss current dialog
                dialogInterface.dismiss();
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDownloadDialog();
                    }
                });
                //show downloading dialog
            }
        });

        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showDownloadDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("下载中");
        View view=LayoutInflater.from(context).inflate(R.layout.homepage_update_dialog_progress,null);
        progressBar=view.findViewById(R.id.homepage_update_process_bar);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                isCancelled=true;
            }
        });

        download_dialog=builder.create();
        download_dialog.show();
        downLoadApk();
    }

    public File getFileFromServer(String path,String filename) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);


            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            FileOutputStream fos=null;
            try {
                fos = new FileOutputStream(file);
            }catch (Exception e){
                Log.e(e.getMessage(),"update1");
            }
            Log.e("1","update1");
            BufferedInputStream bis = new BufferedInputStream(is);
            int length=conn.getContentLength();
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                progress=(int) (((float) total/length)*100);
                updateProgressBarHandler.sendEmptyMessage(1);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    protected void downLoadApk() {

        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(mVersion_path,"app-debug.apk");
                    sleep(3000);
                    //updateProgressBarHandler.sendEmptyMessage(2);
                    installApk(file);
                } catch (Exception e) {
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"下载Apk失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e(mVersion_path,"update1");
                }
            }}.start();
    }

    private Handler updateProgressBarHandler=new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                        }
                    });

                    break;

                case 2:
                    download_dialog.dismiss();
                    //installApk();
                    break;
            }

        }
    };

    //安装apk
    protected void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        fragmentActivity.startActivity(intent);
        if (context == null || TextUtils.isEmpty(file.getAbsolutePath())) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "wingsoloar.com.buding_oa.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

}
