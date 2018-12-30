package wingsoloar.com.buding_oa.setting;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.homepage.Homepage_todolist;

import static android.media.MediaRecorder.VideoSource.CAMERA;

/**
 * Created by wingsolarxu on 2017/9/13.
 */

public class Personal_setting_avator extends Activity{

    private ImageView return_button;
    private Button expand_button;
    private TextView title;
    private RelativeLayout whole_page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_setting_avator);
        title=findViewById(R.id.top_message_title);
        whole_page=findViewById(R.id.whole_page);
        title.setText("个人头像");
        return_button=findViewById(R.id.top_message_return_button);
        return_button.setVisibility(View.VISIBLE);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        expand_button=findViewById(R.id.personal_setting_expand_button);
        expand_button.setVisibility(View.VISIBLE);
        expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_personal_info();
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void show_personal_info(){
        View contentView=getLayoutInflater().inflate(R.layout.personal_setting_avator_operations, null, false);

        TextView take_photo=contentView.findViewById(R.id.take_photo);
        TextView choose_from_album=contentView.findViewById(R.id.choose_from_gallary);
        TextView cannel=contentView.findViewById(R.id.cannel);

        final PopupWindow window=new PopupWindow(contentView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT-180, true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissAnimator().start();
            }
        });
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开系统拍照程
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA);
                window.dismiss();
                dismissAnimator().start();
            }
        });
//        choose_from_album.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 打开系统图库选择图片
//                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(picture, PICTURE);
//                popupWindow.dismiss();
//                lighton();
//            }
//        });

        cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
                dismissAnimator().start();
            }
        });

        window.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        animation.start();
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
