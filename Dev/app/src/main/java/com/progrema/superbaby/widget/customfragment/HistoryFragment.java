package com.progrema.superbaby.widget.customfragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.util.Calendar;

public class HistoryFragment extends Fragment {

    /*
     * Animation State and calculation variable used for managing the animation
     */
    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int iState = STATE_ONSCREEN;
    ObserveableListView olvListView;
    private LinearLayout llPlaceHolder;
    private TranslateAnimation taAnimation;
    private View vQuickReturn;
    private boolean bShowBackground = false;
    private int iQuickReturnHeight;
    private int iCacheVerticalRange;
    private int iScrollY;
    private int iRawY;
    private int iMinRawY = 0;

    public void attachQuickReturnView(View vRoot, int iId) {
        this.vQuickReturn = vRoot.findViewById(iId);
    }

    public void attachPlaceHolderLayout(View vPlaceholderRoot, int iId) {
        this.llPlaceHolder = (LinearLayout) vPlaceholderRoot.findViewById(iId);
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
                        llPlaceHolder.setMinimumHeight(iQuickReturnHeight);
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
                String iStateTrace = "";

                if (olvListView.scrollYIsComputed()) {
                    iScrollY = olvListView.getComputedScrollY();
                }

                iRawY = - Math.min(iCacheVerticalRange - olvListView.getHeight() , iScrollY);

                switch (iState) {
                    case STATE_ONSCREEN: // state 0
                        iStateTrace = "00";
                        if (iRawY < -iQuickReturnHeight) {
                            iState = STATE_OFFSCREEN;
                            iMinRawY = iRawY;
                            iStateTrace = "01";
                        }
                        if (iRawY >= llPlaceHolder.getHeight()) {
                            iRawY = 0;
                            iStateTrace = "02";
                        }
                        iTranslationY = iRawY;
                        break;

                    case STATE_OFFSCREEN: // state 1
                        iStateTrace = "10";
                        if (iRawY <= iMinRawY) {
                            iMinRawY = iRawY;
                            iStateTrace = "11";
                        } else {
                            iState = STATE_RETURNING;
                            iStateTrace = "12";
                        }
                        iTranslationY = iRawY;
                        break;

                    case STATE_RETURNING: // state 2
                        iTranslationY = (iRawY - iMinRawY) - iQuickReturnHeight;
                        iStateTrace = "20";
                        if (iTranslationY > 0) {
                            iTranslationY = 0;
                            iMinRawY = iRawY - iQuickReturnHeight;
                            iStateTrace = "21";
                            if (iRawY < 0 && !bShowBackground) {
                                setShadowBackground(R.drawable.header_shadow);
                                bShowBackground = true;
                                iStateTrace = "24";
                            } else if (iRawY >= 0 && bShowBackground){
                                setShadowBackground(0);
                                bShowBackground = false;
                                iStateTrace = "25";
                            }
                        }
                        if (iRawY >= 0) {
                            iState = STATE_ONSCREEN;
                            iTranslationY = iRawY;
                            iStateTrace = "22";
                        }
                        if (iTranslationY < -iQuickReturnHeight) {
                            iState = STATE_OFFSCREEN;
                            iMinRawY = iRawY;
                            iStateTrace = "23";
                        }
                        if (iStateTrace.equals("20") && !bShowBackground &&
                                (-iTranslationY < iQuickReturnHeight) && (iTranslationY != 0)) {
                            setShadowBackground(R.drawable.header_shadow);
                            bShowBackground = true;
                            iStateTrace = "26";
                        }
                        break;
                }

                Log.i("_DB1", " iScrY=" + iScrollY +
                        " iRawY=" + iRawY +
                        " iStte=" + iStateTrace +
                        " iTranY=" + iTranslationY +
                        " iCache=" + iCacheVerticalRange +
                        " iHght=" + olvListView.getHeight() +
                        " iFun=" + (iCacheVerticalRange - olvListView.getHeight()) +
                        " iQuic=" + iQuickReturnHeight);

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

    private void setShadowBackground(int iBackgroundId) {
        int iPaddingLeft = vQuickReturn.getPaddingLeft();
        int iPaddingTop = vQuickReturn.getPaddingTop();
        int iPaddingRight = vQuickReturn.getPaddingRight();
        int iPaddingBottom = vQuickReturn.getPaddingBottom();
        vQuickReturn.setBackgroundResource(iBackgroundId);
        vQuickReturn.setPadding(iPaddingLeft, iPaddingTop, iPaddingRight, iPaddingBottom);
    }

    public String[] getTimeFilterArg(Bundle bBundle) {

        String sStart;
        String sEnd;

        if (bBundle != null) {
            sStart = bBundle.getString(HomeActivity.TimeFilter.START.getTitle());
            sEnd = bBundle.getString(HomeActivity.TimeFilter.END.getTitle());

        } else {
            Calendar cStart = Calendar.getInstance();
            sEnd = String.valueOf(cStart.getTimeInMillis()); //now, for now

            cStart.set(Calendar.HOUR_OF_DAY, 0);
            cStart.set(Calendar.MINUTE, 0);
            cStart.set(Calendar.SECOND, 0);
            cStart.set(Calendar.MILLISECOND, 0);
            sStart = String.valueOf(cStart.getTimeInMillis());
        }

        String[] aTimeFilterArg = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                sStart, sEnd
        };

        return aTimeFilterArg;
    }
}
