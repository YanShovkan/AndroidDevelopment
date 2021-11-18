package com.example.shovkanlab1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.shovkanlab1.R;

public class DeleteDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog_AppCompat);
        return builder
                .setTitle("Удаление")
                .setMessage("Вы хотите удалить то, что вы выбрали?")
                .setPositiveButton(R.string.alert_dialog_ok, (dialog, id) -> ((MainFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag("TAG_FOR_MAIN_FRAGMENT"))
                        .deleteItems())
                .setNegativeButton(R.string.alert_dialog_cancel, null)
                .create();
    }
}