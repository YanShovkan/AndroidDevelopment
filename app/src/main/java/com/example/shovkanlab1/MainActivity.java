package com.example.shovkanlab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonAdd, buttonSelectAll, buttonResetAll, buttonElementOutput;
    EditText editText;
    ListView listView;

    ArrayAdapter<String> adapter;
    List<String> list;

    MainFragment mainFragment;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            mainFragment = new MainFragment(loadDataFromPreferences());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, mainFragment, "TAG_FOR_MAIN_FRAGMENT")
                    .commit();
        }
    }
    private ArrayList<Object[]> loadDataFromPreferences() {
        ArrayList<Object[]> objects = new ArrayList<Object[]>();
        sPref = getPreferences(Context.MODE_PRIVATE);
        int count = sPref.getInt("count",0);
        for (int i = 0; i < count; i++)
        {
            Object[] ob = new Object[3];
            ob[0] = sPref.getInt("Int " + i,0);
            ob[1] = sPref.getString("String " + i,"");
            ob[2] = sPref.getBoolean("Bool " + i,false);
            objects.add(ob);
        }
        return objects;
    }

    @Override
    public void onPause() {
        mainFragment.SaveData();
        super.onPause();
    }

    public void openDisplayResultActivity(ArrayList<String> list) {
        Intent intent = new Intent(this, DisplayResultActivity.class);
        intent.putStringArrayListExtra("key", list);
        startActivity(intent);
    }

    private void initializeElements() {

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSelectAll = findViewById(R.id.buttonSelectAll);
        buttonResetAll = findViewById(R.id.buttonResetAll);
        buttonElementOutput = findViewById(R.id.buttonElementOutput);
        listView = findViewById(R.id.list);

        String[] items = new String[] { };
        list = new ArrayList<>(Arrays.asList(items));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(this, R.layout.my_listview, list);
        listView.setAdapter(adapter);

        editText = findViewById(R.id.editTextTextPersonName);

        buttonAdd.setOnClickListener(v -> addItem());

        buttonElementOutput.setOnClickListener(v -> Toast.makeText(getApplicationContext(), editText.getText(), Toast.LENGTH_SHORT).show());

        buttonSelectAll.setOnClickListener(view -> selectAll());

        buttonResetAll.setOnClickListener(view -> resetAll());

        buttonElementOutput.setOnClickListener(view -> elementOutput());
    }

    private void addItem() {
        if (editText.getText().length() != 0) {
            list.add(editText.getText().toString());
            adapter.notifyDataSetChanged();
            editText.setText(null);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, AddFragment.class, null)
                .addToBackStack(null)
                .commit();
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
        Toast.makeText(getApplicationContext(), output.toString(), Toast.LENGTH_SHORT).show();
    }
}