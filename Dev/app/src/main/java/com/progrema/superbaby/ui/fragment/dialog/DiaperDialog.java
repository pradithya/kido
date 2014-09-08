package com.progrema.superbaby.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityDiaper;

public class DiaperDialog extends DialogFragment {

    private Button dryHandler;
    private Button wetHandler;
    private Button mixHandler;
    private Callback callback;

    public static DiaperDialog getInstance() {
        return new DiaperDialog();
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View root = inflater.inflate(R.layout.dialog_fragment_diaper, null);
        prepareDryHandler(root);
        prepareWetHandler(root);
        prepareMixHandler(root);
        builder.setView(root);
        builder.setTitle(R.string.diaper_dialog_title);
        builder.setNegativeButton(R.string.diaper_dialog_negative_button, null);
        return builder.create();
    }

    private void prepareDryHandler(View root) {
        dryHandler = (Button) root.findViewById(R.id.dialog_choice_dry);
        dryHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ActivityDiaper.DIAPER_TYPE_KEY,
                        ActivityDiaper.DiaperType.DRY.getTitle());
                DiaperDialog.this.callback.onDiaperDialogSelected(intent);
                getDialog().dismiss();
            }
        });
    }

    private void prepareWetHandler(View root) {
        wetHandler = (Button) root.findViewById(R.id.dialog_choice_wet);
        wetHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ActivityDiaper.DIAPER_TYPE_KEY,
                        ActivityDiaper.DiaperType.WET.getTitle());
                DiaperDialog.this.callback.onDiaperDialogSelected(intent);
                getDialog().dismiss();
            }
        });
    }

    private void prepareMixHandler(View root) {
        mixHandler = (Button) root.findViewById(R.id.dialog_choice_mixed);
        mixHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ActivityDiaper.DIAPER_TYPE_KEY,
                        ActivityDiaper.DiaperType.MIXED.getTitle());
                DiaperDialog.this.callback.onDiaperDialogSelected(intent);
                getDialog().dismiss();
            }
        });

    }

    public static interface Callback {
        public void onDiaperDialogSelected(Intent data);
    }
}
