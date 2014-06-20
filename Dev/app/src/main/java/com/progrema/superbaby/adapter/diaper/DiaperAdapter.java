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
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_diaper, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String sTimestamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String sType = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView tvDay = (TextView) view.findViewById(R.id.history_item_day);
        TextView tvDate = (TextView) view.findViewById(R.id.history_item_date);
        TextView tvTime = (TextView) view.findViewById(R.id.information_time);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        ImageView ivMenuButton = (ImageView) view.findViewById(R.id.menu_button);
        ivMenuButton.setTag(cursor.getString(BabyLogContract.Diaper.Query.OFFSET_ID));
        ivMenuButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView ivMenuButton = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popup = new PopupMenu(context, ivMenuButton);
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            handleEdit(context, ivMenuButton);
                                        } else if (item.getTitle().equals("Delete")) {
                                            handleDelete(context, ivMenuButton);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater miInflater = ((Activity) context).getMenuInflater();
                        miInflater.inflate(R.menu.entry, popup.getMenu());
                        popup.show();
                        Log.i("_DBG_MENU", " Tag = " + ivMenuButton.getTag());
                    }
                }
        );

        tvDay.setText(FormatUtils.fmtDayOnly(context, sTimestamp));
        tvDate.setText(FormatUtils.fmtDateOnly(context, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(context, sTimestamp));

        if (sType.equals(Diaper.DiaperType.WET.getTitle())) {
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
            tvTime.setTextColor(view.getResources().getColor(R.color.blue));
            tvDate.setTextColor(view.getResources().getColor(R.color.blue));
        } else if (sType.equals(Diaper.DiaperType.DRY.getTitle())) {
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
            tvTime.setTextColor(view.getResources().getColor(R.color.orange));
            tvDate.setTextColor(view.getResources().getColor(R.color.orange));
        } else if (sType.equals(Diaper.DiaperType.MIXED.getTitle())) {
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
            tvTime.setTextColor(view.getResources().getColor(R.color.purple));
            tvDate.setTextColor(view.getResources().getColor(R.color.purple));
        }
    }

    private void handleDelete(Context context, View vEntry) {
    }

    private void handleEdit(Context context, View vEntry) {
    }
}
