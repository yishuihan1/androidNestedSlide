package demo.likang.com.embeddedwebview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * MyRecyclerView
 *
 * @author Li Kang
 * @date 16/5/26
 */
public class MyRecyclerView extends RecyclerView {

    private String TAG = "MyRecyclerView";
    private ViewGroup parentView;
    private DirectionDetector scrollDirectionDetector;
    private ScrollStateChangedListener scrollStateChangedListener;
    private int touchSlop;

    public MyRecyclerView(Context paramContext)
    {
        this(paramContext, null);
    }

    public MyRecyclerView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void getEmbeddedParent(View paramView) {
        ViewParent localViewParent = paramView.getParent();
        if (localViewParent != null) {
            if ((localViewParent instanceof ScrollStateChangedListener)) {
                this.parentView = ((ViewGroup) localViewParent);
                setScrollStateChangedListener((ScrollStateChangedListener) localViewParent);
            } else {
                if ((localViewParent instanceof ViewGroup)) {
                    getEmbeddedParent((ViewGroup) localViewParent);
                }
            }
        }
    }

    private void init() {
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.scrollDirectionDetector = new DirectionDetector();
        setOverScrollMode(2);
        addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            int firstVisibleItem;
            int totalItemCount;
            int visibleItemCount;

            public void onScrollStateChanged(RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt)
            {
                super.onScrollStateChanged(paramAnonymousRecyclerView, paramAnonymousInt);
                Log.i(MyRecyclerView.this.TAG, "onScrollStateChanged state = " + paramAnonymousInt);
            }

            public void onScrolled(RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt1, int paramAnonymousInt2)
            {
                super.onScrolled(paramAnonymousRecyclerView, paramAnonymousInt1, paramAnonymousInt2);
                MyRecyclerView.this.scrollDirectionDetector.a(paramAnonymousInt2, true, MyRecyclerView.this.scrollStateChangedListener);
                RecyclerView.LayoutManager localg = paramAnonymousRecyclerView.getLayoutManager();
                ScrollStateChangedListener.ScrollState locala1;
                if ((localg instanceof LinearLayoutManager))
                {
                    LinearLayoutManager localLinearLayoutManager = (LinearLayoutManager)localg;
                    this.visibleItemCount = paramAnonymousRecyclerView.getChildCount();
                    this.totalItemCount = localLinearLayoutManager.getItemCount();
                    this.firstVisibleItem = localLinearLayoutManager.findFirstVisibleItemPosition();
                    Log.i(MyRecyclerView.this.TAG, "onScrolled : totalItemCount = " + this.totalItemCount + "  firstVisibleItem = " + this.firstVisibleItem + "  visibleItemCount = " + this.visibleItemCount + " dy = " + paramAnonymousInt2);
                    locala1 = ScrollStateChangedListener.ScrollState.c;
                    if ((this.totalItemCount != this.visibleItemCount) || (this.totalItemCount <= 1)){
                        if (this.firstVisibleItem == 0) {
                            View localView2 = paramAnonymousRecyclerView.getChildAt(0);
                            if ((localView2 != null) && (localView2.getTop() >= 0)) {
                                locala1 = ScrollStateChangedListener.ScrollState.a;
                                Log.i(MyRecyclerView.this.TAG, "------- POSITION ------- " + locala1);
                            }
                        }
                        if (MyRecyclerView.this.scrollStateChangedListener != null) {
                            MyRecyclerView.this.scrollStateChangedListener.onChildPositionChange(locala1);
                            if (this.visibleItemCount + this.firstVisibleItem == this.totalItemCount) {
                                View localView1 = paramAnonymousRecyclerView.getChildAt(-1 + paramAnonymousRecyclerView.getChildCount());
                                if ((localView1 != null) && (localView1.getBottom() <= paramAnonymousRecyclerView.getHeight())) {
                                    locala1 = ScrollStateChangedListener.ScrollState.b;
                                    Log.i(MyRecyclerView.this.TAG, "------- POSITION ------- " + locala1);
                                }
                            }
                        }
                    }
                    View localView3 = paramAnonymousRecyclerView.getChildAt(0);
                    View localView4 = paramAnonymousRecyclerView.getChildAt(-1 + paramAnonymousRecyclerView.getChildCount());
                    if ((localView3 == null) || (localView3.getTop() < 0) || (localView4 == null) || (localView4.getBottom() > paramAnonymousRecyclerView.getHeight())) {
                        if (this.firstVisibleItem == 0) {
                            View localView2 = paramAnonymousRecyclerView.getChildAt(0);
                            if ((localView2 != null) && (localView2.getTop() >= 0)) {
                                locala1 = ScrollStateChangedListener.ScrollState.a;
                                Log.i(MyRecyclerView.this.TAG, "------- POSITION ------- " + locala1);
                            }
                        }
                        if (MyRecyclerView.this.scrollStateChangedListener != null) {
                            MyRecyclerView.this.scrollStateChangedListener.onChildPositionChange(locala1);
                            if (this.visibleItemCount + this.firstVisibleItem == this.totalItemCount) {
                                View localView1 = paramAnonymousRecyclerView.getChildAt(-1 + paramAnonymousRecyclerView.getChildCount());
                                if ((localView1 != null) && (localView1.getBottom() <= paramAnonymousRecyclerView.getHeight())) {
                                    locala1 = ScrollStateChangedListener.ScrollState.b;
                                    Log.i(MyRecyclerView.this.TAG, "------- POSITION ------- " + locala1);
                                }
                            }
                        }
                    }
                    ScrollStateChangedListener.ScrollState locala2 = ScrollStateChangedListener.ScrollState.d;
                    Log.i(MyRecyclerView.this.TAG, "------- POSITION ------- " + locala2);
                }
            }
        });
    }

    private void resizeHeight() {
        int height = getHeight();
        if (height < 1)
            return;
        int childCount = getChildCount();
           if (childCount <= 0){
               return;
           }
        int sumHeight = 0;
        for (int m = 0; m < childCount; m++) {
            View childView = getChildAt(m);
            if (childView != null)
                sumHeight += childView.getHeight();
        }
        ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
        localLayoutParams.height = Math.min(sumHeight, height);
        Log.i(this.TAG, "resizeWrapHeight : sumHeight = " + sumHeight + "  height = " + height);
        setLayoutParams(localLayoutParams);
    }

    private void setScrollStateChangedListener(ScrollStateChangedListener paramc) {
        Log.i(this.TAG, "scrollStateChangedListener = " + paramc);
        this.scrollStateChangedListener = paramc;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getEmbeddedParent(this);
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4){
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        Log.i(this.TAG, "onLayout : " + paramInt2 + " / " + paramInt4);
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        Log.i(this.TAG, "onSizeChanged : " + paramInt2 + " / " + paramInt4);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool = super.onTouchEvent(paramMotionEvent);
        Log.i(this.TAG, "onTouchEvent : action = " + paramMotionEvent.getAction() + " scrollY = " + paramMotionEvent.getY());
        Log.i(this.TAG, " onTouchEvent : " + bool);
        Log.i(this.TAG, " \n ");
        return bool;
    }

    public void resizeWrapHeight(int paramInt) {
        ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
        localLayoutParams.height = this.parentView.getHeight();
        setLayoutParams(localLayoutParams);
        postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            MyRecyclerView.this.resizeHeight();
                        }
                    }
                , paramInt);
    }

}
