package com.progrema.superbaby.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityNursing;
import com.progrema.superbaby.util.FormatUtils;

public class NursingDialog extends DialogFragment {

    private final int REQ_BREASTFEEDING = 0;
    private final int REQ_FORMULA = 1;
    private Callback callback;
    private Button leftHandler;
    private Button rightHandler;
    private Button formulaHandler;
    private Button formulaOkHandler;

    public static NursingDialog getInstance() {
        return new NursingDialog();
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View root = inflater.inflate(R.layout.dialog_fragment_nursing, null);
        prepareLefHandler(root);
        prepareRightHandler(root);
        prepareFormulaHandler(root);
        builder.setView(root);
        builder.setTitle(R.string.nursing_dialog_title);
        builder.setNegativeButton(R.string.diaper_dialog_negative_button, null);
        return builder.create();
    }

    private void prepareLefHandler(View root) {
        leftHandler = (Button) root.findViewById(R.id.dialog_choice_left);
        leftHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ActivityNursing.NURSING_TYPE_KEY,
                        ActivityNursing.NursingType.LEFT.getTitle());
                NursingDialog.this.callback.onNursingDialogSelected(REQ_BREASTFEEDING, intent);
                getDialog().dismiss();
            }
        });
    }

    private void prepareRightHandler(View root) {
        rightHandler = (Button) root.findViewById(R.id.dialog_choice_right);
        rightHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ActivityNursing.NURSING_TYPE_KEY,
                        ActivityNursing.NursingType.RIGHT.getTitle());
                NursingDialog.this.callback.onNursingDialogSelected(REQ_BREASTFEEDING, intent);
                getDialog().dismiss();
            }
        });
    }

    private void prepareFormulaHandler(View root) {
        formulaHandler = (Button) root.findViewById(R.id.dialog_choice_formula);
        formulaHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout extraInfoFormula =
                        (LinearLayout) getDialog().findViewById(R.id.container_formula_entry);
                extraInfoFormula.setVisibility(View.VISIBLE);
                formulaOkHandler = (Button) extraInfoFormula.findViewById(R.id.button_formula_ok);
                formulaOkHandler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText inputVolume = (EditText) getDialog().findViewById(R.id.entry_text_volume);
                        String volume = inputVolume.getText().toString();
                        if (!checkFormulaEntry(volume)) return;
                        Intent intent = new Intent();
                        intent.putExtra(ActivityNursing.NURSING_TYPE_KEY,
                                ActivityNursing.NursingType.FORMULA.getTitle());
                        intent.putExtra(ActivityNursing.FORMULA_VOLUME_KEY, volume);
                        NursingDialog.this.callback.onNursingDialogSelected(REQ_FORMULA, intent);
                        getDialog().dismiss();
                    }
                });

            }
        });
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
        public void onNursingDialogSelected(int requestCode, Intent data);
    }

}
