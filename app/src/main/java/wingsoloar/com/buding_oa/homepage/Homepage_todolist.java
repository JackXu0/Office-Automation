package wingsoloar.com.buding_oa.homepage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import wingsoloar.com.buding_oa.R;

import static wingsoloar.com.buding_oa.homepage.Homepage_attendence_main.TAKE_PHOTO;

/**
 * Created by wingsolarxu on 2017/8/10.
 */

public class Homepage_todolist extends Activity {
    private WebView mWebview;
    private Button button;
    private WebSettings mWebSettings;
    private LinearLayout back_button;
    private SharedPreferences preferences;
    private TextView name;
    private String ip;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri []> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private final static int TAKE_PHOTO = 1;
    private Uri imageUri;
    private Handler handler;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homepage_sub_pages);
        name=findViewById(R.id.homepage_sub_page_name);
        name.setText("入库管理");

        back_button= (LinearLayout) findViewById(R.id.webview_back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebview = (WebView) findViewById(R.id.webView1);

        preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);;
        String id = preferences.getString("id", "");
        preferences=getSharedPreferences("login",MODE_PRIVATE);;
        String username = preferences.getString("username", "");
        ip=preferences.getString("server_ip","");
        preferences=getSharedPreferences("isToClearCache",MODE_PRIVATE);;
        boolean isToClearCache = preferences.getBoolean("isToClearCache", false);
        if(isToClearCache){
            mWebview.clearCache(true);
            isToClearCache=false;
        }

        initWebview();
        getLocationData();
        mWebview.loadUrl("http://"+ip+"/vueapp/#/?id="+id.replace("\"","")+"&url="+ip+"&push=Workflow");
    }

    private void getLocationData(){
        preferences=getSharedPreferences("code_scan_id",MODE_PRIVATE);;
        String id = preferences.getString("id", "");
        preferences=getSharedPreferences("login",MODE_PRIVATE);;
        String username = preferences.getString("username", "");
        ip=preferences.getString("server_ip","");
        preferences=getSharedPreferences("isToClearCache",MODE_PRIVATE);;
        boolean isToClearCache = preferences.getBoolean("isToClearCache", false);
        if(isToClearCache){
            mWebview.clearCache(true);
            isToClearCache=false;
        }
    }

    private void initWebview(){

        assert mWebview != null;
        mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);

        final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
        mWebview.addJavascriptInterface(myJavaScriptInterface,"AndroidFunction");


        mWebview.setWebChromeClient(new WebChromeClient() {

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                Log.e("5","process");
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                Log.e("6","process");
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                Log.e("7","process");
                uploadMessage = valueCallback;
                openImageChooserActivity();


            }

            //For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                takephoto();

                Log.e("1","process");
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebview.setWebContentsDebuggingEnabled(true);
        }

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((i == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
                    if(keyEvent.getAction()==KeyEvent.ACTION_DOWN){ //只处理一次
                        handler.sendEmptyMessage(1);
                    }
                    return true;
                }
                return false;
            }
        });

        handler= new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1: {
                        mWebview.goBack();
                    }
                    break;
                }
            }
        };

    }

    public class JavaScriptInterface {
        Context mContext;


        public JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void close(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Homepage_todolist.this.finish();
                }
            });


        }
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    private void takephoto(){
        //创建file对象，用于存储拍照后的图片；
        File outputImage = new File(getExternalCacheDir()

                ,System.currentTimeMillis()+"output_image.jpg");

        mCurrentPhotoPath=outputImage.getAbsolutePath();

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this,
                    "wingsoloar.com.buding_oa.fileprovider", outputImage);

        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.e(requestCode+"kk","process");

        if (requestCode == TAKE_PHOTO || requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, Intent intent) {

        if (uploadMessageAboveL == null) {
            return;
        }
        Uri result = null;
        if (requestCode == TAKE_PHOTO) {
            File file = new File(mCurrentPhotoPath);
            Uri localUri = Uri.fromFile(file);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
            result = Uri.fromFile(file);

        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            result = intent.getData();
        }
        uploadMessageAboveL.onReceiveValue(new Uri[]{result});
        uploadMessageAboveL = null;
        return;
    }

    private static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {

            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {

                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }


}
