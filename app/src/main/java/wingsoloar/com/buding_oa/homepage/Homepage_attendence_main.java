package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wingsoloar.com.buding_oa.R;

import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PREFIX;

/**
 * Created by wingsolarxu on 2017/8/5.
 */

public class Homepage_attendence_main extends Activity {

    public static final int TAKE_PHOTO = 1;
    private TextView location_data;
    private EditText memo;
    private ImageView photo_content;
    private ImageView photo;
    private ImageView locate;
    private Button submit;
    private Uri imageUri;
    private File outputImage;
    private String id;
    private String ip;
    private ImageView return_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_attendence_main);
        location_data = findViewById(R.id.attdence_main_location_data);
        locate = findViewById(R.id.attendence_main_get_location);
        photo = findViewById(R.id.attendence_main_get_photo);
        submit = findViewById(R.id.attendence_submit);
        memo=findViewById(R.id.attdence_main_explain_data);
        photo_content=findViewById(R.id.attendence_picture_content);
        return_button=findViewById(R.id.top_message_return_button);
        return_button.setVisibility(View.VISIBLE);

        location_data.setText(getIntent().getStringExtra("location"));
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Homepage_attendence_main.this,Homepage_attendence_map.class);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建file对象，用于存储拍照后的图片；
                outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(Homepage_attendence_main.this,
                            "wingsoloar.com.buding_oa.fileprovider", outputImage);

                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }

        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String i=uploadFile(outputImage,"http://service.sjff119.com.cn/ZC_JsonFacade/SJFF/fileInsert.ashx");
                //Log.e(i,"map1");
                //thread=Thread.currentThread();
                upImage(outputImage);

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

    private void upImage(File file) {
        OkHttpClient mOkHttpClent = new OkHttpClient();
        Log.e(file.getAbsolutePath()+"","map1");
        Log.e(file.length()+"","map1");

        new OkHttpClient.Builder()
                .readTimeout(10,TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(15,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(10,TimeUnit.SECONDS)//设置连接超时时间
                .build();

        SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
        ip=preferences.getString("server_ip","");
        preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);
        id=preferences.getString("id","");

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("application/octet-stream"), file));
        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder=new Request.Builder();
        Request request1 = requestBuilder.url("http://"+ip+"/SJFF/File_upload.ashx")
                .post(requestBody).build();

        Call call = mOkHttpClent.newCall(request1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("map1", "onFailure222: " );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Homepage_attendence_main.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()){
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(Homepage_attendence_main.this, "上传成功", Toast.LENGTH_SHORT).show();
//                    }
//                });

//                Log.e("map1",response.body().string());
//                Log.e("map1",response.message());
                Log.e("map1", "222" );
                FormBody formBody=new FormBody.Builder()
                    .add("ticker",id.replace("\"",""))
                    .add("longitude",getIntent().getStringExtra("lng").replace("f",""))
                    .add("Latitude",getIntent().getStringExtra("lat").replace("f",""))
                    .add("addressName",getIntent().getStringExtra("location"))
                    .add("memo",memo.getText().toString())
                    .add("imagepath",response.body().string()).build();

                    Request request2=new Request.Builder().url("http://"+ip+"/OA/OA_KQRECORDFacade.asmx/InsertKQRECORD").post(formBody).build();
                    executeRequest2(request2);

                    //Toast.makeText(getApplicationContext(),"打卡成功",Toast.LENGTH_SHORT).show();


                    //finish();

                } else{

                }
            }
        });
    }

    private void executeRequest2(Request request2){
        OkHttpClient mOkHttpClient=new OkHttpClient();
        Call call = mOkHttpClient.newCall(request2);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("map1", "onFailure: "+e );

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"打卡成功",Toast.LENGTH_SHORT).show();
                        }
                    });



                    //thread.start();
                    Log.e("map1",response.body().string());
                    Log.e("map1",response.message());

                } else{
                    Log.e("map1", "onFailure: " );
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        FileOutputStream b=null;
                        try {
                            b = new FileOutputStream(outputImage);
                            bm.compress(Bitmap.CompressFormat.JPEG, 25, b);// 把数据写入文件
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                b.flush();
                                b.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e(outputImage.length()+"","map1");
                       photo_content.setImageBitmap(bm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            default:
                break;
        }
    }
}


