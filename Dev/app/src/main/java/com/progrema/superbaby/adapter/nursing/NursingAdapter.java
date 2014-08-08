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
import com.progrema.superbaby.adapter.EntryAdapterServices;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class NursingAdapter extends CursorAdapter implements EntryAdapterServices {

    private String timestamp;
    private String type;
    private String duration;
    private String volume;
    private String entryTag;
    private TextView dateHandler;
    private TextView timeHandler;
    private TextView durationHandler;
    private TextView volumeHandler;
    private ImageView typeHandler;
    private ImageView menuHandler;

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
        storeCursorData(cursor);
        prepareHandler(context, view);
        if (isEntryType(Nursing.NursingType.FORMULA.getTitle())) {
            inflateFormulaEntryLayout(view);
        } else if (isEntryType(Nursing.NursingType.RIGHT.getTitle())) {
            inflateRightEntryLayout(view);
        } else if (isEntryType(Nursing.NursingType.LEFT.getTitle())) {
            inflateLeftEntryLayout(view);
        }
    }

    @Override
    public void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        type = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        duration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        volume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);
        entryTag = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_ACTIVITY_ID);
    }

    @Override
    public void prepareHandler(final Context context, View view) {
        dateHandler = (TextView) view.findViewById(R.id.history_item_day);
        timeHandler = (TextView) view.findViewById(R.id.widget_time);
        durationHandler = (TextView) view.findViewById(R.id.history_item_duration);
        volumeHandler = (TextView) view.findViewById(R.id.history_item_volume);
        typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        volumeHandler.setVisibility(View.GONE);
        dateHandler.setText(FormatUtils.fmtDate(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        durationHandler.setText(FormatUtils.fmtDuration(context, duration));
        menuHandler.setTag(entryTag);
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
    }

    @Override
    public void deleteEntry(Context context, View entry) {
        Nursing nursing = new Nursing();
        nursing.setActivityId(Long.valueOf((String) entry.getTag()));
        nursing.delete(context);
    }

    @Override
    public void editEntry(Context context, View vEntry) {
    }

    private boolean isEntryType(String type) {
        return (this.type.compareTo(type) == 0);
    }

    private void inflateFormulaEntryLayout(View view) {
        volumeHandler.setVisibility(View.VISIBLE);
        volumeHandler.setText(volume + "mL");
        volumeHandler.setTextColor(view.getResources().getColor(R.color.red));
        timeHandler.setTextColor(view.getResources().getColor(R.color.red));
        durationHandler.setTextColor(view.getResources().getColor(R.color.red));
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
    }

    private void inflateRightEntryLayout(View view) {
        durationHandler.setTextColor(view.getResources().getColor(R.color.green));
        timeHandler.setTextColor(view.getResources().getColor(R.color.green));
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
    }

    private void inflateLeftEntryLayout(View view) {
        durationHandler.setTextColor(view.getResources().getColor(R.color.orange));
        timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
    }
}
