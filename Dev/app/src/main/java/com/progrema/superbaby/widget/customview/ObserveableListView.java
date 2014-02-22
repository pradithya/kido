package com.progrema.superbaby.widget.customview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by iqbalpakeh on 22/2/14.
 */
public class ObserveableListView extends ListView
{
    private Callbacks mCallbacks;
    private final int INVALID_POINTER_ID = MotionEvent.INVALID_POINTER_ID;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX, mLastTouchY, mPosX, mPosY;
    private boolean isScrollUp;

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
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (mCallbacks != null)
        {
            switch (ev.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);

                    // Remember where we started (for dragging)
                    mLastTouchX = x;
                    mLastTouchY = y;
                    // Save the ID of this pointer (for dragging)
                    mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                }

                case MotionEvent.ACTION_MOVE:
                {
                    // Find the index of the active pointer and fetch its position
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);

                    // Calculate the distance moved
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();

                    // Remember this touch position for the next move event
                    mLastTouchX = x;
                    mLastTouchY = y;

                    if(dy > 0)
                    {
                        if(!isScrollUp)
                        {
                            mCallbacks.onScrollUp();
                            isScrollUp = true;
                        }
                    }
                    else
                    {
                        if(isScrollUp)
                        {
                            mCallbacks.onScrollDown();
                            isScrollUp = false;
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_UP:
                {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL:
                {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP:
                {
                    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                    if (pointerId == mActivePointerId)
                    {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                        mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                        mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    }
                    break;
                }
            }


        }
        return super.onTouchEvent(ev);
    }

    public static interface Callbacks
    {
        public void onScrollDown();

        public void onScrollUp();
    }
}
