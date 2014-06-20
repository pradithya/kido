package com.progrema.superbaby.adapter.measurement;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class MeasurementAdapter extends CursorAdapter {
    public MeasurementAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater liInflater = LayoutInflater.from(context);
        return liInflater.inflate(R.layout.adapter_measurement, parent, false);
    }

    @Override
    public void bindView(View vView, final Context cContext, Cursor cCursor) {
        String sTimestamp = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_TIMESTAMP);
        String sHeight = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_HEIGHT);
        String sWeight = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_WEIGHT);

        TextView tvDate = (TextView) vView.findViewById(R.id.history_item_day);
        TextView tvTime = (TextView) vView.findViewById(R.id.information_time);
        TextView tvHeight = (TextView) vView.findViewById(R.id.history_item_height);
        TextView tvWeight = (TextView) vView.findViewById(R.id.history_item_weight);

        ImageView ivMenuButton = (ImageView) vView.findViewById(R.id.menu_button);
        ivMenuButton.setTag(cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_ID));
        ivMenuButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView ivMenuButton = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popup = new PopupMenu(cContext, ivMenuButton);
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            handleEdit(cContext, ivMenuButton);
                                        } else if (item.getTitle().equals("Delete")) {
                                            handleDelete(cContext, ivMenuButton);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater miInflater = ((Activity) cContext).getMenuInflater();
                        miInflater.inflate(R.menu.entry, popup.getMenu());
                        popup.show();
                        Log.i("_DBG_MENU", " Tag = " + ivMenuButton.getTag());
                    }
                }
        );

        tvDate.setText(FormatUtils.fmtDate(cContext, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(cContext, sTimestamp));
        tvHeight.setText(sHeight + " cm");
        tvWeight.setText(sWeight + " kg");
    }

    private void handleDelete(Context context, View vEntry) {
    }

    private void handleEdit(Context context, View vEntry) {
    }
}
