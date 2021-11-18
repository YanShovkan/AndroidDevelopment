package com.example.shovkanlab1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.example.shovkanlab1.Element;
import com.example.shovkanlab1.fragments.MainFragment;
import com.example.shovkanlab1.R;
import com.example.shovkanlab1.database.DatabaseService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences sPref;
    private String save_props;

    public static MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save_props = getIntent().getStringExtra("save_props");

        if (savedInstanceState == null) {

            mainFragment = new MainFragment();
            loadData();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, mainFragment, "TAG_FOR_MAIN_FRAGMENT")
                    .commit();
        }
    }

    private void loadData() {
        sPref = getSharedPreferences("Preferences", MODE_PRIVATE);
        switch (save_props)
        {
            case "DB":
                Intent intent = new Intent(this, DatabaseService.class).putExtra("COM", 0);
                startService(intent);
                break;
            case "Preferences":
                Thread thread = new Thread(new Runnable() {
                    ArrayList<Element> objects = new ArrayList<>();
                    public void run() {
                        int count = sPref.getInt("count",0);
                        for (int i = 0; i < count; i++)
                        {
                            Element object = new Element(sPref.getInt("Int " + i,0), sPref.getString("String " + i,""), Boolean.toString(sPref.getBoolean("Bool " + i,false)));
                            objects.add(object);
                        }
                        mainFragment.Init(objects);
                    }
                });
                thread.start();
                break;
        }
    }

    @Override
    public void onPause() {
        mainFragment.saveData();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mainFragment.saveData();
        stopService(new Intent(MainActivity.this, DatabaseService.class));
        super.onDestroy();
    }

    public void saveData(ArrayList<Element> objects) {
        sPref = getSharedPreferences("Preferences", MODE_PRIVATE);
        switch (save_props)
        {
            case "DB":
                Intent myIntent = new Intent(this, DatabaseService.class).putParcelableArrayListExtra("Elements", objects);
                startService(myIntent);
                break;
            case "Preferences":
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        Editor ed = sPref.edit();
                        ed.putInt("count", objects.size());
                        for (int i = 0; i < objects.size(); i++)
                        {
                            ed.putInt("Int " + i, (int)objects.get(i).getID());
                            ed.putString("String " + i, (String)objects.get(i).getName());
                            ed.putBoolean("Bool " + i, (boolean)objects.get(i).getBoolFlag());
                        }
                        ed.commit();
                    }
                });
                t.start();
                break;
        }
    }

    public void openDisplayResultActivity(ArrayList<String> list) {
        Intent intent = new Intent(this, DisplayResultActivity.class);
        intent.putStringArrayListExtra("key", list);
        startActivity(intent);
    }
}