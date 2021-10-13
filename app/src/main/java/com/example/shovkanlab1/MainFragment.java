package com.example.shovkanlab1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    Button buttonAdd, buttonSelectAll, buttonResetAll, buttonElementOutput, buttonLoadData;
    EditText editText;
    ListView listView;

    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ArrayList<Object[]> objects;
    SharedPreferences sPref;

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initializeElements(view);
        return view;
    }

    public MainFragment(ArrayList<Object[]> objects) {
        list = new ArrayList<String>();
        this.objects = objects;
        dataRefill();
    }

    private void initializeElements(View view) {

        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonSelectAll = view.findViewById(R.id.buttonSelectAll);
        buttonResetAll = view.findViewById(R.id.buttonResetAll);
        buttonElementOutput = view.findViewById(R.id.buttonElementOutput);
        buttonLoadData = view.findViewById(R.id.buttonLoadData);
        listView = view.findViewById(R.id.list);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(getContext(), R.layout.my_listview, list);
        listView.setAdapter(adapter);

        editText = view.findViewById(R.id.editTextTextPersonName);

        buttonAdd.setOnClickListener(v -> openAddFragment());

        buttonElementOutput.setOnClickListener(v -> Toast.makeText(getContext(), editText.getText(), Toast.LENGTH_SHORT).show());

        buttonSelectAll.setOnClickListener(v -> openDeleteDialogFragment());

        buttonResetAll.setOnClickListener(v -> openSetFragment());

        buttonElementOutput.setOnClickListener(v -> openResultActivity());

        buttonLoadData.setOnClickListener(v -> loadDataFromJson());
    }

    private void openAddFragment() {
        AddFragment addFragment = new AddFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, addFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void addItem(Object[] obj) {
        objects.add(obj);
        list.add(objects.get(objects.size() - 1)[1].toString());
        adapter.notifyDataSetChanged();
    }

    private void openDeleteDialogFragment() {
        if (listView.getCheckedItemPositions().size() > 0) {
            DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
            dialogFragment.show(getActivity().getSupportFragmentManager(), null);
        }
    }

    public void deleteItems() {
        SparseBooleanArray sbArray = listView.getCheckedItemPositions();
        int cnt = 0;
        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            if (sbArray.get(key)) {
                listView.setItemChecked(key, false);
                list.remove(key - cnt);
                objects.remove(i - cnt);
                cnt++;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private int getFirstSelectedItem() {
        SparseBooleanArray sbArray = listView.getCheckedItemPositions();
        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            if (sbArray.get(key)) {
                listView.setItemChecked(key, false);
                return key;
            }
        }
        return -1;
    }

    private void openSetFragment() {
        int key = getFirstSelectedItem();
        if (key != -1) {
            int id = Integer.parseInt(objects.get(key)[0].toString());
            String name = objects.get(key)[1].toString();
            boolean trueOrFalse = Boolean.parseBoolean(objects.get(key)[2].toString());

            SetFragment setFragment = new SetFragment(key, id, name, trueOrFalse);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, setFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setItem(int key, Object[] obj) {
        objects.set(key, obj);
        list.set(key, objects.get(key)[1].toString());
        adapter.notifyDataSetChanged();
    }

    private void openResultActivity() {
        if (editText.getText().length() != 0) {
            String substring = editText.getText().toString();
            ArrayList<String> newList = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(substring)) {
                    newList.add(list.get(i));
                }
            }
            editText.setText("");
            ((MainActivity)getActivity()).openDisplayResultActivity(newList);
        }
    }

    private String loadJSONFromAsset() {
        String json = "";
        String fileName = editText.getText().toString();
        if (fileName.length() != 0) {
            try {
                InputStream is = getActivity().getAssets().open(fileName + ".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    private void loadDataFromJson() {
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo_inside = jsonArray.getJSONObject(i);
                int Id_value = jo_inside.getInt("ID");
                String Name_value = jo_inside.getString("Name");
                Boolean Boolean_value = jo_inside.getBoolean("Boolean");
                objects.add(new Object[] { Id_value, Name_value, Boolean_value });
                list.add(Name_value);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dataRefill() {
        for (int i = 0; i < objects.size(); i++) {
            list.add(objects.get(i)[1].toString());
        }
    }

    public void SaveData() {
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("count", objects.size());
        for (int i = 0; i < objects.size(); i++)
        {
            ed.putInt("Int " + i, (int)objects.get(i)[0]);
            ed.putString("String " + i, (String)objects.get(i)[1].toString());
            ed.putBoolean("Bool " + i, (boolean)objects.get(i)[2]);
        }
        ed.commit();
    }

    private void selectAll() {
        for ( int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, true);
        }
    }

    private void resetAll() {
        for ( int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, false);
        }
    }

    private void elementOutput() {
        SparseBooleanArray sbArray = listView.getCheckedItemPositions();
        StringBuilder output = new StringBuilder("\n");
        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            if (sbArray.get(key)) {
                output.append(list.get(key)).append("\n");
            }
        }
        Toast.makeText(getContext(), output.toString(), Toast.LENGTH_SHORT).show();
    }
}