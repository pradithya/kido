package com.progrema.superbaby.widget.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by iqbalpakeh on 22/2/14.
 */
public class ObserveableListView extends ListView
{
    private Callbacks mCallbacks;

    public ObserveableListView(Context context)
    {
        super(context);
    }

    public ObserveableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ObserveableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setCallbacks(Callbacks listener)
    {
        mCallbacks = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null)
        {
            mCallbacks.onScrollChanged(t, oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (mCallbacks != null)
        {
            switch (ev.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.onDownMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public static interface Callbacks
    {
        public void onScrollChanged(int scrollY, int oldScrollY);

        public void onDownMotionEvent();
    }
}
