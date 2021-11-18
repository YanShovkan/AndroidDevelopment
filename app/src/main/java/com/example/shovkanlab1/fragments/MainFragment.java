package com.example.shovkanlab1.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shovkanlab1.Element;
import com.example.shovkanlab1.R;
import com.example.shovkanlab1.activities.MainActivity;
import com.example.shovkanlab1.database.DatabaseService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    Button buttonAdd, buttonDelete, buttonChange, buttonFindElements, buttonLoadData, buttonSyncData;
    EditText editText;
    ListView listView;

    private ArrayAdapter adapter;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<Element> objects = new ArrayList<>();

    private String filename = "";
    private Handler handler;

    private SharedPreferences sPref;
    private String save_props;

    @SuppressLint("HandlerLeak")
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initializeElements(view);
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(android.os.Message msg) {
                list.clear();
                adapter.notifyDataSetChanged();
                dataRefill();
            };
        };
        return view;
    }

    private void initializeElements(View view) {

        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonChange = view.findViewById(R.id.buttonChange);
        buttonFindElements = view.findViewById(R.id.buttonSearch);
        buttonLoadData = view.findViewById(R.id.buttonLoadData);
        listView = view.findViewById(R.id.listview);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter(getContext(), R.layout.my_listview, list);
        listView.setAdapter(adapter);

        editText = view.findViewById(R.id.editTextTextPersonName);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
                filename = editText.getText().toString();
                return false;
            }
        });

        buttonAdd.setOnClickListener(v -> openAddFragment());
        buttonDelete.setOnClickListener(v -> openDeleteDialogFragment());
        buttonChange.setOnClickListener(v -> openSetFragment());
        buttonFindElements.setOnClickListener(v -> openResultActivity());

        buttonLoadData.setOnClickListener(v -> loadDataFromJson());
        syncData();
    }

    private void openAddFragment() {
        AddFragment addFragment = new AddFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, addFragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void addItem(Element obj) {
        objects.add(obj);
        list.add(objects.get(objects.size() - 1).getName());
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
                objects.remove(key - cnt);
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
            int id = objects.get(key).getID();
            String name = objects.get(key).getName();
            boolean trueOrFalse = Boolean.parseBoolean(objects.get(key).getBoolFlag().toString());

            SetFragment setFragment = new SetFragment(key, id, name, trueOrFalse);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, setFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setItem(int key, Element obj) {
        objects.set(key, obj);
        list.set(key, objects.get(key).getName());
        adapter.notifyDataSetChanged();
    }

    private void openResultActivity() {
        if (editText.getText().length() != 0) {
            String substring = editText.getText().toString();
            ArrayList<String> resultList = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains(substring)) {
                    resultList.add(list.get(i));
                }
            }
            editText.setText("");
            ((MainActivity)getActivity()).openDisplayResultActivity(resultList);
        }
    }

    private void syncData() {
        save_props = getActivity().getIntent().getStringExtra("save_props");
        sPref = getContext().getSharedPreferences("Preferences", getContext().MODE_PRIVATE);
        switch (save_props)
        {
            case "Preferences":
                Intent intent = new Intent(getContext(), DatabaseService.class).putExtra("COM", 0);
                getContext().startService(intent);
                break;
            case "DB":
                Thread thread = new Thread(new Runnable() {
                    ArrayList<Element> elements = new ArrayList<>();
                    public void run() {
                        try {
                            int count = sPref.getInt("count",0);
                            for (int i = 0; i < count; i++)
                            {
                                Element object = new Element(sPref.getInt("Int " + i,0), sPref.getString("String " + i,""), Boolean.toString(sPref.getBoolean("Bool " + i,false)));
                                elements.add(object);
                            }
                            objects = elements;
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                break;
        }
    }

    private void loadDataFromJson() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String json = "";
                    String filename = editText.getText().toString();
                    if (filename.length() != 0) {
                        try {
                            InputStream is = getActivity().getAssets().open(filename + ".json");
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
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("MyObject");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo_inside = jsonArray.getJSONObject(i);
                        int Id_value = jo_inside.getInt("Int");
                        String Name_value = jo_inside.getString("String");
                        Boolean BoolFlag_value = jo_inside.getBoolean("Boolean");
                        objects.add(new Element(Id_value, Name_value, BoolFlag_value.toString()));
                    }
                    handler.sendEmptyMessage(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void dataRefill() {
        for (int i = 0; i < objects.size(); i++) {
            list.add(objects.get(i).getName());
        }
    }

    public void saveData() {
        ((MainActivity)getActivity()).saveData(objects);
    }

    public void Init(ArrayList<Element> objects) {
        list.clear();
        this.objects = objects;
        dataRefill();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}