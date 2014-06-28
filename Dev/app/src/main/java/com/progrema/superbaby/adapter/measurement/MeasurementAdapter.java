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
import com.progrema.superbaby.models.Measurement;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class MeasurementAdapter extends CursorAdapter {
    public MeasurementAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_measurement, parent, false);
    }

    @Override
    public void bindView(View vView, final Context context, Cursor cCursor) {
        String timestamp = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_TIMESTAMP);
        String height = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_HEIGHT);
        String weight = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_WEIGHT);

        TextView dateHandler = (TextView) vView.findViewById(R.id.history_item_day);
        TextView timeHandler = (TextView) vView.findViewById(R.id.widget_time);
        TextView heightHandler = (TextView) vView.findViewById(R.id.history_item_height);
        TextView weightHandler = (TextView) vView.findViewById(R.id.history_item_weight);
        ImageView menuHandler = (ImageView) vView.findViewById(R.id.menu_button);

        dateHandler.setText(FormatUtils.fmtDate(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        heightHandler.setText(height + " cm");
        weightHandler.setText(weight + " kg");
        menuHandler.setTag(cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_ID));
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView menuHandler = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popupMenu = new PopupMenu(context, menuHandler);
                        popupMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            entryEdit(context, menuHandler);
                                        } else if (item.getTitle().equals("Delete")) {
                                            entryDelete(context, menuHandler);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
                        menuInflater.inflate(R.menu.entry, popupMenu.getMenu());
                        popupMenu.show();
                        Log.i("_DBG_MENU", " Tag = " + menuHandler.getTag());
                    }
                }
        );
    }

    private void entryDelete(Context context, View entry) {
        Measurement measurement = new Measurement();
        measurement.setID(Long.valueOf((String) entry.getTag()));
        measurement.delete(context);
    }

    private void entryEdit(Context context, View entry) {
    }
}
