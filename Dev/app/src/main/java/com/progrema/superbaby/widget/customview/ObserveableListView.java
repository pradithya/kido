package com.progrema.superbaby.widget.customview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ObserveableListView extends ListView {

    private Callbacks cCallbacks;
    private boolean bIsScrollUp = true;
    private boolean bScrollIsComputed = false;
    private float fLastTouchX;
    private float fLastTouchY;
    private float fPositionX;
    private float fPositionY;
    private int INVALID_POINTER_ID = MotionEvent.INVALID_POINTER_ID;
    private int iActivePointerId = INVALID_POINTER_ID;
    private int SCROLLING_BUFFER = 3;
    private int iHeight;
    private int iItemCount;
    private int iItemOffsetY[];

    public ObserveableListView(Context context) {
        super(context);
    }

    public ObserveableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserveableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCallbacks(Callbacks listener) {
        cCallbacks = listener;
    }

    public int getListHeight() {
        return iHeight;
    }

    public void computeScrollY() {
        iHeight = 0;
        iItemCount = getAdapter().getCount();
        iItemOffsetY = null; // Let GC works!!
        iItemOffsetY = new int[iItemCount];
        for (int i = 0; i < iItemCount; ++i) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            iItemOffsetY[i] = iHeight;
            iHeight += view.getMeasuredHeight();
        }
        bScrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return bScrollIsComputed;
    }

    public int getComputedScrollY() {
        int iPosition, iScrollY, iItemY;
        View view = null;
        iPosition = getFirstVisiblePosition();
        view = getChildAt(0);
        iItemY = view.getTop();
        iScrollY = iItemOffsetY[iPosition] - iItemY;
        /*
        Log.i("__DEBUG",
                "iPosition = " + iPosition +
                        " height = " + view.getHeight()
        );
        */
        return iScrollY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (cCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    final int iPointerIndex = MotionEventCompat.getActionIndex(ev);
                    final float fCoordinateX = MotionEventCompat.getX(ev, iPointerIndex);
                    final float fCoordinateY = MotionEventCompat.getY(ev, iPointerIndex);

                    // Remember where we started (for dragging)
                    fLastTouchX = fCoordinateX;
                    fLastTouchY = fCoordinateY;
                    // Save the ID of this pointer (for dragging)
                    iActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    final int iPointerIndex = MotionEventCompat.findPointerIndex(ev, iActivePointerId);
                    final float fCoordinateX = MotionEventCompat.getX(ev, iPointerIndex);
                    final float fCoordinateY = MotionEventCompat.getY(ev, iPointerIndex);

                    // Calculate the distance moved
                    final float fDeltaX = fCoordinateX - fLastTouchX;
                    final float fDeltaY = fCoordinateY - fLastTouchY;

                    fPositionX += fDeltaX;
                    fPositionY += fDeltaY;

                    invalidate();

                    // Remember this touch position for the next move event
                    fLastTouchX = fCoordinateX;
                    fLastTouchY = fCoordinateY;

                    if (fDeltaY > SCROLLING_BUFFER) {
                        if (!bIsScrollUp) {
                            cCallbacks.onScrollDown();
                            bIsScrollUp = true;
                        }
                    } else if (fDeltaY < -SCROLLING_BUFFER) {
                        if (bIsScrollUp) {
                            cCallbacks.onScrollUp();
                            bIsScrollUp = false;
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    iActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    iActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    final int iPointerIndex = MotionEventCompat.getActionIndex(ev);
                    final int iPointerId = MotionEventCompat.getPointerId(ev, iPointerIndex);

                    if (iPointerId == iActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = iPointerIndex == 0 ? 1 : 0;
                        fLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                        fLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                        iActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    }
                    break;
                }
            }


        }
        return super.onTouchEvent(ev);
    }

    public static interface Callbacks {
        public void onScrollUp();

        public void onScrollDown();
    }
}
