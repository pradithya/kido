package com.progrema.superbaby.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class MeasurementDialog extends DialogFragment {

    private Callback callback;
    private String height;
    private String weight;

    public static MeasurementDialog getInstance() {
        return new MeasurementDialog();
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_fragment_measurement, null);
        builder.setView(view);
        builder.setTitle(R.string.measurement_dialog_title)
                .setNegativeButton(R.string.measurement_dialog_negative_button, null)
                .setPositiveButton(R.string.measurement_dialog_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getHeight();
                                getWeight();
                                if (!checkHeightAndWeight()) return;
                                submitAndDismissDialog();
                            }
                        });
        return builder.create();
    }

    private void getHeight() {
        EditText inputHeight = (EditText) getDialog().findViewById(R.id.entry_text_height);
        height = inputHeight.getText().toString();
    }

    private void getWeight() {
        EditText inputWeight = (EditText) getDialog().findViewById(R.id.entry_text_weight);
        weight = inputWeight.getText().toString();
    }

    private boolean checkHeightAndWeight() {
        if (!FormatUtils.isValidNumber(height) || !FormatUtils.isValidNumber(weight)) {
            Toast invalidNumber = Toast.makeText(getActivity(), "invalid number", Toast.LENGTH_LONG);
            invalidNumber.show();
            return false;
        }
        return true;
    }

    private void submitAndDismissDialog() {
        Intent intent = new Intent();
        intent.putExtra(BabyLogContract.Measurement.HEIGHT, height);
        intent.putExtra(BabyLogContract.Measurement.WEIGHT, weight);
        MeasurementDialog.this.callback.onMeasurementDialogSelected(intent);
        getDialog().dismiss();
    }

    public static interface Callback {
        public void onMeasurementDialogSelected(Intent data);
    }


}
