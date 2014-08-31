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

    private Callbacks mCallbacks;
    private String height;
    private String weight;

    public static MeasurementDialog getInstance() {
        return new MeasurementDialog();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
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
                                getWeigh();
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

    private void getWeigh() {
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
        Intent result = new Intent();
        result.putExtra(BabyLogContract.Measurement.HEIGHT, height);
        result.putExtra(BabyLogContract.Measurement.WEIGHT, weight);
        MeasurementDialog.this.mCallbacks.onMeasurementChoiceSelected(0, result);
        getDialog().dismiss();
    }

    public static interface Callbacks {
        public void onMeasurementChoiceSelected(int resultCode, Intent data);
    }


}
