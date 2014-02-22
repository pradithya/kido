package com.progrema.superbaby.ui.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Diaper;

/**
 * Created by aria on 20/2/14.
 */
public class DiaperDialogFragment extends DialogFragment
{
    public static DiaperDialogFragment getInstance()

    {
        return new DiaperDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_fragment_diaper, parent, false);
        Button buttonDry = (Button) view.findViewById(R.id.dialog_choice_dry);
        buttonDry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent result = new Intent();
                result.putExtra(Diaper.DIAPER_TYPE_KEY, Diaper.DiaperType.DRY.getTitle());
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, result);
                getDialog().dismiss();
            }
        });

        Button buttonWet = (Button) view.findViewById(R.id.dialog_choice_wet);
        buttonWet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent result = new Intent();
                result.putExtra(Diaper.DIAPER_TYPE_KEY, Diaper.DiaperType.WET.getTitle());
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, result);
                getDialog().dismiss();
            }
        });

        Button buttonMixed = (Button) view.findViewById(R.id.dialog_choice_mixed);
        buttonMixed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent result = new Intent();
                result.putExtra(Diaper.DIAPER_TYPE_KEY, Diaper.DiaperType.MIXED.getTitle());
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, result);
                getDialog().dismiss();
            }
        });

        return view;
    }


}
