package com.progrema.superbaby.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityNursing;
import com.progrema.superbaby.util.FormatUtils;

public class NursingDialog extends DialogFragment {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int FORMULA = 2;
    private Callback callback;

    public static NursingDialog getInstance() {
        return new NursingDialog();
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.nursing_dialog_title)
                .setItems(R.array.nursing_selection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case LEFT:
                                insertLeftEntry();
                                break;
                            case RIGHT:
                                insertRightEntry();
                                break;
                            case FORMULA:
                                insertFormulaEntry();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.diaper_dialog_negative_button, null);
        return builder.create();
    }

    private void insertLeftEntry() {
        Intent result = new Intent();
        result.putExtra(ActivityNursing.NURSING_TYPE_KEY, ActivityNursing.NursingType.LEFT.getTitle());
        NursingDialog.this.callback.onNursingChoiceSelected(0, result);
        getDialog().dismiss();
    }

    private void insertRightEntry() {
        Intent result = new Intent();
        result.putExtra(ActivityNursing.NURSING_TYPE_KEY, ActivityNursing.NursingType.RIGHT.getTitle());
        NursingDialog.this.callback.onNursingChoiceSelected(0, result);
        getDialog().dismiss();
    }

    private void insertFormulaEntry() {
        EditText inputVolume = (EditText) getDialog().findViewById(R.id.entry_text_volume);
        String volume = inputVolume.getText().toString();
        if (!checkFormulaEntry(volume)) return;
        Intent result = new Intent();
        result.putExtra(ActivityNursing.NURSING_TYPE_KEY, ActivityNursing.NursingType.FORMULA.getTitle());
        result.putExtra(ActivityNursing.FORMULA_VOLUME_KEY, volume);
        NursingDialog.this.callback.onNursingChoiceSelected(0, result);
        getDialog().dismiss();
    }

    private boolean checkFormulaEntry(String volume) {
        if (!FormatUtils.isValidNumber(volume)) {
            Toast invalidNumber = Toast.makeText(getActivity(), "invalid number", Toast.LENGTH_LONG);
            invalidNumber.show();
            return false;
        }
        return true;
    }

    public static interface Callback {
        public void onNursingChoiceSelected(int resultCode, Intent data);
    }
}
