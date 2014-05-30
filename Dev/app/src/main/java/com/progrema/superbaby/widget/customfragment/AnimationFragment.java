package com.progrema.superbaby.widget.customfragment;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.progrema.superbaby.R;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class AnimationFragment extends Fragment {

    /*
     * Animation State and calculation variable used for managing the animation
     */
    private LinearLayout llPlaceHolder;
    private TranslateAnimation taAnimation;
    private View vQuickReturn;
    ObserveableListView olvListView;
    private static final int STATE_ONSCREEN = 0;
    private int iState = STATE_ONSCREEN;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int iQuickReturnHeight;
    private int iCacheVerticalRange;
    private int iScrollY;
    private int iRawY;
    private int iMinRawY = 0;

    public void attachQuickReturnView(View vRoot, int iId) {
        this.vQuickReturn = vRoot.findViewById(R.id.header_nursing);
    }

    public void attachPlaceHolderLayout(View vPlaceholderRoot, int iId) {
        this.llPlaceHolder = (LinearLayout) vPlaceholderRoot.findViewById(R.id.placeholder_nursing);
    }

    public void attachListView(ObserveableListView olvListView) {
        this.olvListView = olvListView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // add global layout listener
        olvListView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        iQuickReturnHeight = vQuickReturn.getHeight();
                        olvListView.computeScrollY();
                        iCacheVerticalRange = olvListView.getListHeight();
                    }
                });

        // add scroll listener
        olvListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // nothing happened here
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                iScrollY = 0;
                int iTranslationY = 0;

                if (olvListView.scrollYIsComputed()) {
                    iScrollY = olvListView.getComputedScrollY();
                }

                iRawY = -Math.min(iCacheVerticalRange - olvListView.getHeight(), iScrollY);

                switch (iState) {
                    case STATE_OFFSCREEN:
                        if (iRawY <= iMinRawY) {
                            iMinRawY = iRawY;
                        } else {
                            iState = STATE_RETURNING;
                        }
                        iTranslationY = iRawY;
                        break;

                    case STATE_ONSCREEN:
                        if (iRawY < -iQuickReturnHeight) {
                            iState = STATE_OFFSCREEN;
                            iMinRawY = iRawY;
                        }
                        if (iRawY >= llPlaceHolder.getHeight()) {
                            iRawY = 0;
                        }
                        iTranslationY = iRawY;
                        break;

                    case STATE_RETURNING:
                        iTranslationY = (iRawY - iMinRawY) - iQuickReturnHeight;
                        if (iTranslationY > 0) {
                            iTranslationY = 0;
                            iMinRawY = iRawY - iQuickReturnHeight;
                        }
                        if (iRawY > 0) {
                            iState = STATE_ONSCREEN;
                            iTranslationY = iRawY;
                        }
                        if (iTranslationY < -iQuickReturnHeight) {
                            iState = STATE_OFFSCREEN;
                            iMinRawY = iRawY;
                        }
                        break;
                }

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                    taAnimation = new TranslateAnimation(0, 0, iTranslationY, iTranslationY);
                    taAnimation.setFillAfter(true);
                    taAnimation.setDuration(0);
                    vQuickReturn.startAnimation(taAnimation);
                } else {
                    vQuickReturn.setTranslationY(iTranslationY);
                }
            }
        });
    }
}
