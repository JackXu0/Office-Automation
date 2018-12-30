package wingsoloar.com.buding_oa.homepage;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;
import org.greenrobot.eventbus.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wingsoloar.com.buding_oa.MainActivity;
import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.mEventBus;
import wingsoloar.com.buding_oa.manager.UpdateManager;
import wingsoloar.com.buding_oa.setting.Personal_setting_main;
import wingsoloar.com.buding_oa.views.personal_info_view;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wingsolarxu on 2017/7/26.
 */

public class Homepage_main extends Fragment implements View.OnClickListener{

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<View> views=new ArrayList<View>();
    private Intent intent;
    private TextView extra;
    private WebView mWebview;
    private WebSettings mWebSettings;
    private SharedPreferences preferences;
    private ImageView clearcache;
    private LinearLayout attendence;
    private LinearLayout update;
    private LinearLayout personal_docs;
    private LinearLayout homepage_messages;
    private LinearLayout homepage_news;
    private LinearLayout homepage_setting;
    private LinearLayout homepage_arrangement;
    private LinearLayout homepage_job_record;
    private LinearLayout homepage_public_doc;
    private personal_info_view homepage_personal_info;
    private LinearLayout line1;
    private LinearLayout line2;
    private LinearLayout line3;
    private LinearLayout line4;
    private LinearLayout line5;
    private RelativeLayout top_message;
    private RelativeLayout todolist;
    private TextView todolist_size;
    private Handler handler;
    private String id;
    private String username;
    private String ip;
    private String company_name;
    private boolean isToClearCache;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri []> uploadMessageAboveL=null;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private final static int TAKE_PHOTO = 1;
    private Uri imageUri;
    private String mCurrentPhotoPath;
    private View view1;
    private View view2;
    private View rootView;
    private ImageView expand_button;
    private PopupWindow popupWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        JMessageClient.init(getContext());
        JMessageClient.registerEventReceiver(this);

        rootView= inflater.inflate(R.layout.homepage_root, container, false);
        viewPager=rootView.findViewById(R.id.homepage_test_view_pager);
        view1=inflater.inflate(R.layout.homepage_main_original,null);
        view2=inflater.inflate(R.layout.homepage_sub_pages,null);
        views.add(view1);
        views.add(view2);

        setPagerAdapter();

        initOriginalView();
        initWebview();
        initLocalData();

        getToDoListSize();

        setOnClickListener();

        mWebview.loadUrl("http://"+ip+"/vueapp/#/?id="+id.replace("\"","")+"&jockeyNumber="+username+"&url="+ip+"&push=Home");

