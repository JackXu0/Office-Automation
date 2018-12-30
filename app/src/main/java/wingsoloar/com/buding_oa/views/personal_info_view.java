package wingsoloar.com.buding_oa.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/9/11.
 */

public class personal_info_view extends FrameLayout {
    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private boolean mIsExpand=false;

    public personal_info_view(Context context) {
        this(context,null);
        initExpandView();
        // TODO Auto-generated constructor stub
    }
    public personal_info_view(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        initExpandView();
        // TODO Auto-generated constructor stub
    }
    public personal_info_view(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initExpandView();
    }
    public void initExpandView() {
        LayoutInflater.from(getContext()).inflate(R.layout.homepage_personal_info, this, true);

        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.in_from_left);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
//                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_from_left);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }
        });

    }
    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            clearAnimation();
            startAnimation(mCollapseAnimation);
        }
    }

    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public void setIsExpand(boolean b){
        mIsExpand=b;
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    public void setContentView(){
        View view = null;
        view = LayoutInflater.from(getContext()).inflate(R.layout.homepage_personal_info, null);
        removeAllViews();
        addView(view);
    }
}
