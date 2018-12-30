package wingsoloar.com.buding_oa.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wingsolarxu on 2017/8/22.
 */

public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //测量父控件和子控件的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0, n = getChildCount(); i < n; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            //Log.i("main", "width = " + child.getMeasuredWidth() + " height = " + child.getMeasuredHeight() + " leftMargin = " + marginLayoutParams.leftMargin + " rightMargin = " + marginLayoutParams.rightMargin);
            childWidth = child.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            childHeight = child.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            //换行
            if (lineWidth + childWidth > width) {
                lineWidth = childWidth;
                height += lineHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            }
            if (i == n - 1) {
                height += lineHeight;
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    private List<Integer> mLineHeights = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeights.clear();
        int width = getWidth(); // 父布局的宽度
        List<View> lineViews = new ArrayList<View>();
        int childWidth = 0;
        int childHeight = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        //Log.i("main", "childCount = " + getChildCount() + " width = " + width);
        for (int i = 0, n = getChildCount(); i < n; i++) {
            View child = getChildAt(i);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            childHeight = child.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            if (childWidth + lineWidth > width) {
                mAllViews.add(lineViews);
                mLineHeights.add(lineHeight);
                lineViews = new ArrayList<View>();
                lineWidth = 0;
                lineHeight = childHeight;

            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);
        }
        mAllViews.add(lineViews);
        mLineHeights.add(lineHeight);
        int top = 0;
        int left = 0;
        // Log.i("main", "n = " + mAllViews.size() + " m = " + lineViews.size());

        for(int i = 0, n = mAllViews.size(); i < n; i++)
        {
            lineViews = mAllViews.get(i);
            for(int j = 0, m = lineViews.size(); j < m; j++)
            {
                View child = lineViews.get(j);
                if(child.getVisibility() == View.GONE)
                {
                    continue;
                }
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + marginLayoutParams.leftMargin;
                int tc = top + marginLayoutParams.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                //Log.i("main", "lc = " + lc + " tc = " + tc + " rc = " + rc + " bc = " + bc);
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            }
            left = 0;
            top += mLineHeights.get(i);
        }
    }

    @Override
     public LayoutParams generateLayoutParams(AttributeSet attrs) {
                 return new MarginLayoutParams(getContext(), attrs);
             }
}

//public class FlowLayout extends ViewGroup {
//
//
//    public FlowLayout(Context context) {
//        super(context);
//    }
//
//    public FlowLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//        }
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int mViewGroupWidth = getMeasuredWidth();  //容器宽度
//
//        int mPainterPosX = 0;  //当前绘图X
//        int mPainterPosY = 0;  //当前绘图Y
//
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            int width = childView.getMeasuredWidth();
//            int height = childView.getMeasuredHeight();
//            //宽度大于容器宽度时，则移到下一行开始位置
//            if (mPainterPosX + width > mViewGroupWidth) {
//                mPainterPosX = 0;
//                mPainterPosY += height;
//            }
//            //位置摆放
//            childView.layout(mPainterPosX, mPainterPosY, mPainterPosX + width, mPainterPosY + height);
//            //记录当前已经绘制到的横坐标位置
//            mPainterPosX += width;
//        }
//
//    }
//}
