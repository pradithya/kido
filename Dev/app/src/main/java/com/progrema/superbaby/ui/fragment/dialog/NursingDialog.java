package com.progrema.superbaby.ui.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.util.FormatUtils;

public class NursingDialog extends DialogFragment {
    private Callbacks mCallbacks;

    public static NursingDialog getInstance() {
        return new NursingDialog();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_nursing, parent, false);
        Button buttonLeft = (Button) view.findViewById(R.id.dialog_choice_left);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(Nursing.NURSING_TYPE_KEY, Nursing.NursingType.LEFT.getTitle());
                NursingDialog.this.mCallbacks.onNursingChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        Button buttonRight = (Button) view.findViewById(R.id.dialog_choice_right);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(Nursing.NURSING_TYPE_KEY, Nursing.NursingType.RIGHT.getTitle());
                NursingDialog.this.mCallbacks.onNursingChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        Button buttonFormula = (Button) view.findViewById(R.id.dialog_choice_formula);
        buttonFormula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout extraInfoFormula =
                        (LinearLayout) getDialog().findViewById(R.id.container_formula_entry);
                extraInfoFormula.setVisibility(View.VISIBLE);
            }
        });
        Button formulaOK = (Button) view.findViewById(R.id.button_formula_ok);
        formulaOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputVolume = (EditText) getDialog().findViewById(R.id.entry_text_volume);
                String volume = inputVolume.getText().toString();

                if (!FormatUtils.isValidNumber(volume)) {
                    Toast invalidNumber =
                            Toast.makeText(getActivity(), "invalid number", Toast.LENGTH_LONG);
                    invalidNumber.show();
                    return; //invalid volume
                }
                Intent result = new Intent();
                result.putExtra(Nursing.NURSING_TYPE_KEY, Nursing.NursingType.FORMULA.getTitle());
                result.putExtra(Nursing.FORMULA_VOLUME_KEY, volume);
                NursingDialog.this.mCallbacks.onNursingChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        return view;
    }

    public static interface Callbacks {
        public void onNursingChoiceSelected(int resultCode, Intent data);
    }
}