        return rootView;
    }

    private void setPagerAdapter(){

        pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=views.get(position);
                container.addView(view);
                return view;
            }



        };

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EventBus.getDefault().post(new mEventBus(position));
            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new mEventBus(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void initLocalData(){
        //get local data
        preferences=getActivity().getSharedPreferences("code_scan_id",MODE_PRIVATE);;
        id = preferences.getString("id", "");
        preferences=getActivity().getSharedPreferences("login",MODE_PRIVATE);;
        username = preferences.getString("username", "");
        ip=preferences.getString("server_ip","");
        preferences=getActivity().getSharedPreferences("company",MODE_PRIVATE);
        company_name=preferences.getString("name","").replace("\"","");
        extra.setText(company_name);
        preferences=getActivity().getSharedPreferences("isToClearCache",MODE_PRIVATE);;
        isToClearCache = preferences.getBoolean("isToClearCache", false);
    }

    private void initOriginalView(){
        //public components
        extra= (TextView) view1.findViewById(R.id.top_message_title);

        //original components
        clearcache=view1.findViewById(R.id.homepage_clear_cache);
        attendence=view1.findViewById(R.id.homepage_attendence_check);
        update=view1.findViewById(R.id.homepage_update);
        homepage_messages=view1.findViewById(R.id.homepage_messages);
        personal_docs=view1.findViewById(R.id.personal_docs);
        todolist=view1.findViewById(R.id.homepage_todolist);
        homepage_news=view1.findViewById(R.id.homepage_news);
        homepage_setting=view1.findViewById(R.id.homepage_setting);
        todolist_size= (TextView) view1.findViewById(R.id.bar_num);
        homepage_arrangement=view1.findViewById(R.id.homepage_arrangement);
        homepage_job_record=view1.findViewById(R.id.homepage_job_record);
        homepage_public_doc=view1.findViewById(R.id.homepage_public_doc);
        mWebview = (WebView) view2.findViewById(R.id.webView1);
        homepage_personal_info=view1.findViewById(R.id.homepage_personal_info);
        line1=view1.findViewById(R.id.line1);
        line2=view1.findViewById(R.id.line2);
        line3=view1.findViewById(R.id.line3);
        line4=view1.findViewById(R.id.line4);
        line5=view1.findViewById(R.id.line5);
        top_message=view1.findViewById(R.id.top_message);
        expand_button=view1.findViewById(R.id.expand);
        //expand_button.setVisibility(View.VISIBLE);
        homepage_personal_info.initExpandView();
    }

    private void initWebview(){

        assert mWebview != null;
        mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);

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
                showOptions();
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

        mWebview.setWebViewClient(new WebViewClient(){});

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

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        //alertDialog.setOnCancelListener(new ReOnCancelListener());
        String[] options={"拍照","相册"};
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    takephoto();
                } else {
                        openImageChooserActivity();
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void getToDoListSize(){
        SharedPreferences preferences=getContext().getSharedPreferences("login",MODE_PRIVATE);
        String ip=preferences.getString("server_ip","");
        preferences=getContext().getSharedPreferences("code_scan_id",MODE_PRIVATE);
        String id=preferences.getString("id","");
        FormBody requestBodyBuilder=new FormBody.Builder()
                .add("ticker",id.replace("\"",""))
                .add("JsonCondition","").build();
        Request.Builder builder=new Request.Builder();
        Log.e(ip,"process");
        Request request=builder.url("http://"+ip+"/WorkFlow/JSon/TaskFacade.asmx/getTaskCountForNoComplete").post(requestBodyBuilder).build();
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                todolist_size.setText(response.body().string());
                                //setBadgeOfMIUI(getContext(),Integer.parseInt(response.body().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else{

                }
            }
        });
    }

    private void setOnClickListener(){

        todolist.setOnClickListener(this);
        clearcache.setOnClickListener(this);
        attendence.setOnClickListener(this);
        update.setOnClickListener(this);
        personal_docs.setOnClickListener(this);
        homepage_messages.setOnClickListener(this);
        homepage_news.setOnClickListener(this);
        homepage_setting.setOnClickListener(this);
        homepage_public_doc.setOnClickListener(this);
        homepage_arrangement.setOnClickListener(this);
        homepage_job_record.setOnClickListener(this);
        //expand_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.homepage_clear_cache:

                deleteDir(getContext().getCacheDir());
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    deleteDir(getContext().getExternalCacheDir());
                    Toast.makeText(getContext(),"清除成功，请重启APP",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"需打开文件读取权限",Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.homepage_attendence_check:
                intent = new Intent(getContext(), Homepage_attendence_map.class);
                startActivity(intent);
                break;
            case R.id.homepage_update:
                new UpdateManager(getContext(),getActivity(),ip,id).checkUpdate();
                break;
            case R.id.homepage_todolist:
                intent =new Intent(getContext(),Homepage_todolist.class);
                startActivity(intent);
                break;

            case R.id.personal_docs:



//                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(selectedUri, "*/*");
//
//
//                    startActivity(intent);

//                File file=new File(getContext().getExternalFilesDir("/").getPath());
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //intent.setAction(android.content.Intent.ACTION_VIEW);
//
//
//                intent.setDataAndType(Uri.fromFile(file),"*/*");

              // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.e("eeeee","file1");
//                File file=new File(getActivity().getExternalCacheDir().getPath());
//                Log.e(getActivity().getExternalCacheDir().getPath(),"file1");
//
//                Log.e(Environment.getExternalStorageDirectory().getPath(),"file1");
//                if(file==null || !file.exists()){
//                    Log.e("ddddd","file1");
//                    return;
//                }
                //intent.setData(Uri.fromFile(file));
//                Log.e("ffff","file1");
//                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"");
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setDataAndType(selectedUri, "*/*");
//                final int REQUEST_IMAGE_GET = 1;
//
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones).
               // intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only text files.
                //intent.setType("*/*");

                //startActivityForResult(intent, 4);
                //startActivity(intent);
               // startActivity(Intent.createChooser(intent,"选择浏览工具"));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.homepage_messages:
//                intent=new Intent(getContext(),Homepage_messages.class);
//                startActivity(intent);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.homepage_news:
//                intent=new Intent(getContext(),Homepage_news.class);
//                startActivity(intent);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.homepage_setting:
                intent =new Intent(getContext(),Homepage_setting_main.class);
                startActivity(intent);
                break;

            case R.id.homepage_arrangement:

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.homepage_job_record:

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.homepage_public_doc:

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.expand:
                show_personal_info();

                break;

        }
    }

    @Override
    public void onDestroyView() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences=getActivity().getSharedPreferences("isToReload",MODE_PRIVATE);;
        boolean isToReload = preferences.getBoolean("isToReload", false);
        if(isToReload){
            mWebview.reload();
            SharedPreferences preferences=getContext().getSharedPreferences("isToReload",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isToReload",false);
            editor.commit();
        }
    }

    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                getToDoListSize();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;


            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
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
        File outputImage = new File(getActivity().getExternalCacheDir()

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
            imageUri = FileProvider.getUriForFile(getContext(),
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
            getActivity().sendBroadcast(localIntent);
            result = Uri.fromFile(file);

        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if(intent==null){
                uploadMessageAboveL.onReceiveValue(new Uri[]{});
                uploadMessageAboveL = null;
                return;
            }

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

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void show_personal_info(){
        View contentView=LayoutInflater.from(getContext()).inflate(R.layout.homepage_personal_info, null, false);
        LinearLayout todo_list=contentView.findViewById(R.id.to_do_list);
        LinearLayout personal_docs=contentView.findViewById(R.id.personal_docs);
        LinearLayout setting=contentView.findViewById(R.id.setting);

        todo_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(getContext(),Homepage_todolist.class);
                startActivity(intent);
            }
        });

        personal_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(getContext(), Personal_setting_main.class);
                startActivity(intent);
            }
        });

        Point size = new Point();
        int height = top_message.getMeasuredHeight();
        Log.e(""+height,"height1");
        PopupWindow window=new PopupWindow(contentView, 700, ViewPager.LayoutParams.MATCH_PARENT-height, true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setAnimationStyle(R.anim.in_from_left);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                    dismissAnimator().start();
            }
        });
        window.showAsDropDown(top_message);
        showAnimator().start();
    }

    private ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                backgroundAlpha(alpha);
            }
        });
        animator.setDuration(20);
        return animator;
    }

    private ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.7f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                backgroundAlpha(alpha);
            }
        });
        animator.setDuration(20);
        return animator;
    }

}
