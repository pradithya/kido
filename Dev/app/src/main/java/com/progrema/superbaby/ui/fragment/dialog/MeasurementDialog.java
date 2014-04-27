package com.progrema.superbaby.ui.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

/**
 * Created by iqbalpakeh on 27/2/14.
 */
public class MeasurementDialog extends DialogFragment {
    private Callbacks mCallbacks;

    public static MeasurementDialog getInstance() {
        return new MeasurementDialog();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_measurement, parent, false);
        Button ok = (Button) view.findViewById(R.id.button_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputHeight = (EditText) getDialog().findViewById(R.id.entry_text_height);
                String height = inputHeight.getText().toString();
                if (!FormatUtils.isValidNumber(height)) {
                    Toast invalidNumber =
                            Toast.makeText(getActivity(), "invalid number", Toast.LENGTH_LONG);
                    invalidNumber.show();
                    return; //invalid height
                }

                EditText inputWeight = (EditText) getDialog().findViewById(R.id.entry_text_weight);
                String weight = inputWeight.getText().toString();
                if (!FormatUtils.isValidNumber(height)) {
                    Toast invalidNumber =
                            Toast.makeText(getActivity(), "invalid number", Toast.LENGTH_LONG);
                    invalidNumber.show();
                    return; //invalid weight
                }

                Intent result = new Intent();
                result.putExtra(BabyLogContract.Measurement.HEIGHT, height);
                result.putExtra(BabyLogContract.Measurement.WEIGHT, weight);
                MeasurementDialog.this.mCallbacks.onMeasurementChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public static interface Callbacks {
        public void onMeasurementChoiceSelected(int resultCode, Intent data);
    }


}
