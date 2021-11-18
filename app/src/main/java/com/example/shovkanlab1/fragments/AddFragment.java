package com.example.shovkanlab1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shovkanlab1.Element;
import com.example.shovkanlab1.R;

public class AddFragment extends Fragment {

    Button buttonAdd, buttonCancel;
    EditText editTextName, editTextID, editTextTrueOrFalse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        initializeElements(view);
        return view;
    }

    private void initializeElements(View view) {

        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        editTextName = view.findViewById(R.id.editTextTextPersonName);
        editTextID = view.findViewById(R.id.editTextTextID);
        editTextTrueOrFalse = view.findViewById(R.id.editTextTrueOrFalse);

        buttonAdd.setOnClickListener(v -> addItem());

        buttonCancel.setOnClickListener(v -> cancel());
    }

    private void addItem() {
        if (editTextName.getText().length() != 0 && editTextID.getText().length() != 0 && editTextTrueOrFalse.getText().length() != 0) {
            Element obj = new Element(Integer.parseInt(editTextID.getText().toString()), editTextName.getText().toString(), editTextTrueOrFalse.getText().toString());
            ((MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("TAG_FOR_MAIN_FRAGMENT")).addItem(obj);
            editTextName.setText(null);
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void cancel() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}