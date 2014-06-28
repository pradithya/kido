package com.progrema.superbaby.adapter.diaper;

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
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class DiaperAdapter extends CursorAdapter {
    public DiaperAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_diaper, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String timestamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView dayHandler = (TextView) view.findViewById(R.id.history_item_day);
        TextView dateHandler = (TextView) view.findViewById(R.id.history_item_date);
        TextView timeHandler = (TextView) view.findViewById(R.id.widget_time);
        ImageView typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        ImageView menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        dayHandler.setText(FormatUtils.fmtDayOnly(context, timestamp));
        dateHandler.setText(FormatUtils.fmtDateOnly(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        menuHandler.setTag(cursor.getString(BabyLogContract.Diaper.Query.OFFSET_ID));
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ImageView menuHandler = (ImageView) view.findViewById(R.id.menu_button);
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

        if (type.equals(Diaper.DiaperType.WET.getTitle())) {
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
            timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
            dateHandler.setTextColor(view.getResources().getColor(R.color.blue));
        } else if (type.equals(Diaper.DiaperType.DRY.getTitle())) {
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            dateHandler.setTextColor(view.getResources().getColor(R.color.orange));
        } else if (type.equals(Diaper.DiaperType.MIXED.getTitle())) {
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
            timeHandler.setTextColor(view.getResources().getColor(R.color.purple));
            dateHandler.setTextColor(view.getResources().getColor(R.color.purple));
        }
    }

    private void entryDelete(Context context, View entry) {
        Diaper diaper = new Diaper();
        diaper.setID(Long.valueOf((String) entry.getTag()));
        diaper.delete(context);
    }

    private void entryEdit(Context context, View entry) {
    }
}
