package com.progrema.superbaby.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityDiaper;

public class DiaperDialog extends DialogFragment {

    private Callback callback;
    private static final int DRY = 0;
    private static final int WET = 1;
    private static final int MIXED = 2;

    public static DiaperDialog getInstance() {
        return new DiaperDialog();
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diaper_dialog_title)
                .setItems(R.array.diaper_selection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DRY:
                                insertDryEntry();
                                break;
                            case WET:
                                insertWetEntry();
                                break;
                            case MIXED:
                                insertMixedEntry();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.diaper_dialog_negative_button, null);
        return builder.create();
    }

    private void insertDryEntry() {
        Intent result = new Intent();
        result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.DRY.getTitle());
        DiaperDialog.this.callback.onDiaperChoiceSelected(0, result);
        getDialog().dismiss();
    }

    private void insertWetEntry() {
        Intent result = new Intent();
        result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.WET.getTitle());
        DiaperDialog.this.callback.onDiaperChoiceSelected(0, result);
        getDialog().dismiss();
    }

    private void insertMixedEntry() {
        Intent result = new Intent();
        result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.MIXED.getTitle());
        DiaperDialog.this.callback.onDiaperChoiceSelected(0, result);
        getDialog().dismiss();
    }

    public static interface Callback {
        public void onDiaperChoiceSelected(int result, Intent data);
    }
}
