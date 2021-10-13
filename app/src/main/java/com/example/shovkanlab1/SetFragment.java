package com.example.shovkanlab1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SetFragment extends Fragment {
    Button buttonSet, buttonCancel;
    EditText editTextName, editTextID, editTextTrueOrFalse;

    private final String name;
    private final int id;
    private final boolean trueOrFalse;
    private final int key;

    public SetFragment(int key, int id, String name, boolean trueOrFalse) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.trueOrFalse = trueOrFalse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        initializeElements(view);
        return view;
    }

    private void initializeElements(View view) {

        buttonSet = view.findViewById(R.id.buttonSet);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        editTextName = view.findViewById(R.id.editTextTextPersonName);
        editTextID = view.findViewById(R.id.editTextTextID);
        editTextTrueOrFalse = view.findViewById(R.id.editTextTrueOrFalse);

        editTextID.setText(String.valueOf(id));
        editTextName.setText(name);
        editTextTrueOrFalse.setText(String.valueOf(trueOrFalse));

        buttonSet.setOnClickListener(v -> setItem());

        buttonCancel.setOnClickListener(v -> cancel());
    }

    private void setItem() {
        if (editTextName.getText().length() != 0 && editTextID.getText().length() != 0 && editTextTrueOrFalse.getText().length() != 0) {
            Object[] obj = { Integer.parseInt(editTextID.getText().toString()), editTextName.getText(), Boolean.parseBoolean(editTextTrueOrFalse.getText().toString()) };
            ((MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("TAG_FOR_MAIN_FRAGMENT")).setItem(key, obj);
            editTextName.setText(null);
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void cancel() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}