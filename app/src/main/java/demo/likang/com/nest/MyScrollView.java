package demo.likang.com.nest;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * MyScrollView
 *
 * @author Li Kang
 * @date 16/5/25
 */
public class MyScrollView extends ScrollView implements NestedScrollingParent {
    private static final String TAG = "MyScrollView";
    private int myScrollDis = 640;
    private int sumScroll = 0;
    private NestedScrollingParentHelper parentHelper;
    private List<View> scrollingChildList = null;
    private boolean hasNestedScroll = false;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parentHelper = new NestedScrollingParentHelper(this);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        stopScrolling();
        boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
        if (this.hasNestedScroll) {
            bool = false;
        }
        return bool;
    }

    private void stopScrolling() {
        smoothScrollBy(0, 0);
    }

    //~~~~~~~ 添加一个方法，在代码中设置这个父类滚动的限度 ~~~~~~~
    public void setMyScrollDis(int myScrollDis){
        this.myScrollDis = myScrollDis;
    }

    //~~~~~~~ 嵌套滑动 ~~~~~~~
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.hasNestedScroll = true;
        Log.i(TAG,">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(target, dx, dy, consumed);
        //切记，把父类滑剩下的给子类
        Log.i(TAG,">>>>>>>>>>>>>>>>>>");
        //~~~~~~~ 正向滑动 ~~~~~~~
        if((sumScroll+dy)>sumScroll){
            //超过边界
            if((Math.abs(sumScroll+dy))>myScrollDis){
                MyScrollView.this.scrollBy(dx, myScrollDis - sumScroll);//最多滑动myScrollDis，当超出边界的时候父类只需要滑动剩下的距离
                consumed[1] = dy - (myScrollDis - sumScroll);//将没滑动的距离给子类，让子类滑动
                sumScroll = myScrollDis;
            }else{//未超过边界
                MyScrollView.this.scrollBy(dx, dy);
                sumScroll += dy;//记录这个view滑动的真实距离
                consumed[1] = 0;
            }
        }
        //~~~~~~~ 反向互动 ~~~~~~~
        else if((sumScroll+dy)<sumScroll){
            //超过边界
            if((sumScroll+dy)<0){
                MyScrollView.this.scrollBy(dx, -sumScroll);//
                consumed[1] = sumScroll+dy;
                sumScroll = 0;
            }else{//未超过边界
                MyScrollView.this.scrollBy(dx, dy);
                sumScroll += dy;//记录这个view滑动的真实距离
                consumed[1] = 0;
            }
        }else {

        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        analyNestedScrollingChildViews();
    }

    private void analyNestedScrollingChildViews() {
        View childView = getChildAt(0);
        if ((childView == null) || (!(childView instanceof ViewGroup)))
            throw new IllegalArgumentException("EmbeddedScrollView root child illegal");
        this.scrollingChildList = new ArrayList();
        ViewGroup localViewGroup = (ViewGroup)childView;
        for (int i = 0; i < localViewGroup.getChildCount(); i++) {
            View localView2 = localViewGroup.getChildAt(i);
            if ((localView2 instanceof NestedScrollingChild))
                this.scrollingChildList.add(localView2);
        }
    }
}
