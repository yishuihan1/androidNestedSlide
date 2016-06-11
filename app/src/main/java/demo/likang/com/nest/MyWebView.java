package demo.likang.com.nest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;

import demo.likang.com.embeddedwebview.JSInterface;
import demo.likang.com.embeddedwebview.SafeWebView;

/**
 * MyWebView
 *
 * @author Li Kang
 * @date 16/5/25
 */
public class MyWebView extends SafeWebView implements NestedScrollingChild {
    private static final String TAG = "MyWebView";
    private NestedScrollingChildHelper mChildHelper;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private int mTouchSlop;
    private int mLastMotionY;
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    private int mActivePointerId = -1;
    private boolean mIsBeingDragged = false;
    private VelocityTracker mVelocityTracker;
    private int webviewHeight;
    private int contentHeight;
    int preContentHeight = 0;
    private float density;
    private int originHeight;

    public MyWebView(Context paramContext)
    {
        this(paramContext, null);
    }

    public MyWebView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init(){
        mChildHelper = new NestedScrollingChildHelper(this);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.density = getResources().getDisplayMetrics().density;
        addJavascriptInterface(new JSGetContentHeight(), "InjectedObject");
        setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        this.mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.contentHeight = ((int) (getContentHeight() * getScale()));
        Log.i(TAG, "invalidate contentHeight = " + this.contentHeight + " height = " + getHeight() + " scale = " + getScale());
        if (this.contentHeight != this.preContentHeight) {
            loadUrl("javascript:window.InjectedObject.getContentHeight(document.getElementsByTagName('div')[0].scrollHeight)");
            this.preContentHeight = this.contentHeight;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(TAG,"lll=="+l+" tt=="+t+"  oldl=="+oldl + "  oldt=="+oldt);
        Log.i(TAG,"webviewHeightwebviewHeight====="+webviewHeight);
        Log.i(TAG,"contentHeightcontentHeight====="+contentHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged : " + h + " contentHeight = " + getContentHeight());
        this.webviewHeight = h;
//        if (this.contentHeight < 1) {
//            setContentHeight(this.webviewHeight);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
//                scrollBy(0,1);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }




    private void endTouch() {
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        releaseVelocityTracker();
        stopNestedScroll();
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null)
        {
            this.mVelocityTracker.clear();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void flingWithNestedDispatch(int paramInt) {
        if (!dispatchNestedPreFling(0.0F, paramInt))
            Log.i(TAG, "dispatchNestedPreFling : velocityY : " + paramInt);
    }

    private class JSGetContentHeight {
        private JSGetContentHeight() {
        }

        @JSInterface
        public void getContentHeight(WebView paramWebView, int paramInt) {
            int i = MyWebView.this.getHeight();
//      if (EmbeddedWebView.this.originHeight == 0)
//        EmbeddedWebView.access$102(EmbeddedWebView.this, i);
            Log.i(MyWebView.TAG, "JS getContentHeight contentHeight " + paramInt + " originHeight = " + MyWebView.this.originHeight + " viewHeight = " + i);
            int j = (int) (paramInt * MyWebView.this.density);
            int k = 0;
            if ((i > 0) && (j > 0)) {
                if (i - j <= 200) {
                    k = 0;
                    if (j > i) {
                        k = j;
                    }
                } else {
                    k = j + 100;
                }

            }
            if (k > MyWebView.this.originHeight)
                k = MyWebView.this.originHeight;
            if ((k > 0) && (k != i)) {
                final int m = k;
                MyWebView.this.post(new Runnable() {
                    public void run() {
                        ViewGroup.LayoutParams localLayoutParams = MyWebView.this.getLayoutParams();
                        localLayoutParams.height = m;
                        Log.i(MyWebView.TAG, "resize height = " + localLayoutParams.height);
                        MyWebView.this.setLayoutParams(localLayoutParams);
                    }
                });
            }
        }
    }

}
