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
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_nursing, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        String sTimestamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        String sType = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        String sDuration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        String sVolume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);

        TextView tvDate = (TextView) view.findViewById(R.id.history_item_day);
        TextView tvTime = (TextView) view.findViewById(R.id.information_time);
        TextView tvDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tvVolume = (TextView) view.findViewById(R.id.history_item_volume);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        ImageView ivMenuButton = (ImageView) view.findViewById(R.id.menu_button);
        ivMenuButton.setTag(cursor.getString(BabyLogContract.Nursing.Query.OFFSET_ID));
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
                    }
                }
        );

        tvVolume.setVisibility(View.GONE);
        tvDate.setText(FormatUtils.fmtDate(context, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(context, sTimestamp));
        tvDuration.setText(FormatUtils.fmtDuration(context, sDuration));

        if (sType.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            tvVolume.setVisibility(View.VISIBLE);
            tvVolume.setText(sVolume + "mL");
            tvVolume.setTextColor(view.getResources().getColor(R.color.red));
            tvTime.setTextColor(view.getResources().getColor(R.color.red));
            tvDuration.setTextColor(view.getResources().getColor(R.color.red));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (sType.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
            tvDuration.setTextColor(view.getResources().getColor(R.color.green));
            tvTime.setTextColor(view.getResources().getColor(R.color.green));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if (sType.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
            tvDuration.setTextColor(view.getResources().getColor(R.color.orange));
            tvTime.setTextColor(view.getResources().getColor(R.color.orange));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }

    private void handleDelete(Context context, View vEntry) {
        Nursing nNursing = new Nursing();
        nNursing.setID(Long.valueOf((String) vEntry.getTag()));
        nNursing.delete(context);
    }

    private void handleEdit(Context context, View vEntry) {
    }
}
