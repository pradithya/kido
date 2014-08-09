package com.progrema.superbaby.ui.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityDiaper;

public class DiaperDialog extends DialogFragment {
    private Callbacks mCallbacks;

    public static DiaperDialog getInstance() {
        return new DiaperDialog();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_diaper, parent, false);
        Button buttonDry = (Button) view.findViewById(R.id.dialog_choice_dry);
        buttonDry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.DRY.getTitle());
                DiaperDialog.this.mCallbacks.onDiaperChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        Button buttonWet = (Button) view.findViewById(R.id.dialog_choice_wet);
        buttonWet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.WET.getTitle());
                DiaperDialog.this.mCallbacks.onDiaperChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        Button buttonMixed = (Button) view.findViewById(R.id.dialog_choice_mixed);
        buttonMixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(ActivityDiaper.DIAPER_TYPE_KEY, ActivityDiaper.DiaperType.MIXED.getTitle());
                DiaperDialog.this.mCallbacks.onDiaperChoiceSelected(0, result);
                getDialog().dismiss();
            }
        });
        return view;
    }

    public static interface Callbacks {
        public void onDiaperChoiceSelected(int result, Intent data);
    }
}
