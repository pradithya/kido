package com.progrema.superbaby.ui.fragment.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface HistoryFragmentServices {
    public void prepareFragment(LayoutInflater inflater, ViewGroup container);
    public void prepareHandler();
    public void prepareListView();
    public void prepareLoaderManager();
}
