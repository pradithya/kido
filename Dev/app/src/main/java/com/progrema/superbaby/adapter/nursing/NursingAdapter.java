package com.progrema.superbaby.adapter.nursing;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class NursingAdapter extends CursorAdapter {

    public NursingAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_nursing, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        String timestamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        String type = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        String duration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        String volume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);

        TextView dateHandler = (TextView) view.findViewById(R.id.history_item_day);
        TextView timeHandler = (TextView) view.findViewById(R.id.widget_time);
        TextView durationHandler = (TextView) view.findViewById(R.id.history_item_duration);
        TextView volumeHandler = (TextView) view.findViewById(R.id.history_item_volume);
        ImageView typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        ImageView menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        volumeHandler.setVisibility(View.GONE);
        dateHandler.setText(FormatUtils.fmtDate(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        durationHandler.setText(FormatUtils.fmtDuration(context, duration));
        menuHandler.setTag(cursor.getString(BabyLogContract.Nursing.Query.OFFSET_ID));
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView menuHandler = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popup = new PopupMenu(context, menuHandler);
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            editEntry(context, menuHandler);
                                        } else if (item.getTitle().equals("Delete")) {
                                            deleteEntry(context, menuHandler);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
                        menuInflater.inflate(R.menu.entry, popup.getMenu());
                        popup.show();
                    }
                }
        );

        if (type.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            volumeHandler.setVisibility(View.VISIBLE);
            volumeHandler.setText(volume + "mL");
            volumeHandler.setTextColor(view.getResources().getColor(R.color.red));
            timeHandler.setTextColor(view.getResources().getColor(R.color.red));
            durationHandler.setTextColor(view.getResources().getColor(R.color.red));
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (type.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
            durationHandler.setTextColor(view.getResources().getColor(R.color.green));
            timeHandler.setTextColor(view.getResources().getColor(R.color.green));
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if (type.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
            durationHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }

    private void deleteEntry(Context context, View entry) {
        Nursing nursing = new Nursing();
        nursing.setID(Long.valueOf((String) entry.getTag()));
        nursing.delete(context);
    }

    private void editEntry(Context context, View vEntry) {
    }
}
